package madelyntav.c4q.nyc.chipchop.DBObjects;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class Item {
    String userID;
    String nameOfItem;
    String quantityAvailable;
    String descriptionOfItem;
    String encodedImageString;

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    String itemID;

    public Item(String userID, String nameOfItem, String quantityAvailable, String descriptionOfItem, String encodedImageString){
        this.userID=userID;
        this.nameOfItem=nameOfItem;
        this.quantityAvailable=quantityAvailable;
        this.descriptionOfItem=descriptionOfItem;
        this.encodedImageString=encodedImageString;
    }

    public String getNameOfItem() {
        return nameOfItem;
    }

    public void setNameOfItem(String nameOfItem) {
        this.nameOfItem = nameOfItem;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getDescriptionOfItem() {
        return descriptionOfItem;
    }

    public void setDescriptionOfItem(String descriptionOfItem) {
        this.descriptionOfItem = descriptionOfItem;
    }
    public String getEncodedImageString() {
        return encodedImageString;
    }

    public void setEncodedImageString(String encodedImageString) {
        this.encodedImageString = encodedImageString;
    }


}
