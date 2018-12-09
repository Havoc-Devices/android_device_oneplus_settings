/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oneplus.settings.device;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.preference.TwoStatePreference;
import android.widget.ListView;

import com.oneplus.settings.device.utils.Constants;
import com.oneplus.settings.device.utils.NodePreferenceActivity;

public class DeviceSettings extends NodePreferenceActivity {

    private static final String KEY_HAPTIC_FEEDBACK = "touchscreen_gesture_haptic_feedback";
    private static final String SPECTRUM_KEY = "spectrum";
    private static final String SPECTRUM_CATEGORY_KEY = "spectrum_category";
    private static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    private SwitchPreference mHapticFeedback;
    private TwoStatePreference mHBMModeSwitch;
    private ListPreference mSpectrum;
    private PreferenceCategory mSpectrumCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.device_settings);

        ListView lv = getListView();
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lv.setDividerHeight(0);

        mHapticFeedback = (SwitchPreference) findPreference(KEY_HAPTIC_FEEDBACK);
        mHapticFeedback.setOnPreferenceChangeListener(this);

        mHBMModeSwitch = (TwoStatePreference) findPreference(Constants.KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled(this));
        mHBMModeSwitch.setOnPreferenceChangeListener(new HBMModeSwitch());

        mSpectrum = (ListPreference) findPreference(SPECTRUM_KEY);
        if( mSpectrum != null ) {
            mSpectrum.setValue(SystemProperties.get(SPECTRUM_SYSTEM_PROPERTY, "0"));
            mSpectrum.setOnPreferenceChangeListener(this);
        }

        mSpectrumCategory = (PreferenceCategory) findPreference(SPECTRUM_CATEGORY_KEY);
        if (!getResources().getBoolean(R.bool.device_supports_spectrum)) {
            getPreferenceScreen().removePreference(mSpectrumCategory);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        String strvalue;
        if (KEY_HAPTIC_FEEDBACK.equals(key)) {
            final boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), KEY_HAPTIC_FEEDBACK, value ? 1 : 0);
            return true;
        } else if (SPECTRUM_KEY.equals(key)) {
            strvalue = (String) newValue;
            SystemProperties.set(SPECTRUM_SYSTEM_PROPERTY, strvalue);
            return true;
        }
        return super.onPreferenceChange(preference, newValue);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If running on a phone, remove padding around the listview
        getListView().setPadding(0, 0, 0, 0);

        mHapticFeedback.setChecked(
                Settings.System.getInt(getContentResolver(), KEY_HAPTIC_FEEDBACK, 1) != 0);
    }
}
