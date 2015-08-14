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
public class DBHelper {
    Firebase firebaseRef;
    private static final String URL="https://chipchop.firebaseio.com/";
    Context context;
    String UID;

    private static final String userID="userID";
    private static final DBHelper dbHelper = null;
    private static final String sStreet="streetAddress";
    private static final String sApartment="apartment";
    private static final String sCity="city";
    private static final String sState="state";
    private static final String sZipCode="zipCode";
    private static final String nameOfItem="nameOfItem";
    private static final String descriptionOfItem="descriptionOfItem";
    private static final String quantityAvailable="quantityAvailable";
    private static final String itemID="itemID";
    private static final String imageLink="imageLink";
    private static final String sName="name";
    private static final String sEmailAddress="eMail";
    private static final String sPhoneNumber="phoneNumber";
    private static final String sAddress="address";
    private static final String sPhotoLink="photoLink";
    User user;
    Address address;

    public DBHelper(Context context){
        this.context=context;
        firebaseRef=new Firebase(URL);
    }

    public Firebase getFirebaseRef() {
        return firebaseRef;
    }
    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    public void createUser(final String email, String password){
        UID = "";

        firebaseRef.createUser(email,password , new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                String userIDOne=String.valueOf(stringObjectMap.get("uid"));
                for(int i=12;i<userIDOne.length();i++){
                    UID +=userIDOne.charAt(i);
                }

                SharedPreferences sharedPreferences= context.getSharedPreferences("UID",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("id",UID);
                editor.apply();
                    firebaseRef = new Firebase(URL+"UserLogins");
                firebaseRef.child(UID);

//                firebaseRef.child(UID).child("UserName").setValue(email);
//                firebaseRef.child(UID).child("UserName").child("passWord").setValue("Tomorrow");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(context,"Email already in use",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserProfileInfoToDB(User user){
        firebaseRef= new Firebase(URL+"UserProfiles");
        UID=user.getUserId();

        firebaseRef.child(UID).push();
        firebaseRef.child(UID).child(sName).setValue(user.getName());
        firebaseRef.child(UID).child(sEmailAddress).setValue(user.getEmailAddress());
        firebaseRef.child(UID).child(sPhoneNumber).setValue(user.getPhoneNumber());
        firebaseRef.child(UID).child(sPhotoLink).setValue(user.getEncodedPhotoString());
        firebaseRef.child(UID).child(sAddress).setValue(user.getAddress().toString());

    }

    public void addUserAddressToProfile(User user){
        UID="";
        Address address=user.getAddress();
        UID=address.getUserID();
        firebaseRef= new Firebase(URL+"Addresses");

        firebaseRef.child(UID).push();
        firebaseRef.child(UID).child(sStreet).setValue(address.getStreetAddress());
        firebaseRef.child(UID).child(sApartment).setValue(address.getApartment());
        firebaseRef.child(UID).child(sCity).setValue(address.getCity());
        firebaseRef.child(UID).child(sState).setValue(address.getState());
        firebaseRef.child(UID).child(sZipCode).setValue(address.getZipCode());

    }

    public void addItemToDB(Item item){
        UID="";
        UID=item.getUserID();
        firebaseRef= new Firebase(URL+"Items/"+UID);

        Firebase fire1=firebaseRef.child(UID).push();
        String itemID=fire1.getKey();
        item.setItemID(itemID);

        firebaseRef.child(UID).child(itemID).push();
        firebaseRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
        firebaseRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
        firebaseRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
        firebaseRef.child(itemID).child("ImageLink").setValue(item.getImageLink());

        Log.d("ItemID", itemID);
    }

    public void addOrderToDB(Order order){
        UID="";
        UID=order.getUserID();

        firebaseRef= new Firebase(URL+"Orders/"+UID);

        ArrayList<Item> itemsOrdered=order.getItemsOrdered();

        Firebase fire1=firebaseRef.child(UID).push();
        String orderID=fire1.getKey();



        for(Item item: itemsOrdered) {

            String itemID=item.getItemID();
            firebaseRef.child(UID).child(orderID).push();
            firebaseRef.child(orderID).child(itemID);
            firebaseRef.child(orderID).child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
            firebaseRef.child(orderID).child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
            firebaseRef.child(orderID).child(itemID).child("quantityAvailable").setValue(item.getQuantityAvailable());
            firebaseRef.child(orderID).child(itemID).child("imageLink").setValue(item.getImageLink());
        }

    }

    public void currentlyOnSale(Item item){

        UID="";
        UID=item.getUserID();
        firebaseRef= new Firebase(URL+"itemsForSale/"+UID);

        Firebase fire1=firebaseRef.child(UID).push();
        String itemID=fire1.getKey();
        item.setItemID(itemID);

        firebaseRef.child(UID).child(itemID).push();
        firebaseRef.child(itemID).child("nameOfItem").setValue(item.getNameOfItem());
        firebaseRef.child(itemID).child("descriptionOfItem").setValue(item.getDescriptionOfItem());
        firebaseRef.child(itemID).child("userID").setValue(item.getQuantityAvailable());
        firebaseRef.child(itemID).child("imageLink").setValue(item.getImageLink());

        Log.d("ItemID", itemID);
    }

    public void getSellersOnSaleItems(Order order){
        UID="";

        UID=order.getUserID();
        String orderID=order.getOrderID();

        firebaseRef=new Firebase(URL+"Orders/"+"5"+"/"+orderID);

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Number",dataSnapshot.getChildrenCount()+"");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                     Item item=dataSnapshot1.getValue(Item.class);
                        Log.d("SNAPSHOT","Got Snapshot");
                   String key= dataSnapshot1.getKey();
                         Log.d(itemID,key+"");
                     String nameOfItem1=item.nameOfItem;
                        Log.d(nameOfItem,nameOfItem1+"");

                    String descriptionOfItem1=item.descriptionOfItem;
                    Log.d(descriptionOfItem,descriptionOfItem1+"");

                    String quantityAvailable1=item.quantityAvailable;
                    Log.d(quantityAvailable,quantityAvailable1+"");

                    String imageLink1=item.imageLink;
                    Log.d(imageLink,imageLink1+"");

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Error Retrieving","Error");

            }
        });

        }

}
