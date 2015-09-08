package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.FoodListAdapter;


public class Fragment_Buyer_SellerProfile extends Fragment {

    public static final String TAG = "fragment_buyer_seller_profile";

    android.support.design.widget.FloatingActionButton cartButton;
    private ArrayList<Item> foodItems;
    private RecyclerView foodList;

    RelativeLayout loadingPanel;
    LinearLayout containingView;

    CircleImageView storeImage;
    TextView storeName,storeDescription;

    private DBHelper dbHelper;
    private Seller seller;
    private BuyActivity activity;
    private DBCallback emptyCallback;
    private Order order;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer_seller_profile, container, false);

        initializeData();
        bindViews(root);
        initializeViews();

        setListeners();

        loadingPanel.setVisibility(root.VISIBLE);
        load();

        return root;
    }

    private void initializeViews() {
        Picasso.with(activity).load(seller.getPhotoLink()).fit().into(storeImage);
        storeName.setText(seller.getStoreName());
    }

    private void load() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("Buyer - load seller", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("Buyer - load seller", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(foodItems == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(foodItems != null) {
                    setAdapter();
                }else{
                    Toast.makeText(activity,"Seller food items not found",Toast.LENGTH_SHORT).show();
                }
                loadingPanel.setVisibility(View.GONE);
                containingView.setVisibility(View.VISIBLE);

            }
        }.execute();
    }

    private void setListeners() {
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyActivity activity = (BuyActivity) getActivity();
                activity.replaceFragment(new Fragment_Buyer_ViewCart());
            }
        });
    }

    private void setAdapter() {
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        FoodListAdapter foodListAdapter = new FoodListAdapter(getActivity(),foodItems);
        foodList.setAdapter(foodListAdapter);
    }

    private void bindViews(View root) {
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
        containingView = (LinearLayout) root.findViewById(R.id.container);
        containingView.setVisibility(View.INVISIBLE);

        cartButton = (android.support.design.widget.FloatingActionButton) root.findViewById(R.id.viewCartButton);
        foodList = (RecyclerView) root.findViewById(R.id.seller_items_list);
        storeImage = (CircleImageView) root.findViewById(R.id.profile_image);
        storeName = (TextView) root.findViewById(R.id.seller_name);
        storeDescription = (TextView) root.findViewById(R.id.store_description);
    }

    private void initializeData() {
        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }
            @Override
            public void runOnFail() {

            }
        };

        seller = activity.getSellerToView();
        foodItems = dbHelper.getSellersOnSaleItems(seller.getUID(), emptyCallback);
        order = activity.getCurrentOrder();
        order.setBuyerID(dbHelper.getUserID());
        order.setSellerID(seller.getUID());
        order.setIsActive(true);
        order.setStoreName(seller.getStoreName());
        activity.setCurrentOrder(order);
    }

}
