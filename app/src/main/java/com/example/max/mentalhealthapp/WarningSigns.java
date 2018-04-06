package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

//First section of the safety plan feature where users create a list of their warning signs of a crisis
public class WarningSigns extends SetupClass {
    ListView signsList;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText newItem;
    SharedPreferences prefs;
    int removeIndex;
    Animation animation;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warning_signs);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusRed);

        prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        newItem = (EditText) findViewById(R.id.newItem);
        signsList = (ListView) findViewById(R.id.signsList);

        key = "listItems";
        setArrayAdapter();
        setAnimation();
        setListListener();
        setInfoDialog();
    }

    //detects when a list item is clicked and allows user to delete warning sign
    public void setListListener() {
        signsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WarningSigns.this);
                builder.setTitle("Delete Warning Sign")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeIndex = position;
                                view.startAnimation(animation);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}}) //does nothing
                        .show();
            }
        });
    }

    //adds user's entered warning sign to list to be displayed
    public void onAddItem (View v) {
        itemsAdapter.add(newItem.getText().toString());
        newItem.setText("");
    }

    @Override
    protected void onStop(){ //stores list of warning signs before activity ends
        super.onStop();
        String json = new Gson().toJson(items);
        prefs.edit().putString(key,json).apply();
    }

    //opens information dialog when icon is clicked
    public void setInfoDialog() {
        ImageView info = (ImageView) findViewById(R.id.infoIcon);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WarningSigns.this);
                builder.setTitle("Tips")
                        .setMessage("Enter in your personal warning signs that a crisis is developing. Consider thoughts, images, situations, moods, or behaviors.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}})
                        .show();
            }
        });
    }

    //fills list with previously saved list
    public void setArrayAdapter(){
        if (prefs.getString(key, "").equals(""))
            items = new ArrayList<>();
        else
            items = new Gson().fromJson(prefs.getString(key, ""), new TypeToken<ArrayList<String>>() {
            }.getType());
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        signsList.setAdapter(itemsAdapter);
    }

    //sets animation object that is used for when an item is deleted
    public void setAnimation(){
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) { //when animation is finished
                items.remove(removeIndex); //remove item from list
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }
}
