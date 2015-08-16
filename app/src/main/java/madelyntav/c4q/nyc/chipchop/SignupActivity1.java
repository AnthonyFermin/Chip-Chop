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

        emailET = (EditText) findViewById(R.id.emailAddress);
        passET = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passET.getText().toString();

                //if(dbHelper.logInUser(email,password){
                    Intent sellActivityIntent = new Intent(getApplicationContext(), SellActivity.class);
                    startActivity(sellActivityIntent);
                    finish();
            }
        });

        newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                password = passET.getText().toString();

                //if(dbHelper.createUser(email, password){
                    Intent createUserIntent = new Intent(getApplicationContext(), SignupActivity2.class);
                    startActivity(createUserIntent);
                    finish();
            }
        });

    }

}
