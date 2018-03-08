package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class ReportDialog extends DialogFragment {

    //so that host activity can do something when dialog buttons are clicked
    interface ReportDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    //use this instance of the interface to deliver action events
    ReportDialogListener mListener;

    //override the Fragment.onAttach() method to instantiate the ReportDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //verify that the host activity implements the callback interface
        try {
            //instantiate the ReportDialogListener so we can send events to the host
            mListener = (ReportDialogListener) context;
        } catch (ClassCastException e) {
            //the activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ReportDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final SharedPreferences prefs = getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
        final int position = prefs.getInt("listPosition",0);
        final ArrayList<MoodReport> moodArray = new Gson().fromJson(prefs.getString("moodArray", ""), new TypeToken<ArrayList<MoodReport>>() {
        }.getType());

            MoodReport obj = moodArray.get(moodArray.size() - position - 1); //gets mood report object that corresponds to item clicked by user

            View v = inflater.inflate(R.layout.mood_dialog, null);
            TextView happy = (TextView) v.findViewById(R.id.happyRating);
            TextView sad = (TextView) v.findViewById(R.id.sadRating);
            TextView energized = (TextView) v.findViewById(R.id.energizedRating);
            TextView irritated = (TextView) v.findViewById(R.id.irritatedRating);
            TextView anxious = (TextView) v.findViewById(R.id.anxiousRating);
            TextView dateTime = (TextView) v.findViewById(R.id.dateTimeText);
            TextView notesText = (TextView) v.findViewById(R.id.notesText);
            TextView notesLabel = (TextView) v.findViewById(R.id.notesLabel);

            //fill dialog with information from mood report
            happy.setText(String.valueOf(obj.getHappy()));
            sad.setText(String.valueOf(obj.getSad()));
            energized.setText(String.valueOf(obj.getEnergy()));
            irritated.setText(String.valueOf(obj.getIrritated()));
            anxious.setText(String.valueOf(obj.getAnxious()));
            dateTime.setText(obj.getDate().substring(0, obj.getDate().length() - 3) + " | " + obj.getTime());
            if (!obj.getNotes().equals("")) {
                notesText.setText(obj.getNotes());
            } else { //does not show text boxes if there are no notes in mood report
                notesLabel.setText("");
                notesLabel.setHeight(0);
                notesText.setHeight(0);
            }

        builder.setView(v);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Delete Report", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moodArray.remove(moodArray.size()-position-1); //deletes mood report
                        Collections.sort(moodArray);
                        prefs.edit().putString("moodArray",new Gson().toJson(moodArray)).commit();
                        mListener.onDialogNegativeClick(ReportDialog.this);
                    }
                });

        //create the AlertDialog object and return it
        return builder.create();
    }
}