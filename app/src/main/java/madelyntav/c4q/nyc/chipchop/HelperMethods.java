package madelyntav.c4q.nyc.chipchop;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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


}
