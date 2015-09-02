package madelyntav.c4q.nyc.chipchop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Geolocation;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.GeolocationAPI;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Location;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity2 extends AppCompatActivity {

    private final String ENDPOINT = "https://maps.googleapis.com/maps/api/geocode";
    private final String APIKEY2 = "AIzaSyAZ6ZNk_DPZN2A_0dpHb0MpWdGpEPLxom4";
    private final String APIKEY = "AIzaSyDTaAeiCfVCXJhdweubPkgIvsni3s1-9ss";

    private Button startButton;
    private EditText nameET;
    private EditText addressET;
    private EditText aptET;
    private EditText cityET;
    private EditText zipET;
    private EditText phoneNumberET;

    private String address;
    private String apt;
    private String city;
    private String zipcode;

    private String email;

    private String name;
    private String phoneNumber;

    private Address userAddress;

    DBHelper dbHelper;

    ImageView profilePhoto;
    public static final int RESULT_OK = -1;
    private Uri imageFileUri;
    Intent intent;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        //TODO: onclick circleImageview allows user to upload a profile image

        dbHelper = DBHelper.getDbHelper(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        nameET = (EditText) findViewById(R.id.name);
        addressET = (EditText) findViewById(R.id.address);
        aptET = (EditText) findViewById(R.id.apt);
        cityET = (EditText) findViewById(R.id.city);
        zipET = (EditText) findViewById(R.id.zipcode);
        phoneNumberET = (EditText) findViewById(R.id.phone_number);



        startButton = (Button) findViewById(R.id.createUserButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: CODE TO LINK TO CHECKOUT FRAGMENT
                //TODO: Uses google api to get geolocation of address, if successful, save profile info to DB
                getGeoLocation();

            }
        });


        profilePhoto = (ImageView) findViewById(R.id.profile_image);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListViewDialog();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
        dialogBuilder.setTitle("Set Profile Image");
        final String[] items = {"Camera", "Gallery"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equalsIgnoreCase("Camera")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.parse(stringVariable));

                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
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

    private void getGeoLocation(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        GeolocationAPI geolocationAPI = restAdapter.create(GeolocationAPI.class);

        address = addressET.getText().toString().trim();
        address = address.trim().replace(" ", "+");
        name = nameET.getText().toString().trim();
        apt = aptET.getText().toString().trim();
        city = cityET.getText().toString().trim().replace(' ','+');
        phoneNumber = phoneNumberET.getText().toString().trim().replace(" ", "");
        zipcode = zipET.getText().toString().trim();


        String queryString = address + ",+" + city + ",+NY";// + "&key=" + APIKEY;
        Log.i("RETROFIT- Geocode Query",queryString);

        geolocationAPI.getGeolocation(queryString, new Callback<Geolocation>() {
            @Override
            public void success(Geolocation geolocation, Response response) {
                String uid = dbHelper.getUserID();
                Location location = geolocation.getResults().get(0).getGeometry().getLocation();

                address = address.replace('+', ' ');
                city = city.replace('+', ' ');

                userAddress = new Address(address, apt, city, "NY", zipcode, uid);

                Log.i("RETROFIT: LatLng", "" + location.getLat() + ", " + location.getLng());

                userAddress.setLatitude(location.getLat());
                userAddress.setLongitude(location.getLng());
                User user = dbHelper.getCurrentUser();
                user.setAddress(userAddress);
                user.setName(name);
                user.setUID(uid);
                user.seteMail(email);
                user.setPhoneNumber(phoneNumber);

                dbHelper.addUserProfileInfoToDB(user);

                saveToSharedPrefs();

                Intent buyActivity = new Intent(getApplicationContext(), BuyActivity.class);
                startActivity(buyActivity);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SignupActivity2.this, "Invalid Address", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void saveToSharedPrefs(){
        SharedPreferences sp = getSharedPreferences(SignupActivity1.USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SignupActivity1.ADDRESS, address)
                .putString(SignupActivity1.NAME, name)
                .putString(SignupActivity1.APT, apt)
                .putString(SignupActivity1.CITY,city)
                .putString(SignupActivity1.ZIPCODE, zipcode)
                .putString(SignupActivity1.PHONE_NUMBER, phoneNumber)
                .commit();
    }



}
