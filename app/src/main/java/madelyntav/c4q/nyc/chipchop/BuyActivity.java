package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

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

    private RatingBar ratingBar;
    private Button contactButton;
    private FrameLayout frameLayout;
    private LinearLayout DrawerLinear;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mListTitles;
    private Fragment fragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView drawerUserNameTV;
    public ArrayList<Order> orders;

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
    View coordinatorLayoutView;

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
        if (isLoggedIn) {
            String email = userInfoSP.getString(SignupActivity1.SP_EMAIL, null);
            String pass = userInfoSP.getString(SignupActivity1.SP_PASS, null);

            if (email != null && pass != null) {
                Log.d("AUTO LOG-IN", "" + email);
                dbHelper.logInUser(email, pass, new DBCallback() {
                    @Override
                    public void runOnSuccess() {
                        load();
                        ratingBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void runOnFail() {
                        // clears user login info if login authentication failed
                        clearLogin();
                    }
                });
            }
        } else {
            clearLogin();
        }
    }

    private void clearLogin() {
        stopService(new Intent(this, ServiceBuyerNotify.class));
        stopService(new Intent(this, ServiceSellerNotify.class));
        userInfoSP.edit().clear().commit();
        mListTitles[3] = "Sign In";
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.navdrawer_list_item, mListTitles));
        dbHelper.signOutUser(emptyCallback);
        drawerUserNameTV.setText("");
        HelperMethods.setUser(null);
        ratingBar.setVisibility(View.GONE);
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isLoggedIn = userInfoSP.getBoolean(SignupActivity1.SP_IS_LOGGED_IN, false);
        if (isLoggedIn) {

        }

        if (dbHelper.userIsLoggedIn()) {
            mListTitles[3] = "Sign Out";
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.navdrawer_list_item, mListTitles));
        }

    }

    private void selectFragmentToLoad(Bundle savedInstanceState) {

        //if items are still in cart go to checkout
        SharedPreferences sPref = getSharedPreferences(Fragment_Buyer_ViewCart.FROM_CART, MODE_PRIVATE);
        boolean fromCart = sPref.getBoolean(Fragment_Buyer_ViewCart.FROM_CART, false);
        Intent intent = getIntent();
        boolean toOrder = false;

        if (intent != null) {
            toOrder = getIntent().getBooleanExtra("To Orders View", false);
        }
        if (toOrder) {
            orderToView = HelperMethods.getCurrentOrder();
            replaceFragment(new Fragment_Buyer_OrderDetails());
        } else if (fromCart
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
//
            }

            public void onDrawerOpened(View drawerView) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                Button sellButton = (Button) findViewById(R.id.sellButton);
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (dbHelper.userIsLoggedIn()) {
                            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
                            startActivity(sellIntent);
                        } else {
                            mDrawerLayout.closeDrawer(DrawerLinear);
                            Snackbar
                                    .make(coordinatorLayoutView, "Must be logged for this feature", Snackbar.LENGTH_SHORT)
                                    .show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent signUpIntent = new Intent(getApplicationContext(), SignupActivity1.class);
                                    signUpIntent.putExtra(TO_SELL_ACTIVITY, true);
                                    startActivity(signUpIntent);
                                    finish();
                                }
                            }, 1000);

                        }
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

    private void bindViews() {
        drawerUserNameTV = (TextView) findViewById(R.id.drawer_user_nameTV);
        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        contactButton = (Button) findViewById(R.id.contact_button);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

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
            if (dbHelper.userIsLoggedIn())
                fragment = new Fragment_Buyer_Orders();
            else {
                clearLogin();
                Snackbar
                        .make(coordinatorLayoutView, "Must log in to view profile", Snackbar.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(BuyActivity.this, SignupActivity1.class));
                    }
                }, 1500);
            }
        } else if (position == 2) {
            if (dbHelper.userIsLoggedIn())
                fragment = new Fragment_Buyer_ProfileSettings();
            else {
                clearLogin();
                Snackbar
                        .make(coordinatorLayoutView, "Must log in to view profile", Snackbar.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(BuyActivity.this, SignupActivity1.class));
                    }
                }, 1500);
            }
        } else if (position == 3) {
            //SIGN OUT/IN DRAWER ITEM
            boolean isLoggedIn = userInfoSP.getBoolean(SignupActivity1.SP_IS_LOGGED_IN, false);
            if (isLoggedIn) {
                clearLogin();
                user = null;
                LoginManager.getInstance().logOut();
                Snackbar
                        .make(coordinatorLayoutView, "Sign out successful", Snackbar.LENGTH_SHORT)
                        .show();

                //if not currently in fragment_buyer_map, replace current fragment with buyer_map fragment
                if (!getCurrentFragment().equals(Fragment_Buyer_Map.TAG)) {
                    replaceFragment(new Fragment_Buyer_Map());
                }

            } else {
                Intent intent = new Intent(BuyActivity.this, SignupActivity1.class);
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
    public void onConfigurationChanged(Configuration newConfig) {
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
        switch (getCurrentFragment()) {
            case (Fragment_Buyer_Checkout.TAG):
                replaceFragment(new Fragment_Buyer_ViewCart());
                break;
            case (Fragment_Buyer_Map.TAG):
                super.onBackPressed();
                break;
            case (Fragment_Buyer_Orders.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case (Fragment_Buyer_ProfileSettings.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case (Fragment_Buyer_SellerProfile.TAG):
                replaceFragment(new Fragment_Buyer_Map());
                break;
            case (Fragment_Buyer_ViewCart.TAG):
                if (getLastFragment() != null && getLastFragment().equals(Fragment_Buyer_ViewCart.TAG)) {
                    replaceFragment(new Fragment_Buyer_Map());
                } else {
                    replaceFragment(new Fragment_Buyer_SellerProfile());
                }
                break;
            case (Fragment_Buyer_OrderDetails.TAG):
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

    private void load() {

        String name = userInfoSP.getString(SignupActivity1.SP_NAME, "");
        String email = userInfoSP.getString(SignupActivity1.SP_EMAIL, "");
        String streetAddress = userInfoSP.getString(SignupActivity1.SP_ADDRESS, "");
        String apt = userInfoSP.getString(SignupActivity1.SP_APT, "");
        String city = userInfoSP.getString(SignupActivity1.SP_CITY, "");
        String state = userInfoSP.getString(SignupActivity1.SP_STATE, "");
        String zipcode = userInfoSP.getString(SignupActivity1.SP_ZIPCODE, "");
        String phoneNumber = userInfoSP.getString(SignupActivity1.SP_PHONE_NUMBER, "");
        String photoLink = userInfoSP.getString(SignupActivity1.SP_PHOTO_LINK, "");
        Address address = new Address(streetAddress, apt, city, state, zipcode, dbHelper.getUserID());

        user = new User(dbHelper.getUserID(), email, name, address, photoLink, phoneNumber);
        HelperMethods.setUser(user);

        drawerUserNameTV.setText(user.getName());
        mListTitles[3] = "Sign Out";
        mDrawerList.setAdapter(new ArrayAdapter<>(BuyActivity.this,
                R.layout.navdrawer_list_item, mListTitles));

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
        return HelperMethods.getSellerToView();
    }

    public void setSellerToView(Seller sellerToView) {
        HelperMethods.setSellerToView(sellerToView);
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

    public void checkIfLastOrderHasBeenReviewedAndIfNotSetReview(){
        dbHelper.getAllPreviouslyBoughtOrders(user.getUID(), new DBCallback() {
            @Override
            public void runOnSuccess() {
                orders.addAll(dbHelper.getAllPreviouslyBoughtOrders(user.getUID(), emptyCallback));


                if(!orders.get(0).isReviewed()){

                }

            }

            @Override
            public void runOnFail() {

            }
        });

    }
}
