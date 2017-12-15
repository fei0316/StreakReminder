package com.feikuan.streakreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button snapSent = findViewById(R.id.button);
        snapSent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveTime();
            }
        });
        Button openSnap = findViewById(R.id.button3);
        openSnap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LaunchSnap();
            }
        });
            LoadConfig();
        }

    public void onResume() {
        super.onResume();
        LoadConfig();
    }

    public void LoadConfig () {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        long time_last = settings.getLong("lastsnaptime", 0);
        LoadCurrentTime(time_last);
    }

    public void LoadCurrentTime (long time_last) {
        long millis_now = System.currentTimeMillis();
        long time_till = millis_now - time_last ;
        String showTime;
        int showHours, showMin, showSec;
        if (time_till>604800000) {
            showTime = "Long ago...";
        }
        else {
            long longHours = (time_till / 1000 / 60 / 60);
            long longMin = (time_till / 1000 / 60 % 60);
            long longSec = (time_till / 1000 % 60);
            showHours = (int) longHours;
            showMin = (int) longMin;
            showSec = (int) longSec;
            showTime = String.format("%02d", showHours)+":"+String.format("%02d", showMin)+":"+String.format("%02d", showSec);
        }
        TextView lastSnapTime = findViewById(R.id.textView4);

       if (time_till > 43200000 && time_till < 72000000) {
           lastSnapTime.setTextColor(getResources().getColor(R.color.colorOrange));
       }

       if (time_till>72000000) {
           lastSnapTime.setTextColor(getResources().getColor(R.color.colorRed));
       }

       if (time_till<0) {
           showTime = "Corrupted Time! Clear Data!";
           lastSnapTime.setTextColor(getResources().getColor(R.color.colorRed));
       }
        lastSnapTime.setText(showTime);

//       scheduleNotification();

    }

/*    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }*/

//      inconplete implementation of notification

    private void SaveTime () {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", System.currentTimeMillis());
        editor.apply();
        LoadConfig();
    }

    private void LaunchSnap () {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
            /*else {
                Snackbar.make(findViewById(android.R.id.content), "Snapchat not installed", Snackbar.LENGTH_SHORT).show;
            }*/
        }
    }