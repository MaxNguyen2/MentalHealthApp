package com.example.max.mentalhealthapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

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
            password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { //when switch preference changes state
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if(prefs.getBoolean("pin", false)) //opens DisablePassword if feature is on
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp","com.example.max.mentalhealthapp.DisablePassword")));
                    else //opens SetPassword if feature is off
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp","com.example.max.mentalhealthapp.SetPassword")));
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
            if (prefs.getString("password","").equals("") && switched)
                password.setChecked(false);
            else if (!prefs.getString("password","").equals("") && !switched)
                password.setChecked(true);

            //stores value of switch 
            editor.putBoolean("pin", password.isChecked());
            editor.commit();
            super.onResume();
        }


    }
}

