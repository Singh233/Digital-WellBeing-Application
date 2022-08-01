package timeline.android.digitalwellbeing.TYProject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String STATUS = "logged";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setStatus(Context ctx, String status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(STATUS, status);
        editor.apply();
    }

    public static String getStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getString(STATUS, "");
    }

    public static void clearStatus(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }


}
