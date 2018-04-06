package com.example.max.mentalhealthapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

//Third section of the safety plan feature where users can import contacts of friends and family to a list
public class FamilyContact extends SetupClass {

    private static final int CONTACT_PICKER_RESULT = 1001;
    ListView familyList;
    SharedPreferences prefs;
    int index;
    Animation animation;
    String key, permission;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_family_contact);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusRed);

        prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        familyList = (ListView) findViewById(R.id.familyList);
        key = "familyList"; //sets key for storing the list of contacts
        setArrayAdapter(); //fills list view with previously saved list
        setAnimation(); //sets up animation object for when items are deleted
        setListListener(); //allows detection of when list items are clicked

        FloatingActionButton addContact = (FloatingActionButton) findViewById(R.id.addButton);
        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                permission = Manifest.permission.READ_CONTACTS;
                askForPermission(); //checks if permission is granted for reading contacts
            }
        });
    }

    public void setArrayAdapter() {
        if (prefs.getString(key, "").equals(""))
            items = new ArrayList<>();
        else
            items = new Gson().fromJson(prefs.getString(key, ""), new TypeToken<ArrayList<String>>() {
            }.getType());
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        familyList.setAdapter(itemsAdapter);
    }

    //sets animation object for when objects are deleted
    public void setAnimation() {
        animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { //called when the animation is finished
                items.remove(index); //removes item from list
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    //opens dialog when list item is clicked allowing users to call or delete the contact
    public void setListListener() {
        familyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FamilyContact.this);
                builder
                        .setMessage("Choose whether to call this contact, delete this contact from this list, or cancel the dialog.")
                        .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { //calls contact
                                index = position;
                                permission = Manifest.permission.CALL_PHONE;
                                askForPermission();
                            }
                        })
                        .setNeutralButton("DELETE CONTACT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { //deletes contact
                                index = position;
                                view.startAnimation(animation);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            } //does nothing
                        })
                        .show();
            }
        });
    }

    //opens dialog for users to import contact
    public void launchContactPicker() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    //processes information from contact that is selected
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = "";
        String name = "";
        if (resultCode == RESULT_OK) {
            Uri result = data.getData();
            String id = result.getLastPathSegment();
            Cursor cursor = getContentResolver().query(
                    Phone.CONTENT_URI, null,
                    Phone.CONTACT_ID + "=?",
                    new String[]{id}, null);
            int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int phoneIdx = cursor.getColumnIndex(Phone.NUMBER);

            //gets the name of the contact and phone number of the contact
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIdx);
                phone = cursor.getString(phoneIdx);
            }
            if (name.equals("")) //if contact has no phone number
                Toast.makeText(this, "No phone number is associated with this contact", Toast.LENGTH_LONG).show();
            else
                itemsAdapter.add(name + " | " + phone); //adds imported contact to list
        }
    }

    //checks if app has permission to read contacts
    public void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    builder.setTitle("Call access needed");
                    builder.setMessage("Please confirm call access");
                } else {
                    builder.setTitle("Contacts access needed");
                    builder.setMessage("Please confirm contacts access");
                }
                builder.setPositiveButton(android.R.string.ok, null);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{permission}, 1);
                    }
                });
                builder.show();
            } else
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        } else {
            if (permission.equals(Manifest.permission.READ_CONTACTS))
                launchContactPicker();
            else
                launchCall();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permission.equals(Manifest.permission.READ_CONTACTS))
                        launchContactPicker();
                    else
                        launchCall();

                } else {
                    Toast.makeText(this, "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onStop() { //stores list when activity is closed
        super.onStop();
        String json = new Gson().toJson(items);
        prefs.edit().putString(key, json).apply();
    }

    //starts phone call
    public void launchCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String item = items.get(index);
        String number = item.substring(item.lastIndexOf("|")).replaceAll("\\D+", "");
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            return;

        startActivity(callIntent);
    }
}
