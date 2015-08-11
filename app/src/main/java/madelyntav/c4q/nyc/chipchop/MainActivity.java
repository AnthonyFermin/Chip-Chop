package madelyntav.c4q.nyc.chipchop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    Firebase firebaseRef;
    String userID;
    String eMail;
    String passWord;
    String userName;
    String streetAddress;
    String apartment;
    String city;
    String state;
    double zipCode;
    String URL="https://chipchop.firebaseio.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(URL);





    }
    //Ignore code below just playing with Firebase



    public void createUser() {

        firebaseRef.child("blah").setValue("I want donuts and milk");

        //firebaseRef.child("messages").setValue("I want donuts and milk");
        firebaseRef.child("blah").push().setValue("ljhn hu");

        String name = "NAME";
        String Password = "PASSWORD";
        String city = "CITY";
        String zipCode = "Zipcode";

        String name1 = "Susie";
        String pw1 = "password";
        String city1 = "New York";
        double zip = 10040;
        String name2 = "french";
        String pw2 = "plz";
        String city2 = "San Fran";
        double zip2 = 45667;

        for (int i = 0; i <= 2; i++) {
            String userNum = "user " + i;

            firebaseRef.child(userNum);
            firebaseRef.child(userNum).child(name).setValue(name1);
            firebaseRef.child(userNum).child(name).setValue(name1);
            firebaseRef.child(userNum).child(Password).setValue(pw1);
            firebaseRef.child(userNum).child(city).setValue(city1);
            firebaseRef.child(userNum).child(zipCode).setValue(zip);

        }

//    public void createUser(View view){
//        final String userName="majjjdakjjknnffggv@chipchop.com";
//        final String passWord="1234";
//
//        firebaseRef.createUser(userName,passWord , new Firebase.ValueResultHandler<Map<String, Object>>() {
//            @Override
//            public void onSuccess(Map<String, Object> stringObjectMap) {
//                Toast.makeText(MainActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
//                userID = stringObjectMap.get("uid").toString();
//                Log.i("USER ID", userID);
//                firebaseRef.child("UserName").setValue(userName);
//                firebaseRef.child("UserName").child("passWord").setValue("Tomorrow");
//
//
//
//            }
//
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                Toast.makeText(MainActivity.this,"Email already in use",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    }

    public void addImageAndDescription(){

    }

}
