package com.sgp.spendsync.Database;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.window.SplashScreen;

import androidx.core.app.NotificationCompat;

import com.sgp.spendsync.Activity.SplashActivity;
import com.sgp.spendsync.R;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            repeatingIntent = new Intent(context, SplashScreen.class);
        }
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,repeatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.finalpng)
                .setContentTitle("Budget Tracker")
                .setContentText("Please Add your today's incomes/expense here..!!!")
                .setAutoCancel(true);
         notificationManager.notify(100,builder.build());
    }
}
