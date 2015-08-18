package madelyntav.c4q.nyc.chipchop.DBObjects;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class Item {
    String userID;
    String nameOfItem;
    int quantityAvailable;
    String descriptionOfItem;
    String imageLink;

    public Item(){}

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    String itemID;

    public Item(String userID, String nameOfItem, int quantityAvailable, String descriptionOfItem, String imageLink){
        this.userID=userID;
        this.nameOfItem=nameOfItem;
        this.quantityAvailable=quantityAvailable;
        this.descriptionOfItem=descriptionOfItem;
        this.imageLink=imageLink;
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

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getDescriptionOfItem() {
        return descriptionOfItem;
    }

    public void setDescriptionOfItem(String descriptionOfItem) {
        this.descriptionOfItem = descriptionOfItem;
    }
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }


}
