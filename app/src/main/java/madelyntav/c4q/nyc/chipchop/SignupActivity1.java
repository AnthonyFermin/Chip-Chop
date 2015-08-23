package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    public static final String USER_INFO = "userinfo";
    public static final String EMAIL = "email";
    public static final String PASS = "pass";
    public static final String NAME = "name";

    String email;
    String password;

    LinearLayout containingView;
    RelativeLayout loadingPanel;
    private User user;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        dbHelper = DBHelper.getDbHelper(this);

        containingView = (LinearLayout) findViewById(R.id.container);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        intent = new Intent(getApplicationContext(), BuyActivity.class);


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
                    public void runOnSuccess(Context context) {
                        user = dbHelper.getUserFromDB(dbHelper.getUserID());
                        loadingPanel.setVisibility(View.VISIBLE);
                        containingView.setVisibility(View.GONE);
                        load();
                    }

                    @Override
                    public void runOnFail(Context context) {
                    }
                });

                }

        });

        newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString().trim();
                password = passET.getText().toString();

                dbHelper.createUserAndLaunchIntent(email, password, intent);
                checkRememberMe();
            }
        });


    }

    private void checkRememberMe(){

        Address address = user.getAddress();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        if(rememberMe.isChecked()){
            editor.putString(PASS, password);
        }
        editor.putString(NAME, user.getName());
        editor.commit();

    }

    private void load(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("Signup - LOAD USER", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("Signup - LOAD USER", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(user == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(user != null) {
                    checkRememberMe();
                    Log.d("SIGNUPACTIVITY1", "USER LOG IN SUCCESSFUL");
                    startActivity(intent);
                    finish();
                }else{
                    //TODO:display cannot connect to internet error message
                    Toast.makeText(SignupActivity1.this, "Cannot Connect to Internet", Toast.LENGTH_SHORT).show();
                    loadingPanel.setVisibility(View.GONE);
                    containingView.setVisibility(View.VISIBLE);
                    dbHelper.signOutUser();
                }
            }
        }.execute();
    }


}
