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
            String yourDate = "12/04/2018";
            String yourHour = "17:46";
            Date d = new Date();

            String prefTime = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("pref_qod_clock", null);

            Log.d("MY_QUOTES", "prefTime" + prefTime);

            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat hour = new SimpleDateFormat("HH:mm");
            String sdate = date.format(d);
            String shour = hour.format(d);

            Log.d("MY_QUOTES", "date: "+sdate);
            Log.d("MY_QUOTES", "hour: "+shour);
            //if (date.equals(yourDate) && hour.equals(yourHour)) {
            if (sdate.equals(yourDate) && shour.equals(yourHour)) {
                Log.d("MY_QUOTES", "heure match");
                Intent it = new Intent(context, MainActivity.class);
                createNotification(context, it, "new mensage", "body!", "this is a mensage");
            }
        } catch (Exception e) {
            Log.i("date", "error == " + e.getMessage());
        }
    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.icon_app);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.icon_app, n);
        //create a vibration
        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }
}