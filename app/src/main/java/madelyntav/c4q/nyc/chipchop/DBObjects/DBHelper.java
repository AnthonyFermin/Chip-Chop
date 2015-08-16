package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by c4q-madelyntavarez on 8/12/15.
 */
public class DBHelper extends Firebase {
    static DBHelper firebaseRef;
    private static final String URL = "https://chipchop.firebaseio.com/";
    public static Context mContext;
    String UID;
     static ArrayList<Address> addresses;
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

    public DBHelper() {
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
            addresses= new ArrayList<>();
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
        fRef.child(UID).push();
        fRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        fRef.child(UID).child(sApartment).setValue(address.getApartment());
        fRef.child(UID).child(sCity).setValue(address.getCity());
        fRef.child(UID).child(sState).setValue(address.getState());
        fRef.child(UID).child(sZipCode).setValue(address.getZipCode());

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

    public ArrayList<Address> addAddressesToMap() {

        Firebase fRef = firebaseRef.child("Addresses");
        //final ArrayList<android.location.Address> addresses= new ArrayList<android.location.Address>();

        //UID = user.getUserId();

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Address address = dataSnapshot1.getValue(Address.class);
                    String address1 = address.toString();
                    addresses.add(address);

                    Log.d("AddressList",addresses.toString());

                }
            }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        return addresses;
    }



}
