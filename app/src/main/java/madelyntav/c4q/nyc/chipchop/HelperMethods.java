package madelyntav.c4q.nyc.chipchop;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

/**
 * Created by c4q-anthonyf on 8/23/15.
 */
public class HelperMethods {

    public static Order currentOrder = null;
    public static User user = null;
    public static Seller sellerToView = null;

    public static Address parseAddressString(String addressString, String uid){

        String streetAddress, apt, city, zip, state;
        String[] addressLines = addressString.split(",");
        streetAddress = addressLines[0].trim();
        apt = addressLines[1].trim();
        city = addressLines[2].trim();
//        zip = addressLines[3].trim().split(" ")[1];
        state = addressLines[3].trim().split(" ")[0];

//        Log.d("Address","" + streetAddress + " " + apt + " " + city + ", " + state + " " + zip);

        Address address = new Address(streetAddress, apt, city, state, uid);


        return address;
    }

    public static String saveImageToEncodedString(String fileName){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public static void setCurrentOrder(Order currentOrder) {
        HelperMethods.currentOrder = currentOrder;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        HelperMethods.user = user;
    }

    public static Seller getSellerToView() {
        return sellerToView;
    }

    public static void setSellerToView(Seller sellerToView) {
        HelperMethods.sellerToView = sellerToView;
    }

}
