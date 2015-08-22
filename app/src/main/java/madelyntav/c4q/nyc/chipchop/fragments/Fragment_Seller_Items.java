package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;

public class Fragment_Seller_Items extends Fragment {

    private Button addButton;
    private OnHeadlineSelectedListener mCallback;
    private RecyclerView foodList;
    private RelativeLayout loadingPanel;
    private LinearLayout containingView;
    private Button saveChanges;

    private DBHelper dbHelper;
    private SellActivity activity;

    private ArrayList<Item> sellerItems = null;
    private ArrayList<Item> itemsToAdd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__items, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());
        activity = (SellActivity) getActivity();

        sellerItems = dbHelper.getSellerItems(dbHelper.getUserID());

        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
        containingView = (LinearLayout) root.findViewById(R.id.container);

        containingView.setVisibility(View.INVISIBLE);

        foodList = (RecyclerView) root.findViewById(R.id.seller_items_list);

        saveChanges = (Button) root.findViewById(R.id.save_button);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerItems.addAll(itemsToAdd);
                activity.setSellerItems(sellerItems);
                for(Item item: itemsToAdd) {
                    dbHelper.addItemToSellerProfileDB(item);
                }
                itemsToAdd = null;
                activity.setItemsToAdd(null);

                if(activity.isCurrentlyCooking()){
                    dbHelper.sendArrayListOfItemsToItemsForSale(sellerItems,dbHelper.getUserID());
                }

                SellActivity activity = (SellActivity) getActivity();
                activity.replaceSellerFragment(new Fragment_SellerProfile());
            }
        });

        addButton = (Button) root.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges.setClickable(true);
                SellActivity activity = (SellActivity) getActivity();
                activity.replaceSellerFragment(new Fragment_Seller_CreateItem());
            }
        });



        if(activity.isFromItemCreation()){
            ArrayList<Item> unsavedItemsList = activity.getSellerItems();
            itemsToAdd = activity.getItemsToAdd();
            unsavedItemsList.addAll(itemsToAdd);
            SellerItemsAdapter sellerItemsAdapter = new SellerItemsAdapter(getActivity(),unsavedItemsList);
            foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
            foodList.setAdapter(sellerItemsAdapter);
            activity.setFromItemCreation(false);

            loadingPanel.setVisibility(View.GONE);
            containingView.setVisibility(View.VISIBLE);

        }else {
            load();
        }
        return root;
    }

    private void load(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("LOAD SELLER ITEMS", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("LOAD SELLER ITEMS", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(sellerItems == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                activity.setSellerItems(sellerItems);
                itemsToAdd = activity.getItemsToAdd();

                SellerItemsAdapter sellerItemsAdapter = new SellerItemsAdapter(getActivity(),sellerItems);
                foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
                foodList.setAdapter(sellerItemsAdapter);
                Log.d("LOAD SELLER ITEMS LIST", sellerItems.toString());

                loadingPanel.setVisibility(View.GONE);
                containingView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onSellerItemSelected(int position);
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
