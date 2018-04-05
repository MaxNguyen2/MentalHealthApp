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


public class FamilyContact extends SetupClass {

    private static final int CONTACT_PICKER_RESULT = 1001;
    ListView familyList;
    SharedPreferences prefs;
    int index;
    Animation animation;
    String key;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_family_contact);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusRed);

        prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        familyList = (ListView) findViewById(R.id.familyList);
        key = "familyList";
        setArrayAdapter();
        setAnimation();
        setListListener();

        FloatingActionButton addContact = (FloatingActionButton) findViewById(R.id.addButton);
        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                permission = Manifest.permission.READ_CONTACTS;
                askForPermission();
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
            public void onAnimationEnd(Animation animation) {
                items.remove(index);
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setListListener() {
        familyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FamilyContact.this);
                builder
                        .setMessage("Choose whether to call this contact, delete this contact from this list, or cancel the dialog.")
                        .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                index = position;
                                permission = Manifest.permission.CALL_PHONE;
                                askForPermission();
                            }
                        })
                        .setNeutralButton("DELETE CONTACT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                index = position;
                                view.startAnimation(animation);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    public void launchContactPicker() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = "";
        String name = "";
        if (resultCode == RESULT_OK) {
            Uri result = data.getData();
            // get the contact id from the Uri
            String id = result.getLastPathSegment();
            // query for everything email
            Cursor cursor = getContentResolver().query(
                    Phone.CONTENT_URI, null,
                    Phone.CONTACT_ID + "=?",
                    new String[]{id}, null);
            int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int phoneIdx = cursor.getColumnIndex(Phone.NUMBER);

            // let's just get the first email
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIdx);
                phone = cursor.getString(phoneIdx);
            }
            if (name.equals(""))
                Toast.makeText(this, "No phone number associated with this contact", Toast.LENGTH_LONG).show();
            else
                itemsAdapter.add(name + " | " + phone);
        } else {
            // gracefully handle failure
        }
    }

    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                            requestPermissions(new String[] {permission}, 1);
                        }
                    });
                    builder.show();
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
                }
            } else {
                if (permission.equals(Manifest.permission.READ_CONTACTS))
                    launchContactPicker();
                else
                    launchCall();
            }
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
    protected void onStop() {
        super.onStop();
        String json = new Gson().toJson(items);
        prefs.edit().putString(key, json).apply();
    }

    public void launchCall(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String item = items.get(index);
        String number = item.substring(item.lastIndexOf("|")).replaceAll("\\D+", "");
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}
