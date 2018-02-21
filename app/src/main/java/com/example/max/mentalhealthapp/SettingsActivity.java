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
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.TimePicker;

//Settings page for the app
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
            final SharedPreferences.Editor prefsEdit = prefs.edit();

            password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { //when switch preference changes state
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if (prefs.getBoolean("pin", false) == true) //opens DisablePassword if feature is on
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp", "com.example.max.mentalhealthapp.DisablePassword")));
                    else //opens SetPassword if feature is off
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp", "com.example.max.mentalhealthapp.SetPassword")));
                    return true;
                }
            });


            final SwitchPreference notification = (SwitchPreference) findPreference("notificationSwitch");
            notification.setSummary(prefs.getString("summary", (String) notification.getSummary()));
            Log.d("READ",prefs.getString("summary",(String) notification.getSummary()));
            notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { //when switch preference changes state
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if (notification.isChecked() == false) {// when no notification is set
                        Calendar mcurrentTime = Calendar.getInstance(); //gets current time
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker = new TimePickerDialogFixedNougatSpinner(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                boolean isPM = (selectedHour >= 12);
                                String text = "Choose to have notifications that remind you to report your mood at a specified time daily.\nNOTIFICATION TIME: " + String.format("%d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM");
                                notification.setSummary(text); //sets text to be selected time
                                setAlarm(selectedHour, selectedMinute);
                                prefsEdit.putString("summary",text);
                                prefsEdit.commit();
                            }

                        }, hour, minute, false);
                        mTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                if (notification.getSummary().equals("Choose to have notifications that remind you to report your mood at a specified time daily."))
                                    notification.setChecked(false);
                            }
                        });
                        mTimePicker.setTitle("Select Time"); //title of time picker dialog
                        mTimePicker.show();

                    } else {
                        notification.setSummary("Choose to have notifications that remind you to report your mood at a specified time daily.");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(getContext(), AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                        am.cancel(pendingIntent);
                    }

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

        public void setAlarm(int hour, int min) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Intent intent1 = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }


    }
}

