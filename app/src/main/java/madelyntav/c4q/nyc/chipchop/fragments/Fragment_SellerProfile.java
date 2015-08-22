package madelyntav.c4q.nyc.chipchop.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Geolocation;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.GeolocationAPI;
import madelyntav.c4q.nyc.chipchop.GeolocationAPI.Location;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.SellActivity;
import madelyntav.c4q.nyc.chipchop.adapters.SellerItemsAdapter;
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

    Seller seller;
    User user = null;
    Address userAddress;
    ArrayList<Item> sellerItems;

    SellActivity activity;

    RelativeLayout loadingPanel;
    LinearLayout containingView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        dbHelper = DBHelper.getDbHelper(getActivity());
        activity = (SellActivity) getActivity();

        containingView = (LinearLayout) root.findViewById(R.id.container);
        containingView.setVisibility(View.INVISIBLE);
        loadingPanel = (RelativeLayout) root.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        if(activity.getUser() == null) {
            user = dbHelper.getUserFromDB(dbHelper.getUserID());
            load();
        }else{
            user = activity.getUser();
            setEditTexts();
        }

        cookingStatus = (ToggleButton) root.findViewById(R.id.cooking_status);
        cookingStatus.setChecked(activity.isCurrentlyCooking());

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

                address = address.replace('+', ' ');
                city = city.replace('+', ' ');


                userAddress = new Address(address, apt, city, "NY", zipcode, uid);

                Log.i("RETROFIT: LatLng", "" + location.getLat() + ", " + location.getLng());

                userAddress.setLatitude(location.getLat());
                userAddress.setLongitude(location.getLng());

                seller = new Seller(uid, "temp@gmail.com", storeName, userAddress, phoneNumber);

                dbHelper.addSellerProfileInfoToDB(seller);

                if (cookingStatus.getText().toString().equalsIgnoreCase("on")) {
                    //TODO: add confirmation dialog when changing cooking status mention to click save to commit changes
                    sellerItems = activity.getSellerItems();
                    if (sellerItems != null & hasPositiveQuantity()){
                        dbHelper.addActiveSellerToTable(seller);
                        activity.setCookingStatus(true);
                    }else{
                        Toast.makeText(activity,"Please add items for sale",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dbHelper.removeSellersFromActiveSellers(seller);
                    activity.setCookingStatus(false);
                }

                activity.replaceSellerFragment(new Fragment_Seller_Items());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Invalid Address", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void load(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                int i = 0;
                do{
                    Log.d("LOAD SELLER PROFILE", "Attempt #" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > 10){
                        Log.d("LOAD SELLER PROFILE", "DIDN'T LOAD");
                        break;
                    }
                    i++;
                }while(user == null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(user != null) {
                    loadingPanel.setVisibility(View.GONE);
                    containingView.setVisibility(View.VISIBLE);
                    setEditTexts();
                    activity.setUser(user);
                }else{
                    //TODO:display cannot connect to internet error message
                }
            }
        }.execute();
    }

    private void setEditTexts(){
        Address address = user.getAddress();
        sellerName.setText(user.getName());
        storeNameET.setText("FOOD KITCHEN");
        if(address != null) {
            addressET.setText(address.getStreetAddress());
            aptET.setText(address.getApartment());
            cityET.setText(address.getCity());
            zipcodeET.setText(address.getZipCode());
        }
        phoneNumberET.setText(user.getPhoneNumber());
    }


    private boolean hasPositiveQuantity(){

        for(Item item: sellerItems){
            if(item.getQuantityAvailable() > 0)
                return true;

        }

        return false;
    }

}
