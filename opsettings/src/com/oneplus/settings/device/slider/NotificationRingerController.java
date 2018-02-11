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

package com.oneplus.settings.device.slider;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseIntArray;

import com.oneplus.settings.device.SliderControllerBase;

public final class NotificationRingerController extends SliderControllerBase {

    public static final int ID = 6;

    private static final String TAG = "NotificationRingerController";

    private static final int NOTIFICATION_TOTAL_SILENCE = 60;
    private static final int NOTIFICATION_PRIORITY_ONLY = 62;
    private static final int NOTIFICATION_ALL = 63;
    private static final int RINGER_VIBRATE = 64;
    private static final int RINGER_SILENT = 65;

    private static final SparseIntArray MODES = new SparseIntArray();
    static {
        MODES.put(NOTIFICATION_TOTAL_SILENCE,
                Settings.Global.ZEN_MODE_NO_INTERRUPTIONS);
        MODES.put(NOTIFICATION_PRIORITY_ONLY,
                Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS);
        MODES.put(NOTIFICATION_ALL,
                Settings.Global.ZEN_MODE_OFF);
        MODES.put(RINGER_VIBRATE, AudioManager.RINGER_MODE_VIBRATE);
        MODES.put(RINGER_SILENT, AudioManager.RINGER_MODE_SILENT);
    }

    private final NotificationManager mNotificationManager;
    private final AudioManager mAudioManager;

    public NotificationRingerController(Context context) {
        super(context);
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE);
        mAudioManager = getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected boolean processAction(int action) {
        Log.d(TAG, "slider action: " + action);
        if (MODES.indexOfKey(action) < 0)
            return false;

        if (action >= RINGER_VIBRATE) {
            mNotificationManager.setZenMode(Settings.Global.ZEN_MODE_OFF, null, TAG);
            mAudioManager.setRingerModeInternal(MODES.get(action));
            return true;
        } else if (action <= NOTIFICATION_TOTAL_SILENCE) {
            mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_SILENT);
            mNotificationManager.setZenMode(MODES.get(action), null, TAG);
            return true;
        } else {
            mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_NORMAL);
            mNotificationManager.setZenMode(MODES.get(action), null, TAG);
            return true;
        }
    }

    @Override
    public void reset() {
        mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_NORMAL);
        mNotificationManager.setZenMode(Settings.Global.ZEN_MODE_OFF, null, TAG);
    }
}
