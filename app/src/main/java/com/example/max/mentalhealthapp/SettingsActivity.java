package com.example.max.mentalhealthapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;


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
            final SharedPreferences.Editor editor = prefs.edit();
            password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference p, Object o) {
                    if(prefs.getBoolean("pin", false))
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp","com.example.max.mentalhealthapp.DisablePassword")));
                    else
                        password.setIntent(new Intent().setComponent(new ComponentName("com.example.max.mentalhealthapp","com.example.max.mentalhealthapp.SetPassword")));
                    boolean switched = ((SwitchPreference) p).isChecked();
                    editor.putBoolean("pin", !switched);
                    editor.commit();
                    return true;
                }
            });
        }

    }
}

