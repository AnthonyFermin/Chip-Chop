package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;

public class SignupActivity1 extends AppCompatActivity {

    Button signInButton;
    Button newUserButton;
    EditText emailET;
    EditText passET;
    DBHelper dbHelper;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        dbHelper = DBHelper.getDbHelper(this);

        emailET = (EditText) findViewById(R.id.eMail);
        passET = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passET.getText().toString();

                if(dbHelper.logInUser(email,password)){
                    Intent sellActivityIntent = new Intent(getApplicationContext(), SellActivity.class);
                    startActivity(sellActivityIntent);
                    finish();
                }
            }
        });

        newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passET.getText().toString();

                // TODO: dbHelper.createUser(email, password, Class class)
                // inside onSuccess also launch the activity;

                if(dbHelper.createUser(email, password)) {
                    launch(SignupActivity2.class);
                }
            }
        });
    }

    public void launch(Class activityToLaunch){
        Intent launch = new Intent(getApplicationContext(), activityToLaunch);
        startActivity(launch);
        finish();
    }

}
