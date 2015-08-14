package madelyntav.c4q.nyc.chipchop;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SearchMapFragment mapFragment = new SearchMapFragment();
        ft.replace(R.id.container,mapFragment);
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.commit();

    }

}
