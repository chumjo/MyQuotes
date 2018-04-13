package com.ift2905.myquotes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by augus on 12/04/2018.
 */

public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("MY_QUOTES", "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        Log.d("MY_QUOTES", "notification");
        Intent itAlarm = new Intent("NOTIFICATION");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,itAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pendingIntent);

        Log.d("MY_QUOTES", "it worked?");


        return START_STICKY;
    }

    @Override
    public void onCreate(){
        Log.d("MY_QUOTES", "onCreate");
    }

    @Override
    public void onDestroy(){
        Log.d("MY_QUOTES", "onDestroy");
        super.onDestroy();
    }
}
