package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by c4q-madelyntavarez on 8/12/15.
 */
public class DBHelper extends Firebase {
    static DBHelper firebaseRef;
    private static final String URL = "https://chipchop.firebaseio.com/";
    public static Context mContext;
    String UID;
    public static GeoFire geoFire;
    private static final String userID = "userID";
    private static final String sStreet = "streetAddress";
    private static final String sApartment = "apartment";
    private static final String sCity = "city";
    private static final String sState = "state";
    private static final String sZipCode = "zipCode";
    private static final String nameOfItem = "nameOfItem";
    private static final String descriptionOfItem = "descriptionOfItem";
    private static final String quantityAvailable = "quantityAvailable";
    private static final String itemID = "itemID";
    private static final String imageLink = "imageLink";
    private static final String sName = "name";
    private static final String sEmailAddress = "eMail";
    private static final String sPhoneNumber = "phoneNumber";
    private static final String sAddress = "address";
    private static final String sPhotoLink = "photoLink";
    User user;
    Address address;

    private DBHelper() {
        super(URL);

    }

    public Firebase getFirebaseRef() {
        return firebaseRef;
    }

    public static DBHelper getDbHelper(Context context) {

        mContext = context;
        if (firebaseRef == null) {
            Firebase.setAndroidContext(context);
            firebaseRef = new DBHelper();
            geoFire = new GeoFire(firebaseRef);
        }
        return firebaseRef;

    }

    public void createUser(final String email, String password) {
        UID = "";

        firebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(mContext, "Account Created", Toast.LENGTH_SHORT).show();

                String userIDOne = String.valueOf(stringObjectMap.get("uid"));

                for (int i = 12; i < userIDOne.length(); i++) {
                    UID += userIDOne.charAt(i);
                }

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", UID);
                editor.apply();

                Firebase fRef = firebaseRef.child("UserLogins");
                fRef.child(UID);
                fRef.child(UID).child("UserName").setValue(email);
                fRef.child(UID).child("UserName").child("passWord").setValue("Tomorrow");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Email already in use", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", firebaseError.toString());
            }
        });
    }

    public void addUserProfileInfoToDB(User user) {
//        firebaseRef= (DBHelper) new Firebase(URL+"UserProfiles");
        Firebase fRef = firebaseRef.child("UserProfiles");
        UID = user.getUserId();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.getEmailAddress());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getEncodedPhotoString());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

        addUserAddressToProfile(user);

    }

    public void addUserAddressToProfile(User user) {
        UID = "";
        Address address = user.getAddress();
        UID = address.getUserID();
        Firebase fRef = firebaseRef.child("Addresses");
        firebaseRef.child(UID).push();
        firebaseRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        firebaseRef.child(UID).child(sApartment).setValue(address.getApartment());
        firebaseRef.child(UID).child(sCity).setValue(address.getCity());
        firebaseRef.child(UID).child(sState).setValue(address.getState());
        firebaseRef.child(UID).child(sZipCode).setValue(address.getZipCode());

    }

    public void addItemToDB(Item item) {
        UID = "";
        UID = item.getUserID();
        Firebase fRef = firebaseRef.child("Items/" + UID);

        Firebase fire1 = firebaseRef.child(UID).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(UID).child(itemID).push();
        fRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
        fRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("ImageLink").setValue(item.getImageLink());

        Log.d("ItemID", itemID);
    }

    public void addOrderToDB(Order order) {
        UID = "";
        UID = order.getUserID();

        Firebase fRef = firebaseRef.child("Orders/" + UID);

        ArrayList<Item> itemsOrdered = order.getItemsOrdered();

        Firebase fire1 = fRef.child(UID).push();
        String orderID = fire1.getKey();


        for (Item item : itemsOrdered) {

            String itemID = item.getItemID();
            fRef.child(UID).child(orderID).push();
            fRef.child(orderID).child(itemID);
            fRef.child(orderID).child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            fRef.child(orderID).child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            fRef.child(orderID).child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            fRef.child(orderID).child(itemID).child("imageLink").setValue(item.getImageLink());
        }

    }

    public void currentlyOnSale(Item item) {

        UID = "";
        UID = item.getUserID();
        Firebase fRef = firebaseRef.child("itemsForSale/" + UID);

        Firebase fire1 = fRef.child(UID).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(UID).child(itemID).push();
        fRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("userID").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("imageLink").setValue(item.getImageLink());

        Log.d("ItemID", itemID);
    }

    public void getSellersOnSaleItems(Order order) {
        UID = "";

        UID = order.getUserID();
        String orderID = order.getOrderID();

        Firebase fRef = firebaseRef.child("Orders/" + "5" + "/" + orderID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Item item = dataSnapshot1.getValue(Item.class);
                    Log.d("SNAPSHOT", "Got Snapshot");
                    String key = dataSnapshot1.getKey();
                    Log.d(itemID, key + "");
                    String nameOfItem1 = item.nameOfItem;
                    Log.d(nameOfItem, nameOfItem1 + "");

                    String descriptionOfItem1 = item.descriptionOfItem;
                    Log.d(descriptionOfItem, descriptionOfItem1 + "");

                    String quantityAvailable1 = item.quantityAvailable;
                    Log.d(quantityAvailable, quantityAvailable1 + "");

                    String imageLink1 = item.imageLink;
                    Log.d(imageLink, imageLink1 + "");

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Error Retrieving", "Error");

            }
        });

    }

    public void getAddressFromDB(String id) {
        UID = "";
        this.UID = id;

        Firebase fRef = firebaseRef.child("Addresses/" + id);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Address address = dataSnapshot.getValue(Address.class);
                String streetAddress1 = address.streetAddress;
                Log.d("Street", streetAddress1);

                String apartment = address.apartment;
                Log.d("apartment", apartment);

                String city = address.city;
                Log.d("city", city);

                String state = address.state;
                Log.d("state", state);

                String zipCode = address.zipCode;
                Log.d("zipCode", zipCode);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void addAddressesToMap(LatLng latLng) {

        Firebase fRef = firebaseRef.child("UserProfiles");
        UID = user.getUserId();

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    String address = user.address.toString();

                    Geocoder gc = new Geocoder(mContext);

                    if(gc.isPresent()){
                        List<android.location.Address> list = null;
                        try {
                            list = gc.getFromLocationName(address, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        android.location.Address address1 = list.get(0);

                        double lat = address1.getLatitude();
                        double lng = address1.getLongitude();

                        geoFire.setLocation(user.getName(), new GeoLocation(lat, lng));

                    }

                    // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers



                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //querying for one mile

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 90);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(FirebaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }


}

