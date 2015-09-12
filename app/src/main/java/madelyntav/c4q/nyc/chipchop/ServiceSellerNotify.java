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

import madelyntav.c4q.nyc.chipchop.DBObjects.DBHelper;
import madelyntav.c4q.nyc.chipchop.DBObjects.Order;
import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;

/**
 * Created by c4q-anthonyf on 9/11/15.
 */
public class ServiceSellerNotify extends Service {

    public static String TAG = "NOTIFICATION SERVICE";
    public static String SELLER_ID = "sellerid";

    private static final String URL = "https://chipchop.firebaseio.com/";
    private String sellerID;
    private Order order;

    private Firebase sellerOrdersRef;
    private ChildEventListener orderCEL;

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
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Firebase.setAndroidContext(this);
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
                notification.defaults |= Notification.DEFAULT_SOUND;
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
                sellerOrdersRef.removeEventListener(orderCEL);
                stopSelf();
            }
        });

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"DESTROYED");
        super.onDestroy();
    }
}
