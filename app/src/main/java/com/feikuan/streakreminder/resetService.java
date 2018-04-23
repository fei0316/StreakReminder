package com.feikuan.streakreminder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class resetService extends IntentService {
    public static final String ACTION1 = "ACTION1";
    private static final String TAG = "resetService";

    public resetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {
            long saveLongTime = System.currentTimeMillis();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("lastsnaptime", saveLongTime);
            editor.apply();
            String s = Context.NOTIFICATION_SERVICE;
            NotificationManager mNM = (NotificationManager) this.getSystemService(s);
            mNM.cancel(1);
        }
        else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}

/*


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

    */
/*
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.apply();
        //scheduleNotif();
*//*


    }*/