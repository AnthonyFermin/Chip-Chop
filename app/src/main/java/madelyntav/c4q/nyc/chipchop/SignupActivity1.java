package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Arrays;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class SignupActivity1 extends AppCompatActivity {

    Button signInButton;
    Button newUserButton;
    EditText emailET;
    EditText passET;
    DBHelper dbHelper;
    CheckBox rememberMe;
    LoginButton loginButton;
    public static final String USER_INFO = "userinfo";
    public static final String EMAIL = "email";
    public static final String PASS = "pass";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String APT = "apt";
    public static final String CITY = "city";
    public static final String ZIPCODE = "zipcode";
    public static final String PHONE_NUMBER = "phone_number";
    String email;
    String password;
    LinearLayout containingView;
    RelativeLayout loadingPanel;
    private User user;
    Intent intent;
    CallbackManager callbackManager;
    AccessTokenTracker mFacebookAccessTokenTracker;
    DBCallback emptyCallback;
    AccessTokenTracker accessTokenTracker;
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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK) {
            dbHelper.onFacebookAccessTokenChange(AccessToken.getCurrentAccessToken(), emptyCallback);
            Log.d("request Code", String.valueOf(resultCode));
            Intent intent1 = new Intent(SignupActivity1.this, SignupActivity2.class);
            startActivity(intent1);
        }
        else{
            Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show();

        }
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
        accessTokenTracker.stopTracking();
        mFacebookAccessTokenTracker.stopTracking();
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
                    String addressString = user.getAddressString();
                    Address address = HelperMethods.parseAddressString(addressString, dbHelper.getUserID());
                    editor.putString(NAME, user.getName());
                    editor.putString(ADDRESS, address.getStreetAddress());
                    editor.putString(APT, address.getApartment());
                    editor.putString(CITY, address.getCity());
                    editor.putString(ZIPCODE, address.getZipCode());
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


        };



