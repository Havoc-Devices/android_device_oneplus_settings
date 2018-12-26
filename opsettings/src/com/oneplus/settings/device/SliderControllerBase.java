/*
 * Copyright (C) 2018 crDroid Android Project
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

import android.content.Context;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import com.oneplus.settings.device.utils.FileUtils;

public abstract class SliderControllerBase {

    private static final String TAG = "SliderControllerBase";

    private static final int KEY_SLIDER_TOP = 601;
    private static final int KEY_SLIDER_MIDDLE = 602;
    private static final int KEY_SLIDER_BOTTOM = 603;

    private static final String SLIDER_STATE = "/sys/class/switch/tri-state-key/state";

    private static final String KEY_GESTURE_HAPTIC_FEEDBACK =
            "touchscreen_gesture_haptic_feedback";

    protected final Context mContext;

    private Vibrator mVibrator;

    private int[] mActions = null;

    public SliderControllerBase(Context context) {
        mContext = context;
        mVibrator = context.getSystemService(Vibrator.class);
        if (mVibrator == null || !mVibrator.hasVibrator()) {
            mVibrator = null;
        }
    }

    public final void update(int[] actions) {
        if (actions != null && actions.length == 3) {
            mActions = actions;
        }
    }

    public final boolean isSupported(int key) {
        return key == KEY_SLIDER_TOP ||
                key == KEY_SLIDER_MIDDLE ||
                key == KEY_SLIDER_BOTTOM;
    }

    protected abstract boolean processAction(int action);

    public final boolean processEvent(int key) {
        if (mActions == null) {
            return false;
        }

        boolean processed = false;
        switch (key) {
            case KEY_SLIDER_TOP:
                processed = processAction(mActions[0]);
                break;
            case KEY_SLIDER_MIDDLE:
                processed = processAction(mActions[1]);
                break;
            case KEY_SLIDER_BOTTOM:
                processed = processAction(mActions[2]);
                break;
        }

        if (processed) {
            doHapticFeedback();
        }

        return processed;
    }

    public abstract void reset();

    public final void restoreState() {
        if (mActions == null) {
            return;
        }

        try {
            int state = Integer.parseInt(FileUtils.readOneLine(SLIDER_STATE));
            processAction(mActions[state - 1]);
        } catch (Exception e) {
            Log.e(TAG, "Failed to restore slider state", e);
        }
    }

    private void doHapticFeedback() {
        if (mVibrator == null) {
            return;
        }
        boolean enabled = Settings.System.getInt(mContext.getContentResolver(),
                KEY_GESTURE_HAPTIC_FEEDBACK, 1) != 0;
        if (enabled) {
            mVibrator.vibrate(50);
        }
    }

    protected final <T> T getSystemService(String name) {
        return (T) mContext.getSystemService(name);
    }
}
