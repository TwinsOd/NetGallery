package com.example.twins.netgallery.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.twins.netgallery.R;

/**
 * Created by Twins on 29.08.2016.
 */

public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_pref);
    }
}
