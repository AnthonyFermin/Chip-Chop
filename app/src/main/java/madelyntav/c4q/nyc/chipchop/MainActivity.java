package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import madelyntav.c4q.nyc.chipchop.DBObjects.Address;
import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class MainActivity extends AppCompatActivity {

    Firebase firebaseRef;
    String userID="";
    private DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = dbHelper.getDbHelper(this);

    }


    //Madelyn's Testing Code Below

    public void createUserLets(View view){

        //dbHelper.createUser("hjg00fhjhn909hjkhjjh@gmail.com", "hjdfjvghdafi");


        //dbHelper.getSellersOnSaleItems(order);
        Address address= new Address("570 west 189 street", "Apt 64", "New York", "NY", "10040", "1");
        User user= new User("1","MadelynTav@Gmail.com","Madelyn Tavarez",address,"Photo","677-987-0564");
        //47-98 31st Pl, Queens, NY, 11101
        Address address1=new Address("47-98 31st Pl","","Queens","NY","11101","2");
        User user1=new User("2","coalition@gmail.com","Coalition4Queens",address1,"Photo","677-988-0988");

        dbHelper.addUserAddressToProfile(user);
        dbHelper.addUserAddressToProfile(user1);

    }
    public void getData(View v){
        LatLng latLng=new LatLng(40.748817,-73.985428);
        dbHelper.addAddressesToMap(latLng);

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
