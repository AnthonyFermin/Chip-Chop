package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.BuyerOrdersAdapter;


public class Fragment_Buyer_Orders extends Fragment {

    private ArrayList<Item> foodItems;
    private RecyclerView foodList;

    private DBHelper dbHelper;
    private BuyActivity activity;

    public static final String TAG = "fragment_buyer_orders";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer__orders, container, false);
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        //TODO: Madelyn missing method to retrieve a list of a user's orders - Can you possibly add the orderID when you send it down too?
        //TODO: Anthony the methods for these are getAllPreviouslyBoughtOrders and getAllPreviouslySoldOrders which come in an List<Orders> containing Items
        // TODO: Madelyn Also need a method to retrieve a specific order's list of items ordered so we can display a receipt when the user clicks an order.
        //TODO: Anthony I have added the method to get an orders Items in order to create a receip for both the user and the seller
        // TODO: Madelyn will need the same methods for Active Sellers and Sellers that are not currently cooking so that we can display their orders too
        //TODO: Anthony? I am not sure what you are asking for here, I have a method which retrieves orders for a seller which can be used for
        //both active and inactive sellers. Please elaborate
        try {
            foodItems = activity.getCurrentOrder().getItemsOrdered();
        }catch(NullPointerException e){
            foodItems = new ArrayList<>();
            Toast.makeText(activity,"No orders found",Toast.LENGTH_SHORT).show();
        }

        foodList = (RecyclerView) root.findViewById(R.id.buyers_orders_list);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        BuyerOrdersAdapter buyerOrdersAdapter = new BuyerOrdersAdapter(getActivity(),foodItems);
        foodList.setAdapter(buyerOrdersAdapter);

        return root;
    }


}
