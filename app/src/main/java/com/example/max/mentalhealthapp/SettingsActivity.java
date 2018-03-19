package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.TimePicker;

//settings page for the app
public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            final SwitchPreference password = (SwitchPreference) findPreference("passwordProtection");
            final SharedPreferences prefs = this.getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);

            password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { //when switch preference changes state
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if (prefs.getBoolean("pin", false)) //opens DisablePassword if feature is on
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp", "com.example.max.mentalhealthapp.DisablePassword")));
                    else //opens SetPassword if feature is off
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp", "com.example.max.mentalhealthapp.SetPassword")));
                    return true;
                }
            });

            final SwitchPreference notification = (SwitchPreference) findPreference("notificationSwitch");
            notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { //when switch preference changes state
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if (!notification.isChecked()) {// when no notification is set
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, 8);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 8, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                        long start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.HOUR_OF_DAY, 10);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.HOUR_OF_DAY, 12);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 12, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.HOUR_OF_DAY, 16);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 16, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.HOUR_OF_DAY, 18);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 18, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        //code does not work
                        /*
                        for (int hour = 8; hour < 20; hour = hour + 2) {
                            if (hour == 14)
                                hour = 16;
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            Intent intent = new Intent(getContext(), AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), hour, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            long start = calendar.getTimeInMillis();
                            if (calendar.before(Calendar.getInstance()))
                                start = start + AlarmManager.INTERVAL_DAY;
                            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);
                        }
/*
                        calendar.set(Calendar.MINUTE, 56);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.MINUTE, 57);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 12, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.MINUTE, 58);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 16, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent);

                        calendar.set(Calendar.MINUTE, 59);
                        pendingIntent = PendingIntent.getBroadcast(getContext(), 18, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        start = calendar.getTimeInMillis();
                        if (calendar.before(Calendar.getInstance()))
                            start = start + AlarmManager.INTERVAL_DAY;
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, start, pendingIntent); */


                    }
                    else { //disables alarms
                        for (int hour = 8; hour < 20; hour = hour + 2) {
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), hour, new Intent(getContext(), AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                        am.cancel(pendingIntent);
                    }}

                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            SwitchPreference password = (SwitchPreference) findPreference("passwordProtection");
            boolean switched = password.isChecked();
            SharedPreferences prefs = this.getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            //accounts for cases when user backs out of activity without setting or disabling password
            if (prefs.getString("password", "").equals("") && switched)
                password.setChecked(false);
            else if (!prefs.getString("password", "").equals("") && !switched)
                password.setChecked(true);

            //stores value of switch 
            editor.putBoolean("pin", password.isChecked());
            editor.commit();
            super.onResume();
        }
    }
}


