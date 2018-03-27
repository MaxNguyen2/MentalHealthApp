package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
        setupStatusBar(R.color.StatusRed);

        prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        newItem = (EditText) findViewById(R.id.newItem);
        signsList = (ListView) findViewById(R.id.signsList);

        key = "listItems";
        setArrayAdapter();
        setAnimation();
        setListListener();
        setInfoDialog();
    }

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
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }
    /*
    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Slide slide = new Slide();
            slide.setInterpolator(new LinearInterpolator());
            slide.setSlideEdge(Gravity.RIGHT);
            window.setEnterTransition(slide);
            window.setReturnTransition(slide);
        }
    } */

    public void onAddItem (View v) {
        itemsAdapter.add(newItem.getText().toString());
        newItem.setText("");
    }

    @Override
    protected void onStop(){
        super.onStop();
        String json = new Gson().toJson(items);
        prefs.edit().putString(key,json).apply();
    }

    public void setInfoDialog() {
        ImageView info = (ImageView) findViewById(R.id.infoIcon);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WarningSigns.this);
                builder.setTitle("Tips")
                        .setMessage("Enter in your personal warning signs that a crisis is developing. Consider thoughts, images, situations, moods, or behaviors.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    public void setArrayAdapter(){
        if (prefs.getString(key, "").equals(""))
            items = new ArrayList<>();
        else
            items = new Gson().fromJson(prefs.getString(key, ""), new TypeToken<ArrayList<String>>() {
            }.getType());
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        signsList.setAdapter(itemsAdapter);
    }

    public void setAnimation(){
        animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_out);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                items.remove(removeIndex);
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }
}
