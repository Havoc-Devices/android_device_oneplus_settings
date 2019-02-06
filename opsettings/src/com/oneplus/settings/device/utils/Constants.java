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

package com.oneplus.settings.device.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceManager;

public class Constants {

    // Gestures
    public static final String TOUCHSCREEN_CAMERA_GESTURE_KEY = "touchscreen_gesture_camera";
    public static final String TOUCHSCREEN_MUSIC_GESTURE_KEY = "touchscreen_gesture_music";
    public static final String TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY = "touchscreen_gesture_flashlight";

    // Alert slider
    public static final String NOTIF_SLIDER_PANEL_KEY = "notification_slider";
    public static final String NOTIF_SLIDER_USAGE_KEY = "slider_usage";
    public static final String NOTIF_SLIDER_ACTION_TOP_KEY = "action_top_position";
    public static final String NOTIF_SLIDER_ACTION_MIDDLE_KEY = "action_middle_position";
    public static final String NOTIF_SLIDER_ACTION_BOTTOM_KEY = "action_bottom_position";

    // Gestures nodes
    public static final String TOUCHSCREEN_CAMERA_NODE = "/proc/touchpanel/letter_o_enable";
    public static final String TOUCHSCREEN_DOUBLE_SWIPE_NODE = "/proc/touchpanel/double_swipe_enable";
    public static final String TOUCHSCREEN_LEFT_ARROW = "/proc/touchpanel/left_arrow_enable";
    public static final String TOUCHSCREEN_RIGHT_ARROW = "/proc/touchpanel/right_arrow_enable";
    public static final String TOUCHSCREEN_FLASHLIGHT_NODE = "/proc/touchpanel/down_arrow_enable";

    // Array of music-related gestures
    public static final String[] TOUCHSCREEN_MUSIC_GESTURES_ARRAY = {TOUCHSCREEN_DOUBLE_SWIPE_NODE, TOUCHSCREEN_LEFT_ARROW, TOUCHSCREEN_RIGHT_ARROW};

    // Gestures nodes default values
    public static final boolean TOUCHSCREEN_CAMERA_DEFAULT = true;
    public static final boolean TOUCHSCREEN_MUSIC_DEFAULT = true;
    public static final boolean TOUCHSCREEN_FLASHLIGHT_DEFAULT = true;

    // Alert slider nodes
    public static final String NOTIF_SLIDER_NODE = "/sys/class/switch/tri-state-key/state";

    public static final String NOTIF_SLIDER_FOR_NOTIFICATION = "1";
    public static final String NOTIF_SLIDER_FOR_FLASHLIGHT = "2";
    public static final String NOTIF_SLIDER_FOR_BRIGHTNESS = "3";
    public static final String NOTIF_SLIDER_FOR_ROTATION = "4";
    public static final String NOTIF_SLIDER_FOR_RINGER = "5";
    public static final String NOTIF_SLIDER_FOR_NOTIFICATION_RINGER = "6";

    public static final String ACTION_UPDATE_SLIDER_SETTINGS
            = "com.oneplus.settings.device.UPDATE_SLIDER_SETTINGS";

    public static final String EXTRA_SLIDER_USAGE = "usage";
    public static final String EXTRA_SLIDER_ACTIONS = "actions";

    // Display modes
    public static final String KEY_SRGB_SWITCH = "srgb";
    public static final String KEY_NIGHT_SWITCH = "night";
    public static final String KEY_ONEPLUS_SWITCH = "oneplus_mode";
    public static final String KEY_DCI_SWITCH = "dci";
    public static final String KEY_HBM_SWITCH = "hbm";

    // Holds <preference_key> -> <proc_node> mapping
    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();
    public static final Map<String, String> sStringNodePreferenceMap = new HashMap<>();

    // Holds <preference_key> -> <default_values> mapping
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();

    public static final String[] sGesturePrefKeys = {
        TOUCHSCREEN_CAMERA_GESTURE_KEY,
        TOUCHSCREEN_MUSIC_GESTURE_KEY,
        TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY
    };

    static {
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_DOUBLE_SWIPE_NODE);
        sBooleanNodePreferenceMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY,
                TOUCHSCREEN_FLASHLIGHT_NODE);

        sNodeDefaultMap.put(TOUCHSCREEN_CAMERA_GESTURE_KEY, TOUCHSCREEN_CAMERA_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_MUSIC_GESTURE_KEY, TOUCHSCREEN_MUSIC_DEFAULT);
        sNodeDefaultMap.put(TOUCHSCREEN_FLASHLIGHT_GESTURE_KEY, TOUCHSCREEN_FLASHLIGHT_DEFAULT);
    }

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }

    public static String getPreferenceString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, (String) sNodeDefaultMap.get(key));
    }

    public static boolean isNotificationSliderSupported() {
        return true;
    }
}
