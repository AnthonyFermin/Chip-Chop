package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    String eMail;
    String name;
    Address address;
    String photoLink;
    String phoneNumber;
    String UID;
    ArrayList<Item> userItems;
    String latitude;
    String longitude;
    int numOfReviews;
    String addressString;
    String cardNumber;
    int cardExpirationMonth;
    int cardExpirationYear;
    String cardCVC;

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

    public User(String name, String cardNumber, int cardExpirationMonth, int cardExpirationYear, String cardCVC, String UID) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cardExpirationMonth = cardExpirationMonth;
        this.cardExpirationYear = cardExpirationYear;
        this.cardCVC = cardCVC;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Item> getUserItems() {
        return userItems;
    }

    public void setUserItems(ArrayList<Item> userItems) {
        this.userItems = userItems;
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

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public void clearUser(){
        eMail =null;
        name=null;
        address=null;
        phoneNumber=null;
        UID=null;
        photoLink=null;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(int cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public int getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(int cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public String getCardCVC() {
        return cardCVC;
    }

    public void setCardCVC(String cardCVC) {
        this.cardCVC = cardCVC;
    }
}
