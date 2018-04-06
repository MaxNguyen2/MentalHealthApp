package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

//Supposed to set alarms when the device reboots
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Calendar calendar = Calendar.getInstance();
            for (int hour = 8; hour < 20; hour = hour + 2) {
                if (hour == 14)
                    hour = hour + 2;
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                long start = calendar.getTimeInMillis();
                if (calendar.before(Calendar.getInstance()))
                    start = start + AlarmManager.INTERVAL_DAY;
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);
            }
        }
    }
}
