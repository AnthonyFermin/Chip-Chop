package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import de.hdodenhof.circleimageview.CircleImageView;
import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Geolocation;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.GeolocationAPI;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Location;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Fragment_SellerProfile extends Fragment {


    private final String ENDPOINT = "https://maps.googleapis.com/maps/api/geocode";

    DBHelper dbHelper;

    ToggleButton cookingStatus;
    CircleImageView profileImage;
    TextView sellerName;

    EditText storeNameET;
    EditText addressET;
    EditText aptET;
    EditText cityET;
    EditText zipcodeET;
    EditText phoneNumberET;

    Button saveButton;

    String storeName;
    String address;
    String apt;
    String city;
    String zipcode;
    String phoneNumber;

    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());
        dbHelper.getUserFromDB(dbHelper.getUserID());

        cookingStatus = (ToggleButton) root.findViewById(R.id.cooking_status);
        profileImage = (CircleImageView) root.findViewById(R.id.profile_image);
        sellerName = (TextView) root.findViewById(R.id.seller_name);
        storeNameET = (EditText) root.findViewById(R.id.store_name);
        addressET = (EditText) root.findViewById(R.id.address);
        aptET = (EditText) root.findViewById(R.id.apt);
        cityET = (EditText) root.findViewById(R.id.city);
        zipcodeET = (EditText) root.findViewById(R.id.zipcode);
        phoneNumberET = (EditText) root.findViewById(R.id.phone_number);

        saveButton = (Button) root.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: check if all EditTexts are filled in correctly ie: phone number has correct format
                getGeoLocation();
            }
        });

        return root;
    }

    private void getGeoLocation(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        GeolocationAPI geolocationAPI = restAdapter.create(GeolocationAPI.class);

        address = addressET.getText().toString();
        address = address.trim().replace(" ", "+");
        storeName = storeNameET.getText().toString().trim();
        apt = aptET.getText().toString();
        city = cityET.getText().toString().trim().replace(' ','+');
        phoneNumber = phoneNumberET.getText().toString().replace("-","");


        String queryString = address + ",+" + city + ",+NY";// + "&key=" + APIKEY;
        Log.i("RETROFIT- Geocode Query", queryString);

        geolocationAPI.getGeolocation(queryString, new Callback<Geolocation>() {
            @Override
            public void success(Geolocation geolocation, Response response) {
                String uid = dbHelper.getUserID();
                Location location = geolocation.getResults().get(0).getGeometry().getLocation();

                address = address.replace('+',' ');
                city = city.replace('+',' ');


                Address userAddress = new Address(address, apt, city,"NY", zipcode, uid);

                Log.i("RETROFIT: LatLng", "" + location.getLat() + ", " + location.getLng());

                userAddress.setLatitude(location.getLat());
                userAddress.setLongitude(location.getLng());

                Seller seller = new Seller(uid,"temp@gmail.com",storeName,userAddress,phoneNumber);

                dbHelper.addSellerProfileInfoToDB(seller);


                ((SellActivity) getActivity()).replaceSellerFragment(new Fragment_Seller_Items());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Invalid Address", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
