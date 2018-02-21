package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//page for users to make a report on their mood
public class MoodReporting extends MoodMonitoring {
    TextView dateText;
    TextView timeText;
    Calendar myCalendar;
    SeekBar happySlider, energySlider, irritatedSlider, anxiousSlider;
    EditText eventNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_reporting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets up navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();

        //sets status bar color to be dark orange
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.StatusOrange));

        setupTimeDate();

        happySlider = (SeekBar) findViewById(R.id.happySlider);
        energySlider = (SeekBar) findViewById(R.id.energySlider);
        irritatedSlider = (SeekBar) findViewById(R.id.irritatedSlider);
        anxiousSlider = (SeekBar) findViewById(R.id.anxiousSlider);
        eventNotes = (EditText) findViewById(R.id.eventNotes);

        ImageView gear = (ImageView) findViewById(R.id.imageView);
        gear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MoodReporting.this, SettingsActivity.class);
                MoodReporting.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             submitReport();
                Intent myIntent = new Intent(MoodReporting.this, MoodMonitoring.class);
                MoodReporting.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        SharedPreferences mPrefs = getSharedPreferences("key",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        if (mPrefs.getBoolean("alarm",false)== false){
            setAlarm();
            prefsEditor.putBoolean("alarm",true);
        }
        prefsEditor.commit();


    }

    //used to update date displayed in button
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    public void setupTimeDate () {
        myCalendar = Calendar.getInstance(); //gets current time and date information
        dateText = (Button) findViewById(R.id.dateButton); //button that displays date
        updateLabel(); //displays current date before a different date is selected

        //when date is selected in dialog, new date is displayed
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        //opens date picker dialog when button is clicked
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MoodReporting.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeText = (Button) findViewById(R.id.timeButton); //button that displays the time
        timeText.setText(new SimpleDateFormat("h:mm a", Locale.US).format(myCalendar.getTime())); //displays current time

        //when button is clicked, opens time picker dialog which updates button text with user selected time
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance(); //gets current time
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialogFixedNougatSpinner(MoodReporting.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        timeText.setText(String.format("%d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM")); //sets text to be selected time
                    }

                }, hour, minute, false);
                mTimePicker.setTitle("Select Time"); //title of time picker dialog
                mTimePicker.show();

            }
        });

    }

    public void submitReport() {
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        int happyRate = happySlider.getProgress();
        int energyRate = energySlider.getProgress();
        int irritatedRate = irritatedSlider.getProgress();
        int anxiousRate = anxiousSlider.getProgress();
        String notes = eventNotes.getText().toString();

        ArrayList<MoodReport> moodArray;
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("key",MODE_PRIVATE);
        String jsonRetrieve = mPrefs.getString("moodArray",null);
        if (jsonRetrieve == null)
            moodArray = new ArrayList<>();
        else
            moodArray = gson.fromJson(jsonRetrieve, new TypeToken<ArrayList<MoodReport>>(){}.getType());
        MoodReport obj = new MoodReport(date, time, happyRate, energyRate, irritatedRate, anxiousRate, notes);

        moodArray.add(obj);
        String json = gson.toJson(moodArray);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("moodArray",json);
        prefsEditor.commit();
    }

    public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 52);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MoodReporting.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MoodReporting.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MoodReporting.this.getSystemService(MoodReporting.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    /*
    public void testReport() {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("moodArray", "");
        ArrayList<MoodReport> obj = gson.fromJson(json, new TypeToken<ArrayList<MoodReport>>(){}.getType());
        Log.d("TESTING", obj.get(0).getNotes());
    }
    */
}
