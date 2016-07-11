package net.borkiss.weatherforecast.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class Prefs {

    private static final String PREF_LAST_UPDATE_TIME = "lastUpdateTime";

    /**
     *
     * @param context Context
     * @return Time in milliseconds
     */
    public static long getLastUpdateTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_LAST_UPDATE_TIME, 0);
    }

    /**
     *
     * @param context Context
     * @param lastUpdateTime Time in milliseconds
     */
    public static void setLastUpdateTime(Context context, long lastUpdateTime) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_LAST_UPDATE_TIME, lastUpdateTime)
                .apply();
    }
}
