package madelyntav.c4q.nyc.chipchop.DBObjects;

import java.util.ArrayList;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class Order {
    String userID;
    String orderID;
    ArrayList<Item> itemsOrdered;

    public Order(ArrayList<Item> itemsOrdered){
        this.itemsOrdered=itemsOrdered;

    }

    public Order(String userID, ArrayList<Item> itemsOrdered){
        this.userID=userID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
