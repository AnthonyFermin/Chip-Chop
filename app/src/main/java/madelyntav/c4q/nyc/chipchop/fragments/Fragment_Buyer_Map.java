package madelyntav.c4q.nyc.chipchop.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.BuyActivity;
import madelyntav.c4q.nyc.chipchop.DBCallback;
import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;
import madelyntav.c4q.nyc.chipchop.R;
import madelyntav.c4q.nyc.chipchop.adapters.SellerListAdapter;

import static android.content.res.Resources.*;


public class Fragment_Buyer_Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    FloatingActionButton refreshButton;
    ImageView arrowImage;
    public SlidingUpPanelLayout slidingPanel;
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String PREF_NAME = "Settings";
    public static final String LASTLONGITUDE = "LastLongitude";
    public static final String LASTLATITUDE = "LastLatitude";
    public ArrayList<madelyntav.c4q.nyc.chipchop.DBObjects.Address> addressList;
    private LocationRequest mLocationRequest;
    private GoogleMap map;
    private Circle mCircle;
    private Geofence mGeofence;
    private SharedPreferences preferences;
    private Location location = new Location("Current Location");
    private boolean gps_enabled = false;
    private GoogleApiClient googleApiClient;
    private DBHelper dbHelper;
    private ArrayList<Seller> sellers;
    private RecyclerView itemsRView;
    private View root;

    private DBCallback emptyCallback;

    public static final String TAG = "fragment_buyer_map";

    private BuyActivity activity;
    private AsyncTask<Void, Void, Void> addSellerMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initializeData(inflater, container);
        bindViews();
        initializeMap();
        setListeners();

        initializeListPanel();

        //addSellerMarkers() is in onResume()

        return root;
    }

    private void setListeners() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: WRITE CODE TO REFRESH RECYCLERVIEW !!
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        addSellerMarkers();

        // clear cart when entering this fragment
        activity.setCurrentOrder(new Order());
        activity.setItemToCart(null);
    }


    private void initializeListPanel() {
        slidingPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                arrowImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
            }

            @Override
            public void onPanelExpanded(View view) {
                arrowImage.setImageDrawable(getResources().getDrawable(R.drawable.down));
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        itemsRView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        sellersList.addItemDecoration(new MarginDe(this));
        itemsRView.setHasFixedSize(true);
//        sellersList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        sellersList.setAdapter(new NumberedAdapter(30));

    }

    private void initializeMap() {
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

    }

    private void bindViews() {
        arrowImage = (ImageView) root.findViewById(R.id.arrow_image);
        slidingPanel = (SlidingUpPanelLayout) root.findViewById(R.id.slidinglayout);
        itemsRView = (RecyclerView) root.findViewById(R.id.buyers_orders_list);
        refreshButton = (FloatingActionButton) root.findViewById(R.id.refresh_button);

    }

    private void initializeData(LayoutInflater inflater, ViewGroup container) {

        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null)
                parent.removeView(root);
        }
        try {
            root = inflater.inflate(R.layout.fragment_buyer_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        dbHelper = DBHelper.getDbHelper(getActivity());

        activity = (BuyActivity) getActivity();

        activity.setCurrentFragment(TAG);

        emptyCallback = new DBCallback() {
            @Override
            public void runOnSuccess() {

            }

            @Override
            public void runOnFail() {

            }
        };

        sellers = dbHelper.getAllActiveSellers(emptyCallback);
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
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (location != null)
                    handleNewLocation(location);
            }
        }, 3000);
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
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

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

    public void addWithinRangeMarkersToMap() {
        Log.d("Buyer Map Fragment", "ADDING SELLER MARKERS");

        for (Seller seller : sellers) {
            Log.d("SELLER INFO", "UID: " + seller.getUID());
            Log.d("SELLER INFO", "Store Name: " + seller.getStoreName());
            Log.d("SELLER INFO", "Lat: " + seller.getLatitude());
            Log.d("SELLER INFO", "Long:" + seller.getLongitude());
            Log.d("SELLER INFO", "Description:" + seller.getDescription());

            double lat = 0;
            double lng = 0;

            double gLat = 0;
            double gLng = 0;

            String userName = "Local Food";

            try {
                userName = seller.getStoreName();
                gLat = Double.parseDouble(seller.getLatitude());
                gLng = Double.parseDouble(seller.getLongitude());

                lat = location.getLatitude();
                lng = location.getLongitude();
            } catch (NullPointerException e) {
                Log.d("Buyer Map Fragment","Failed to get coordinates");
                lat = 40.737256;
                lng = -73.855278;
            }

            Circle circle = map.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lng))
                    .radius(100000)
                    .strokeColor(Color.RED));

            float[] distance = new float[sellers.size()];

            Location.distanceBetween(lat, lng,
                    gLat, gLng, distance);

            Log.d("Buyer Map Fragment", "Seller Lat: " + gLat);
            Log.d("Buyer Map Fragment","Seller Long: " + gLng);
            Log.d("Buyer Map Fragment","User Lat: " + lat);
            Log.d("Buyer Map Fragment","User Long: " + lng);

            if (distance[0] < circle.getRadius()) {
                Log.d("Fragment Buyer Map", "MARKER ADDED");
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(gLat, gLng))
                        .title(userName))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));

            }
        }
    }

    private void addSellerMarkers() {

        // Task to get LatLng List and populate markers when done
        addSellerMarkers = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground (Void...voids){

                int i = 0;
                do {
                    // thread cannot continue until dbHelper.getUserListLatLng returns an ArrayList
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sellers.size() != 0)
                        break;

                    i++;
                } while (i < 10);

                //create markers list by sorting throught latsList
                return null;
            }
            @Override
            protected void onPostExecute (Void aVoid){
                super.onPostExecute(aVoid);
                if (sellers == null) {
                    Toast.makeText(activity, "No sellers found in area", Toast.LENGTH_SHORT).show();
                } else {
                    SellerListAdapter sellersListAdapter = new SellerListAdapter(getActivity(), sellers);
                    itemsRView.setAdapter(sellersListAdapter);
                    addWithinRangeMarkersToMap();
                }
            }
        }.execute();
}

    @Override
    public void onDetach() {
        activity.setCurrentFragment("");
        super.onDetach();
    }

    @Override
    public void onPause() {
        addSellerMarkers.cancel(true);
        super.onPause();
    }
}