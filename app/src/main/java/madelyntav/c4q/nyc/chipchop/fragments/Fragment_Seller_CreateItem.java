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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
    Button addButton;
    EditText dishNameET;
    EditText portionsET;
    EditText descriptionET;
    CheckBox vegCB, glutFreeCB, dairyCB, eggsCB, peanutsCB, shellfishCB;

    public static final String TAG = "fragment_seller_create_item";

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

        bindViews(root);
        initializeData();
        setListeners();

        return root;
    }

    private void editItem(){
        Item itemToEdit = activity.getItemToEdit();
        dishNameET.setText(itemToEdit.getNameOfItem());
        portionsET.setText(itemToEdit.getQuantity() + "");
        descriptionET.setText(itemToEdit.getDescriptionOfItem());
        String price = itemToEdit.getPrice() + "";
        if(price.contains(".") && price.length() > 2){
            int decInd = price.indexOf('.');
            String dollarAmt = price.substring(0, decInd);
//            String centAmt = price.substring(decInd + 1);
            dollarPriceET.setText(dollarAmt);
//            centPriceET.setText(centAmt);
        }else{
            dollarPriceET.setText(price);
//            centPriceET.setText("00");
        }

        vegCB.setChecked(itemToEdit.isVegetarian());
        glutFreeCB.setChecked(itemToEdit.isGlutenFree());
        dairyCB.setChecked(itemToEdit.isContainsDairy());
        eggsCB.setChecked(itemToEdit.isContainsEggs());
        peanutsCB.setChecked(itemToEdit.isContainsPeanuts());
        shellfishCB.setChecked(itemToEdit.isContainsShellfish());
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

        activity.setCurrentFragment(TAG);
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
                item.setIsVegetarian(vegCB.isChecked());
                item.setGlutenFree(glutFreeCB.isChecked());
                item.setContainsDairy(dairyCB.isChecked());
                item.setContainsEggs(eggsCB.isChecked());
                item.setContainsPeanuts(peanutsCB.isChecked());
                item.setContainsShellfish(shellfishCB.isChecked());

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

    private void bindViews(View root){


        addButton = (Button) root.findViewById(R.id.add_item_button);

        dishNameET = (EditText) root.findViewById(R.id.dish_name);
        portionsET = (EditText) root.findViewById(R.id.portions);
        descriptionET = (EditText) root.findViewById(R.id.description);
        dollarPriceET = (EditText) root.findViewById(R.id.price_dollar_amount);
//        centPriceET = (EditText) root.findViewById(R.id.price_cents_amount);

        dishPhotoButton = (ImageButton) root.findViewById(R.id.dish_image);

        vegCB = (CheckBox) root.findViewById(R.id.veg_cb);
        glutFreeCB = (CheckBox) root.findViewById(R.id.glut_free_cb);
        dairyCB = (CheckBox) root.findViewById(R.id.dairy_cb);
        eggsCB = (CheckBox) root.findViewById(R.id.eggs_cb);
        peanutsCB = (CheckBox) root.findViewById(R.id.peanuts_cb);
        shellfishCB = (CheckBox) root.findViewById(R.id.shellfish_cb);

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
            dishPhotoButton.setImageURI(imageFileUri);
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
