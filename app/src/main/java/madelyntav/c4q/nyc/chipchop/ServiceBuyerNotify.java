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
import com.firebase.client.ValueEventListener;

import madelyntav.c4q.nyc.chipchop.DBObjects.Seller;

/**
 * Created by c4q-anthonyf on 9/11/15.
 */
public class ServiceBuyerNotify extends Service {

    public static String TAG = "BUYER NOTIFICATION SERVICE";
    public static String SELLER_ID = "sellerid";
    public static String ORDER_ID = "orderid";
    public static String BUYER_ID = "buyerid";


    private static final String URL = "https://chipchop.firebaseio.com/";
    private String orderID;
    private String buyerID;
    private String sellerID;

    private Firebase buyerOrderRef;
    private Firebase sellerRef;
    private ChildEventListener sellerCEL;
    private ValueEventListener orderVEL;

    private NotificationManager notificationManager;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orderID = intent.getStringExtra(ORDER_ID);
        buyerID = intent.getStringExtra(BUYER_ID);
        sellerID = intent.getStringExtra(SELLER_ID);
        Firebase.setAndroidContext(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mainTask();

        return START_REDELIVER_INTENT;
    }

    private void mainTask() {
        buyerOrderRef = new Firebase(URL + "UserProfiles/" + buyerID + "/Orders/" + orderID + "/isActive");
        orderVEL = buyerOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue(Boolean.class)) {
                    //send Notification
                    Intent intent = new Intent(ServiceBuyerNotify.this, BuyActivity.class)
                            .putExtra("fromService", true)
                            .putExtra("orderid", orderID);

                    PendingIntent pendingIntent = PendingIntent.getActivity(ServiceBuyerNotify.this,
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
                    notificationManager.notify(123, notification);
                    Log.d(TAG, "ORDER COMPLETE");
                    stopSelf();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                buyerOrderRef.removeEventListener(orderVEL);
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
