package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;

public class Fragment_Seller_Items extends Fragment {

    /**
     * The number of pages (wizard steps) to show in this demo.
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
    private RecyclerView foodList;
    private RelativeLayout loadingPanel;
    private RelativeLayout containingView;

    public static final String TAG = "fragment_seller_items";

    private DBHelper dbHelper;
    private SellActivity activity;

    private ArrayList<Item> sellerItems = null;

    DBCallback emptyCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__items, container, false);

        initializeData();
        initializeViews(root);
        setListeners();
        loadList();


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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
        activity.setCurrentFragment(TAG);
    }

    private void loadList(){
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
                }while(sellerItems == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                activity.setSellerItems(sellerItems);

                SellerItemsAdapter sellerItemsAdapter = new SellerItemsAdapter(getActivity(),sellerItems);
//                foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
//                foodList.setAdapter(sellerItemsAdapter);
                Log.d("LOAD SELLER ITEMS LIST", sellerItems.toString());

                loadingPanel.setVisibility(View.GONE);
//                containingView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void initializeViews(View root){
//
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
//        containingView = (RelativeLayout) root.findViewById(R.id.container);
//
//        containingView.setVisibility(View.INVISIBLE);
//
//        foodList = (RecyclerView) root.findViewById(R.id.seller_items_list);
        coordinatorLayoutView = root.findViewById(R.id.snackbarPosition);
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
                fragment = new ActiveSellerItems();
                Bundle args = new Bundle();
                args.putInt(ActiveSellerItems.ARG_OBJECT, position);
            } else if (position == 1) {
                fragment = new InactiveSellerItems();
                Bundle args = new Bundle();
                args.putInt(InactiveSellerItems.ARG_OBJECT, position + 1);
            }

//            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }



    }




    public static class ActiveSellerItems extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.active_seller_items, container, false);
//            Bundle args = getArguments();
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }

    public static class InactiveSellerItems extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.inactive_seller_items, container, false);
//            Bundle args = getArguments();
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }



}
