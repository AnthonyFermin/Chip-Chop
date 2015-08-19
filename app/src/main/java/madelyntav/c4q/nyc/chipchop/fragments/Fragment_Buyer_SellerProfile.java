package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SignupActivity1;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;


public class Fragment_Buyer_SellerProfile extends Fragment {

    public static final String FROM_CHECKOUT = "FromCheckout";
    Button checkoutButton, cartButton;
    private ArrayList<Item> foodItems;
    private RecyclerView foodList;
    private DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer_seller_profile, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());

        foodItems = new ArrayList<>();
        populateItems();

        foodList = (RecyclerView) root.findViewById(R.id.seller_items_list);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        FoodListAdapter foodListAdapter = new FoodListAdapter(getActivity(),foodItems);
        foodList.setAdapter(foodListAdapter);

        checkoutButton = (Button) root.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dbHelper.userIsLoggedIn()) {
                    ((BuyActivity) getActivity()).replaceFragment(new Fragment_Buyer_Checkout());
                }else{
                    SharedPreferences preferences = getActivity().getSharedPreferences(FROM_CHECKOUT, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(FROM_CHECKOUT, true);
                    editor.apply();
                    Intent signupIntent = new Intent(getActivity(), SignupActivity1.class);
                    startActivity(signupIntent);
                }
            }
        });


        cartButton = (Button) root.findViewById(R.id.viewCartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyActivity activity = (BuyActivity) getActivity();
                activity.replaceFragment(new Fragment_Buyer_ViewCart());
            }
        });

        return root;
    }

    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {
            foodItems.add(new Item("test", "Something Fancy", "3", "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }

    public interface OnSellerProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
