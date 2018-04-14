package com.ift2905.myquotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages notifications sending
 * Base code from somewhere in StackOverflow...
*/

public class SendNotificationManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            boolean notifActive = sharedPreferences.getBoolean("pref_qod_activate", false);
            boolean notifSound = sharedPreferences.getBoolean("pref_qod_sound", false);
            String prefTime = sharedPreferences.getString("pref_qod_clock", null);

            // Check if user's notification preference is disabled
            if(!notifActive)
                return;

            String[] parsedPrefTime = prefTime.split(":");

            Date d = new Date();
            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            String strHour = hourFormat.format(d);

            String[] parsedCurrentTime = strHour.split(":");

            String date = dateFormat.format(d);

            int prefHour = Integer.parseInt(parsedPrefTime[0]);
            int prefMin = Integer.parseInt(parsedPrefTime[1]);
            int currHour = Integer.parseInt(parsedCurrentTime[0]);
            int currMin = Integer.parseInt(parsedCurrentTime[1]);

            // If current time matches user's set time send notification
            if (prefHour == currHour && prefMin == currMin) {

                Bundle bundle = new Bundle();
                bundle.putInt("qod_key", Integer.parseInt(date));

                Intent it = new Intent(context, MainActivity.class);
                it.putExtras(bundle);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                createNotification(context, it,
                        context.getResources().getString(R.string.notif_ticker),
                        context.getResources().getString(R.string.notif_body),
                        context.getResources().getString(R.string.notif_message),
                        notifSound);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the notification to be sent
     * @param context
     * @param intent
     * @param ticker
     * @param title
     * @param description
     * @param notifSound
     */
    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence description, boolean notifSound) {

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.icon_app_alpha);
        builder.setContentIntent(p);
        Notification n = builder.build();

        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.icon_app, n);

        //create a vibration
        try {
            // check state of sound option notifications
            if (notifSound) {
                // default sound notification
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                Ringtone toque = RingtoneManager.getRingtone(context, uri);
                toque.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}