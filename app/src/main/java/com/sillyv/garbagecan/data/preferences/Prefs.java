package com.sillyv.garbagecan.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class created by Vasili on 27/01/2018.
 */

public class Prefs {


    private static final String GARBAGE_CAN_PREFERENCES = "GarbageCanPreferences";
    private static final String EMAIL = "EMAIL";
    private static final String NAME = "NAME";
    private static final String FIRST_TIME = "FIRST_TIME";
    private static final String EMPTY_STRING = "";
    private final SharedPreferences sharedPref;

    private Prefs(Context context) {
        sharedPref = context.getSharedPreferences(GARBAGE_CAN_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static Prefs get(Context context) {
        return new Prefs(context);
    }

    public void writeName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public void writeEmail(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void writeFirstTime() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FIRST_TIME, false);
        editor.apply();
    }

    public String readName() {
        return sharedPref.getString(NAME, EMPTY_STRING);
    }

    public String readEmail() {
        return sharedPref.getString(EMAIL, EMPTY_STRING);
    }

    public boolean readFirstTime() {
        return sharedPref.getBoolean(FIRST_TIME, true);
    }

}
