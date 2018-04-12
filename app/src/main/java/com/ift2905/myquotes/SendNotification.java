package com.ift2905.myquotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.os.Build;
import android.app.NotificationChannel;
import android.util.Log;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

/*
- Classe por la gestion de notifications de l'application
- Affiche une quote avec auteur dans la notification
*/

public class SendNotification extends BroadcastReceiver {

    Uri uri;
    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";


    @Override
    public void onReceive(Context context, Intent intent) {

        Quote quote_notification = RandomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(context));

        String [] qod = new String [4];
        qod [0] = quote_notification.getQuote();
        qod [1] = quote_notification.getAuthor();
        qod [2] = quote_notification.getCategory().toString();
        qod [3] = quote_notification.getId();

        Bundle bundleQod = new Bundle();
        bundleQod.putStringArray("qod_key", qod);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtras(bundleQod);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // check state of sound option notifications
        if (sharedPreferences.getBoolean("pref_qod_sound",false)){
            // default sound notification
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            uri = null;
        }

        Notification notification = builder.setContentTitle("My Quotes notification")
                .setContentText(quote_notification.getQuote() + "\n" + quote_notification.getAuthor())
                .setTicker("new message from My Quotes")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_action_name)
                .setSound(uri)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification MyQuotes",
                    IMPORTANCE_DEFAULT
            );

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0,notification);
        }

    }
}
