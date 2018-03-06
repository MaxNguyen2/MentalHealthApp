package com.example.max.mentalhealthapp;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("key",Context.MODE_PRIVATE); /*
        int repeat = prefs.getInt("repeatAlarm",0);
        repeat++;
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt("repeatAlarm",repeat);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        long when = System.currentTimeMillis();
        int hour = prefs.getInt("hourAlarm", 10);
        if (hour == 18)
            hour = 8;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        hour = hour + 2;
        prefsEdit.putInt("hourAlarm",hour).commit();
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, (repeat * when), pendingIntent); */

        SharedPreferences.Editor prefsEdit = prefs.edit();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        long when = System.currentTimeMillis();
        int hour = prefs.getInt("hourAlarm", 6);
        hour = hour + 2;
        if (hour == 20)
            hour = 8;
        if (hour == 14)
            hour = 16;
        prefsEdit.putInt("hourAlarm",hour);
        prefsEdit.commit();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY + when, pendingIntent);


        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MoodReporting.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendIntent = PendingIntent.getActivity(context, hour,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("Reminder")
                .setContentText("Report how your mood has been.").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(hour, mNotifyBuilder.build());

        Log.d("HIIII","ALARM POPPING OFF");

    }

}