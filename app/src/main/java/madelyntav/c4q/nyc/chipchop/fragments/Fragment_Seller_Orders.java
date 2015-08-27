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

import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;
import madelyntav.c4q.nyc.chipchop.adapters.SellerOrdersAdapter;

public class Fragment_Seller_Orders extends Fragment {

    OnHeadlineSelectedListener mCallback;
    private ArrayList<Item> foodOrders;
    private RecyclerView orderList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__orders, container, false);


        foodOrders = new ArrayList<>();
        populateItems();

        orderList = (RecyclerView) root.findViewById(R.id.sellers_orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        SellerOrdersAdapter sellerOrdersAdapter = new SellerOrdersAdapter(getActivity(),foodOrders);
        orderList.setAdapter(sellerOrdersAdapter);

        return root;
    }

    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {

            foodOrders.add(new Item("test", "Something Fancy", 3, "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }


    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onSellerOrderSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBuyerOrderSelectedListener");
        }
    }





}
