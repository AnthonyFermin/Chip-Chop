package madelyntav.c4q.nyc.chipchop.DBObjects;
import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */

public class Order {
    String buyerID;
    String orderID;
    Review review;
    String sellerID;
    ArrayList<Item> itemsOrdered;

    public Order(){
        //this.buyerID=buyerID;
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


}
