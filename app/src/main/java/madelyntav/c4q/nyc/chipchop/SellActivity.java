package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Review;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_CreateItem;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_ProfileSettings;

import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Items;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Orders;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Review;

public class SellActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    LinearLayout DrawerLinear;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView drawerUserNameTV;
    private String[] mListTitles;
    private Fragment fragment;
    private ActionBarDrawerToggle mDrawerToggle;

    // for storing data between fragments in SellActivity

    private ArrayList<Item> sellerItems = null;
    private boolean currentlyCooking = false;
    private User user = null;
    private Seller seller = null;
    private boolean fromItemCreation = false;
    private Item itemToEdit = null;
    private String currentFragment;

    private DBHelper dbHelper;

    DBCallback emptyCallback;

    private SharedPreferences userInfoSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        dbHelper = DBHelper.getDbHelper(this);

        bindViews();
        initializeData();
        setUpNavActionBar();
        initializeUser();

        if (savedInstanceState == null) {
            selectItem(2);
        }

    }

    private void setUpNavActionBar() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navdrawer);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D51F27"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    private void initializeData() {
        userInfoSP = getSharedPreferences(SignupActivity1.SP_USER_INFO, MODE_PRIVATE);
        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };
        mListTitles = getResources().getStringArray(R.array.SELLER_nav_drawer_titles);

    }

    private void bindViews(){
        drawerUserNameTV = (TextView) findViewById(R.id.drawer_user_nameTV);
        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
            fragment = new Fragment_Seller_ProfileSettings();
        } else if (position == 3) {
            clearLogin();

            Intent intent = new Intent(this,BuyActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Sign out successful",Toast.LENGTH_SHORT).show();
            finish();
        }

            // Create fragment manager to begin interacting with the fragments and the container
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).addToBackStack(null).commit();


            // update selected item and title in nav drawer, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(DrawerLinear);

    }

    private void clearLogin() {
        userInfoSP.edit().clear().commit();
        dbHelper.signOutUser(emptyCallback);
        drawerUserNameTV.setText("");
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
        switch(getCurrentFragment()){
            case Fragment_Seller_Orders.TAG:
                replaceSellerFragment(new Fragment_Seller_ProfileSettings());
                break;
            case Fragment_Seller_OrderDetails.TAG:
                replaceSellerFragment(new Fragment_Seller_Orders());
                break;
            case Fragment_Seller_ProfileSettings.TAG:
                startActivity(new Intent(SellActivity.this, BuyActivity.class));
                finish();
                break;
            case Fragment_Seller_Items.TAG:
                replaceSellerFragment(new Fragment_Seller_ProfileSettings());
                break;
            case Fragment_Seller_CreateItem.TAG:
                replaceSellerFragment(new Fragment_Seller_Items());
                break;
            default:
        }
    }

    public void replaceSellerFragment(Fragment fragment) {
        FragmentManager BuyFragmentManager = getSupportFragmentManager();
        BuyFragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).commit();

    }

    private void initializeUser(){
        SharedPreferences sp = getSharedPreferences(SignupActivity1.SP_USER_INFO, MODE_PRIVATE);
        String email = sp.getString(SignupActivity1.SP_EMAIL, "");
        String name = sp.getString(SignupActivity1.SP_NAME, "");
        String address = sp.getString(SignupActivity1.SP_ADDRESS,"");
        String apt = sp.getString(SignupActivity1.SP_APT, "");
        String city = sp.getString(SignupActivity1.SP_CITY, "");
        String zip = sp.getString(SignupActivity1.SP_ZIPCODE, "");
        String phoneNumber = sp.getString(SignupActivity1.SP_PHONE_NUMBER, "");

        Address userAddress = new Address(address, apt, city, "NY", zip, dbHelper.getUserID());
        user = new User(dbHelper.getUserID(), email, name, userAddress, phoneNumber);

        drawerUserNameTV.setText(user.getName());
    }

    // for storing data between fragments in SellActivity

    public ArrayList<Item> getSellerItems(){
        return sellerItems;
    }

    public void setSellerItems(ArrayList<Item> sellerItems){
        this.sellerItems = sellerItems;
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

    public Item getItemToEdit() {
        return itemToEdit;
    }

    public void setItemToEdit(Item itemToEdit) {
        this.itemToEdit = itemToEdit;
    }

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }
}
