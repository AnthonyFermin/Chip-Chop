package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.CartListAdapter;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Buyer_ViewCart extends Fragment {

    private ArrayList<Item> cartItems;
    private RecyclerView cartList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buyer__view_cart, container, false);


        cartItems = new ArrayList<>();
        populateItems();

        cartList = (RecyclerView) root.findViewById(R.id.cart_list);
        cartList.setLayoutManager(new LinearLayoutManager(getActivity()));

        CartListAdapter cartListAdapter = new CartListAdapter(getActivity(), cartItems);
        cartList.setAdapter(cartListAdapter);


        return root;

    }


    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {
            cartItems.add(new Item("test", "Something Fancy", "3", "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }

}
