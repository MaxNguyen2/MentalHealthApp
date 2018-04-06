package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

//Second section of safety plan feature where users can make a list of their personal coping strategies
public class CopingStrategies extends WarningSigns {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Coping Strategies");
        setStatusBar(R.color.StatusRed);

        key = "strategiesList"; //sets key for storing list of strategies
        setArrayAdapter(); //fills list with previously saved list of strategies
    }

    @Override
    public void setListListener(){ //when list item is clicked, opens dialog that allows user to delete entry
        signsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CopingStrategies.this);
                builder.setTitle("Delete Coping Strategy")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeIndex = position; //removes list item at that position
                                view.startAnimation(animation); //animates deletion
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //does nothing besides closing the dialog
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void setInfoDialog() { //opens information dialog when icon is clicked
        ImageView info = (ImageView) findViewById(R.id.infoIcon);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CopingStrategies.this);
                builder.setTitle("Tips")
                        .setMessage("Enter in your personal coping strategies to use during a crisis. This can include relaxation techniques, a distracting physical activity, or a safe social setting to go to.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }
}
