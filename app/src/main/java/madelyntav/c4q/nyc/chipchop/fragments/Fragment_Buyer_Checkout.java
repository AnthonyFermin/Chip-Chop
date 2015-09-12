package madelyntav.c4q.nyc.chipchop.fragments;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DeliveryDialog;
import madelyntav.c4q.nyc.chipchop.PaymentDialog;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.ReviewDialogFragment;
import madelyntav.c4q.nyc.chipchop.adapters.CheckoutListAdapter;


public class Fragment_Buyer_Checkout extends Fragment {

    ImageView confirmImage;
    Button confirmOrder;
    private ArrayList<Item> cartItems;
    private RecyclerView cartRView;
    private TextView totalPriceTV;

    private BuyActivity activity;
    private Order order;
    private DBHelper dbHelper;
    private int total;

    public static final String TAG = "fragment_buyer_checkout";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buyer_checkout, container, false);

        initializeData();
        bindViews(root);
        initializeViews();
        setListAdapter();
        setListeners();

        return root;

    }

    private void setListeners() {
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = activity.getSupportFragmentManager();
                DeliveryDialog alertDialog = new DeliveryDialog();
                alertDialog.show(fm, "fragment_alert");


            }
        });
    }

    private void setListAdapter() {
        cartRView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CheckoutListAdapter checkoutListAdapter = new CheckoutListAdapter(getActivity(), cartItems);
        cartRView.setAdapter(checkoutListAdapter);

    }

    private void initializeViews() {
        totalPriceTV.setText("$ " + total + "");

    }

    private void bindViews(View root) {
        cartRView = (RecyclerView) root.findViewById(R.id.checkout_items_list);
        totalPriceTV = (TextView) root.findViewById(R.id.total_price_tv);
        confirmOrder = (Button) root.findViewById(R.id.confirmOrderButton);
        confirmImage = (ImageView) root.findViewById(R.id.confirm_image);


    }

    private void initializeData() {
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        activity.setCurrentFragment(TAG);

        order = activity.getCurrentOrder();
        order.setBuyerID(dbHelper.getUserID());
        cartItems = order.getItemsOrdered();
        //debug
        for(Item item: cartItems){
            Log.d("Checkout Item","Buyer ID: " + item.getBuyerID());
            Log.d("Checkout Item","Seller ID: " + item.getSellerID());
            Log.d("Checkout Item","Item ID: " + item.getItemID());
            Log.d("Checkout Item","Item Name: " + item.getNameOfItem());
            Log.d("Checkout Item","Quantity Wanted: " + item.getQuantityWanted());
            Log.d("Checkout Item","          ------");
        }

        total = 0;
        for(Item item: cartItems){
            item.setBuyerID(dbHelper.getUserID());
            total = total + (item.getPrice() * item.getQuantityWanted());
        }
    }

}
