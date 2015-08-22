package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ViewCart;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_SellerProfile;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Items;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Orders;

public class SellActivity extends AppCompatActivity implements Fragment_Seller_Orders.OnHeadlineSelectedListener, Fragment_Seller_Items.OnHeadlineSelectedListener {

    FrameLayout frameLayout;
    LinearLayout DrawerLinear;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mListTitles;
    private Fragment fragment;
    private ActionBarDrawerToggle mDrawerToggle;

    // for storing data between fragments in SellActivity

    private ArrayList<Item> sellerItems = null;
    private ArrayList<Item> itemsToAdd = null;
    private boolean currentlyCooking = false;
    private User user = null;
    private Seller seller = null;
    private boolean fromItemCreation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mListTitles = getResources().getStringArray(R.array.SELLER_nav_drawer_titles);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navdrawer_list_item, mListTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                new Toolbar(this), R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(SellActivity.this);

            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(SellActivity.this);


                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                Button buyButton = (Button) findViewById(R.id.buyButton);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent buyIntent = new Intent(SellActivity.this, BuyActivity.class);
                        startActivity(buyIntent);
                        finish();
                    }
                });
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            selectItem(2);
        }

    }



    // TODO: PUT CODE FOR INTERACTION WITH 'ITEMS' LIST HERE !!
    @Override
    public void onSellerItemSelected(int position) {

    }


    // TODO: PUT CODE FOR INTERACTION WITH 'ORDERS' LIST HERE !!
    @Override
    public void onSellerOrderSelected(int position) {

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
            fragment = new Fragment_Seller_Items();
        } else if (position == 1) {
            fragment = new Fragment_Seller_Orders();
        } else if (position == 2) {
            // TODO: PROFILE SETTINGS
//            Intent profileIntent = new Intent(getApplicationContext(), SignupActivity2.class);
//            startActivity(profileIntent);
            fragment = new Fragment_SellerProfile();
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

    public void replaceSellerFragment(Fragment fragment) {
        FragmentManager BuyFragmentManager = getSupportFragmentManager();
        BuyFragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).addToBackStack(null).commit();

    }

    // for storing data between fragments in SellActivity

    public ArrayList<Item> getSellerItems(){
        return sellerItems;
    }

    public void setSellerItems(ArrayList<Item> sellerItems){
        this.sellerItems = sellerItems;
    }

    public ArrayList<Item> getItemsToAdd(){
        return itemsToAdd;
    }

    public void setItemsToAdd(ArrayList<Item> itemsToAdd){
        this.itemsToAdd = itemsToAdd;
    }

    public void setCookingStatus(boolean condition){
        currentlyCooking = condition;
    }

    public boolean isCurrentlyCooking(){
        return currentlyCooking;
    }

    public void setSeller(Seller seller){
        this.seller = seller;
    }

    public Seller getSeller(){
        return seller;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public boolean isFromItemCreation(){
        return fromItemCreation;
    }

    public void setFromItemCreation(boolean condition){
        fromItemCreation = condition;
    }

}
