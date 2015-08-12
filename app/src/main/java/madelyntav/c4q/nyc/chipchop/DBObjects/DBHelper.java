package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by c4q-madelyntavarez on 8/12/15.
 */
public class DBHelper {
    Firebase firebaseRef;
    String URL="https://chipchop.firebaseio.com/";
    Context context;
    String UID;
    String sName="NAME";
    String sEmailAddress="Email";
    String sPhoneNumber="Phone Number";
    String sAddress="ADDRESS";
    String sPhotoLink="Photo Link";
    String sStreet="STREET";
    String sApartment="APARTMENT";
    String sCity="CITY";
    String sState="STATE";
    String sZipCode="ZIPCODE";
    User user;
    Address address;

    public DBHelper(Context context){
        this.context=context;
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
//                firebaseRef.child("UserName").setValue(email);
//                firebaseRef.child("UserName").child("passWord").setValue("Tomorrow");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(context,"Email already in use",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserProfileInfoToDB(User user){
        firebaseRef= new Firebase(URL+"Users");
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
        firebaseRef.child(itemID).child("ImageLink").setValue(item.getEncodedImageString());

        Log.d("ItemID",itemID);

    }

    public void addOrderToDB(Order order){
        UID="";
        UID=order.getUserID();
        firebaseRef= new Firebase(URL+"Orders/"+UID);

        ArrayList<Item> itemsOrdered=order.getItemsOrdered();

        Firebase fire1=firebaseRef.child(UID).push();
        String orderID=fire1.getKey();
        firebaseRef.child(UID).child(orderID).push();

        for(Item item: itemsOrdered) {
            String itemID=item.getItemID();
            firebaseRef.child(UID).child(itemID).child(itemID).push();
            firebaseRef.child(itemID).child("NAME").setValue(item.getNameOfItem());
            firebaseRef.child(itemID).child("DESCRIPTION").setValue(item.getDescriptionOfItem());
            firebaseRef.child(itemID).child("QUANTITY").setValue(item.getQuantityAvailable());
            firebaseRef.child(itemID).child("ImageLink").setValue(item.getEncodedImageString());
        }

    }
}
