package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;
import madelyntav.c4q.nyc.chipchop.adapters.SellerOrdersAdapter;

public class Fragment_Seller_Orders extends Fragment {

    private ArrayList<Item> foodOrders = null;
    private RecyclerView orderList;

    public static final String TAG = "fragment_seller_orders";
    private SellActivity activity;
    private DBHelper dbHelper;
    private DBCallback emptyCallback;

    private RelativeLayout loadingPanel;
    private LinearLayout containingView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__orders, container, false);

        initializeData();
        bindViews(root);
        loadOrders();
        setListAdapter();


        return root;
    }

    private void loadOrders() {
//        foodOrders = dbHelper.getAllPreviouslySoldOrders(dbHelper.getUserID(), emptyCallback);
    }

    private void setListAdapter() {
        orderList.setLayoutManager(new LinearLayoutManager(activity));

        SellerOrdersAdapter sellerOrdersAdapter = new SellerOrdersAdapter(activity,foodOrders);
        orderList.setAdapter(sellerOrdersAdapter);
    }

    private void bindViews(View root) {
        orderList = (RecyclerView) root.findViewById(R.id.sellers_orders_list);
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
        containingView = (LinearLayout) root.findViewById(R.id.container);
    }

    private void initializeData() {
        activity = (SellActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        activity.setCurrentFragment(TAG);

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };
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
                }while(foodOrders == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(foodOrders != null) {
                    setListAdapter();
                    Log.d("LOAD ORDERS", "Complete");
                }

                loadingPanel.setVisibility(View.GONE);
                containingView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}
