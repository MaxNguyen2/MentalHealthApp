package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MoodReportAdapter extends ArrayAdapter<MoodReport> {

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