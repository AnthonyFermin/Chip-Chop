package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.CheckoutListAdapter;

/**
 * Created by alvin2 on 8/26/15.
 */
public class Fragment_Seller_OrderDetails extends Fragment {

    FloatingActionButton completedButton;
    private ArrayList<Item> foodItems;
    private RecyclerView foodList;

    public static final String TAG = "fragment_seller_order_details";

    private SellActivity activity;
    private DBHelper dbHelper;
    private Order order;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_seller_orderdetail, container, false);

        initializeData();
        bindViews(root);
        setListeners();

        return root;

    }

    private void setListeners() {
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.changeOrderActiveStatus(order);
                Toast.makeText(activity,"Order Complete",Toast.LENGTH_SHORT).show();

                dbHelper.moveOrderFromActiveToFulfilled(dbHelper.getUserID(), order.getOrderID(), order.getBuyerID(), new DBCallback() {
                    @Override
                    public void runOnSuccess() {

                    }

                    @Override
                    public void runOnFail() {

                    }
                });
            }
        });
    }

    private void bindViews(View root) {
        foodList = (RecyclerView) root.findViewById(R.id.checkout_items_list);
        completedButton = (FloatingActionButton) root.findViewById(R.id.completedButton);
    }

    private void initializeData() {
        activity = (SellActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        activity.setCurrentFragment(TAG);
        order = activity.getOrderToView();
        if(order != null) {
            Log.d("ORDER DETAILS", "" + order.getOrderID());
        }
        Log.d("ORDER DETAILS","Order: " + order);

        foodItems = dbHelper.getReceiptForSpecificOrderForSeller(order.getOrderID(), dbHelper.getUserID(), new DBCallback() {
            @Override
            public void runOnSuccess() {
                Log.d("ORDER DETAILS", "ORDER ITEMS LOADED");
                for (Item item : foodItems) {
                    Log.d("ORDER DETAILS ITEM", "item name: " + item.getNameOfItem());
                }
                setAdapter();
            }

            @Override
            public void runOnFail() {

            }
        });
    }

    private void setAdapter() {
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        CheckoutListAdapter checkoutListAdapter = new CheckoutListAdapter(getActivity(),foodItems);
        foodList.setAdapter(checkoutListAdapter);
    }

}
