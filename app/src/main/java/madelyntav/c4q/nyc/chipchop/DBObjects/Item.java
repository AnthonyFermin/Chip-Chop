package madelyntav.c4q.nyc.chipchop.DBObjects;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class Item {
    String buyerID;
    String sellerID;
    String nameOfItem;
    int quantityAvailable;
    String descriptionOfItem;
    String imageLink;
    Boolean vegan;
    Boolean glutenFree;
    Boolean containsPeanuts;

    public Item(){}

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    String itemID;

    public Item(String buyerID,String sellerID, String nameOfItem, int quantityAvailable, String descriptionOfItem, String imageLink){
        this.buyerID = buyerID;
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

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
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

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getContainsPeanuts() {
        return containsPeanuts;
    }

    public void setContainsPeanuts(Boolean containsPeanuts) {
        this.containsPeanuts = containsPeanuts;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }
}
