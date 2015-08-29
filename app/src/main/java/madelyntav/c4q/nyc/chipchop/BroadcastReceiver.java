package madelyntav.c4q.nyc.chipchop;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import madelyntav.c4q.nyc.chipchop.fragments.Fragment_Seller_OrderDetails;

/**
 * Created by alvin2 on 8/29/15.
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {
    public BroadcastReceiver() {
    }

    private static final String TAG = BroadcastReceiver.class.toString();

    @Override
    public void onReceive(Context context, Intent intent) {
        // When logging, TAG is useful for searching through the logs by filtering by TAG
        Log.i(TAG, "I received an intent with action:" + intent.getAction());

        // NOTIFICATION CODE
        Fragment_Seller_OrderDetails.notificationManager.notify(1234, Fragment_Seller_OrderDetails.notification);


    }
}
