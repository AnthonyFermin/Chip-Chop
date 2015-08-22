package madelyntav.c4q.nyc.chipchop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Seller_CreateItem extends Fragment {

    Button addButton;
    EditText dishNameET;
    EditText portionsET;
    EditText priceET;
    EditText descriptionET;

    DBHelper dbHelper;
    SellActivity activity;

    ArrayList<Item> itemsToAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__create_item, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());
        activity = (SellActivity) getActivity();

        itemsToAdd = activity.getItemsToAdd();
        if(itemsToAdd == null) {
            itemsToAdd = new ArrayList<>();
        }

        dishNameET = (EditText) root.findViewById(R.id.dish_name);
        portionsET = (EditText) root.findViewById(R.id.portions);
        priceET = (EditText) root.findViewById(R.id.price);
        descriptionET = (EditText) root.findViewById(R.id.description);

        addButton = (Button) root.findViewById(R.id.add_item_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add item to sellers items in db and arraylist displayed in profile/items fragment recycler views
                String dishName = dishNameET.getText().toString();
                int portions = 0;
                if(!portionsET.getText().toString().isEmpty()) {
                    portions = Integer.parseInt(portionsET.getText().toString());
                }
                double price = 1.00;
                if(!priceET.getText().toString().isEmpty()){
                    price = Double.parseDouble(priceET.getText().toString());
                }
                String description = descriptionET.getText().toString();

                Item item = new Item("",dbHelper.getUserID(),dishName,portions,price,description, "https://tahala.files.wordpress.com/2010/12/avocado-3.jpg");
                itemsToAdd.add(item);
                activity.setItemsToAdd(itemsToAdd);
                activity.setFromItemCreation(true);

                activity.replaceSellerFragment(new Fragment_Seller_Items());

            }
        });


        return root;
    }
}
