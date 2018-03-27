package com.example.max.mentalhealthapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


//page for users to make a report on their mood
public class MoodReporting extends SetupClass {
    TextView dateText;
    TextView timeText;
    Calendar myCalendar;
    DiscreteSeekBar happySlider, energySlider, irritatedSlider, anxiousSlider, sadSlider;
    EditText eventNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mood_reporting);
        super.onCreate(savedInstanceState);

        setupStatusBar(R.color.StatusOrange);
        setupTimeDate();

        happySlider = (DiscreteSeekBar) findViewById(R.id.happySlider);
        energySlider = (DiscreteSeekBar) findViewById(R.id.energySlider);
        irritatedSlider = (DiscreteSeekBar) findViewById(R.id.irritatedSlider);
        anxiousSlider = (DiscreteSeekBar) findViewById(R.id.anxiousSlider);
        eventNotes = (MaterialEditText) findViewById(R.id.eventNotes);
        sadSlider = (DiscreteSeekBar)  findViewById(R.id.sadSlider);

        ImageView gear = (ImageView) findViewById(R.id.imageView);
        gear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MoodReporting.this, SettingsActivity.class);
                MoodReporting.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.getBackground().setColorFilter(Color.parseColor("#ffa726"), PorterDuff.Mode.MULTIPLY);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             submitReport();
                Intent myIntent = new Intent(MoodReporting.this, MoodMonitoring.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MoodReporting.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

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
                        timeText.setText(String.format(Locale.US,"%d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM")); //sets text to be selected time
                        myCalendar.set(Calendar.HOUR,selectedHour);
                        myCalendar.set(Calendar.MINUTE,selectedMinute);
                    }

                }, hour, minute, false);
                mTimePicker.setTitle("Select Time"); //title of time picker dialog
                mTimePicker.show();

            }
        });

    }

    //stores information selected by user into an object stored into an array
    public void submitReport() {
        //gets value of widgets
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        int happyRate = happySlider.getProgress();
        int energyRate = energySlider.getProgress();
        int irritatedRate = irritatedSlider.getProgress();
        int anxiousRate = anxiousSlider.getProgress();
        int sadRate = sadSlider.getProgress();
        String notes = eventNotes.getText().toString();
        Date dateObj = myCalendar.getTime();

        ArrayList<MoodReport> moodArray;
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("key",MODE_PRIVATE);
        String jsonRetrieve = mPrefs.getString("moodArray",null);
        if (jsonRetrieve == null)
            moodArray = new ArrayList<>();
        else
            moodArray = gson.fromJson(jsonRetrieve, new TypeToken<ArrayList<MoodReport>>(){}.getType()); //gets array of mood reports

        MoodReport obj = new MoodReport(date, time, happyRate, energyRate, irritatedRate, anxiousRate, sadRate, notes, dateObj); //creates object with data

        moodArray.add(obj); //adds mood report to existing array
        Collections.sort(moodArray);
        String json = gson.toJson(moodArray);
        mPrefs.edit().putString("moodArray",json).commit(); //stores array
    }
    /*
    public void testReport() {
        SharedPreferences mPrefs = getSharedPreferences("key",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("moodArray", "");
        ArrayList<MoodReport> obj = gson.fromJson(json, new TypeToken<ArrayList<MoodReport>>(){}.getType());
        Log.d("TESTING",String.valueOf(obj.size()));
        Log.d("TESTING", String.valueOf(obj.get(obj.size()-1).getHappy()));
    }
    */
}
class MoodReportAdapter extends ArrayAdapter<MoodReport> {

    private ArrayList<MoodReport> mArray;

    //view lookup cache
    private static class ViewHolder {
        TextView dateTime;
        TextView moodRating;
        ImageView notesIcon;
    }

    public MoodReportAdapter(Context context, ArrayList<MoodReport> reports) {
        super(context, R.layout.item_report, reports);
        Collections.reverse(reports); //reverses order of array so that top of list can be the latest mood reports
        mArray = reports;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (mArray.size() != 0) { //only executes if array of mood reports is not empty
            //get the data item for this position
            MoodReport obj = mArray.get(position);

            //check if an existing view is being reused
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_report, parent, false);
                viewHolder.dateTime = (TextView) convertView.findViewById(R.id.dateTime);
                viewHolder.moodRating = (TextView) convertView.findViewById(R.id.moodRating);
                viewHolder.notesIcon = (ImageView) convertView.findViewById(R.id.notesIcon);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            }
            else {
                //view is being recycled, retrieve the viewHolder object from tag
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //populate the data into the template view using the data object
            viewHolder.dateTime.setText(obj.getDate().substring(0,obj.getDate().length()-3) + " | " + obj.getTime());
            SharedPreferences prefs = getContext().getSharedPreferences("key", Context.MODE_PRIVATE);
            switch (prefs.getString("spinnerText","Happy")) {
                case "Happy":
                    viewHolder.moodRating.setText("Rating: " + obj.getHappy());
                    break;
                case "Sad":
                    viewHolder.moodRating.setText("Rating: " + obj.getSad());
                    break;
                case "Energized":
                    viewHolder.moodRating.setText("Rating: " + obj.getEnergy());
                    break;
                case "Irritated":
                    viewHolder.moodRating.setText("Rating: " + obj.getIrritated());
                    break;
                case "Anxious":
                    viewHolder.moodRating.setText("Rating: " + obj.getAnxious());
                    break;
            }

            if (!obj.getNotes().equals(""))
                viewHolder.notesIcon.setImageResource(R.drawable.notes_icon);
            //return the completed view to render on screen
            return convertView;
        }
        else {
            return(LayoutInflater.from(getContext()).inflate(R.layout.empty, parent, false)); //returns empty list
        }
    }}

class TimePickerDialogFixedNougatSpinner extends TimePickerDialog {

    public TimePickerDialogFixedNougatSpinner(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {

        super(context, listener, hourOfDay, minute, is24HourView);
        fixSpinner(context, hourOfDay, minute, is24HourView);
    }

    public TimePickerDialogFixedNougatSpinner(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {

        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        fixSpinner(context, hourOfDay, minute, is24HourView);
    }

    private void fixSpinner(Context context, int hourOfDay, int minute, boolean is24HourView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // android:timePickerMode spinner and clock began in Lollipop
            try {
                // Get the theme's android:timePickerMode
                final int MODE_SPINNER = 2;
                Class<?> styleableClass = Class.forName("com.android.internal.R$styleable");
                Field timePickerStyleableField = styleableClass.getField("TimePicker");
                int[] timePickerStyleable = (int[]) timePickerStyleableField.get(null);
                final TypedArray a = context.obtainStyledAttributes(null, timePickerStyleable, android.R.attr.timePickerStyle, 0);
                Field timePickerModeStyleableField = styleableClass.getField("TimePicker_timePickerMode");
                int timePickerModeStyleable = timePickerModeStyleableField.getInt(null);
                final int mode = a.getInt(timePickerModeStyleable, MODE_SPINNER);
                a.recycle();
                if (mode == MODE_SPINNER) {
                    TimePicker timePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
                    Class<?> delegateClass = Class.forName("android.widget.TimePicker$TimePickerDelegate");
                    Field delegateField = findField(TimePicker.class, delegateClass, "mDelegate");
                    Object delegate = delegateField.get(timePicker);
                    Class<?> spinnerDelegateClass;
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                        spinnerDelegateClass = Class.forName("android.widget.TimePickerSpinnerDelegate");
                    } else {
                        // TimePickerSpinnerDelegate was initially misnamed TimePickerClockDelegate in API 21!
                        spinnerDelegateClass = Class.forName("android.widget.TimePickerClockDelegate");
                    }
                    // In 7.0 Nougat for some reason the timePickerMode is ignored and the delegate is TimePickerClockDelegate
                    if (delegate.getClass() != spinnerDelegateClass) {
                        delegateField.set(timePicker, null); // throw out the TimePickerClockDelegate!
                        timePicker.removeAllViews(); // remove the TimePickerClockDelegate views
                        Constructor spinnerDelegateConstructor = spinnerDelegateClass.getConstructor(TimePicker.class, Context.class, AttributeSet.class, int.class, int.class);
                        spinnerDelegateConstructor.setAccessible(true);
                        // Instantiate a TimePickerSpinnerDelegate
                        delegate = spinnerDelegateConstructor.newInstance(timePicker, context, null, android.R.attr.timePickerStyle, 0);
                        delegateField.set(timePicker, delegate); // set the TimePicker.mDelegate to the spinner delegate
                        // Set up the TimePicker again, with the TimePickerSpinnerDelegate
                        timePicker.setIs24HourView(is24HourView);
                        timePicker.setHour(hourOfDay);
                        timePicker.setMinute(minute);
                        timePicker.setOnTimeChangedListener(this);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Field findField(Class objectClass, Class fieldClass, String expectedName) {
        try {
            Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {} // ignore
        // search for it if it wasn't found under the expected ivar name
        for (Field searchField : objectClass.getDeclaredFields()) {
            if (searchField.getType() == fieldClass) {
                searchField.setAccessible(true);
                return searchField;
            }
        }
        return null;
    }
}
