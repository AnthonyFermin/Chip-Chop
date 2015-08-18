package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Buyer_Map;


/**
 * Created by c4q-madelyntavarez on 8/12/15.
 */
public class DBHelper extends Firebase {
    static DBHelper fireBaseRef;
    private static final String URL = "https://chipchop.firebaseio.com/";
    public static Context mContext;
    private static Fragment_Buyer_Map fragment_buyer_map;
    String UID;
    public static ArrayList<User> allUsers;
    public static ArrayList<Item> items;
    public static ArrayList<User> userList;
    private static String userID = "userID";
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
    public static User user;
    public static final String sLatitude="latitude";
    public static final String sLongitude="longitude";
    public boolean mSuccess = false;
    public static Address address;
    long sizeofAddDBList;
    public static ArrayList<LatLng> latLngList;
    public static ArrayList<Address> addressList;

    public DBHelper() {
        super(URL);
    }

    public Firebase getFirebaseRef() {
        return fireBaseRef;
    }

    public static DBHelper getDbHelper(Context context) {
        mContext = context;
        if (fireBaseRef == null) {
            Firebase.setAndroidContext(context);
            fireBaseRef = new DBHelper();
        }
        userList = new ArrayList<>();
        items = new ArrayList<>();
        allUsers = new ArrayList<>();
        user = new User();
        latLngList = new ArrayList<>();
        addressList= new ArrayList<>();
        fragment_buyer_map=new Fragment_Buyer_Map();
        return fireBaseRef;
    }

    public boolean createUser(final String email, final String password) {
        UID = "";

        fireBaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(mContext, "Account Created", Toast.LENGTH_SHORT).show();
                mSuccess = true;

                String userIDOne = String.valueOf(stringObjectMap.get("uid"));
                for (int i = 12; i < userIDOne.length(); i++) {
                    UID += userIDOne.charAt(i);
                }

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("UID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", UID);
                editor.putString("eMail", email);
                editor.putString("password", password);
                editor.apply();

                user = new User(UID, email);

                Firebase fRef = new Firebase(URL + "UserProfiles");
                fRef.child(UID);
                fRef.child(UID).child(sEmailAddress).setValue(email);

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Email already in use", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", firebaseError.toString());
                mSuccess = false;
            }
        });

        return mSuccess;
    }

    public void addUserProfileInfoToDB(User user) {
        Firebase fRef = new Firebase(URL + "UserProfiles");
        UID = user.getUserId();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

        addUserAddressToProfile(user.address);
    }

    public void addUserAddressToProfile(Address address) {
        UID = address.getUserID();
        Firebase fRef = new Firebase(URL + "Addresses");

        fRef.child(UID).push();
        fRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        fRef.child(UID).child(sApartment).setValue(address.getApartment());
        fRef.child(UID).child(sCity).setValue(address.getCity());
        fRef.child(UID).child(sState).setValue(address.getState());
        fRef.child(UID).child(sZipCode).setValue(address.getZipCode());
        fRef.child(UID).child(sLatitude).setValue(address.getLatitude());
        fRef.child(UID).child(sLongitude).setValue(address.getLongitude());
    }

    public void updateUserProfile(String UID) {
        this.UID = UID;
        Firebase fRef = new Firebase(URL + "UserProfiles/" + UID);

        fRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        fRef.child(UID).child(sApartment).setValue(address.getApartment());
        fRef.child(UID).child(sCity).setValue(address.getCity());
        fRef.child(UID).child(sState).setValue(address.getState());
        fRef.child(UID).child(sZipCode).setValue(address.getZipCode());

        updateUserAddress(UID);
    }

    public void updateUserAddress(String UID) {
        this.UID = UID;
        Firebase fRef = new Firebase(URL + "Addresses/" + UID);

        fRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        fRef.child(UID).child(sApartment).setValue(address.getApartment());
        fRef.child(UID).child(sCity).setValue(address.getCity());
        fRef.child(UID).child(sState).setValue(address.getState());
        fRef.child(UID).child(sZipCode).setValue(address.getZipCode());
        fRef.child(UID).child("latitude").setValue(address.getLatitude());
        fRef.child(UID).child("longitude").setValue(address.getLongitude());
    }

    public void addItemToDB(Item item) {
        UID = "";
        UID = item.getUserID();
        Firebase fRef = new Firebase(URL + "Items/" + UID);

        Firebase fire1 = fRef.child(UID).push();
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

        Firebase fRef = new Firebase(URL + "Orders/" + UID);

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
        Firebase fRef = new Firebase(URL + "itemsForSale/" + UID);

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

        Firebase fRef = new Firebase(URL + "Orders/" + "5" + "/" + orderID);

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

        Firebase fRef = new Firebase(URL + "Addresses/" + id);

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

    public ArrayList<User> addAddressesToMap() {

        Firebase fRef = new Firebase(URL + "UserProfiles/Seller/");
        //final ArrayList<android.location.Address> addresses= new ArrayList<android.location.Address>();
        //UID = user.getUserId();

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    User user = dataSnapshot1.getValue(User.class);
                    String address1 = user.toString();
                    userList.add(user);

                    Log.d("AddressList", userList.toString());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return userList;
    }

    public ArrayList<Item> getSellerItems(String UID) {
        this.UID = UID;

        Firebase fRef = new Firebase(URL + "Items/" + UID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Item item = dataSnapshot1.getValue(Item.class);
                    items.add(item);

                    Log.d("SellerItems", items.toString());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return items;
    }

    public void createSellerItems(Item item) {
        UID = "";
        UID = item.getUserID();
        Firebase fRef = new Firebase(URL + "UserProfiles/Sellers/Items/" + UID);


        Firebase fire1 = fRef.child(UID).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(UID).child(itemID).push();
        fRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
        fRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("ImageLink").setValue(item.getImageLink());

        Log.d("ItemID", itemID);
    }

    public void createSellerProfile(User user) {
        UID = user.getUserId();
        Firebase fRef = new Firebase(URL + "UserProfiles/Seller/" + UID);

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

        addUserAddressToProfile(user.address);
    }

    public void createBuyerProfile(User user) {
        UID = user.getUserId();
        Firebase fRef = new Firebase(URL + "UserProfiles/Buyer/" + UID);

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

        addUserAddressToProfile(user.address);
    }

    public ArrayList<User> getAllUsers() {
        Firebase fRef = new Firebase(URL + "UserProfiles");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                allUsers = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    User user = dataSnapshot1.getValue(User.class);
                    //Log.d("Fin", user.phoneNumber);
                    allUsers.add(user);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        getUserListLatLng();


        return allUsers;
    }

    public void getUserListLatLng() {
        Log.d("DBHELPERUSERLIST", allUsers.toString());

        Firebase fRef = new Firebase(URL + "Addresses");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // sizeofAddDBList = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Address address = dataSnapshot1.getValue(Address.class);
                    //Log.d("Fin", String.valueOf(address.latLng.latitude));

                    double lat = address.latitude;
                    double lng = address.longitude;

                    LatLng latLng = new LatLng(lat, lng);
                    latLngList.add(latLng);
                }

                updateUserList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    public ArrayList<LatLng> updateUserList(){

        if(latLngList.size()<sizeofAddDBList){
            getUserListLatLng();
        }

        return latLngList;
    }

    public void getUserAddressList(){
        Firebase fRef = new Firebase(URL + "Addresses");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sizeofAddDBList = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Address address = dataSnapshot1.getValue(Address.class);
                    //Log.d("Fin", String.valueOf(address.latLng.latitude));


                    addressList.add(address);
                }

                updateAddressList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d("DBHelperLatsList", addressList.toString());
    }

    public ArrayList<Address> updateAddressList(){

        if(addressList.size()<=sizeofAddDBList){
            getUserAddressList();
        }

        return addressList;
    }

    public User getSpecificUser(String UID) {
        this.UID = UID;
        Firebase fRef = new Firebase(URL+"UserProfiles/"+ UID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    user = dataSnapshot1.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return user;
    }

    public boolean signOutUser() {

        fireBaseRef.unauth();
        userID=null;
        user.clearUser();
        address.clearAddress();
        userList.clear();
        items.clear();
        return true;
    }

    public boolean logInUser(String email, String password) {
        mSuccess = false;
        fireBaseRef.authWithPassword(email, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) { /* ... */
                        mSuccess = true;
                        UID = authData.getUid();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                        switch (error.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                Toast.makeText(mContext, "E-mail or Password Invalid", Toast.LENGTH_LONG).show();
                                mSuccess = false;
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                Toast.makeText(mContext, "Invalid Password", Toast.LENGTH_LONG).show();
                                mSuccess = false;
                                break;
                            default:
                                // handle other errors
                                break;
                        }
                    }
                });
        return mSuccess;
    }

    public boolean userIsLoggedIn() {

        if(UID==null){

            return false;
        }
        else{return true; }
    }

    public String getUserID() {
            return UID;
    }

    public User getCurrentUser() {
        return user;
    }



    public void addToInviteTree(String newUserInviteEmail){
        Firebase fRef =  new Firebase(URL+"InviteTree");

        fRef.child(UID).push();
        fRef.child(UID).child("invited").setValue(newUserInviteEmail);
    }

    public ArrayList<String> getInviteListForSpecificUser(){
        final ArrayList<String> invitesSent=new ArrayList<>();

        Firebase fRef = new Firebase(URL+"InviteTree/"+UID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String email = (String) dataSnapshot1.getValue();
                    invitesSent.add(email);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
     return invitesSent;
    }

    public void addUserReviewToUserProfile(User buyer, User seller, int numOfStars, String details){

    }

    public void addReviewToSellerProfile(User buyer, User seller, int numOfStarts, String details){

    }

    public void getReviewsForCertainPerson(String id){

    }

}
