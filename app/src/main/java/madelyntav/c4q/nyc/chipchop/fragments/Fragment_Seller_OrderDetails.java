package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    public static NotificationManager notificationManager;
    public static Notification notification;
    public static final String NOTIFICATION_ACTION = "ahhhlvin.c4q.nyc.notification";



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
                sendCompleteNotification();
            }
        });


        // NOTIFICATION CODE
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity().getApplicationContext())
                        .setSmallIcon(R.drawable.chipchop_small)
                        .setContentTitle("ChipChop")
                        .setContentText("Your order is ready!");

        mBuilder.setAutoCancel(true);
        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        return root;

    }



    //test method to populate RecyclerView
    private void populateItems(){
        for(int i = 0; i < 10; i++) {

            foodItems.add(new Item("test", "Something Fancy", 3, "The fanciest homemade meal you've ever had", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }

    public void sendCompleteNotification() {
        // getPackageName() will take "ahhhlvin.c4q.nyc" if you aren't creating a public final String for the intent
        Intent intent = new Intent(NOTIFICATION_ACTION);
        getActivity().sendBroadcast(intent);
    }


    public interface OnBuyerCheckoutFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
