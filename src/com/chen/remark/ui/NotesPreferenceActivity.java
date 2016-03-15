package com.chen.remark.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import com.chen.remark.R;

/**
 * Created by chenfayong on 16/3/8.
 */
public class NotesPreferenceActivity extends PreferenceActivity {

    public static final String PREFERENCE_SET_BG_COLOR_KEY = "pref_key_bg_random_appear";

    public static final String PREFERENCE_SYNC_ACCOUNT_KEY = "pref_sync_account_key";

    private PreferenceCategory mAccountCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // using the app icon for navigation
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        this.addPreferencesFromResource(R.xml.preferences);
        this.mAccountCategory = (PreferenceCategory)this.findPreference(PREFERENCE_SYNC_ACCOUNT_KEY);
    }
}
