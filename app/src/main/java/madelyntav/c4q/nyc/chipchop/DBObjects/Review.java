package madelyntav.c4q.nyc.chipchop.DBObjects;

/**
 * Created by c4q-madelyntavarez on 8/19/15.
 */
public class Review {
    String sellerID;
    String buyerID;
    int numOfStars;
    String description;

    public Review(){}

    public Review(String sellerID, String buyerID, int numOfStars) {
        this.sellerID = sellerID;
        this.buyerID = buyerID;
        this.numOfStars = numOfStars;
    }

    public Review(String sellerID, String description, int numOfStars, String buyerID) {
        this.sellerID = sellerID;
        this.description = description;
        this.numOfStars = numOfStars;
        this.buyerID = buyerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public int getNumOfStars() {
        return numOfStars;
    }

    public void setNumOfStars(int numOfStars) {
        this.numOfStars = numOfStars;
    }




}
