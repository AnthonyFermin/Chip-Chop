package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Geolocation;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.GeolocationAPI;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Location;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Checkout;
import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_SellerProfile;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity2 extends AppCompatActivity {

    private final String ENDPOINT = "https://maps.googleapis.com/maps/api/geocode";

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

    private Address userAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        //TODO: onclick circleImageview allows user to upload a profile image

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
                // Else give error message to user "invalid address"

            }
        });

    }

    private void getGeoLocation(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        GeolocationAPI geolocationAPI = restAdapter.create(GeolocationAPI.class);

        String address = addressET.getText().toString();
        address = address.trim().replace(" ", "+");

        String queryString = address + ",";

        geolocationAPI.getGeolocation(queryString, new Callback<Geolocation>() {
            @Override
            public void success(Geolocation geolocation, Response response) {
                Location location = geolocation.getResults().get(0).getGeometry().getLocation();
                location.getLng();
                LatLng latlng = new LatLng(location.getLat(), location.getLng());
                Address = new Address(address,apt,city,"NY")
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }



}
