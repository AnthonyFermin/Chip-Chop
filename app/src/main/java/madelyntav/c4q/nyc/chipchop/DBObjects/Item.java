package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    String buyerID;
    String sellerID;
    String nameOfItem;

    int quantity;
    double price;
    String descriptionOfItem;
    String imageLink;
    boolean isVegetarian;
    boolean containsEggs;
    boolean containsShellfish;
    boolean containsDairy;
    Boolean glutenFree;
    Boolean containsPeanuts;
    String itemID;

    int quantityWanted;

    public Item(){}

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }


    public Item(String sellerID,String buyerID, String nameOfItem, int quantity, String descriptionOfItem, String imageLink){
        this.buyerID = buyerID;
        this.sellerID=sellerID;
        this.nameOfItem=nameOfItem;
        this.quantity = quantity;
        this.descriptionOfItem=descriptionOfItem;
        this.imageLink=imageLink;
    }

    public Item(String sellerID, String nameOfItem, int quantity, String descriptionOfItem, String imageLink){
        this.sellerID=sellerID;
        this.nameOfItem=nameOfItem;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Boolean getVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.isVegetarian = vegetarian;
    }

    public Boolean getContainsPeanuts() {
        return containsPeanuts;
    }

    public void setContainsPeanuts(Boolean containsPeanuts) {
        this.containsPeanuts = containsPeanuts;
    }

    public int getQuantityWanted() {
        return quantityWanted;
    }

    public void setQuantityWanted(int quantityWanted) {
        this.quantityWanted = quantityWanted;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isVegan() {
        return isVegetarian;
    }

    public void setIsVegan(boolean isVegan) {
        this.isVegetarian = isVegan;
    }

    public boolean isContainsShellfish() {
        return containsShellfish;
    }

    public void setContainsShellfish(boolean containsShellfish) {
        this.containsShellfish = containsShellfish;
    }

    public boolean isContainsEggs() {
        return containsEggs;
    }

    public void setContainsEggs(boolean containsEggs) {
        this.containsEggs = containsEggs;
    }

    public boolean isContainsDairy() {
        return containsDairy;
    }

    public void setContainsDairy(boolean containsDairy) {
        this.containsDairy = containsDairy;
    }
}
