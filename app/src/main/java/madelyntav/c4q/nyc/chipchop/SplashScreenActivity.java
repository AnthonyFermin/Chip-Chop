package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent splashIntent = new Intent(getApplicationContext(), BuyActivity.class);
                startActivity(splashIntent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
