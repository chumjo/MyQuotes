package com.ift2905.myquotes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.os.Build;
import android.app.NotificationChannel;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

/*
- Classe por la gestion de notifications de l'application
- Affiche une quote avec auteur dans la notification
*/

public class SendNotificationManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("MY_QUOTES","SendNotificationManager");

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            boolean notifActive = sharedPreferences.getBoolean("pref_qod_activate", false);
            boolean notifSound = sharedPreferences.getBoolean("pref_qod_sound", false);
            String prefTime = sharedPreferences.getString("pref_qod_clock", null);

            if(!notifActive)
                return;


            Log.d("MY_QUOTES", "prefTime" + prefTime);
            String[] parsedPrefTime = prefTime.split(":");
            Log.d("MY_QUOTES", "hour" + parsedPrefTime[0]);
            Log.d("MY_QUOTES", "minute" + parsedPrefTime[1]);

            Date d = new Date();
            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            String strHour = hourFormat.format(d);
            String[] parsedCurrentTime = strHour.split(":");

            String date = dateFormat.format(d);

            Log.d("MY_QUOTES", "date : " + date);

            int prefHour = Integer.parseInt(parsedPrefTime[0]);
            int prefMin = Integer.parseInt(parsedPrefTime[1]);
            int currHour = Integer.parseInt(parsedCurrentTime[0]);
            int currMin = Integer.parseInt(parsedCurrentTime[1]);

            Log.d("MY_QUOTES", "hour: "+strHour);
            //if (date.equals(yourDate) && hour.equals(yourHour)) {
            if (prefHour == currHour && prefMin == currMin) {
                Log.d("MY_QUOTES", "heure match");

                Bundle bundle = new Bundle();
                bundle.putInt("qod_key", Integer.parseInt(date));

                Intent it = new Intent(context, MainActivity.class);
                it.putExtras(bundle);
                //it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                createNotification(context, it,
                        context.getResources().getString(R.string.notif_ticker),
                        context.getResources().getString(R.string.notif_body),
                        context.getResources().getString(R.string.notif_message),
                        notifSound);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MY_QUOTES", "PASSÉ ICI");
            }
        } catch (Exception e) {
            Log.i("date", "error == " + e.getMessage());
        }
    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence description, boolean notifSound) {

        PendingIntent p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.icon_app);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.icon_app, n);

        Log.d("MY_QUOTES", "PASSÉ ICI5");
        //create a vibration
        try {

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // check state of sound option notifications
            if (notifSound){
                // default sound notification
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            Ringtone toque = RingtoneManager.getRingtone(context, uri);
            toque.play();
        } catch (Exception e) {
        }
    }


}