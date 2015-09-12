package madelyntav.c4q.nyc.chipchop;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.plus.internal.model.people.PersonEntity;

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;

/**
 * Created by c4q-anthonyf on 9/11/15.
 */
public class ServiceSellerNotify extends Service {

    public static String TAG = "NOTIFICATION SERVICE";
    private static final String URL = "https://chipchop.firebaseio.com/";
    private String sellerID;
    private Seller seller;
    private Order order;

    private DBHelper dbHelper;
    private Firebase sellerOrdersRef;
    private Firebase sellerRef;
    private ChildEventListener orderCEL, sellerCEL;

    private NotificationManager notificationManager;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"SERVICE STARTED");
        sellerID = intent.getStringExtra("sellerid");
        dbHelper = DBHelper.getDbHelper(this.getApplicationContext());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mainTask();
        return START_REDELIVER_INTENT;
    }

    private void mainTask(){
        sellerOrdersRef = new Firebase(URL + "ActiveSellers/" + sellerID + "/Orders/");
        orderCEL = sellerOrdersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                order = dataSnapshot.getValue(Order.class);
                //send Notification
                Intent intent = new Intent(ServiceSellerNotify.this, SellActivity.class)
                        .putExtra("fromService",true)
                        .putExtra("orderid",order.getOrderID());

                PendingIntent pendingIntent = PendingIntent.getActivity(ServiceSellerNotify.this,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.chipchop_small)
                                .setContentTitle("ChipChop")
                                .setContentText("New Order Received")
                                .setContentIntent(pendingIntent);

                mBuilder.setAutoCancel(true);
                notification = mBuilder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(123,notification);
                Log.d(TAG,"ORDER RECEIVED");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                sellerRef.removeEventListener(sellerCEL);
                sellerOrdersRef.removeEventListener(orderCEL);
                stopSelf();
            }
        });

        sellerRef = new Firebase(URL + "ActiveSellers/");
        sellerCEL = sellerRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                seller = dataSnapshot.getValue(Seller.class);
                if(seller.getUID().equals(sellerID)){
                    sellerRef.removeEventListener(sellerCEL);
                    sellerOrdersRef.removeEventListener(orderCEL);
                    stopSelf();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
