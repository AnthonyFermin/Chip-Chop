package madelyntav.c4q.nyc.chipchop.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.CheckoutListAdapter;

/**
 * Created by alvin2 on 8/26/15.
 */
public class Fragment_Seller_OrderDetails extends Fragment {

    FloatingActionButton completedButton;
    private ArrayList<Item> foodItems;
    private RecyclerView foodList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_seller_orderdetail, container, false);


        foodItems = new ArrayList<>();
        populateItems();

        foodList = (RecyclerView) root.findViewById(R.id.checkout_items_list);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        CheckoutListAdapter checkoutListAdapter = new CheckoutListAdapter(getActivity(),foodItems);
        foodList.setAdapter(checkoutListAdapter);

        completedButton = (FloatingActionButton) root.findViewById(R.id.completedButton);
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send notification out to buyer! Subtract from seller's quantity??
                Toast.makeText(getActivity().getApplicationContext(), "Order Completed!", Toast.LENGTH_SHORT).show();
            }
        });


        return root;

    }



    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {

            foodItems.add(new Item("test", "Something Fancy", 3, "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }


    public interface OnBuyerCheckoutFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
