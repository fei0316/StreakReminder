package com.feikuan.streakreminder;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Context;

/**
 * Created by fei on 1/20/18.
 */

public class ResetService extends IntentService {
    public ResetService() {
        super(ResetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent (Intent intent) {
        if ("sent".equals(intent.getAction())) {
            long saveLongTime = System.currentTimeMillis();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("lastsnaptime", saveLongTime);
            editor.apply();

        }

    /*
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.apply();
        //scheduleNotif();
*/

    }
}
