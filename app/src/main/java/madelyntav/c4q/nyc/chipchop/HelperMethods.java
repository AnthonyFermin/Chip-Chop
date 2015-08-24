package madelyntav.c4q.nyc.chipchop;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;

/**
 * Created by c4q-anthonyf on 8/23/15.
 */
public class HelperMethods {

    public static Address parseAddressString(String addressString, String uid){

        String streetAddress, apt, city, zip;
        String[] addressLines = addressString.split(",");
        streetAddress = addressLines[0];
        apt = addressLines[1];
        city = addressLines[2];
        zip = addressLines[3].split(" ")[1];

        Address address = new Address(streetAddress, apt, city, "NY", zip, uid);


        return address;
    }


}
