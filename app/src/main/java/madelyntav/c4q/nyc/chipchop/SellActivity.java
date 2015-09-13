package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import android.view.Gravity;
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
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_CreateItem;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_ProfileSettings;

import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Items;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Orders;

public class SellActivity extends AppCompatActivity {

    Button contactButton;
    View coordinatorLayoutView;
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
    private ArrayList<Item> inactiveSellerItems = null;
    private boolean currentlyCooking = false;
    private User user = null;
    private Seller seller = null;
    private Item itemToEdit = null;
    private Item inactiveItemToEdit = null;
    private String currentFragment;
    private Intent serviceIntent;
    private Order orderToView = null;

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
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(Intent.ACTION_SEND);
                contactIntent.setType("text/html");
                contactIntent.putExtra(Intent.EXTRA_EMAIL, "chipchopcontact@gmail.com");
                startActivity(Intent.createChooser(contactIntent, "Create Email"));
            }
        });

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

//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navdrawer);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D51F27"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

//        BitmapDrawable background = new BitmapDrawable (BitmapFactory.decodeResource(getResources(), R.drawable.actionbar));
//        background.setGravity(Gravity.CENTER);
//        getSupportActionBar().setBackgroundDrawable(background);
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
        serviceIntent = new Intent(this,ServiceSellerNotify.class).putExtra(ServiceSellerNotify.SELLER_ID,dbHelper.getUserID());
    }

    private void bindViews(){
        drawerUserNameTV = (TextView) findViewById(R.id.drawer_user_nameTV);
        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        contactButton = (Button) findViewById(R.id.contact_button);

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

            Snackbar
                    .make(coordinatorLayoutView, "Sign out successful", Snackbar.LENGTH_SHORT)
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), BuyActivity.class);
                    startActivity(intent);

                    finish();
                }
            }, 2000);

        }

            // Create fragment manager to begin interacting with the fragments and the container
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).commit();


            // update selected item and title in nav drawer, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(DrawerLinear);

    }

    private void clearLogin() {
        dbHelper.setSellerCookingStatus(false);
        dbHelper.removeSellersFromActiveSellers(seller, emptyCallback);
        userInfoSP.edit().clear().commit();
        dbHelper.signOutUser(emptyCallback);
        drawerUserNameTV.setText("");
        stopService(serviceIntent);
        stopService(new Intent(this,ServiceBuyerNotify.class));
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
        String state = sp.getString(SignupActivity1.SP_STATE,"");
        String zip = sp.getString(SignupActivity1.SP_ZIPCODE, "");
        String phoneNumber = sp.getString(SignupActivity1.SP_PHONE_NUMBER, "");
        String photoLink = sp.getString(SignupActivity1.SP_PHOTO_LINK,"");

        Address userAddress = new Address(address, apt, city, state, zip, dbHelper.getUserID());
        user = new User(dbHelper.getUserID(), email, name, userAddress, phoneNumber);
        user.setPhotoLink(photoLink);

        drawerUserNameTV.setText(user.getName());
    }

    // for storing data between fragments in SellActivity

    public ArrayList<Item> getSellerItems(){
        return sellerItems;
    }

    public void setSellerItems(ArrayList<Item> sellerItems){
        this.sellerItems = sellerItems;
    }

    public ArrayList<Item> getInactiveSellerItems() {
        return inactiveSellerItems;
    }

    public void setInactiveSellerItems(ArrayList<Item> inactiveSellerItems) {
        this.inactiveSellerItems = inactiveSellerItems;
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

    public Item getItemToEdit() {
        return itemToEdit;
    }

    public void setItemToEdit(Item itemToEdit) {
        this.itemToEdit = itemToEdit;
    }

    public Item getInactiveItemToEdit() {
        return inactiveItemToEdit;
    }

    public void setInactiveItemToEdit(Item inactiveItemToEdit) {
        this.inactiveItemToEdit = inactiveItemToEdit;
    }

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }

    public Intent getServiceIntent() {
        return serviceIntent;
    }

    public void setServiceIntent(Intent serviceIntent) {
        this.serviceIntent = serviceIntent;
    }

    public Order getOrderToView() {
        return orderToView;
    }

    public void setOrderToView(Order orderToView) {
        this.orderToView = orderToView;
    }
}
