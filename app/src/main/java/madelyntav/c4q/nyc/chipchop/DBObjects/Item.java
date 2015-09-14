package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Item{
    String buyerID;
    String sellerID;
    String nameOfItem;
    int quantity;
    int price;
    String descriptionOfItem;
    String imageLink;
    boolean isVegetarian;
    boolean containsEggs;
    boolean containsShellfish;
    boolean containsDairy;
    boolean glutenFree;
    boolean containsPeanuts;
    String itemID;
    int quantityWanted;
    String sellerPhoneNumber;
    String buyerPhoneNumber;

    public Item(){}
    public Item(String itemID){
        this.itemID=itemID;
    }

    public Item(boolean containsPeanuts){
        this.containsPeanuts=containsPeanuts;
    }

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

    //TODO: ANTHONY - USE THIS CONSTRUCTOR WHEN ADDING TO CART
    public Item(String itemID,String sellerID,String buyerID, String nameOfItem, int quantityWanted){
        this.buyerID = buyerID;
        this.itemID=itemID;
        this.sellerID=sellerID;
        this.nameOfItem=nameOfItem;
        this.quantityWanted = quantityWanted;
    }

    public Item(String sellerID, String nameOfItem, int quantity, String descriptionOfItem, String imageLink){
        this.sellerID=sellerID;
        this.nameOfItem=nameOfItem;
        this.quantity = quantity;
        this.descriptionOfItem=descriptionOfItem;
        this.imageLink=imageLink;
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

    public String getNameOfItem() {
        return nameOfItem;
    }

    public void setNameOfItem(String nameOfItem) {
        this.nameOfItem = nameOfItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public boolean isContainsEggs() {
        return containsEggs;
    }

    public void setContainsEggs(boolean containsEggs) {
        this.containsEggs = containsEggs;
    }

    public boolean isContainsShellfish() {
        return containsShellfish;
    }

    public void setContainsShellfish(boolean containsShellfish) {
        this.containsShellfish = containsShellfish;
    }

    public boolean isContainsDairy() {
        return containsDairy;
    }

    public void setContainsDairy(boolean containsDairy) {
        this.containsDairy = containsDairy;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isContainsPeanuts() {
        return containsPeanuts;
    }

    public void setContainsPeanuts(boolean containsPeanuts) {
        this.containsPeanuts = containsPeanuts;
    }

    public int getQuantityWanted() {
        return quantityWanted;
    }

    public void setQuantityWanted(int quantityWanted) {
        this.quantityWanted = quantityWanted;
    }

    public String getSellerPhoneNumber() {
        return sellerPhoneNumber;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber) {
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    @Override
    public Item clone() {
        Item item = new Item(this.getSellerID(),this.getBuyerID(),this.getNameOfItem(),this.getQuantity()
        ,this.getDescriptionOfItem(),this.getImageLink());
        item.setPrice(this.getPrice());
        item.setContainsShellfish(this.isContainsShellfish());
        item.setContainsPeanuts(this.isContainsPeanuts());
        item.setContainsDairy(this.isContainsDairy());
        item.setContainsEggs(this.isContainsEggs());
        item.setIsVegetarian(this.isVegetarian());
        item.setGlutenFree(this.isGlutenFree());
        item.setItemID(this.getItemID());
        item.setQuantityWanted(this.getQuantityWanted());

        return item;
    }
}
