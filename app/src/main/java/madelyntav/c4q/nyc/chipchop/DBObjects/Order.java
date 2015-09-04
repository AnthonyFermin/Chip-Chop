package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    String buyerID;
    double price;
    String orderID;
    Review review;
    String sellerID;
    ArrayList<Item> itemsOrdered;
    String timeStamp;
    boolean isActive;
    String storeName;

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
    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID, String timeStamp){
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
    public Order(String buyerID, ArrayList<Item> itemsOrdered,String sellerID, String timeStamp, boolean isActive){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.timeStamp=timeStamp;
        this.isActive=isActive;
        this.itemsOrdered=itemsOrdered;
    }

    public Order(String buyerID, double price, String orderID, Review review, String sellerID, String timeStamp, boolean isActive, String storeName) {
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
        this.review = review;
        this.sellerID = sellerID;
        this.timeStamp = timeStamp;
        this.isActive = isActive;
        this.storeName = storeName;
    }

    public Order(String buyerID, double price, String orderID, Review review, ArrayList<Item> itemsOrdered, String sellerID, String timeStamp, boolean isActive, String storeName) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
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
}
