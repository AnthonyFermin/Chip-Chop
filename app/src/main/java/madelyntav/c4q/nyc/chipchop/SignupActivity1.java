package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class SignupActivity1 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    static GoogleApiClient mGoogleApiClient;

    Button signInButton;
    Button newUserButton;
    EditText emailET;
    EditText passET;
    DBHelper dbHelper;
    CheckBox rememberMe;
    LoginButton loginButton;

    public static final String TAG = "1";
    public static final String USER_INFO = "userinfo";
    public static final String EMAIL = "email";
    public static final String PASS = "pass";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String APT = "apt";
    public static final String CITY = "city";
    public static final String ZIPCODE = "zipcode";
    public static final String PHONE_NUMBER = "phone_number";

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    String email;
    String password;
    LinearLayout containingView;
    RelativeLayout loadingPanel;
    private User user;
    Intent intent;
    CallbackManager callbackManager;
    AccessTokenTracker mFacebookAccessTokenTracker;
    DBCallback emptyCallback;
    Firebase.AuthStateListener mAuthStateListener;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_signup1);

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

        containingView = (LinearLayout) findViewById(R.id.container);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        intent = new Intent(getApplicationContext(), BuyActivity.class);
        loginButton=(LoginButton) findViewById(R.id.login_button);

        emailET = (EditText) findViewById(R.id.eMail);
        passET = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.remember_me);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        });

        newUserButton = (Button)

                findViewById(R.id.newUserButton);

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString().trim();
                password = passET.getText().toString();

                dbHelper.createUserAndCallback(email, password, new DBCallback() {
                    @Override
                    public void runOnSuccess() {
                        checkRememberMe();
                        Intent intent1 = new Intent(SignupActivity1.this, SignupActivity2.class);
                        intent1.putExtra("email", email);
                        startActivity(intent1);
                        finish();
                    }

                    @Override
                    public void runOnFail() {

                    }
                });
            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithFacebook();            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_in_button) {
                    onSignInClicked();
                }

            }
        });


    }




    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
//        mStatusTextView.setText(R.string.signing_in);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }else if(resultCode== RESULT_OK) {
            dbHelper.onFacebookAccessTokenChange(AccessToken.getCurrentAccessToken(), emptyCallback);
            Log.d("request Code", String.valueOf(resultCode));
            Intent intent1 = new Intent(SignupActivity1.this, SignupActivity2.class);
            startActivity(intent1);
        }
        else{
            Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show();

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
        mShouldResolve = false;

        // Show the signed-in UI
        Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_SHORT).show();

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

    private void checkRememberMe() {

        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        if (rememberMe.isChecked()) {
            editor.putString(PASS, password);
        }
        // when user clicks sign in
        if (user != null) {
            //Log.d("Address",user.getAddressString()+"");
            String addressString = user.getAddressString();
//            Address address = HelperMethods.parseAddressString(addressString, dbHelper.getUserID());
            editor.putString(NAME, user.getName());
           // editor.putString(ADDRESS, address.getStreetAddress());
           // editor.putString(APT, address.getApartment());
           // editor.putString(CITY, address.getCity());
           // editor.putString(ZIPCODE, address.getZipCode());
            editor.putString(PHONE_NUMBER, user.getPhoneNumber());
        }
        editor.commit();

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
                        } while (user == null);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (user != null) {
                            checkRememberMe();
                            Log.d("SIGNUPACTIVITY1", "USER LOG IN SUCCESSFUL");
                            startActivity(intent);
                            finish();
                        } else {
                            //TODO:display cannot connect to internet error message
                            Toast.makeText(SignupActivity1.this, "Cannot Connect to Internet", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Error with connection", Toast.LENGTH_SHORT).show();
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
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}
