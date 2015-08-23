package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;

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

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        dbHelper = DBHelper.getDbHelper(this);

        emailET = (EditText) findViewById(R.id.eMail);
        passET = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.remember_me);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString().trim();
                password = passET.getText().toString();


                Intent sellActivityIntent = new Intent(getApplicationContext(), SellActivity.class);
                dbHelper.logInUser(email, password, sellActivityIntent);
                checkRememberMe();
                finish();

                }

        });

        newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString().trim();
                password = passET.getText().toString();

                Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
                dbHelper.createUserAndLaunchIntent(email, password, intent);
                checkRememberMe();
            }
        });


    }

    private void checkRememberMe(){
        if(rememberMe.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EMAIL, email);
            editor.putString(PASS, password);
            editor.commit();
        }
    }


}
