package com.hakivin.submission3.notification;

import android.content.Context;
import android.content.SharedPreferences;

import com.hakivin.submission3.entity.Reminder;

public class ReminderPreference {
    private static final String PREFS_NAME = "reminder_pref";

    private static final String DAILY = "daily";
    private static final String RELEASE = "release";

    private final SharedPreferences sharedPreferences;

    public ReminderPreference(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setPreference(Reminder reminder){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DAILY, reminder.getDaily());
        editor.putBoolean(RELEASE, reminder.getRelease());
        editor.apply();
    }

    public Reminder getPreference(){
        Reminder reminder = new Reminder();
        reminder.setDaily(sharedPreferences.getBoolean(DAILY, true));
        reminder.setRelease(sharedPreferences.getBoolean(RELEASE, true));
        return reminder;
    }


}
