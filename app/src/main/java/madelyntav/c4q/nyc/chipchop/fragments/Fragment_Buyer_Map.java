package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.SellersListAdapter;


public class Fragment_Buyer_Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String PREF_NAME = "Settings";
    public static final String LASTLONGITUDE = "LastLongitude";
    public static final String LASTLATITUDE = "LastLatitude";
    public Button signupButton;
    private LocationRequest mLocationRequest;
    private GoogleMap map;
    private Circle mCircle;
    private SharedPreferences preferences;
    private Location location = new Location("Current Location");
    private boolean gps_enabled = false;
    private GoogleApiClient googleApiClient;
    private DBHelper dbHelper;
    private ArrayList<Item> sellers;
    private RecyclerView sellersList;
    private View root;
    ArrayList<User> userList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_buyer_map, container, false);
        dbHelper = DBHelper.getDbHelper(getActivity());


        signupButton = (Button) root.findViewById(R.id.signupButton);


        // Connect to Geolocation API to make current location request
        locationServiceIsAvailable();
        connectGoogleApiClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 seconds, in milliseconds
                .setFastestInterval(5000); // 1 second, in milliseconds

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
//        dbHelper = new DBHelper(getActivity().getApplicationContext());  TODO: DBHelper constructor should set androidContext

        //TODO: next line should be sellers = dbHelper.getAvailableFoodItems(currentLocation);
        sellers = new ArrayList<>();
        populateItems();

        sellersList = (RecyclerView) root.findViewById(R.id.sellersList);
        sellersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        SellersListAdapter sellersListAdapter = new SellersListAdapter(getActivity(), sellers);
        sellersList.setAdapter(sellersListAdapter);

        madelyntav.c4q.nyc.chipchop.DBObjects.Address address = new madelyntav.c4q.nyc.chipchop.DBObjects.Address("570 west 189 street", "Apt 64", "New York", "NY", "10040", "5");
        User user = new User("1", "MadelynTav@Gmail.com", "Madelyn Tavarez", address, "Photo", "677-987-0564");
        //47-98 31st Pl, Queens, NY, 11101
        madelyntav.c4q.nyc.chipchop.DBObjects.Address address1 = new madelyntav.c4q.nyc.chipchop.DBObjects.Address("47-98 31st Pl", "", "Queens", "NY", "11101", "2");
        User user1 = new User("2", "coalition@gmail.com", "Coalition4Queens", address1, "Photo", "677-988-0988");

        dbHelper.addUserProfileInfoToDB(user);
        dbHelper.addUserProfileInfoToDB(user1);
        Log.d("Done Adding User", "Added Users ");

        userList = new ArrayList<>();
        userList.addAll(dbHelper.addAddressesToMap());


        return root;
    }

    //test method to populate RecyclerView
    private void populateItems() {
        for (int i = 0; i < 10; i++) {
            sellers.add(new Item("test", "Github Cat", "3", "Spanish Food", "http://wisebread.killeracesmedia.netdna-cdn.com/files/fruganomics/imagecache/605x340/blog-images/food-186085296.jpg"));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        locationServiceIsAvailable();
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
    }

    public void locationServiceIsAvailable() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Toast.makeText(getActivity(), "Location Services disabled", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        else
            handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Map", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override // must override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        preferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LASTLATITUDE, String.valueOf(location.getLatitude()));
        editor.putString(LASTLONGITUDE, String.valueOf(location.getLongitude())).apply();
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng locationLatLng = new LatLng(latitude, longitude);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        // Set initial view to current location
        map.moveCamera(CameraUpdateFactory.newLatLng(locationLatLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));

        if (gps_enabled)
            geocodeTask.execute();
    }

    // Task to decode current location
    AsyncTask<Void, Void, Address> geocodeTask = new AsyncTask<Void, Void, Address>() {
        @Override
        protected Address doInBackground(Void... params) {
            Address address = null;

            Geocoder geocoder = new Geocoder(getActivity());
            try {
                List<Address> locations = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1 /* maxResults */);
                address = locations.get(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return address;
        }

        @Override
        protected void onPostExecute(Address address) {
            String zipcode = address.getPostalCode();
            // would launch another async that returns available food providers based on buyer's current zip
            // would then use list of food providers to populate markers and listview
        }
    };

    protected synchronized void connectGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
        Log.d("Map", "Connected to Google API Client");
    }


    public interface OnBuyerMapFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void addWithinRangeMarkersToMap(ArrayList<User> usersList) {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (User user : usersList) {
            madelyntav.c4q.nyc.chipchop.DBObjects.Address address = user.getAddress();
            LatLng latLng = address.getLatLng();
            latLngList.add(latLng);
            LatLng user1=DBHelper.user.getAddress().getLatLng();

            float[] distance = new float[2];

            Location.distanceBetween( latLng.latitude, latLng.longitude,
                    user1.latitude, user1.longitude, distance);

            if( distance[0] > 100  ){
                Toast.makeText(getActivity(), "Outside", Toast.LENGTH_LONG).show();
                addMarker(latLng.latitude,latLng.longitude);

            } else {
                Toast.makeText(getActivity(), "Inside", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void addMarker(double lat, double lon){
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Marker in Zone"));
    }
}