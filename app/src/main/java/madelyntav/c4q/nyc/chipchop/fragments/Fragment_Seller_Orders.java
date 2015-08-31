package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;
import madelyntav.c4q.nyc.chipchop.adapters.SellerOrdersAdapter;

public class Fragment_Seller_Orders extends Fragment {

    private ArrayList<Item> foodOrders;
    private RecyclerView orderList;

    public static final String TAG = "fragment_seller_orders";
    private SellActivity activity;
    private DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__orders, container, false);
        activity = (SellActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);
        activity.setCurrentFragment(TAG);

        foodOrders = new ArrayList<>();

        orderList = (RecyclerView) root.findViewById(R.id.sellers_orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        SellerOrdersAdapter sellerOrdersAdapter = new SellerOrdersAdapter(getActivity(),foodOrders);
        orderList.setAdapter(sellerOrdersAdapter);

        return root;
    }

}
