package com.crowdint.vigilente.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by DarkAbsolutE on 7/27/15.
 */
public class PreferencesManager extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static String ReadPreferences(String registration, Context context){
        SharedPreferences preferences = context.getSharedPreferences("Status", Context.MODE_PRIVATE);
        return preferences.getString(registration,"");
    }

    public static Long ReadPreferencesLong(String registration, Context context){
        SharedPreferences preferences = context.getSharedPreferences("Status", Context.MODE_PRIVATE);
        return preferences.getLong(registration, 0);
    }

    public static void SavePreferencesGuardaPreferencias(String registration, String value, Context context){
        SharedPreferences preferences =  context.getSharedPreferences("Status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(registration, value);
        editor.commit();
    }

    public static void SavePreferences(String registration, Long value, Context context){
        SharedPreferences preferences =  context.getSharedPreferences("Status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(registration, value);
        editor.commit();
    }

}
