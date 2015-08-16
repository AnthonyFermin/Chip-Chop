package madelyntav.c4q.nyc.chipchop.DBObjects;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class User {
    String sName="name";
    String sEmailAddress="eMail";
    String sPhoneNumber="phoneNumber";
    String sAddress="address";
    String sPhotoLink="photoLink";
    String emailAddress;
    String name;
    Address address;
    String encodedPhotoString;
    String phoneNumber;
    Firebase firebaseRef;
    Context context;
    String UID;
    String URL="https://chipchop.firebaseio.com/";

    public User(){}

    public User(String UID){
        this.UID=UID;
    }

    public User(String UID, String emailAddress, String name, Address address, String encodedPhotoString, String phoneNumber){
        this.UID=UID;
        this.emailAddress=emailAddress;
        this.name=name;
        this.address=address;
        this.encodedPhotoString=encodedPhotoString;
        this.phoneNumber=phoneNumber;
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

    public void clearUser(){
        emailAddress=null;
        name=null;
        address=null;
        phoneNumber=null;
        UID=null;
        encodedPhotoString=null;
    }

}
