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
import android.support.v4.app.ActivityCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_CreateItem;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Items;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_Orders;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_ProfileSettings;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class SellActivity extends AppCompatActivity {

    public static final String TAG = "Sell Activity";
    Button contactButton;
    View coordinatorLayoutView;
    FrameLayout frameLayout;
    LinearLayout DrawerLinear;
    RelativeLayout loadingPanel;

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

    public static final String SHOWCASE_ID = "SHOWCASE_ID_7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        bindViews();
        initializeData();
        setUpNavActionBar();
        initializeUser();

    }

    private void setUpNavActionBar() {
        Log.d(TAG,"Setting up Nav Drawer and Actionbar");
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

                Button buyButton = (Button) findViewById(R.id.buyButton);

                new MaterialShowcaseView.Builder(SellActivity.this)
                        .setTarget(buyButton)
                        .setMaskColour(Color.parseColor("#D51F27"))
                        .setFadeDuration(5)
                        .setDismissOnTouch(true)
                        .setDismissText("GOT IT")
                        .setContentText("Click here to go back to being a Buyer!")
                        .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                        .show();

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


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
        Log.d(TAG,"Initializing data");
        dbHelper = DBHelper.getDbHelper(this);
        sellerItems = new ArrayList<>();
        userInfoSP = getSharedPreferences(Constants.USER_INFO_KEY, MODE_PRIVATE);
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
        Log.d(TAG,"binding views");
        drawerUserNameTV = (TextView) findViewById(R.id.drawer_user_nameTV);
        frameLayout = (FrameLayout) findViewById(R.id.sellerFrameLayout);
        DrawerLinear = (LinearLayout) findViewById(R.id.DrawerLinear);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        contactButton = (Button) findViewById(R.id.contact_button);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
    }


    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {
        Log.d(TAG,"Selecting Fragment");
        loadingPanel.setVisibility(View.GONE);
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
        dbHelper.setSellerCookingStatus(dbHelper.getUserID(),false);
        dbHelper.removeSellersFromActiveSellers(seller, emptyCallback);
        userInfoSP.edit().clear().commit();
        dbHelper.signOutUser(emptyCallback);
        drawerUserNameTV.setText("");
        stopService(serviceIntent);
        LoginManager.getInstance().logOut();
//        stopService(new Intent(this, ServiceBuyerNotify.class));
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
        Log.d(TAG,"Initializing user");
        user=HelperMethods.getUser();
        drawerUserNameTV.setText(user.getName());

        Log.d(TAG, "Loading Seller");
        seller = dbHelper.getSellerFromDB(dbHelper.getUserID(), new DBCallback() {
            @Override
            public void runOnSuccess() {
                Log.d("Load Seller Info", "Successful: " + dbHelper.getUserID());
                setCookingStatus(seller.getIsCooking());
                loadItems();
            }

            @Override
            public void runOnFail() {
                //new seller
                Log.d("Load Seller Info", "Failed: " + dbHelper.getUserID());
                seller = null;
                selectItem(2);
            }
        });
    }

    private void loadItems() {
        Log.d(TAG,"Loading Seller Items");
        if (isCurrentlyCooking()) {
            Log.d("SELLER ID","ID: " + dbHelper.getUserID());
            dbHelper.getSellersOnSaleItems(dbHelper.getUserID(), new DBCallback() {
                @Override
                public void runOnSuccess() {
                    Log.d(TAG,"On Sale Items Loaded Successfully");
                    sellerItems.addAll(dbHelper.getSellersOnSaleItems(dbHelper.getUserID(),emptyCallback));
                    selectItem(2);
                }

                @Override
                public void runOnFail() {
                    Log.d(TAG,"On Sale Items Failed to Load");
                    sellerItems = null;
                    selectItem(2);
                }
            });
        } else {
            dbHelper.getSellerItems(dbHelper.getUserID(), new DBCallback() {
                @Override
                public void runOnSuccess() {
                    Log.d(TAG,"Seller Items Loaded Successfully");
                    sellerItems.addAll(dbHelper.getSellerItems(dbHelper.getUserID(), emptyCallback));
                    selectItem(2);
                }

                @Override
                public void runOnFail() {
                    Log.d(TAG,"Seller Items Failed to Load");
                    sellerItems = null;
                    Snackbar
                            .make(coordinatorLayoutView, "Unable To Load Items, Please Try Again", Snackbar.LENGTH_SHORT)
                            .show();
                    selectItem(2);
                }
            });
        }
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
