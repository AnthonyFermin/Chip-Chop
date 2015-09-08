package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.BuyerOrdersAdapter;


public class Fragment_Buyer_Orders extends Fragment {

    private ArrayList<Order> foodOrders;
    private RecyclerView foodList;

    private DBHelper dbHelper;
    private BuyActivity activity;
    private DBCallback emptyCallback;

    private RelativeLayout loadingPanel;
    private LinearLayout containingView;

    public static final String TAG = "fragment_buyer_orders";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer__orders, container, false);
        activity = (BuyActivity) getActivity();
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

        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
        containingView = (LinearLayout) root.findViewById(R.id.container);

        foodOrders = dbHelper.getAllPreviouslyBoughtOrders(dbHelper.getUserID(),emptyCallback);

        foodList = (RecyclerView) root.findViewById(R.id.buyers_orders_list);

        loadList();

        return root;
    }

    private void loadList(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("LOAD BUYER ORDERS", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("LOAD BUYER ORDERS", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(foodOrders.size() == 0);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(foodOrders != null) {
                    for(Order order: foodOrders){
                        Log.d("Order Id","" + order.getOrderID());
                    }

                    Collections.sort(foodOrders, new Comparator<Order>() {
                        @Override
                        public int compare(Order order, Order t1) {
                            if (order.getTimeStamp() > t1.getTimeStamp()) {
                                return -1;
                            } else if (order.getTimeStamp() < t1.getTimeStamp()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    setListAdapter();
                    Log.d("LOAD ORDERS", "Complete");

                }

                loadingPanel.setVisibility(View.GONE);
                containingView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void setListAdapter() {
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        BuyerOrdersAdapter buyerOrdersAdapter = new BuyerOrdersAdapter(activity, foodOrders);
        foodList.setAdapter(buyerOrdersAdapter);
    }


}
