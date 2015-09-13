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
import android.widget.ImageView;
import android.widget.TextView;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
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

    DBHelper dbHelper;
    BuyActivity activity;
    User user;

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
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageFileUri = Uri.parse(stringVariable);
        }


        if (imageFileUri != null) {
            profilePhoto.setImageURI(imageFileUri);
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

}
