package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.HelperMethods;
import madelyntav.c4q.nyc.chipchop.R;

/**
 * Created by alvin2 on 8/22/15.
 */
public class Fragment_Buyer_ProfileSettings extends Fragment {

    public static final String TAG = "fragment_buyer_profile_settings";

    TextView buyerName, address, apt, city, state, zipcode, phoneNumber;
    ImageView profilePhoto;
    Button saveChanges;
    public static final int RESULT_OK = -1;
    private Uri imageFileUri;
    Intent intent;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";
    String filePath;
    DBHelper dbHelper;
    BuyActivity activity;
    User user;
    String imageLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buyer_profile_settings, container, false);

        activity = (BuyActivity) getActivity();
        dbHelper = DBHelper.getDbHelper(activity);

        bindViews(root);

        user = activity.getUser();

        activity.setCurrentFragment(TAG);

        profilePhoto = (ImageView) root.findViewById(R.id.profile_image);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListViewDialog();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addUserProfileInfoToDB(user);
            }
        });


        buyerName.setText(user.getName());
        address.setText(user.getAddress().getStreetAddress());
        apt.setText(user.getAddress().getApartment());
        city.setText(user.getAddress().getCity());
        state.setText(user.getAddress().getState());
        zipcode.setText(user.getAddress().getZipCode());
        phoneNumber.setText(user.getPhoneNumber());

        return root;
    }

    private void bindViews(View root) {
        buyerName = (TextView) root.findViewById(R.id.name);
        address = (TextView) root.findViewById(R.id.address);
        apt = (TextView) root.findViewById(R.id.apt);
        city = (TextView) root.findViewById(R.id.city);
        state = (TextView) root.findViewById(R.id.state);
        zipcode = (TextView) root.findViewById(R.id.zipcode);
        phoneNumber = (TextView) root.findViewById(R.id.phone_number);
        saveChanges= (Button) root.findViewById(R.id.saveChanges);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addUserProfileInfoToDB(user);

            }
        });

    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageFileUri = data.getData();
            filePath = imageFileUri.getPath();
            rescaleImageForDb(filePath);

        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageFileUri = Uri.parse(stringVariable);
            filePath = imageFileUri.getPath();
            rescaleImageForDb(filePath);

        }


        if (imageFileUri != null) {
            profilePhoto.setImageURI(imageFileUri);
            filePath = imageFileUri.getPath();
            rescaleImageForDb(filePath);

        }
    }



    //This is for the dialog box: Camera or Gallery
    private void showListViewDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Set Profile Image");
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
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

    }

    private void rescaleImageForDb(final String filePath) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Bitmap rotatedBitmap = rotateBitmap(bitmap,filePath);
                Bitmap scaledBitmap;
                if(rotatedBitmap != null) {
                    scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 500, 333, false);
                }else{
                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500,333, false);
                }


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
                return scaledBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageLink = HelperMethods.saveImageToEncodedString(filePath);
                profilePhoto.setImageBitmap(bitmap);
                Log.d("Item Creation", "ImageLink: " + imageLink);
            }
        }.execute();
    }

    private Bitmap rotateBitmap(Bitmap bitmap, String filePath){
        Bitmap rotatedBitmap = null;

        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
            Matrix matrix = new Matrix();

            switch(orientation){
                case 3:
                    matrix.postRotate(180);
                    break;
                case 6:
                    matrix.postRotate(90);
                    break;
                case 8:
                    matrix.postRotate(270);
                    break;
            }

            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotatedBitmap;
    }

}
