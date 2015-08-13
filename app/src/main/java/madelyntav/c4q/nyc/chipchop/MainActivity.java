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
import java.util.ArrayList;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Item;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;

public class MainActivity extends AppCompatActivity {

    Firebase firebaseRef;
    String userID="";
    private static DBHelper dbHelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        if(dbHelper == null) {
            dbHelper = new DBHelper(getApplicationContext());
        }

    }


    public void createUserLets(View view){
        Item item=new Item("5","Beans","5","yellow","The fin");
        item.setItemID("hgg");
        Item item1= new Item("5","Rice","2","Black","IGHGH");
        item1.setItemID("gff");
        Item item2= new Item("5","Chicken","6","Brown","jfjfd");
        item2.setItemID("Iggj");

        ArrayList<Item> items=new ArrayList<>();
        items.add(item);
        items.add(item1);
        items.add(item2);
        Order order=new Order(item.getUserID(),items);

        dbHelper.addOrderToDB(order);
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
