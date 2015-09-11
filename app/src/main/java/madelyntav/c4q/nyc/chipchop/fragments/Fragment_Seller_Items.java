package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

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

    private android.support.design.widget.FloatingActionButton addButton;
    private RelativeLayout loadingPanel;

    public static final String TAG = "fragment_seller_items";

    private DBHelper dbHelper;
    private SellActivity activity;

    private ArrayList<Item> sellerItems = null;
    private ArrayList<Item> inActiveItems = null;

    private RecyclerView inactiveList;
    private RecyclerView activeList;
    private final String INACTIVE_LIST_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "inactive_list";


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

    private void initializeData(){
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

        //seller items
        if(activity.isCurrentlyCooking()) {
            sellerItems = dbHelper.getSellersOnSaleItems(dbHelper.getUserID(),emptyCallback);
        }else{
            sellerItems = dbHelper.getSellerItems(dbHelper.getUserID(), emptyCallback);
        }
        inActiveItems = loadInactiveItems();
        activity.setInactiveSellerItems(inActiveItems);

        activity.setCurrentFragment(TAG);
    }

    private ArrayList<Item> loadInactiveItems() {
        ArrayList<Item> items = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(INACTIVE_LIST_DIRECTORY);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<Item>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return items;
    }

    private void loadActiveItems(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("LOAD SELLER ITEMS", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("LOAD SELLER ITEMS", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(sellerItems == null || sellerItems.size() == 0);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                activity.setSellerItems(sellerItems);

                activeList.getAdapter().notifyDataSetChanged();
                inactiveList.getAdapter().notifyDataSetChanged();

                Log.d("LOAD SELLER ITEMS LIST", sellerItems.size() + "");

                loadingPanel.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void initializeViews(View root){
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);

        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        addButton = (android.support.design.widget.FloatingActionButton) root.findViewById(R.id.addButton);

    }

    private void setListeners(){

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
            if(position == 0){
                return "Active Dishes";
            }
            if(position == 1){
                return "InActive Dishes";
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
        try {
            FileOutputStream fout = new FileOutputStream(INACTIVE_LIST_DIRECTORY);

            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(inActiveItems);

            fout.close();
            oos.close();

            Toast.makeText(activity,"Inactive dishes saved to device",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
