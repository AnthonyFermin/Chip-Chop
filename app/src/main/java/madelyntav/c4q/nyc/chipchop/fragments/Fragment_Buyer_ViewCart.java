package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SignupActivity1;
import madelyntav.c4q.nyc.chipchop.adapters.CartListAdapter;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Buyer_ViewCart extends Fragment {

    public static final String TAG = "fragment_buyer_view_cart";

    android.support.design.widget.FloatingActionButton checkoutButton;
    private ArrayList<Item> cartItems;
    private RecyclerView cartList;
    private DBHelper dbHelper;
    public static final String FROM_CHECKOUT = "FromCheckout";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buyer__view_cart, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());


        cartItems = new ArrayList<>();
        populateItems();

        cartList = (RecyclerView) root.findViewById(R.id.cart_list);
        cartList.setLayoutManager(new LinearLayoutManager(getActivity()));

        CartListAdapter cartListAdapter = new CartListAdapter(getActivity(), cartItems);
        cartList.setAdapter(cartListAdapter);

        checkoutButton = (android.support.design.widget.FloatingActionButton) root.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dbHelper.userIsLoggedIn()) {
                    ((BuyActivity) getActivity()).replaceFragment(new Fragment_Buyer_Checkout());
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences(FROM_CHECKOUT, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(FROM_CHECKOUT, true);
                    editor.apply();
                    Intent signupIntent = new Intent(getActivity(), SignupActivity1.class);
                    startActivity(signupIntent);
                }
            }
        });



        return root;

    }


    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {
            //cartItems.add(new Item("test", "Something Fancy", 3, "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }

}
