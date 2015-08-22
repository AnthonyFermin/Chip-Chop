package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Intent;
import android.net.Uri;
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
import madelyntav.c4q.nyc.chipchop.SignupActivity1;
import madelyntav.c4q.nyc.chipchop.adapters.CheckoutListAdapter;


public class Fragment_Buyer_Checkout extends Fragment {

    Button confirmOrder;
    private ArrayList<Item> foodItems;
    private RecyclerView foodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_buyer_checkout, container, false);


        foodItems = new ArrayList<>();
        populateItems();

        foodList = (RecyclerView) root.findViewById(R.id.checkout_items_list);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));

        CheckoutListAdapter checkoutListAdapter = new CheckoutListAdapter(getActivity(),foodItems);
        foodList.setAdapter(checkoutListAdapter);

        confirmOrder = (Button) root.findViewById(R.id.confirmOrderButton);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: check if signed in, if not then go to signupactivity
                Intent signupIntent = new Intent(getActivity(), SignupActivity1.class);
                startActivity(signupIntent);
            }
        });


        return root;

    }



    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {
           // foodItems.add(new Item("test", "Something Fancy", 3, "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }


    public interface OnBuyerCheckoutFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
