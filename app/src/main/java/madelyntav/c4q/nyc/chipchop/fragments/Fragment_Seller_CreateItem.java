package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.HelperMethods;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;

/**
 * Created by alvin2 on 8/16/15.
 */
public class Fragment_Seller_CreateItem extends Fragment {

    View coordinatorLayoutView;
    static String filePath;
    EditText dollarPriceET;
    ImageView dishPhotoButton;
    Button addButton;
    EditText dishNameET;
    EditText portionsET;
    EditText descriptionET;
    CheckBox vegCB, glutFreeCB, dairyCB, eggsCB, peanutsCB, shellfishCB;

    String imageLink;

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

    private void editItem(Item itemToEdit){
        dishNameET.setText(itemToEdit.getNameOfItem());
        portionsET.setText(itemToEdit.getQuantity() + "");
        descriptionET.setText(itemToEdit.getDescriptionOfItem());
        dollarPriceET.setText(itemToEdit.getPrice() + "");

        if(itemToEdit.getImageLink() != null && !itemToEdit.getImageLink().isEmpty() && itemToEdit.getImageLink().length() > 200) {
            final String imageLink = itemToEdit.getImageLink();
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(imageLink.getBytes());
                    return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    dishPhotoButton.setImageBitmap(bitmap);
                }
            }.execute();
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

        imageLink = "";

        if(activity.getItemToEdit() != null){
            editItem(activity.getItemToEdit());
        }else if(activity.getInactiveItemToEdit() != null){
            editItem(activity.getInactiveItemToEdit());
        }

        itemAddCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {
                Snackbar
                        .make(coordinatorLayoutView, "Item added", Snackbar.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.replaceSellerFragment(new Fragment_Seller_Items());
                    }
                }, 1000);
            }

            @Override
            public void runOnFail() {
                Snackbar
                        .make(coordinatorLayoutView, "Failed to add item", Snackbar.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.replaceSellerFragment(new Fragment_Seller_Items());
                    }
                }, 1000);
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
                if (dishNameET.getText().toString().isEmpty()
                        || dollarPriceET.getText().toString().isEmpty()
                        || Integer.parseInt(dollarPriceET.getText().toString()) == 0
                        || portionsET.getText().toString().isEmpty()
                        || Integer.parseInt(portionsET.getText().toString()) == 0
                        || descriptionET.getText().toString().isEmpty()
                        || imageLink.isEmpty()) {
                    Toast.makeText(activity, "Please fill in all required fields and select a photo", Toast.LENGTH_SHORT).show();
                } else {
                    String dishName = dishNameET.getText().toString();
                    int portions = 0;
                    if (!portionsET.getText().toString().isEmpty()) {
                        portions = Integer.parseInt(portionsET.getText().toString());
                    }
                    String price = dollarPriceET.getText().toString();
                    int numPrice = 1;
                    try {
                        numPrice = Integer.parseInt(price);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    String description = descriptionET.getText().toString();


                    Item item = new Item(dbHelper.getUserID(), "", dishName, portions, description, imageLink);
                    Log.d("Item Created", "ImageLink: " + imageLink);
                    item.setPrice(numPrice);
                    item.setIsVegetarian(vegCB.isChecked());
                    item.setGlutenFree(glutFreeCB.isChecked());
                    item.setContainsDairy(dairyCB.isChecked());
                    item.setContainsEggs(eggsCB.isChecked());
                    item.setContainsPeanuts(peanutsCB.isChecked());
                    item.setContainsShellfish(shellfishCB.isChecked());

                    if (activity.getInactiveItemToEdit() != null) {
                        ArrayList<Item> inactiveItems = activity.getInactiveSellerItems();
                        boolean itemAdded = false;
                        item.setItemID(activity.getInactiveItemToEdit().getItemID());
                        for (int i = 0; i < inactiveItems.size(); i++) {
                            Item item1 = inactiveItems.get(i);
                            if (activity.getInactiveItemToEdit().getItemID().equalsIgnoreCase(item1.getItemID())) {
                                inactiveItems.remove(i);
                                inactiveItems.add(i, item);
                                itemAdded = true;
                            }
                        }
                        if(!itemAdded){
                            inactiveItems.add(item);
                        }
                    } else if (activity.getItemToEdit() != null) {
                        item.setItemID(activity.getItemToEdit().getItemID());
                        if (activity.isCurrentlyCooking()) {
                            dbHelper.editItemInActiveSellerProfile(item, itemAddCallback);
                        } else {
                            Log.d("EDIT ITEM","ITEM ID: " + item.getItemID());
                            Log.d("EDIT ITEM","SELLER ID: " + item.getSellerID());
                            Log.d("SELLER ITEMS","GLUTEN FREE: " + item.isGlutenFree());
                            dbHelper.editItemInSellerProfile(item, itemAddCallback);
                        }
                    } else {
                        if (activity.isCurrentlyCooking()) {
                            dbHelper.addItemToActiveSellerProfile(item, itemAddCallback);
                        } else {
                            dbHelper.addItemToSellerProfileDB(item, itemAddCallback);
                        }
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

        dishPhotoButton = (ImageView) root.findViewById(R.id.dish_image);
        coordinatorLayoutView = (View) root.findViewById(R.id.snackbarPosition);

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

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.getContentResolver().query(imageFileUri,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            Log.d("Seller Profile","ImageLink: " + imageLink);
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            imageFileUri = Uri.parse(stringVariable);
            filePath = imageFileUri.getPath();
//            resizeImageForDB(filePath);

        }

        resizeImageForDB(filePath);
        dishPhotoButton.setImageURI(imageFileUri);


    }

    private void resizeImageForDB(final String filePath) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Bitmap scaledBitmap =  Bitmap.createScaledBitmap(bitmap,500,333,false);

                File file = new File(filePath);
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                imageLink = HelperMethods.saveImageToEncodedString(filePath);
                Log.d("Item Creation", "ImageLink: " + imageLink);
            }
        }.execute();
    }

    //This is for the dialog box: Camera or Gallery
    private void showListViewDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Set Dish Image");
        final String[] items = {"Gallery"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                if (items[which].equalsIgnoreCase("Camera")) {
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.parse(stringVariable));
//
//                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivityForResult(intent, 0);
//                    }
//                }

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
        activity.setInactiveItemToEdit(null);
        super.onDetach();
    }
}
