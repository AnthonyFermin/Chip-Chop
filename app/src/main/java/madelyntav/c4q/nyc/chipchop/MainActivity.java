package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class MainActivity extends AppCompatActivity {

    Firebase firebaseRef;
    String userID="5";
    String sellerID="101";
    String name="Madey";
    String phoneNumber="646-549-0877";
    String streetAddress="570 West 189 street";
    String apt="5E";
    String city="New York";
    String state="NY";
    String zipCode="10040";
    Item item;
    String email="madey189@aol.com";
    Order order;
    User user;
    Seller seller;
    Address address;
    Address sellerAddress;
    String sellerName="Jack";
    String sellerPhone="646-777-9087";
    String sellerStreet="256 Wadsworth Avenue";
    String sellerApt="2A";
    String sellerCity="New York";
    String sellerState="NY";
    String sellerZipcode="10033";
    String sellerEmail="JackFinnish@gmail.com";


    private static DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=DBHelper.getDbHelper(this);


        item=new Item();
        order=new Order();
        address=new Address(streetAddress,apt,city,state,zipCode,userID);
        user=new User(userID,email,name,address,phoneNumber);
        sellerAddress= new Address(sellerStreet,sellerApt,sellerCity,sellerState,sellerZipcode,sellerID);
        seller=new Seller(sellerID,sellerEmail,sellerName,sellerAddress,sellerPhone);


    }


    //Madelyn's Testing Code Below


    public void sendData(View view) {
        //dbHelper.addUserProfileInfoToDB(user);
        //dbHelper.addSellerProfileInfoToDB(seller);
        //dbHelper.addSellerProfileInfoToDB(seller);
        dbHelper.addActiveSellerToTable(seller);

    }


    public void getData(View v){
       // dbHelper.getSellerFromDB("101");
        dbHelper.getSpecificActiveSeller(sellerID);

    }

   public void saveImageToEncodedString(String fileName){
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
   }
}
