package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.HelperMethods;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SignupActivity1;
import madelyntav.c4q.nyc.chipchop.adapters.CartListAdapter;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Buyer_ViewCart extends Fragment {

    public static final String TAG = "fragment_buyer_view_cart";
    public static final String FROM_CART = "from_cart";

    android.support.design.widget.FloatingActionButton checkoutButton;
    private ArrayList<Item> cartItems;
    private RecyclerView cartList;
    private DBHelper dbHelper;
    private Order order;
    private BuyActivity activity;
    View coordinatorLayoutView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buyer__view_cart, container, false);

        initializeData();
        bindViews(root);
        setListAdapter();
        setListeners();

        return root;

    }

    private void setListeners() {
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!dbHelper.userIsLoggedIn()) {
                    User user = HelperMethods.getUser();
                    order.setBuyerName(user.getName());
                    order.setBuyerAddress(user.getAddressString());
                    Intent signupIntent = new Intent(getActivity(), SignupActivity1.class);
                    SharedPreferences sPref = activity.getSharedPreferences(FROM_CART, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sPref.edit();
                    editor.putBoolean(FROM_CART,true);
                    editor.commit();
                    startActivity(signupIntent);
                } else if(cartItems.size() == 0){
                    Snackbar
                            .make(coordinatorLayoutView, "Cart is empty", Snackbar.LENGTH_SHORT)
                            .show();

                }else {
                    activity.replaceFragment(new Fragment_Buyer_Checkout());
                }
            }
        });
    }

    private void setListAdapter() {
        cartList.setLayoutManager(new LinearLayoutManager(activity));

        CartListAdapter cartListAdapter = new CartListAdapter(activity, cartItems);
        cartList.setAdapter(cartListAdapter);
    }

    private void bindViews(View root) {
        checkoutButton = (android.support.design.widget.FloatingActionButton) root.findViewById(R.id.checkoutButton);
        cartList = (RecyclerView) root.findViewById(R.id.cart_list);
        coordinatorLayoutView = root.findViewById(R.id.snackbarPosition);

    }

    private void initializeData() {
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);

        activity.setCurrentFragment(TAG);

        order = activity.getCurrentOrder();
        cartItems = order.getItemsOrdered();
        //debug
        for(Item item : cartItems){
            Log.d("Item",item.getNameOfItem() + "");
        }
    }


}
