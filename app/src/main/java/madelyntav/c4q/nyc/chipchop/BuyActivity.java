package madelyntav.c4q.nyc.chipchop;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;


import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Checkout;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Map;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Orders;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ProfileSettings;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_SellerProfile;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ViewCart;

public class BuyActivity extends AppCompatActivity implements Fragment_Buyer_Orders.OnBuyerOrderSelectedListener, Fragment_Buyer_Map.OnBuyerMapFragmentInteractionListener {

    FrameLayout frameLayout;
    LinearLayout DrawerLinear;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mListTitles;
    private Fragment fragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        dbHelper = DBHelper.getDbHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SignupActivity1.USER_INFO, MODE_PRIVATE);
        String email = sharedPreferences.getString(SignupActivity1.EMAIL, null);
        String pass = sharedPreferences.getString(SignupActivity1.PASS,null);
        Log.d("AUTO LOG-IN",email + ", " + pass);

        if(email != null && pass != null){
            Log.d("AUTO LOG-IN",email + ", " + pass);
            dbHelper.logInUser(email,pass);
        }



        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mListTitles = getResources().getStringArray(R.array.BUYER_nav_drawer_titles);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navdrawer_list_item, mListTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                new Toolbar(this), R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(BuyActivity.this);
            }

            public void onDrawerOpened(View drawerView) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(BuyActivity.this);


                Button sellButton = (Button) findViewById(R.id.sellButton);
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // TODO:
                        //launch sellactivity
                        //else launch signUpActivity1
                        if(dbHelper.userIsLoggedIn()){
                            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
                            startActivity(sellIntent);
                        }else{
                            Intent signUpIntent = new Intent(getApplicationContext(), SignupActivity1.class);
                            startActivity(signUpIntent);
                        }



                    }
                });

            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        SharedPreferences sharedPreferences1 = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CHECKOUT, MODE_PRIVATE);

        if(sharedPreferences1.getBoolean(Fragment_Buyer_ViewCart.FROM_CHECKOUT, false)) {
            replaceFragment(new Fragment_Buyer_Checkout());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Fragment_Buyer_ViewCart.FROM_CHECKOUT, false);
            editor.commit();
        } else if (savedInstanceState == null) {
            selectItem(0);
        }

        // NOTIFICATION CODE
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.chipchop_small)
                        .setContentTitle("ChipChop")
                        .setContentText("Your order is ready!");

        mBuilder.setAutoCancel(true);
        Notification notification = mBuilder.build();
        notificationManager.notify(1234, notification);


    }

    // TODO: PUT CODE FOR INTERACTION WITH LIST HERE !!
    @Override
    public void onArticleSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {
        // update the main content by replacing fragments

        if (position == 0) {
            fragment = new Fragment_Buyer_Map();
        } else if (position == 1) {
            fragment = new Fragment_Buyer_Orders();
        } else if (position == 2) {
          fragment = new Fragment_Buyer_ProfileSettings();
        } else if (position == 3) {
            // TODO: SIGN OUT CODE !
        }

            // Create fragment manager to begin interacting with the fragments and the container
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).addToBackStack(null).commit();


            // update selected item and title in nav drawer, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(DrawerLinear);
        }


    @Override
    protected void onStop () {
        SharedPreferences sharedPreferences = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CHECKOUT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        super.onStop();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles

        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(DrawerLinear);
        return drawerOpen;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager BuyFragmentManager = getSupportFragmentManager();
        BuyFragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).addToBackStack(null).commit();

    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CHECKOUT, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();

    }
}
