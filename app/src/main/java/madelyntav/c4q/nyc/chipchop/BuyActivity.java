package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Checkout;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Map;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_OrderDetails;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Orders;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ProfileSettings;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_SellerProfile;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_ViewCart;

public class BuyActivity extends AppCompatActivity {

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

    private String lastFragment;
    private String currentFragment;
    private Seller sellerToView = null;
    private Item itemToCart = null;
    private Order currentOrder;
    private Order orderToView;

    public static final String TO_SELL_ACTIVITY = "to_sell";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        bindViews();
        initializeData();
        checkIsLogged();
        setUpDrawer();
        selectFragmentToLoad(savedInstanceState);
    }

    private void checkIsLogged() {
        userInfoSP = getSharedPreferences(SignupActivity1.SP_USER_INFO, MODE_PRIVATE);
        boolean isLoggedIn = userInfoSP.getBoolean(SignupActivity1.SP_IS_LOGGED_IN, false);
        if(isLoggedIn){
            String email = userInfoSP.getString(SignupActivity1.SP_EMAIL, null);
            String pass = userInfoSP.getString(SignupActivity1.SP_PASS, null);

            if(email != null && pass != null){
                Log.d("AUTO LOG-IN",email + ", " + pass);
                loadingPanel.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                DrawerLinear.setVisibility(View.INVISIBLE);
                dbHelper.logInUser(email,pass, new DBCallback() {
                    @Override
                    public void runOnSuccess() {
                        load();
                    }

                    @Override
                    public void runOnFail() {

                        clearLogin();
                        // clears user login info if login authentication failed
                        loadingPanel.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        DrawerLinear.setVisibility(View.VISIBLE);
                    }
                });
            }
        }else{
            clearLogin();
        }
    }

    private void clearLogin() {
        userInfoSP.edit().clear().commit();
        mListTitles[3] = "Sign In";
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.navdrawer_list_item, mListTitles));
        dbHelper.signOutUser(emptyCallback);
        drawerUserNameTV.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isLoggedIn = userInfoSP.getBoolean(SignupActivity1.SP_IS_LOGGED_IN, false);
        if(isLoggedIn){

        }

        if(dbHelper.userIsLoggedIn()){
            mListTitles[3] = "Sign Out";
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.navdrawer_list_item, mListTitles));
        }

    }

    private void selectFragmentToLoad(Bundle savedInstanceState) {

        //if items are still in cart go to checkout
        SharedPreferences sPref = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CART, MODE_PRIVATE);
        boolean fromCart = sPref.getBoolean(Fragment_Buyer_ViewCart.FROM_CART, false);
        if(fromCart
                && getCurrentOrder() != null
                && getCurrentOrder().getItemsOrdered() != null
                && getCurrentOrder().getItemsOrdered().size() != 0) {
            sPref.edit().clear().commit();
            replaceFragment(new Fragment_Buyer_ViewCart());
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

                Button sellButton = (Button) findViewById(R.id.sellButton);
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(dbHelper.userIsLoggedIn()){
                            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
                            startActivity(sellIntent);
                        }else{
                            Toast.makeText(BuyActivity.this,"Must be logged for this feature", Toast.LENGTH_SHORT).show();
                            Intent signUpIntent = new Intent(getApplicationContext(), SignupActivity1.class);
                            signUpIntent.putExtra(TO_SELL_ACTIVITY,true);
                            startActivity(signUpIntent);
                        }
                        finish();
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
            //SIGN OUT/IN DRAWER ITEM
            boolean isLoggedIn = userInfoSP.getBoolean(SignupActivity1.SP_IS_LOGGED_IN, false);
            if(isLoggedIn) {
                clearLogin();
                user = null;
                LoginManager.getInstance().logOut();
                Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show();

                //if not currently in fragment_buyer_map, replace current fragment with buyer_map fragment
                if (!getCurrentFragment().equals(Fragment_Buyer_Map.TAG)) {
                    replaceFragment(new Fragment_Buyer_Map());
                }

            }else{
                Intent intent = new Intent(BuyActivity.this,SignupActivity1.class);
                startActivity(intent);
                finish();  // killing the activity for now when switching to another activity
            }


        }

            // Create fragment manager to begin interacting with the fragments and the container
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).commit();


            // update selected item and title in nav drawer, then close the drawer
        mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(DrawerLinear);
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
            case(Fragment_Buyer_Checkout.TAG):
                replaceFragment(new Fragment_Buyer_ViewCart());
                break;
            case(Fragment_Buyer_Map.TAG):
                super.onBackPressed();
                break;
            case(Fragment_Buyer_Orders.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case(Fragment_Buyer_ProfileSettings.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case(Fragment_Buyer_SellerProfile.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case(Fragment_Buyer_ViewCart.TAG):
                if(getLastFragment() != null && getLastFragment().equals(Fragment_Buyer_ViewCart.TAG)){
                    replaceFragment(new Fragment_Buyer_Map());
                }else {
                    replaceFragment(new Fragment_Buyer_SellerProfile());
                }
                break;
            case(Fragment_Buyer_OrderDetails.TAG):
                replaceFragment(new Fragment_Buyer_Orders());
                break;
            default:
                super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager BuyFragmentManager = getSupportFragmentManager();
        BuyFragmentManager.beginTransaction().replace(R.id.sellerFrameLayout, fragment).commit();

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

                if(user != null && user.getAddressString() != null){
                    Address address = HelperMethods.parseAddressString(user.getAddressString(), user.getUID());
                    user.setAddress(address);
                    userInfoSP.edit()
                            .putString(SignupActivity1.SP_NAME, user.getName())
                            .putString(SignupActivity1.SP_EMAIL, user.geteMail())
                            .putString(SignupActivity1.SP_ADDRESS, address.getStreetAddress())
                            .putString(SignupActivity1.SP_APT, address.getApartment())
                            .putString(SignupActivity1.SP_CITY, address.getCity())
                            .putString(SignupActivity1.SP_ZIPCODE, address.getZipCode())
                            .putString(SignupActivity1.SP_PHONE_NUMBER, user.getPhoneNumber())
                            .commit();



                    drawerUserNameTV.setText(user.getName());
                    mListTitles[3] = "Sign Out";
                    mDrawerList.setAdapter(new ArrayAdapter<>(BuyActivity.this,
                            R.layout.navdrawer_list_item, mListTitles));

                }else{
                    Toast.makeText(BuyActivity.this,"Auto-login failed", Toast.LENGTH_SHORT).show();
                    clearLogin();
                }

                loadingPanel.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                DrawerLinear.setVisibility(View.VISIBLE);

            }
        }.execute();
    }


    // used for communication between fragments

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        setLastFragment(getCurrentFragment());
        this.currentFragment = currentFragment;
    }

    public Seller getSellerToView() {
        return sellerToView;
    }

    public void setSellerToView(Seller sellerToView) {
        this.sellerToView = sellerToView;
    }

    public Item getItemToCart() {
        return itemToCart;
    }

    public void setItemToCart(Item itemToCart) {
        this.itemToCart = itemToCart;
    }

    public Order getCurrentOrder() {
        return HelperMethods.getCurrentOrder();
    }

    public void setCurrentOrder(Order currentOrder) {
        HelperMethods.setCurrentOrder(currentOrder);
    }

    public Order getOrderToView() {
        return orderToView;
    }

    public void setOrderToView(Order orderToView) {
        this.orderToView = orderToView;
    }

    public String getLastFragment() {
        return lastFragment;
    }

    private void setLastFragment(String lastFragment) {
        this.lastFragment = lastFragment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
