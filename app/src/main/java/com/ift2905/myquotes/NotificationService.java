package com.ift2905.myquotes;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Service to allow sending a Quote of the Day notification in the background
 * Code strongly inspired by: https://stackoverflow.com/questions/39674850/send-a-notification-when-the-app-is-closed
 */

public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * Set a Alarm to run in the background and send a notification at
     * the time chosen by the user
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        super.onStartCommand(intent, flags, startId);

        Intent itAlarm = new Intent("NOTIFICATION");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,itAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pendingIntent);

        return START_STICKY;
    }

    @Override
    public void onCreate(){
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
