package madelyntav.c4q.nyc.chipchop.DBObjects;

/**
 * Created by c4q-madelyntavarez on 8/11/15.
 */
public class Address {
    String streetAddress;
    String apartment;
    String city;
    String state;
    String zipCode;
    String userID;

    public Address(String streetAddress, String apartment, String city, String state, String zipCode,String userID){
        this.streetAddress=streetAddress;
        this.apartment=apartment;
        this.city=city;
        this.state=state;
        this.zipCode=zipCode;
        this.userID=userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return streetAddress+", APT:"+apartment+", "+city+", "+state.toUpperCase()+" "+zipCode;
    }
}
