package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.plus.Plus;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Checkout;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Map;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Orders;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ProfileSettings;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ViewCart;

public class BuyActivity extends AppCompatActivity implements Fragment_Buyer_Orders.OnBuyerOrderSelectedListener, Fragment_Buyer_Map.OnBuyerMapFragmentInteractionListener {

    private FrameLayout frameLayout;
    private LinearLayout DrawerLinear;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mListTitles;
    private Fragment fragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView drawerUserNameTV;
    private RelativeLayout loadingPanel;



    private DBHelper dbHelper;

    private User user = null;

    private SharedPreferences userInfoSP;

    private DBCallback emptyCallback;

    private String currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        bindViews();
        initializeData();
        setUpDrawer();
        checkAutoLogIn();
        selectFragmentToLoad(savedInstanceState);

    }

    private void checkAutoLogIn() {
        if(dbHelper.userIsLoggedIn() && user != null){
            load();
        }
    }

    private void selectFragmentToLoad(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences1 = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CHECKOUT, MODE_PRIVATE);

        if(sharedPreferences1.getBoolean(Fragment_Buyer_ViewCart.FROM_CHECKOUT, false)) {
            replaceFragment(new Fragment_Buyer_Checkout());
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean(Fragment_Buyer_ViewCart.FROM_CHECKOUT, false);
            editor.commit();
        } else if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void setUpDrawer() {
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navdrawer_list_item, mListTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                new Toolbar(this), R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
//
            }

            public void onDrawerOpened(View drawerView) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

//

                Button sellButton = (Button) findViewById(R.id.sellButton);
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
        getSupportActionBar().setHomeButtonEnabled(false);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navdrawer);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D51F27"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

    }

    private void bindViews(){
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        drawerUserNameTV = (TextView) findViewById(R.id.drawer_user_nameTV);
        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    private void initializeData() {
        dbHelper = DBHelper.getDbHelper(this);

        mListTitles = getResources().getStringArray(R.array.BUYER_nav_drawer_titles);

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };

        /*
            checks if user info is saved for auto log in
         */

        userInfoSP = getSharedPreferences(SignupActivity1.USER_INFO, MODE_PRIVATE);
        String email = userInfoSP.getString(SignupActivity1.EMAIL, null);
        String pass = userInfoSP.getString(SignupActivity1.PASS, null);

        if(email != null && pass != null){
            Log.d("AUTO LOG-IN",email + ", " + pass);
            loadingPanel.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            DrawerLinear.setVisibility(View.INVISIBLE);
            //TODO: AUTO LOG IN SHOULD INITIALIZE USER OBJECT FOR DRAWER
            dbHelper.logInUser(email,pass, new DBCallback() {
                @Override
                public void runOnSuccess() {
                    load();
                }

                @Override
                public void runOnFail() {
                    // clears user login info if login authentication failed
                    userInfoSP.edit().clear().commit();
                    loadingPanel.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    DrawerLinear.setVisibility(View.VISIBLE);
                }
            });
        }
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

            if (SignupActivity1.mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(SignupActivity1.mGoogleApiClient);
                SignupActivity1.mGoogleApiClient.disconnect();
            }

            dbHelper.signOutUser(emptyCallback);
            Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences(SignupActivity1.USER_INFO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();



            //if not currently in fragment_buyer_map, replace current fragment with buyer_map fragment
            if (!getCurrentFragment().equals(Fragment_Buyer_Map.TAG)) {
                replaceFragment(new Fragment_Buyer_Map());
            }
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

    private void load(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                user = dbHelper.getUserFromDB(dbHelper.getUserID());
                int i = 0;
                do{
                    Log.d("Buy - Load User", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("Buy - Load User", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(user == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(user != null){
                    SharedPreferences.Editor editor = userInfoSP.edit();
                    editor.putString(SignupActivity1.NAME, user.getName());
                    editor.commit();

                    drawerUserNameTV.setText(user.getName());
                }

                loadingPanel.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                DrawerLinear.setVisibility(View.VISIBLE);

            }
        }.execute();
    }

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }


}
