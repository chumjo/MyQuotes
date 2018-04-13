package com.ift2905.myquotes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by augus on 12/04/2018.
 */

public class NotificationService extends Service {

    boolean notification;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("MY_QUOTES", "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        if(notification){
            Log.d("MY_QUOTES", "notification");
            Intent itAlarm = new Intent("NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,itAlarm,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3);
            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pendingIntent);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate(){
        Log.e("MY_QUOTES", "onCreate");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notification = sharedPreferences.getBoolean("pref_qod_activate",false);
    }

    @Override
    public void onDestroy(){
        Log.e("MY_QUOTES", "onDestroy");
        super.onDestroy();
    }
}
