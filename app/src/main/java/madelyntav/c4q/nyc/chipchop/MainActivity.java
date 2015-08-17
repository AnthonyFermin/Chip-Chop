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
import madelyntav.c4q.nyc.chipchop.DBObjects.User;

public class MainActivity extends AppCompatActivity {

    Firebase firebaseRef;
    String userID="";
    private static DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        dbHelper=DBHelper.getDbHelper(this);
        if(dbHelper == null) {
            dbHelper = new DBHelper();
        }



    }


    //Madelyn's Testing Code Below



    public void createUserLets(View view){
//        Item item=new Item("5","Beans","5","yellow","The fin");
//        item.setItemID("hgg");
//        Item item1= new Item("5","Rice","2","Black","IGHGH");
//        item1.setItemID("gff");
//        Item item2= new Item("5","Chicken","6","Blue","jfjfd");
//        item2.setItemID("Iggj");
//
//        ArrayList<Item> items=new ArrayList<>();
//        items.add(item);
//        items.add(item1);
//        items.add(item2);
//        Order order= new Order();
//        order.setOrderID("-Jwh6593TmhGUXVJ2oMP");
//        order.setUserID("5");

        //dbHelper.addOrderToDB(order);


        //dbHelper.getSellersOnSaleItems(order);
        Address address= new Address("560 east 242nd Stret", "Apt 64", "New York", "NY", "10040", "5",1,1);

        User user= new User("5","MadelynTav@Gmail.com","Madelyn Tavarez",address,"Photo","677-987-0564");

        dbHelper.addUserProfileInfoToDB(user);


    }
    public void getData(View v){
     dbHelper.getAddressFromDB("5");
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
