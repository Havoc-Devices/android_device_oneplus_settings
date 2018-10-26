/*
* Copyright (C) 2018 Havoc-OS
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.oneplus.settings.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.TwoStatePreference;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;
import android.os.Bundle;

import com.oneplus.settings.device.utils.Constants;

public class PanelSettings extends PreferenceFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        int checkedButtonId = R.id.off_mode;
        if (NightModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.night_mode;
        } else if (OneplusModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.oneplus_mode;
        } else if (SRGBModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.srgb_mode;
        } else if (DCIModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.dci_mode;
        }
        mRadioGroup.check(checkedButtonId);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.panel_modes, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.inactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.inactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
		return rootView;
	}

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sharedPrefs.edit();
        if (checkedId == R.id.srgb_mode) {
            Utils.writeValue(DCIModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_DCI_SWITCH, false);
            Utils.writeValue(OneplusModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_ONEPLUS_SWITCH, false);
            Utils.writeValue(NightModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_NIGHT_SWITCH, false);
            Utils.writeValue(SRGBModeSwitch.getFile(), "1");
            edit.putBoolean(Constants.KEY_SRGB_SWITCH, true);
        } else if (checkedId == R.id.oneplus_mode) {
            Utils.writeValue(DCIModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_DCI_SWITCH, false);
            Utils.writeValue(SRGBModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_SRGB_SWITCH, false);
            Utils.writeValue(NightModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_NIGHT_SWITCH, false);
            Utils.writeValue(OneplusModeSwitch.getFile(), "1");
            edit.putBoolean(Constants.KEY_ONEPLUS_SWITCH, true);
        } else if (checkedId == R.id.night_mode) {
            Utils.writeValue(DCIModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_DCI_SWITCH, false);
            Utils.writeValue(SRGBModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_SRGB_SWITCH, false);
            Utils.writeValue(OneplusModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_ONEPLUS_SWITCH, false);
            Utils.writeValue(NightModeSwitch.getFile(), "1");
            edit.putBoolean(Constants.KEY_NIGHT_SWITCH, true);
        } else if (checkedId == R.id.dci_mode) {
            Utils.writeValue(OneplusModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_ONEPLUS_SWITCH, false);
            Utils.writeValue(NightModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_NIGHT_SWITCH, false);
            Utils.writeValue(SRGBModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_SRGB_SWITCH, false);
            Utils.writeValue(DCIModeSwitch.getFile(), "1");
            edit.putBoolean(Constants.KEY_DCI_SWITCH, true);
        } else if (checkedId == R.id.off_mode) {
            Utils.writeValue(DCIModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_DCI_SWITCH, false);
            Utils.writeValue(OneplusModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_ONEPLUS_SWITCH, false);
            Utils.writeValue(NightModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_NIGHT_SWITCH, false);
            Utils.writeValue(SRGBModeSwitch.getFile(), "0");
            edit.putBoolean(Constants.KEY_SRGB_SWITCH, false);
        }
        edit.commit();
    }
}
