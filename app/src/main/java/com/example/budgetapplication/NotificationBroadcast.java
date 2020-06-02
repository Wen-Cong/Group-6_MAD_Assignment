package com.example.budgetapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //create notification for daily remindder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "dailyReminders")
                .setSmallIcon(R.drawable.ic_walletnotification)
                .setContentTitle("Daily Reminder")
                .setContentText("Remember to key in your daily transactions!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
