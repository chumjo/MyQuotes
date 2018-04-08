package com.ift2905.myquotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.os.Build;
import android.app.NotificationChannel;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

/*
- Classe por la gestion de notifications de l'application
- Affiche une quote avec auteur dans la notification
*/

public class AlarmeReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {

        Quote quote_notification = RandomQuoteInitialList.getRandomQuoteFromIntialList(MainActivity.preferences);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("My Quotes notification")
                .setContentText(quote_notification.getQuote() + "\n" + quote_notification.getAuthor())
                .setTicker("new message from My Quotes")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
