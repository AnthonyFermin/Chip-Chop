package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Context;
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
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;


public class Fragment_SellerProfile extends Fragment {

    private Context context;
    Button addButton;
    private ArrayList<Item> foodItems;
    private RecyclerView itemsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller_profile, container, false);


        foodItems = new ArrayList<>();
        populateItems();

        itemsList = (RecyclerView) root.findViewById(R.id.seller_profile_items_list);
        itemsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        FoodListAdapter foodListAdapter = new FoodListAdapter(getActivity(), foodItems);
        itemsList.setAdapter(foodListAdapter);

        addButton = (Button) root.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellActivity activity = (SellActivity) context;
                activity.replaceSellerFragment(new Fragment_Seller_CreateItem());
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
