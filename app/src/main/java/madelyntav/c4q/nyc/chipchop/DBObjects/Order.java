package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    String buyerID;
    int price;
    String orderID;
    Review review;
    String sellerID;
    ArrayList<Item> itemsOrdered;
    long timeStamp;
    boolean isActive;
    boolean isPickup;
    boolean toDeliver;
    String storeName;
    String buyerAddress;
    String sellerAddress;
    String sellerPhoneNumber;
    String buyerPhoneNumber;
    String transactionToken;
    String paymentType;
    String buyerName;
    String sellerName;
    boolean isReviewed;

    public Order(){
        this.itemsOrdered = new ArrayList<>();
    }

    public Order(ArrayList<Item> itemsOrdered){
        this.itemsOrdered=itemsOrdered;
    }

    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID,Review review){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.itemsOrdered=itemsOrdered;
        this.review=review;
    }

    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.itemsOrdered=itemsOrdered;
    }
    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID, long timeStamp){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.timeStamp=timeStamp;
        this.itemsOrdered=itemsOrdered;
    }
    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID, boolean isActive){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.isActive=isActive;
        this.itemsOrdered=itemsOrdered;
    }
    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID, long timeStamp, boolean isActive){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.timeStamp=timeStamp;
        this.isActive=isActive;
        this.itemsOrdered=itemsOrdered;
    }

    public Order(String buyerID, int price, String orderID, Review review, String sellerID, long timeStamp, boolean isActive, String storeName) {
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
        this.review = review;
        this.sellerID = sellerID;
        this.timeStamp = timeStamp;
        this.isActive = isActive;
        this.storeName = storeName;
    }

    public Order(String buyerID, int price, String orderID, Review review, ArrayList<Item> itemsOrdered, String sellerID, long timeStamp, boolean isActive, String storeName) {
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
        this.review = review;
        this.itemsOrdered = itemsOrdered;
        this.sellerID = sellerID;
        this.timeStamp = timeStamp;
        this.isActive = isActive;
        this.storeName = storeName;
    }

    public Order(String buyerID, int price, String orderID, String sellerID, long timeStamp, ArrayList<Item> itemsOrdered, String storeName, String sellerPhoneNumber, String buyerPhoneNumber, String transactionToken, String paymentType) {
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
        this.sellerID = sellerID;
        this.timeStamp = timeStamp;
        this.itemsOrdered = itemsOrdered;
        this.storeName = storeName;
        this.sellerPhoneNumber = sellerPhoneNumber;
        this.buyerPhoneNumber = buyerPhoneNumber;
        this.transactionToken = transactionToken;
        this.paymentType = paymentType;
    }

    public Order(String buyerID, String sellerID){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.itemsOrdered= new ArrayList<>();
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public ArrayList<Item> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(ArrayList<Item> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getSellerPhoneNumber() { return sellerPhoneNumber; }

    public void setSellerPhoneNumber(String sellerPhoneNumber) { this.sellerPhoneNumber = sellerPhoneNumber; }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public boolean isPickup() {
        return isPickup;
    }
    public void setIsPickup(boolean isPickup) {
        this.isPickup = isPickup;
    }

    public boolean isToDeliver() {
        return toDeliver;
    }

    public void setToDeliver(boolean toDeliver) {
        this.toDeliver = toDeliver;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(boolean isReviewed) {
        this.isReviewed = isReviewed;
    }
}

