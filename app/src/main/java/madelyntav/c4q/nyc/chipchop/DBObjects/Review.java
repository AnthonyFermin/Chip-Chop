package madelyntav.c4q.nyc.chipchop.DBObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by c4q-madelyntavarez on 8/19/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    String sellerID;
    String buyerID;
    int numOfStars;
    String reviewDescription;

    public Review(){}

    public Review(String sellerID, String buyerID, int numOfStars) {
        this.sellerID = sellerID;
        this.buyerID = buyerID;
        this.numOfStars = numOfStars;
    }

    public Review(String sellerID, String reviewDescription, int numOfStars, String buyerID) {
        this.sellerID = sellerID;
        this.reviewDescription = reviewDescription;
        this.numOfStars = numOfStars;
        this.buyerID = buyerID;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
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
