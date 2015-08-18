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
    String eMail;
    String name;
    Address address;
    String photoLink;
    String phoneNumber;
    Firebase firebaseRef;
    Context context;
    String UID;
    String URL="https://chipchop.firebaseio.com/";


    public User(){}

    public User(String UID){
        this.UID=UID;
    }

    public User(String UID, String eMail){
        this.UID=UID;
        this.eMail = eMail;
    }
    public User(String UID, String eMail, String name, Address address, String phoneNumber){
        this.UID=UID;
        this.eMail = eMail;
        this.name=name;
        this.address=address;
        this.phoneNumber=phoneNumber;
    }

    public User(String UID, String eMail, String name, Address address, String photoLink, String phoneNumber){
        this.UID=UID;
        this.eMail = eMail;
        this.name=name;
        this.address=address;
        this.photoLink=photoLink;
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

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
        eMail =null;
        name=null;
        address=null;
        phoneNumber=null;
        UID=null;
        photoLink=null;
    }

}
