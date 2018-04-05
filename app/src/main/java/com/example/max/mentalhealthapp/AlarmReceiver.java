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

//called when alarm goes off
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("key",Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEdit = prefs.edit();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        long when = System.currentTimeMillis(); //gets current time
        int hour = prefs.getInt("hourAlarm", 6);
        hour = hour + 2;

        if (hour == 20) //to get hour to restart at 8 again
            hour = 8;
        if (hour == 14) //to skip hour 14
            hour = 16;
        prefsEdit.putInt("hourAlarm",hour).commit();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY + when, pendingIntent); //sets alarm to go off the next day

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MoodReporting.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendIntent = PendingIntent.getActivity(context, hour,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder( //sends alarm
                context).setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Reminder")
                .setContentText("Report how your mood has been.").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(hour, mNotifyBuilder.build());

        Log.d("NOTIFICATION","ALARM POPPING OFF");

    }

}