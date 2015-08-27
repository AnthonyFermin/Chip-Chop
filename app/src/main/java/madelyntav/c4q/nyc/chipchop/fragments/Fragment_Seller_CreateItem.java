package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Seller_CreateItem extends Fragment {

    EditText dollarPriceET, centPriceET;
    ImageButton dishPhotoButton;
    ImageView dishPhoto;
    Button addButton;
    EditText dishNameET;
    EditText portionsET;
    EditText descriptionET;

    DBHelper dbHelper;
    SellActivity activity;

    public static final int RESULT_OK = -1;
    private Uri imageFileUri;
    Intent intent;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";
    private DBCallback itemAddCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller__create_item, container, false);

        initializeData();
        initializeViews(root);
        setListeners();

        return root;
    }

    private void editItem(){
        Item itemToEdit = activity.getItemToEdit();
        dishNameET.setText(itemToEdit.getNameOfItem());
        portionsET.setText(itemToEdit.getQuantity());
        descriptionET.setText(itemToEdit.getDescriptionOfItem());
        String[] price = (itemToEdit.getPrice() + "").split(".");
        dollarPriceET.setText(price[0]);
        centPriceET.setText(price[1]);
    }

    private void initializeData(){

        dbHelper = DBHelper.getDbHelper(getActivity());
        activity = (SellActivity) getActivity();

        if(activity.getItemToEdit() != null){
            editItem();
        }

        itemAddCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {
                Toast.makeText(activity,"Item added", Toast.LENGTH_SHORT).show();
                activity.replaceSellerFragment(new Fragment_Seller_Items());

            }

            @Override
            public void runOnFail() {
                Toast.makeText(activity,"Failed to add item", Toast.LENGTH_SHORT).show();
                activity.replaceSellerFragment(new Fragment_Seller_Items());
            }
        };
    }

    private void setListeners(){
        dishPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListViewDialog();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add item to sellers items in db and arraylist displayed in profile/items fragment recycler views
                String dishName = dishNameET.getText().toString();
                int portions = 0;
                if(!portionsET.getText().toString().isEmpty()) {
                    portions = Integer.parseInt(portionsET.getText().toString());
                }
                String price = dollarPriceET.getText().toString() + "." + centPriceET.getText().toString();
                double decimalPrice = 1;
                if(!price.isEmpty()){
                    decimalPrice = Double.parseDouble(price);
                }
                String description = descriptionET.getText().toString();

                Item item = new Item(dbHelper.getUserID(), "",dishName,portions,description, "https://tahala.files.wordpress.com/2010/12/avocado-3.jpg");
                item.setPrice(decimalPrice);

                if(activity.getItemToEdit() == null) {
                    if (activity.isCurrentlyCooking()) {
                        dbHelper.addItemToActiveSellerProfile(item, itemAddCallback);
                    } else {
                        dbHelper.addItemToSellerProfileDB(item, itemAddCallback);
                    }
                }else{
                    if (activity.isCurrentlyCooking()) {
                        dbHelper.editItemInActiveSellerProfile(item, itemAddCallback);
                    } else {
                        dbHelper.editItemInSellerProfile(item, itemAddCallback);
                    }
                }
            }
        });
    }

    private void initializeViews(View root){


        addButton = (Button) root.findViewById(R.id.add_item_button);

        dishNameET = (EditText) root.findViewById(R.id.dish_name);
        portionsET = (EditText) root.findViewById(R.id.portions);
        descriptionET = (EditText) root.findViewById(R.id.description);
        dollarPriceET = (EditText) root.findViewById(R.id.price_dollar_amount);
        centPriceET = (EditText) root.findViewById(R.id.price_cents_amount);


        dishPhoto = (ImageView) root.findViewById(R.id.dish_image);

        dishPhotoButton = (ImageButton) root.findViewById(R.id.dish_image);


    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageFileUri = data.getData();
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageFileUri = Uri.parse(stringVariable);
        }


        if (imageFileUri != null) {
            dishPhoto.setImageURI(imageFileUri);
        }
    }



    //This is for the dialog box: Camera or Gallery
    private void showListViewDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Set Dish Image");
        final String[] items = {"Camera", "Gallery"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equalsIgnoreCase("Camera")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.parse(stringVariable));

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, 0);
                    }
                }

                if (items[which].equalsIgnoreCase("Gallery")) {
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDetach() {
        activity.setItemToEdit(null);
        super.onDetach();
    }
}
