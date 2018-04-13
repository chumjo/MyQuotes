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

    boolean notifSound;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("MY_QUOTES","SendNotificationManager");

            Date d = new Date();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            boolean notifActive = sharedPreferences.getBoolean("pref_qod_activate", false);
            notifSound = sharedPreferences.getBoolean("pref_qod_sound", false);
            String prefTime = sharedPreferences.getString("pref_qod_clock", null);

            if(!notifActive)
                return;

            Log.d("MY_QUOTES", "prefTime" + prefTime);
            String[] parsedPrefTime = prefTime.split(":");
            Log.d("MY_QUOTES", "hour" + parsedPrefTime[0]);
            Log.d("MY_QUOTES", "minute" + parsedPrefTime[1]);

            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            String strHour = hourFormat.format(d);
            String[] parsedCurrentTime = strHour.split(":");

            int prefHour = Integer.parseInt(parsedPrefTime[0]);
            int prefMin = Integer.parseInt(parsedPrefTime[1]);
            int currHour = Integer.parseInt(parsedCurrentTime[0]);
            int currMin = Integer.parseInt(parsedCurrentTime[1]);

            Log.d("MY_QUOTES", "hour: "+strHour);
            //if (date.equals(yourDate) && hour.equals(yourHour)) {
            if (/*prefHour == currHour && prefMin == currMin*/true) {
                Log.d("MY_QUOTES", "heure match");
                Intent it = new Intent(context, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("MY_QUOTES", "PASSÉ ICI");
                createNotification(context, it, "new mensage", "body!", "this is a mensage");
            }
        } catch (Exception e) {
            Log.i("date", "error == " + e.getMessage());
        }
    }

    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence description) {

        Log.d("MY_QUOTES", "PASSÉ ICI2");

        // Create the quote to send and put it in a bundle
        //Quote quote_notification = RandomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(context));
        Quote quote_notification = new Quote("Blablabla","unknown author", Category.art, "bla");

        Log.d("MY_QUOTES", "PASSÉ ICI3");

        String [] qod = new String [4];
        qod [0] = quote_notification.getQuote();
        qod [1] = quote_notification.getAuthor();
        qod [2] = quote_notification.getCategory().toString();
        qod [3] = quote_notification.getId();

        Bundle bundleQod = new Bundle();
        bundleQod.putStringArray("qod_key", qod);

        intent.putExtras(bundleQod);

        Log.d("MY_QUOTES", "PASSÉ ICI4");

        NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }


}