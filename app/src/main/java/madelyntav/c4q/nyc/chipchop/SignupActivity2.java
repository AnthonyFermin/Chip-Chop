package madelyntav.c4q.nyc.chipchop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private String address;
    private String apt;
    private String city;
    private String zipcode;

    private String name;
    private String phoneNumber;

    private Address userAddress;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        //TODO: onclick circleImageview allows user to upload a profile image

        dbHelper = DBHelper.getDbHelper(this);

        nameET = (EditText) findViewById(R.id.name);
        addressET = (EditText) findViewById(R.id.address);
        aptET = (EditText) findViewById(R.id.apt);
        cityET = (EditText) findViewById(R.id.city);
        zipET = (EditText) findViewById(R.id.zipcode);


        startButton = (Button) findViewById(R.id.createUserButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: CODE TO LINK TO CHECKOUT FRAGMENT + DO LAYOUT FOR CHECKOUT
                //TODO: Uses google api to get geolocation of address, if successful, save profile info to DB
                getGeoLocation();

            }
        });

    }

    private void getGeoLocation(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        GeolocationAPI geolocationAPI = restAdapter.create(GeolocationAPI.class);

        address = addressET.getText().toString();
        address = address.trim().replace(" ", "+");
        name = nameET.getText().toString().trim();
        apt = aptET.getText().toString();
        city = cityET.getText().toString().trim().replace(' ','+');


        String queryString = address + ",+" + city + ",+NY";// + "&key=" + APIKEY;
        Log.i("RETROFIT- Geocode Query",queryString);

        geolocationAPI.getGeolocation(queryString, new Callback<Geolocation>() {
            @Override
            public void success(Geolocation geolocation, Response response) {
                String uid = dbHelper.getUserID();
                Location location = geolocation.getResults().get(0).getGeometry().getLocation();

                address = address.replace('+',' ');
                city = city.replace('+',' ');

                userAddress = new Address(address,apt,city,"NY",zipcode, uid);

                Log.i("RETROFIT: LatLng", "" + location.getLat() + ", " + location.getLng());
                userAddress.setLatitude(location.getLat());
                userAddress.setLongitude(location.getLng());
                User user = dbHelper.getCurrentUser();
                user.setAddress(userAddress);
                user.setName(name);

                dbHelper.addUserProfileInfoToDB(user);

               // dbHelper.getUserListLatLng();

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



}
