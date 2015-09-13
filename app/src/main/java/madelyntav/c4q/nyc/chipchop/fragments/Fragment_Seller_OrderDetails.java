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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


    public static NotificationManager notificationManager;
    public static Notification notification;
    public static final String NOTIFICATION_ACTION = "ahhhlvin.c4q.nyc.notification";
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
                //dbHelper.changeOrderStatus(order);
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

        foodItems = dbHelper.getReceiptForSpecificOrderForSeller(order.getOrderID(), dbHelper.getUserID(), new DBCallback() {
            @Override
            public void runOnSuccess() {
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


    public interface OnBuyerCheckoutFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
