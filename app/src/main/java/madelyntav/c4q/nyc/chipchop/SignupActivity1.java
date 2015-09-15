package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.Arrays;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class SignupActivity1 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    public int MY_ACTIVITY_REQUEST_CODE;

    /* Client used to interact with Google APIs. */
    static GoogleApiClient mGoogleApiClient;

    Button signInButton;
    Button newUserButton;
    String token;
    EditText emailET;
    EditText passET;
    DBHelper dbHelper;
    LoginButton loginButton;
    Intent newUserIntent;

    public static final int MY_REQUEST_CODE=0;
    public static final String TAG = "1";
    public static final String SP_USER_INFO = "user_info";
    public static final String SP_EMAIL = "email";
    public static final String SP_PASS = "pass";
    public static final String SP_NAME = "name";
    public static final String SP_ADDRESS = "address";
    public static final String SP_APT = "apt";
    public static final String SP_CITY = "city";
    public static final String SP_STATE = "state";
    public static final String SP_ZIPCODE = "zipcode";
    public static final String SP_PHONE_NUMBER = "phone_number";
    public static final String SP_PHOTO_LINK = "photo_link";
    public static final String SP_IS_LOGGED_IN = "logged_in";

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    public static View coordinatorLayoutView;
    String email;
    String password;
    LinearLayout containingView;
    RelativeLayout loadingPanel;
    private User user;
    CallbackManager callbackManager;
    AccessTokenTracker mFacebookAccessTokenTracker;
    DBCallback emptyCallback;
    Firebase.AuthStateListener mAuthStateListener;
    AccessToken accessToken;
    SignInButton googleButton;

    private boolean toSellActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_signup1);

        initializeData();
        bindViews();
        setListeners();

//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D51F27"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

//        BitmapDrawable background = new BitmapDrawable (BitmapFactory.decodeResource(getResources(), R.drawable.actionbar));
//        background.setGravity(Gravity.CENTER);
//        getSupportActionBar().setBackgroundDrawable(background);

    }

    private void setListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailET.getText().toString().equals("") || passET.getText().toString().equals("")) {
                    Snackbar
                            .make(coordinatorLayoutView, "Please enter in all fields", Snackbar.LENGTH_SHORT)
                            .show();
                } else {

                    email = emailET.getText().toString().trim();
                    password = passET.getText().toString();

                    dbHelper.logInUser(email, password, new DBCallback() {
                        @Override
                        public void runOnSuccess() {
                            user = dbHelper.getUserFromDB(dbHelper.getUserID());
                            loadingPanel.setVisibility(View.VISIBLE);
                            containingView.setVisibility(View.GONE);
                            load();
                        }

                        @Override
                        public void runOnFail() {
                        }
                    });

                }
            }

        });


        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailET.getText().toString().equals("") || passET.getText().toString().equals("")) {
                    Snackbar
                            .make(coordinatorLayoutView, "Please set an Email and Password", Snackbar.LENGTH_SHORT)
                            .show();
                } else {

                    email = emailET.getText().toString().trim();
                    password = passET.getText().toString();

                    newUserIntent = new Intent(SignupActivity1.this, SignupActivity2.class);
                    newUserIntent.putExtra("email", email);
                    newUserIntent.putExtra("pass", password);
                    if (toSellActivity) {
                        newUserIntent.putExtra(BuyActivity.TO_SELL_ACTIVITY, true);
                    }

                    FragmentManager fm = getSupportFragmentManager();
                    TermsDialog alertDialog = new TermsDialog();
                    alertDialog.show(fm, "fragment_alert");
                }
            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithFacebook();
            }
        });

        googleButton= (SignInButton) findViewById(R.id.sign_in_button);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_in_button) {
                    onSignInClicked();
                }

            }
        });
    }



    private void bindViews() {
        containingView = (LinearLayout) findViewById(R.id.container);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        loginButton=(LoginButton) findViewById(R.id.login_button);

        emailET = (EditText) findViewById(R.id.eMail);
        passET = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.signInButton);
        newUserButton = (Button) findViewById(R.id.newUserButton);

        coordinatorLayoutView = findViewById(R.id.snackbarPosition);

    }

    private void initializeData() {
        toSellActivity = getIntent().getBooleanExtra(BuyActivity.TO_SELL_ACTIVITY,false);

        dbHelper = DBHelper.getDbHelper(this);
        callbackManager = CallbackManager.Factory.create();

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

    }


    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        //mGoogleApiClient.connect();
        // Show a message to the user that we are signing in.
//        mStatusTextView.setText(R.string.signing_in);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MY_ACTIVITY_REQUEST_CODE=requestCode;
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
                    Snackbar
                            .make(coordinatorLayoutView, "Login Failed", Snackbar.LENGTH_SHORT)
                            .show();
                    Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show();
            }

            mIsResolving = false;
            //mGoogleApiClient.connect();
        }else if(resultCode== RESULT_OK) {
            dbHelper.onFacebookAccessTokenChange(AccessToken.getCurrentAccessToken());
            Log.d("request Code", String.valueOf(resultCode));
        }
    }

    public void loginWithFacebook() {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        googleButton.setEnabled(false);
        googleButton.setEnabled(true);
        googleButton.setEnabled(true);
        mShouldResolve = false;

        //googleSignInASync();
    }

 public void googleSignInASync() {
     new AsyncTask<Void, Void, Void>() {
         @Override
         protected Void doInBackground(Void... params) {
             final String SCOPES = "https://www.googleapis.com/auth/userinfo.profile";
             try {
                 token = GoogleAuthUtil.getToken(getApplicationContext(),
                         Plus.AccountApi.getAccountName(mGoogleApiClient),
                         "oauth2:" + SCOPES);
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (UserRecoverableAuthException userAuthEx) {
                 // Start the user recoverable action using the intent returned by
                 // getIntent()
                 SignupActivity1.this.startActivityForResult(
                         userAuthEx.getIntent(),
                         MY_ACTIVITY_REQUEST_CODE);
                 return null;
             } catch (GoogleAuthException e) {
                 e.printStackTrace();
             }

             Log.i("", "mustafa olll " + token);
                 return null;
             }


             @Override
             protected void onPostExecute (Void aVoid){
                 super.onPostExecute(aVoid);
                 dbHelper.onGmailAccessTokenChange(token);
             }
         }.execute();
     }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }
        // if changing configurations, stop tracking firebase session.
//        dbHelper.removeAuthStateListener(mAuthStateListener);
    }

    private void storeUserInfo() {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_EMAIL, email);
        editor.putString(SP_PASS, password);
        editor.putBoolean(SP_IS_LOGGED_IN, true);
        // when user clicks sign in
        if (user != null) {
            Log.d("SignUp - User Info","Name: " + user.getName());
            Log.d("SignUp - User Info","Address: " + user.getAddressString());
            Log.d("SignUp - User Info","UID: " + user.getUID());
            Log.d("SignUp - User Info","Email: " + user.geteMail());
            String addressString = user.getAddressString();
            Address address = HelperMethods.parseAddressString(addressString, dbHelper.getUserID());
            editor.putString(SP_NAME, user.getName())
                .putString(SP_ADDRESS, address.getStreetAddress())
                .putString(SP_APT, address.getApartment())
                .putString(SP_CITY, address.getCity())
                .putString(SP_STATE, address.getState())
                .putString(SP_ZIPCODE, address.getZipCode())
                .putString(SP_PHONE_NUMBER, user.getPhoneNumber())
                .putString(SP_PHOTO_LINK, user.getPhotoLink());
            HelperMethods.setUser(user);
        }
        editor.apply();

    }

            private void load() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        int i = 0;
                        do {
                            Log.d("Signup - LOAD USER", "Attempt #" + i);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (i > 10) {
                                Log.d("Signup - LOAD USER", "DIDN'T LOAD");
                                break;
                            }
                            i++;
                        } while (user == null || user.getName() == null || user.getName().isEmpty() );

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (user != null && user.getAddressString() != null) {
                            storeUserInfo();
                            Log.d("SIGNUPACTIVITY1", "USER LOG IN SUCCESSFUL");
                            Intent intent;
                            if(toSellActivity){
                                intent = new Intent(SignupActivity1.this,SellActivity.class);
                            }
                            else{
                                intent = new Intent(SignupActivity1.this,BuyActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            //TODO:display cannot connect to internet error message
                            Snackbar
                                    .make(coordinatorLayoutView, "Failed to load account, please try again", Snackbar.LENGTH_SHORT)
                                    .show();
                            loadingPanel.setVisibility(View.GONE);
                            containingView.setVisibility(View.VISIBLE);
                            dbHelper.signOutUser(emptyCallback);
                        }
                    }
                }.execute();
            }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                Snackbar
                        .make(coordinatorLayoutView, "Error with connection", Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            // Show the signed-out UI

            //This toast should not show everytime this activity is launched
            //Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        float fbIconScale = 1.45F;
        Drawable drawable = getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                (int)(drawable.getIntrinsicHeight()*fbIconScale));
        loginButton.setCompoundDrawables(drawable, null, null, null);
        loginButton.setCompoundDrawablePadding(getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        loginButton.setPadding(
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // mGoogleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BuyActivity.class));
        finish();
    }

    public Intent getNewUserIntent() {
        return newUserIntent;
    }

    public void setNewUserIntent(Intent newUserIntent) {
        this.newUserIntent = newUserIntent;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
