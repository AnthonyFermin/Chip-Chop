package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class User {
    String sName="NAME";
    String sEmailAddress="Email";
    String sPhoneNumber="Phone Number";
    String sAddress="ADDRESS";
    String sPhotoLink="PhotoLink";
    String emailAddress;
    String name;
    Address address;
    String encodedPhotoString;
    String phoneNumber;
    Firebase firebaseRef;
    Context context;
    String UID;
    String URL="https://chipchop.firebaseio.com/";

    public User(String UID, String emailAddress, String name, Address address, String encodedPhotoString, String phoneNumber){
        this.UID=UID;
        this.emailAddress=emailAddress;
        this.name=name;
        this.address=address;
        this.encodedPhotoString=encodedPhotoString;
        this.phoneNumber=phoneNumber;

       // addNewUserToDB();
    }

    public String getUserId() {
        return UID;
    }

    public void setUserId(String userId) {
        this.UID = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEncodedPhotoString() {
        return encodedPhotoString;
    }

    public void setEncodedPhotoString(String encodedPhotoString) {
        this.encodedPhotoString = encodedPhotoString;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private void addNewUserToDB(){
        firebaseRef= new Firebase(URL+"Users");

        firebaseRef.child(UID);
        firebaseRef.child(UID).child(sName).setValue(name);
        firebaseRef.child(UID).child(sEmailAddress).setValue(emailAddress);
        firebaseRef.child(UID).child(sPhoneNumber).setValue(phoneNumber);
        firebaseRef.child(UID).child(sPhotoLink).setValue(encodedPhotoString);
        firebaseRef.child(UID).child(sAddress).setValue(address.toString());

    }
}