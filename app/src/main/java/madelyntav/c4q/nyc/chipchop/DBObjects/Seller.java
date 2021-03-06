package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/20/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Seller {

    ArrayList<Item> items;
    Item itemsForSale;
    String eMail;
    String name;
    Address address;
    String photoLink;
    String phoneNumber;
    String UID;
    String description;
    String latitude;
    String longitude;
    boolean isCooking;
    String storeName;
    int numOfReviews;
    int numOfTotalStars;
    int newReviewNumOfStars;
    String addressString;
    String cardNumber;
    int cardExpirationMonth;
    int cardExpirationYear;
    String cardCVC;
    String routingNumber;
    String accountNumber;
    double distanceFromBuyer;
    boolean pickUpAvailable;
    boolean deliveryAvailable;

    public Seller(){}

    public Seller(String UID){
        this.UID=UID;
    }

    public Seller(String UID, String eMail){
        this.UID=UID;
        this.eMail = eMail;
    }
    public Seller(String UID, String eMail, String name, Address address,String storeName, String phoneNumber){
        this.UID=UID;
        this.eMail = eMail;
        this.name=name;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.storeName=storeName;
        this.photoLink= "";
        this.addressString = address.toString();
    }

    public Seller(String UID, String eMail, String name, Address address, String photoLink,String storeName, String phoneNumber){
        this.UID=UID;
        this.eMail = eMail;
        this.name=name;
        this.address=address;
        this.storeName=storeName;
        this.photoLink=photoLink;
        this.phoneNumber=phoneNumber;
    }
    public Seller(String UID, String eMail, String name, Address address, String photoLink,String storeName, String phoneNumber, Boolean isCooking){
        this.UID=UID;
        this.eMail = eMail;
        this.name=name;
        this.address=address;
        this.storeName=storeName;
        this.photoLink=photoLink;
        this.phoneNumber=phoneNumber;
        this.isCooking=isCooking;
    }


    public Seller(String UID, String cardNumber, int cardExpirationMonth, int cardExpirationYear, String cardCVC) {
        this.UID = UID;
        this.cardNumber = cardNumber;
        this.cardExpirationMonth = cardExpirationMonth;
        this.cardExpirationYear = cardExpirationYear;
        this.cardCVC = cardCVC;
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

    public ArrayList<Item> getItems() { return items; }

    public void setItems(ArrayList<Item> items) { this.items = items; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getUID() { return UID; }

    public void setUID(String UID) { this.UID = UID; }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsCooking() {
        return isCooking;
    }

    public void setIsCooking(Boolean isCooking) {
        this.isCooking = isCooking;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public int getNumOfTotalStars() {
        return numOfTotalStars;
    }

    public void setNumOfTotalStars(int numOfTotalStars) {
        this.numOfTotalStars = numOfTotalStars;
    }

    public int getNewReviewNumOfStars() {
        return newReviewNumOfStars;
    }

    public void setNewReviewNumOfStars(int newReviewNumOfStars) {
        this.newReviewNumOfStars = newReviewNumOfStars;
    }

    public Item getItemsForSale() {
        return itemsForSale;
    }

    public void setItemsForSale(Item itemsForSale) {
        this.itemsForSale = itemsForSale;
    }


    public void clearUser(){
        eMail =null;
        name=null;
        address=null;
        phoneNumber=null;
        UID=null;
        photoLink=null;
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

    public String getCardCVC() {
        return cardCVC;
    }

    public void setCardCVC(String cardCVC) {
        this.cardCVC = cardCVC;
    }

    public int getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(int cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public double getDistanceFromBuyer() {
        return distanceFromBuyer;
    }

    public void setDistanceFromBuyer(double distanceFromBuyer) {
        this.distanceFromBuyer = distanceFromBuyer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public boolean isPickUpAvailable() {
        return pickUpAvailable;
    }

    public void setPickUpAvailable(boolean pickUpAvailable) {
        this.pickUpAvailable = pickUpAvailable;
    }

    public boolean isDeliveryAvailable() {
        return deliveryAvailable;
    }

    public void setDeliveryAvailable(boolean deliveryAvailable) {
        this.deliveryAvailable = deliveryAvailable;
    }
}
