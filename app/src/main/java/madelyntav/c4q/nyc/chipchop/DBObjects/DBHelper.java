package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;
import java.util.Map;
/**
 * Created by c4q-madelyntavarez on 8/12/15.
 */
public class DBHelper extends Firebase {
    static DBHelper fireBaseRef;
    private static final String URL = "https://chipchop.firebaseio.com/";
    public static Context mContext;
    private static Item item;
    String UID;
    public static ArrayList<User> allUsers;
    public static ArrayList<Item> items;
    private static String userID = "buyerID";
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
    public static final String sLatitude = "latitude";
    public static final String sLongitude = "longitude";
    public boolean mSuccess = false;
    public static Address address;
    long sizeofAddDBList;
    public static ArrayList<LatLng> latLngList;
    public static ArrayList<Address> addressList;
    public static ArrayList<User> userList;
    public static ArrayList<Item> arrayListOfSellerItems;
    public static Order returnOrder;
    public ArrayList<Item> listOfItemsSellingForSeller;
    public String sellerId;
    static Seller seller;
    static ArrayList<Seller> allSellersInDB;
    static ArrayList<Review> reviewArrayList;
    static ArrayList<Seller> allActiveSellers;

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
        addressList = new ArrayList<>();
        userList = new ArrayList<>();
        arrayListOfSellerItems = new ArrayList<>();
        returnOrder = new Order();
        arrayListOfSellerItems=new ArrayList<>();
        reviewArrayList= new ArrayList<>();
        allActiveSellers=new ArrayList<>();
        seller=new Seller();
        item=new Item();
        allSellersInDB=new ArrayList<>();
        return fireBaseRef;
    }

    //checks if user is logged in
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

    public boolean createUser(final String email, final String password) {
        UID = "";

        fireBaseRef.createUser(email, password, new ValueResultHandler<Map<String, Object>>() {
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
                Toast.makeText(mContext, "Account Creation Failed", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", firebaseError.toString());
                mSuccess = false;
            }
        });
        return mSuccess;
    }

    public Boolean createUserAndLaunchIntent(final String email, final String password, final Intent intent) {
        UID = "";

        fireBaseRef.createUser(email, password, new ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(mContext, "Account Created", Toast.LENGTH_SHORT).show();
                mSuccess = true;

                String userIDOne = String.valueOf(stringObjectMap.get("uid"));
                for (int i = 12; i < userIDOne.length(); i++) {
                    UID += userIDOne.charAt(i);
                }

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("New User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", UID);
                editor.putString("eMail", email);
                editor.putString("password", password);
                editor.apply();

                user = new User(UID, email);

                Firebase fRef = new Firebase(URL + "UserProfiles");
                fRef.child(UID);
                fRef.child(UID).child(sEmailAddress).setValue(email);

                mContext.startActivity(intent);
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
    //logsInUser without launching an intent
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
    //logs in user and launches an intent
    public boolean logInUser(String email, String password, final Intent intent) {
        mSuccess = false;
        fireBaseRef.authWithPassword(email, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) { /* ... */
                        mSuccess = true;
                        UID = authData.getUid();
                        mContext.startActivity(intent);
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

    public Boolean changeUserEmail(String oldEmail, String newEmail, String password) {
        fireBaseRef.changeUserEmail(oldEmail, newEmail, password);

        //TODO check if successful or not and dispay toast

        return mSuccess;

    }


    public void addUserProfileInfoToDB(User user) {
        Firebase fRef = new Firebase(URL + "UserProfiles/");
        UID = user.getUID();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

    }

    public void addUserProfileInfoToDBAndLaunchIntent(User user, Intent intent) {
        Firebase fRef = new Firebase(URL + "UserProfiles/");
        UID = user.getUID();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

        mContext.startActivity(intent);
    }

    public void addSellerProfileInfoToDB(Seller user) {
        Firebase fRef = new Firebase(URL + "SellerProfiles/");
        UID = user.getUID();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());
        fRef.child(UID).child(sLatitude).setValue(user.getAddress().getLatitude());
        fRef.child(UID).child(sLongitude).setValue(user.getAddress().getLongitude());
    }

    public void addSellerProfileInfoToDB(User user,Intent intent) {
        Firebase fRef = new Firebase(URL + "SellerProfiles/");
        UID = user.getUID();

        fRef.child(UID).push();
        fRef.child(UID).child(sName).setValue(user.getName());
        fRef.child(UID).child(sEmailAddress).setValue(user.geteMail());
        fRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        fRef.child(UID).child(sPhotoLink).setValue(user.getPhotoLink());
        fRef.child(UID).child(sAddress).setValue(user.getAddress().toString());
        fRef.child(UID).child(sLatitude).setValue(user.getAddress().getLatitude());
        fRef.child(UID).child(sLongitude).setValue(user.getAddress().getLongitude());
    }

    public User getSellerFromDB(final String sellerID) {
        sellerId=sellerID;

        Firebase fRef = new Firebase(URL + "SellerProfiles/");
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Seller seller1 = dataSnapshot1.getValue(Seller.class);

                    if (dataSnapshot1.getKey().equals(sellerID)) {
                        seller.setName(seller1.name);
                        seller.setAddress(seller1.address);
                        seller.setDescription(seller1.description);
                        seller.seteMail(seller1.eMail);
                        seller.setUID(seller1.UID);
                        seller.setPhoneNumber(seller1.phoneNumber);
                        seller.setSellerItems(seller1.sellerItems);
                        seller.setLongitude(seller1.latitude);
                        seller.setLatitude(seller1.longitude);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Please Try Again", Toast.LENGTH_SHORT).show();
                user = null;
            }
        });

        return user;
    }

    public User getUserFromDB(final String userID) {
        this.UID=userID;

        Firebase fRef = new Firebase(URL + "UserProfiles/"+userID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user1 = dataSnapshot1.getValue(User.class);

                    if (dataSnapshot1.getKey().equals(userID)) {
                        user.setName(user1.name);
                        user.setAddress(user1.address);
                        user.seteMail(user1.eMail);
                        user.setUID(user1.UID);
                        user.setPhoneNumber(user1.phoneNumber);
                        user.setUserItems(user1.userItems);
                        user.setLongitude(user1.latitude);
                        user.setLongitude(user1.longitude);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Please Try Again", Toast.LENGTH_SHORT).show();
                user = null;
            }
        });

        return user;
    }

    public void addActiveSellerToTable(Seller seller){

        Firebase fRef = new Firebase(URL + "ActiveSellers/");
        sellerId = seller.getUID();

        fRef.child(sellerId).push();
        fRef.child(sellerId).child(sName).setValue(seller.getName());
        fRef.child(sellerId).child(sEmailAddress).setValue(seller.geteMail());
        fRef.child(sellerId).child(sPhoneNumber).setValue(seller.getPhoneNumber());
        fRef.child(sellerId).child(sPhotoLink).setValue(seller.getPhotoLink());
        fRef.child(sellerId).child(sAddress).setValue(seller.getAddress().toString());
        fRef.child(sellerId).child(sLatitude).setValue(seller.getAddress().getLatitude());
        fRef.child(sellerId).child(sLongitude).setValue(seller.getAddress().getLatitude());
    }

    public void getAllActiveSellers(){

        Firebase fRef = new Firebase(URL + "ActiveSellers/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");
                sizeofAddDBList = dataSnapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Seller seller1 = dataSnapshot1.getValue(Seller.class);
                    allActiveSellers.add(seller1);
                }
                updateAllActiveSellersList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Please Try Again", Toast.LENGTH_SHORT).show();
                user = null;
            }
        });

    }
    public ArrayList<Seller> updateAllActiveSellersList() {
        if (allActiveSellers.size() < sizeofAddDBList) {
            getAllActiveSellers();
        }
        return allActiveSellers;
    }

    public Seller getSpecificActiveSeller(final String sellerID){
        Firebase fRef = new Firebase(URL + "ActiveSellers/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Seller seller1 = dataSnapshot1.getValue(Seller.class);

                    if (dataSnapshot1.getKey().equals(sellerID)) {
                        seller.setName(seller1.name);
                        seller.setAddress(seller1.address);
                        seller.setDescription(seller1.description);
                        seller.seteMail(seller1.eMail);
                        seller.setUID(seller1.UID);
                        seller.setPhoneNumber(seller1.phoneNumber);
                        seller.setSellerItems(seller1.sellerItems);
                        seller.setLongitude(seller1.latitude);
                        seller.setLatitude(seller1.longitude);
                    }
                    Log.d("SellerOne", seller.name + "");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Seller not found", Toast.LENGTH_SHORT).show();
                seller = null;
            }
        });

        return seller;
    }
    //Method which adds a seller to the ActiveSellers table and updates their items for sale,
    //checks if quantity is greater than 0 for each item
    public void sendSellerToActiveSellerTable(Seller seller){
        this.sellerId=seller.getUID();
        Firebase fRef = new Firebase(URL + "ActiveSellers/"+sellerId);
        Seller sellerToSend= getSpecificActiveSeller(sellerId);

        fRef.child(sellerId).push();
        fRef.child(sellerId).child(sName).setValue(seller.getName());
        fRef.child(sellerId).child(sEmailAddress).setValue(seller.geteMail());
        fRef.child(sellerId).child(sPhoneNumber).setValue(seller.getPhoneNumber());
        fRef.child(sellerId).child(sPhotoLink).setValue(seller.getPhotoLink());
        fRef.child(sellerId).child(sAddress).setValue(seller.getAddress().toString());
        fRef.child(sellerId).child(sLatitude).setValue(seller.getAddress().getLatitude());
        fRef.child(sellerId).child(sLongitude).setValue(seller.getAddress().getLatitude());

        ArrayList<Item> itemsSellerHas= sellerToSend.getSellerItems();

        for(Item item: itemsSellerHas) {
            if (item.quantityAvailable > 0) {

                Firebase fRef1 = new Firebase(URL + "ActiveSellers" + sellerId + "/itemsForSale/");

                fRef1.child(sellerId).child(item.getNameOfItem()).push();
                fRef1.child(itemID).child("DESCRIPTION").setValue(item.descriptionOfItem);
                fRef1.child(itemID).child("QUANTITY").setValue(item.quantityAvailable);
                fRef1.child(itemID).child("ImageLink").setValue(item.imageLink);
                fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
                fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
                fRef.child(itemID).child("isVegan").setValue(item.getVegan());

            }
        }
    }

    //Method that adds one item to the sellers onSale Items
    public void addItemToActiveSellerTable(Item item) {
        sellerId = item.getBuyerID();
        Firebase fRef = new Firebase(URL + "ActiveSellers"+sellerId+"/itemsForSale/" );
        Firebase fire1 = fRef.child(sellerId).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(itemID).push();
        fRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
        fRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("ImageLink").setValue(item.getImageLink());
        fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
        fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
        fRef.child(itemID).child("isVegan").setValue(item.getVegan());

        Log.d("ItemID", itemID);
    }

    public void addItemToSellerProfileDB(Item item) {
        sellerId = item.getBuyerID();
        Firebase fRef = new Firebase(URL + "SellerProfiles"+sellerId+"/itemsForSale/" );
        Firebase fire1 = fRef.child(sellerId).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(itemID).push();
        fRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
        fRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("ImageLink").setValue(item.getImageLink());
        fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
        fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
        fRef.child(itemID).child("isVegan").setValue(item.getVegan());

        Log.d("ItemID", itemID);
    }

    //Method that sends a list of items to the sellersProfile... if num>0 display on listView
    public void sendArrayListOfItemsToItemsForSale(ArrayList<Item> itemsforSale,String sellerID){
        sellerId = sellerID;

        Firebase fRef = new Firebase(URL + "ActiveSellers/" + sellerId+ "itemsForSale/");

        for (Item item : itemsforSale) {
            String itemID = item.getItemID();

            fRef.child(itemID);
            fRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            fRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            fRef.child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            fRef.child(itemID).child("imageLink").setValue(item.getImageLink());
            fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
            fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
            fRef.child(itemID).child("isVegan").setValue(item.getVegan());
        }
    }

    public void removeSellersFromActiveSellers(Seller seller1){
        final Firebase fRef = new Firebase(URL + "ActiveSellers");
        final String sellerID=seller1.getUID();
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Seller seller1 = dataSnapshot1.getValue(Seller.class);

                    if (dataSnapshot1.getKey().equals(sellerID)) {
                        Log.d("SellerOne", seller.name + "");

                        fRef.child(dataSnapshot1.getKey()).removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Seller not found", Toast.LENGTH_SHORT).show();
                seller = null;
            }
        });

    }

    public Item removeItemFromSale(Item item1){
        sellerId = item1.getBuyerID();
        final String itemid1=item1.getItemID();


        final Firebase fRef = new Firebase(URL + "ActiveSellers/" + sellerId+ "itemsForSale/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Item item1 = dataSnapshot1.getValue(Item.class);

                    if (dataSnapshot1.getKey().equals(itemid1)) {
                        item.setItemID(item1.itemID);
                        item.setContainsPeanuts(item1.containsPeanuts);
                        item.setDescriptionOfItem(item1.descriptionOfItem);
                        item.setGlutenFree(item1.glutenFree);
                        item.setImageLink(item1.imageLink);
                        item.setNameOfItem(item1.nameOfItem);
                        item.setVegan(item1.vegan);
                        item.setQuantityAvailable(item1.quantityAvailable);
                        moveItemToPreviouslySoldItems(item);
                        moveItemToPreviouslyBoughtItems(item);

                        fRef.child(dataSnapshot1.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Error: Seller not found", Toast.LENGTH_SHORT).show();
                seller = null;
            }
        });
        return item;
    }

    public void moveItemToPreviouslySoldItems(Item item){
        Firebase fRef = new Firebase(URL + "PreviouslySoldItems/"+item.getSellerID()+"/"+item.getBuyerID());
        String itemID=item.getItemID();

        fRef.child(item.getSellerID()).child(item.getBuyerID()).child(itemID).push();
        fRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("buyerID").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("imageLink").setValue(item.getImageLink());
        fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
        fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
        fRef.child(itemID).child("isVegan").setValue(item.getVegan());
    }

    public void moveItemToPreviouslyBoughtItems(Item item){
        Firebase fRef = new Firebase(URL + "PreviouslyBoughtItems/"+item.getBuyerID()+"/"+item.getSellerID());
        String itemID=item.getItemID();

        fRef.child(item.getBuyerID()).child(item.getSellerID()).child(itemID).push();
        fRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("buyerID").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("imageLink").setValue(item.getImageLink());
        fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
        fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
        fRef.child(itemID).child("isVegan").setValue(item.getVegan());
    }

    public void sendSingleItemToCurrentlyOnSale(Item item) {
        sellerId = item.getSellerID();
        Firebase fRef = new Firebase(URL + "SellerProfiles/" + sellerId+"/"+"ItemsForSale/");

        Firebase fire1 = fRef.child(sellerId).push();
        String itemID = fire1.getKey();
        item.setItemID(itemID);

        fRef.child(itemID).push();
        fRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(itemID).child("buyerID").setValue(item.getQuantityAvailable());
        fRef.child(itemID).child("imageLink").setValue(item.getImageLink());
        fRef.child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
        fRef.child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
        fRef.child(itemID).child("isVegan").setValue(item.getVegan());

        Log.d("ItemID", itemID);
    }

    public void addCurrentOrderToSellerDB(Order order) {
        UID = order.getBuyerID();
        sellerId=order.getSellerID();

        Firebase fRef = new Firebase(URL + "ActiveSellers/" + sellerId+"/Orders/" + UID);
        ArrayList<Item> itemsOrdered = order.getItemsOrdered();

        Firebase fire1 = fRef.child(sellerId).push();
        String orderID = fire1.getKey();
        for (Item item : itemsOrdered) {

            String itemID = item.getItemID();
            fRef.child(UID).push();
            fRef.child(UID).child(orderID);
            fRef.child(orderID).child(itemID);
            fRef.child(orderID).child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            fRef.child(orderID).child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            fRef.child(orderID).child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            fRef.child(orderID).child(itemID).child("imageLink").setValue(item.getImageLink());
            fRef.child(orderID).child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
            fRef.child(orderID).child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
            fRef.child(orderID).child(itemID).child("isVegan").setValue(item.getVegan());
        }
    }

    //TODO check these 2 methods
    public void getSellersOnSaleItems(String sellerId) {
        this.sellerId=sellerId;

        Firebase fRef = new Firebase(URL + "ActiveSellers/"+sellerId + "itemsForSale/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Item item = dataSnapshot1.getValue(Item.class);
                    arrayListOfSellerItems.add(item);
                    Log.d("SNAPSHOT", "Got Snapshot");
                    String key = dataSnapshot1.getKey();
                    Log.d(itemID, key + "");
                    String nameOfItem1 = item.nameOfItem;
                    Log.d(nameOfItem, nameOfItem1 + "");

                    String descriptionOfItem1 = item.descriptionOfItem;
                    Log.d(descriptionOfItem, descriptionOfItem1 + "");

                    int quantityAvailable1 = item.quantityAvailable;
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

    public ArrayList<Item> updateItemsList(String sellerID){

        if(arrayListOfSellerItems.size()<sizeofAddDBList){
        }
        return arrayListOfSellerItems;
    }


    //TODO test review IDs
    public void sendReviewedOrderToSellerDB(Order order){
        Firebase fRef = new Firebase(URL + "sellerProfiles/" +order.getSellerID() + "/Reviews/" + order.getBuyerID());
        String orderID=order.getOrderID();

        fRef.child(order.getBuyerID()).child(order.getOrderID()).push();
        fRef.child(orderID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(orderID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(orderID).child("buyerID").setValue(item.getQuantityAvailable());
        fRef.child(orderID).child("imageLink").setValue(item.getImageLink());

    }

    public void sendReviewedOrderToBuyerDB(Order order){
        Firebase fRef = new Firebase(URL + "UserProfiles/" + order.getBuyerID()+ "Reviews/"+order.getSellerID());
        String orderID=order.getOrderID();

        fRef.child(order.getSellerID()).child(order.getOrderID()).push();
        fRef.child(orderID).child("nameOfItem").setValue(item.getNameOfItem());
        fRef.child(orderID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        fRef.child(orderID).child("buyerID").setValue(item.getQuantityAvailable());
        fRef.child(orderID).child("imageLink").setValue(item.getImageLink());
    }


    public ArrayList<User> getArrayListOfUsers() {
        //TOOD come back to this method and rethink the proccess

        Firebase fRef = new Firebase(URL + "UserProfiles/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number2", dataSnapshot.getChildrenCount() + "");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    User user = dataSnapshot1.getValue(User.class);
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

    public ArrayList<Item> getSellerItems(String sellerId) {
        this.sellerId = sellerId;

        Firebase fRef = new Firebase(URL + "SellerProfiles/" + sellerId+"/Items");

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



    public ArrayList<User> getAllSellers() {
        Firebase fRef = new Firebase(URL + "SellerProfiles/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sizeofAddDBList = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Seller seller = dataSnapshot1.getValue(Seller.class);
                    //Log.d("Fin", user.phoneNumber);
                    allSellersInDB.add(seller);
                }
                updateAllSellersList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return allUsers;
    }

    //Returns list of all users
    public ArrayList<Seller> updateAllSellersList(){

        if(allSellersInDB.size()<sizeofAddDBList){
            getAllSellers();
        }
        return allSellersInDB;
    }

    //gets a specific user from the DB
    public User getSpecificUser(String UID) {
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
    //gets a specific user from the DB
    public User getSpecificSeller(String sellerID) {
        Firebase fRef = new Firebase(URL+"SellerProfiles/"+ sellerID);

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

    //signs out user and clears data
    public boolean signOutUser() {
        fireBaseRef.unauth();
        userID=null;
        user.clearUser();
        address.clearAddress();
        userList.clear();
        items.clear();

        SharedPreferences sharedPreferences=mContext.getSharedPreferences("New User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();

        return true;
    }


    //adds an email/potential user to a users invite tree and sends an e-mail to the invited person
    public void addToInviteTree(String newUserInviteEmail,String inviteeID, Intent intent){
        Firebase fRef =  new Firebase(URL+"InviteTree");

        fRef.child(inviteeID).push();
        fRef.child(inviteeID).child("invited").setValue(newUserInviteEmail);
    }

    //Returns a list of emails of users invited by a specific user
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

    //method takes in an item and checks who the seller of that item is, then goes
    //into the database and subtracts the number that was bought from the quantity
    //the seller currently has available. So for this we can pass it for each item
    //in the order. You can run this in the background
    public void updateSellerItemsWhenItemIsBought(Item item){
        UID=item.getBuyerID();

        final int quantity=item.getQuantityAvailable();
        final String itemID=item.getItemID();

        Firebase fRef = new Firebase(URL + "SellerProfiles/"+sellerId+ "/Orders/" + "/"+UID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Item item = dataSnapshot1.getValue(Item.class);

                    if (item.getItemID() == itemID) {
                        int updateQuantityAvailable = item.quantityAvailable - quantity;
                        subtractBoughtQuantityFromQuantityInDB(item.getBuyerID(), item.getItemID(), updateQuantityAvailable);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //Method that actually updates the number in the database
    public String subtractBoughtQuantityFromQuantityInDB(String UID, String itemID, int quantityAvailable){
        Firebase fRef = new Firebase(URL + "SellerProfiles/" + UID+"/ItemsOnSale/"+itemID);

        fRef.child(itemID).push();
        fRef.child(itemID).child("quantityAvailable").setValue(quantityAvailable);
        return UID;
    }

    //method to send an order to the sellers database and then send that order to the seller
    public void sendOrderToSeller(String sellerId,Order order, String UID){
        this.UID = UID;
        this.sellerId=sellerId;

        Firebase fRef = new Firebase(URL + "SellerProfiles/" + sellerId+"/Orders/"+UID);

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
        getOrderToSendToSeller(UID, order, UID);
    }

    //method which returns the right order to be sent to the seller when buyer places an order
    public Order getOrderToSendToSeller(String sellerID, final Order order, String buyerID){
        sellerId=sellerID;
        final String orderID=order.getOrderID();

        Firebase fRef = new Firebase(URL + "SellerProfiles/" + sellerId+"/Orders/"+buyerID);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Order order1 = dataSnapshot1.getValue(Order.class);

                    if (order1.getOrderID() == orderID) {
                        returnOrder = order1;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Unable To Send Order To Seller, Please Try Again", Toast.LENGTH_SHORT).show();

            }
        });
        return returnOrder;
    }

    public void addUserReviewToUserProfile(User buyer, User seller, int numOfStars, String details){
        sellerId=seller.getUID();
        UID=buyer.getUID();
        Firebase fRef = new Firebase(URL + "UserProfiles/" + UID+"/Reviews/");

        fRef.child(sellerId).push();
        fRef.child(sellerId).child("numOfStars").setValue(numOfStars);
        fRef.child(sellerId).child("reviewDescription").setValue(details);
    }

    public void addUserReviewToUserProfile(User buyer, User seller, int numOfStars) {

        sellerId=seller.getUID();
        UID=buyer.getUID();
        Firebase fRef = new Firebase(URL + "UserProfiles/" + UID+"/Reviews/");

        fRef.child(sellerId).push();
        fRef.child(sellerId).child("numOfStars").setValue(numOfStars);

    }

    public void addReviewToSellerProfile(User buyer, User seller, int numOfStars){
        sellerId=seller.getUID();
        UID=buyer.getUID();
        Firebase fRef = new Firebase(URL + "SellerProfiles/"+sellerId+"/Reviews/");

        fRef.child(UID).push();
        fRef.child(UID).child("numOfStars").setValue(numOfStars);
    }

    public void addReviewToSellerProfile(User buyer, User seller, int numOfStars, String details){
        sellerId=seller.getUID();
        UID=buyer.getUID();
        Firebase fRef = new Firebase(URL + "SellerProfiles/"+sellerId+"/Reviews/");

        fRef.child(UID).push();
        fRef.child(UID).child("numOfStars").setValue(numOfStars);
        fRef.child(UID).child("reviewDescription").setValue(details);
    }

    public void getAllReviewsForCertainSeller(String sellerID){
        sellerId=sellerID;

        Firebase fRef = new Firebase(URL + "SellerProfiles/"+sellerId+"/Reviews/" );

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sizeofAddDBList = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Review review = dataSnapshot1.getValue(Review.class);
                    reviewArrayList.add(review);
                }
                updateListOfReviewsForSeller();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Unable To Retrieve Reviews Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getListOfReviewsForCertainUser(String UID){
        this.UID=UID;
        Firebase fRef = new Firebase(URL + "UserProfiles/"+ UID+"/Reviews/");

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sizeofAddDBList = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Review review = dataSnapshot1.getValue(Review.class);
                    reviewArrayList.add(review);
                }
                updateListOfReviews();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(mContext, "Unable To Retrieve Reviews Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Review> updateListOfReviews() {
        if (reviewArrayList.size() < sizeofAddDBList) {
            getListOfReviewsForCertainUser(UID);
        }
        return reviewArrayList;
    }

    public ArrayList<Review> updateListOfReviewsForSeller() {
        if (reviewArrayList.size() < sizeofAddDBList) {
            getAllReviewsForCertainSeller(sellerId);
        }
        return reviewArrayList;
    }

    public int increaseNumOfTotalStarsAndCalculateAvg(Seller seller,Review review){
        int numOfReviews= seller.getNumOfReviews();
        numOfReviews=numOfReviews+1;
        seller.setNumOfReviews(numOfReviews);

        int numOfTotalStars=seller.getNumOfTotalStars();
        int newStars=review.getNumOfStars();
        int newNumofTotalstars=numOfTotalStars+newStars;
        int newAvg= newNumofTotalstars/numOfReviews;

        updateSellerProfileWithNewAvg(seller,newAvg);

        return newAvg;
    }

    public void updateSellerProfileWithNewAvg(Seller seller, int newAvg){
        sellerId=seller.getUID();

        Firebase fRef = new Firebase(URL + "SellerProfiles/"+sellerId+"/Reviews/" );
        fRef.child(UID).push();
        fRef.child(UID).child("AvgNumOfStars").setValue(newAvg);
    }

    //May want to implement this later
    public void moveOrderFromActiveToFulfilled(String sellerId,String orderID,String buyerID){
        this.sellerId=sellerId;
        this.UID=buyerID;

        Firebase fRef =  new Firebase(URL + "SellerProfiles/"+sellerId+ "/Orders/" + "/"+UID);

        fRef.child(buyerID).removeValue();
    }
    public void sentToFullfilledOrdersTable(Order order) {
        sellerId = order.getSellerID();
        UID = order.getBuyerID();

        Firebase fRef = new Firebase(URL + "FulfilledOrders/");
        ArrayList<Item> itemsOrdered = order.getItemsOrdered();

        Firebase fire1 = fRef.child(sellerId).push();
        String orderID = fire1.getKey();
        for (Item item : itemsOrdered) {

            String itemID = item.getItemID();
            fRef.child(sellerId).child(UID).push();
            fRef.child(UID).child(orderID);
            fRef.child(orderID).child(itemID);
            fRef.child(orderID).child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            fRef.child(orderID).child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            fRef.child(orderID).child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            fRef.child(orderID).child(itemID).child("imageLink").setValue(item.getImageLink());
            fRef.child(orderID).child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
            fRef.child(orderID).child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
            fRef.child(orderID).child(itemID).child("isVegan").setValue(item.getVegan());
        }
    }
    public void sentToFullfilledOrdersTableWithReview(Order order){
        sellerId=order.getSellerID();
        UID=order.getBuyerID();

        Firebase fRef =  new Firebase(URL + "FulfilledOrders/");
        ArrayList<Item> itemsOrdered = order.getItemsOrdered();

        Firebase fire1 = fRef.child(sellerId).push();
        String orderID = fire1.getKey();
        for (Item item : itemsOrdered) {

            String itemID = item.getItemID();
            fRef.child(sellerId).child(UID).push();
            fRef.child(UID).child(orderID);
            fRef.child(orderID).child(itemID);
            fRef.child(orderID).child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            fRef.child(orderID).child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            fRef.child(orderID).child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            fRef.child(orderID).child(itemID).child("imageLink").setValue(item.getImageLink());
            fRef.child(orderID).child(itemID).child("containsPeanuts").setValue(item.getContainsPeanuts());
            fRef.child(orderID).child(itemID).child("isGluttenFree").setValue(item.getGlutenFree());
            fRef.child(orderID).child(itemID).child("isVegan").setValue(item.getVegan());
            fRef.child(orderID).child(itemID).child("numOfStars").setValue(order.getReview().getNumOfStars());
            fRef.child(orderID).child(itemID).child("reviewDescription").setValue(order.getReview().getReviewDescription());
        }
    }
    public void addFoodAndImageHashmapToFoodAndImageTable(HashMap<String, String> foods){
        Firebase fRef =  new Firebase(URL + "PreMadeFoodDatabase/");
        for(Map.Entry<String, String> food:foods.entrySet()){
            String name=food.getKey();
            String photoLink=food.getValue();
            fRef.child(name).setValue(photoLink);
        }
    }
    //May want to allow users to add their own photos later?
    public void addCustomMadeFoodItemToDatabase(String name, String linkToPhoto){
        Firebase fRef =  new Firebase(URL + "CustomFoodDatabase/");

        fRef.child(name).push().setValue(linkToPhoto);

    }

}
