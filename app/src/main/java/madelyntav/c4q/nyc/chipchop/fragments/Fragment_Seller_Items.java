package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;

public class Fragment_Seller_Items extends Fragment {

    /**
     * The number of pages for the view pager.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    Fragment fragment;

    public static View coordinatorLayoutView;

    private android.support.design.widget.FloatingActionButton addButton;
    private RelativeLayout loadingPanel;

    public static final String TAG = "fragment_seller_items";

    private DBHelper dbHelper;
    private SellActivity activity;

    private ArrayList<Item> sellerItems = null;
    private ArrayList<Item> inActiveItems = null;

    private RecyclerView inactiveList;
    private RecyclerView activeList;


    DBCallback emptyCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__items, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        initializeData();
        initializeViews(root);
        setListeners();
        loadActiveItems();


        return root;
    }

    private void initializeData() {
        dbHelper = DBHelper.getDbHelper(getActivity());
        activity = (SellActivity) getActivity();

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };

        loadSellerItems();
        inActiveItems = loadInactiveItems();
        activity.setInactiveSellerItems(inActiveItems);

        activity.setCurrentFragment(TAG);
    }

    private void loadSellerItems() {
        sellerItems = new ArrayList<>();
        if (activity.isCurrentlyCooking()) {
            Log.d("SELLER ID","ID: " + dbHelper.getUserID());
            dbHelper.getSellersOnSaleItems(dbHelper.getUserID(), new DBCallback() {
                @Override
                public void runOnSuccess() {
                    sellerItems.addAll(dbHelper.getSellersOnSaleItems(dbHelper.getUserID(),emptyCallback));
                }

                @Override
                public void runOnFail() {

                }
            });
        } else {
            dbHelper.getSellerItems(dbHelper.getUserID(), new DBCallback() {
                @Override
                public void runOnSuccess() {
                    sellerItems.addAll(dbHelper.getSellerItems(dbHelper.getUserID(), emptyCallback));
                    for(Item item: sellerItems){
                        Log.d("SELLER ITEMS","ITEM ID: " + item.getItemID());
                        Log.d("SELLER ITEMS","SELLER ID: " + item.getSellerID());
                        Log.d("SELLER ITEMS","GLUTEN FREE: " + item.isGlutenFree());
                    }
                }

                @Override
                public void runOnFail() {
                    Snackbar
                            .make(coordinatorLayoutView, "Unable To Load Items, Please Try Again", Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    private ArrayList<Item> loadInactiveItems() {
        ArrayList<Item> items = new ArrayList<>();

        dbHelper.getInactiveItems(dbHelper.getUserID(), new DBCallback() {
            @Override
            public void runOnSuccess() {
                inActiveItems.addAll(dbHelper.getInactiveItems(dbHelper.getUserID(),emptyCallback));
                if(inactiveList != null){
                    inactiveList.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void runOnFail() {

            }
        });

        return items;
    }

    private void loadActiveItems() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do {
                    Log.d("LOAD SELLER ITEMS", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 20) {
                        Log.d("LOAD SELLER ITEMS", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                } while (sellerItems == null || sellerItems.size() == 0);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                activity.setSellerItems(sellerItems);

                if(activeList != null)
                    activeList.getAdapter().notifyDataSetChanged();
                if(inactiveList != null)
                    inactiveList.getAdapter().notifyDataSetChanged();

                Log.d("LOAD SELLER ITEMS LIST", sellerItems.size() + "");


                loadingPanel.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void initializeViews(View root) {
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);

        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        coordinatorLayoutView = root.findViewById(R.id.snackbarPosition);
        addButton = (android.support.design.widget.FloatingActionButton) root.findViewById(R.id.addButton);

    }

    private void setListeners() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replaceSellerFragment(new Fragment_Seller_CreateItem());
            }
        });

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                fragment = new Fragment_Seller_Items_Active();
            } else if (position == 1) {
                fragment = new Fragment_Seller_Items_Inactive();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "ACTIVE";
            }
            if (position == 1) {
                return "INACTIVE";
            }
            return "";
        }


    }


    public ArrayList<Item> getActiveItems() {
        return sellerItems;
    }

    public void setActiveItems(ArrayList<Item> activeItems) {
        this.sellerItems = activeItems;
        activity.setSellerItems(activeItems);
    }

    public ArrayList<Item> getInActiveItems() {
        return inActiveItems;
    }

    public void setInActiveItems(ArrayList<Item> inActiveItems) {
        this.inActiveItems = inActiveItems;
    }

    public RecyclerView getInactiveList() {
        return inactiveList;
    }

    public void setInactiveList(RecyclerView inactiveList) {
        this.inactiveList = inactiveList;
    }

    public RecyclerView getActiveList() {
        return activeList;
    }

    public void setActiveList(RecyclerView activeList) {
        this.activeList = activeList;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveInactiveList();
    }

    private void saveInactiveList() {

        for(Item item: inActiveItems){
            dbHelper.addInactiveItemToSellerProfileDB(item);
        }

    }
}
