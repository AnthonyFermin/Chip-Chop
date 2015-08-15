package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SignupActivity1 extends AppCompatActivity {

    Button signup1Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        signup1Button = (Button) findViewById(R.id.signup1Button);
        signup1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent2 = new Intent(getApplicationContext(), SignupActivity2.class);
                startActivity(signupIntent2);
                finish();
            }
        });

    }

}
