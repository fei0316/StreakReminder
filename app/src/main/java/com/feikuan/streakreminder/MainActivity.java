package com.feikuan.streakreminder;

//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.content.Context;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.graphics.Color;
//import android.os.SystemClock;
import android.preference.PreferenceManager;
//import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
//import android.widget.RelativeLayout;
import android.widget.TextView;
//import java.util.Locale;


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
                LaunchSnap(v);
            }
        });

        Button testNotif = findViewById(R.id.button4);
        testNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotification();
            }
        });

        LoadConfig();
    }

    public void onResume() {
        super.onResume();
        LoadConfig();
    }

    public void LoadConfig() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        long time_last = settings.getLong("lastsnaptime", 0);
        LoadCurrentTime(time_last);
    }

    public void LoadCurrentTime(long time_last) {
        long millis_now = System.currentTimeMillis();
        long time_till = millis_now - time_last;
        String showTime;
        int showHours, showMin, showSec;
        if (time_till > 604800000) {
            showTime = getString(R.string.long_ago);
        } else {
            long longHours = (time_till / 1000 / 60 / 60);
            long longMin = (time_till / 1000 / 60 % 60);
            long longSec = (time_till / 1000 % 60);
            showHours = (int) longHours;
            showMin = (int) longMin;
            showSec = (int) longSec;
            showTime = String.format("%02d", showHours) + ":" + String.format("%02d", showMin) + ":" + String.format("%02d", showSec);
            // terrible way to convert TimeMillis to hh:mm:ss notation, does not take account into other ways of time formats and may be inefficient
        }
        TextView lastSnapTime = findViewById(R.id.textView4);

        if (time_till>43200000 && time_till<72000000) {
            lastSnapTime.setTextColor(getResources().getColor(R.color.colorOrange));
        }

        if (time_till>72000000) {
            lastSnapTime.setTextColor(getResources().getColor(R.color.colorRed));
        }

        if (time_till<0) {
            showTime = getString(R.string.corrupted_data);
            lastSnapTime.setTextColor(getResources().getColor(R.color.colorRed));
        }

        if (time_till>=0 && time_till<=4320000) {
            lastSnapTime.setTextColor(getResources().getColor(R.color.colorBlack));
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

    public void SaveTime() {
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.apply();
        scheduleNotif();
        LoadConfig();
    }

    public void LaunchSnap(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.snapchat_package));
        if (launchIntent != null) {
            startActivity(launchIntent);
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.cancel(pi); //should be removed soonTM
        } else {
            Snackbar.make(v, R.string.not_installed, Snackbar.LENGTH_SHORT).show();
        }
    }

    /*
    * WIP attempt for snap notification: currently by pressing sent it sets a one-minute timer and sends toast every
    * one minute. Use open snapchat button to stop
    * work needed: notification with action button, time planning for notification (how often)
    * */

    public void scheduleNotif () {
        long trigger_time = System.currentTimeMillis() + 10000;
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, trigger_time, 10000, pi);
    }

    public void SendNotification () {
        String lastSnap = "test";
        Intent openAppInt = new Intent(getPackageManager().getLaunchIntentForPackage(getString(R.string.snapchat_package)));
        Intent openReminderInt = new Intent(this, MainActivity.class);
        PendingIntent openApp = PendingIntent.getActivity(this, 0, openAppInt, 0);
        PendingIntent openReminderApp = PendingIntent.getActivity(this, 0, openReminderInt, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getResources().getString(R.string.id))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getResources().getString(R.string.notif_title))
                        .setContentText(
                                String.format(getResources().getString(R.string.notif_body),
                                        lastSnap))
                        .setContentIntent(openApp);
        mBuilder.addAction(R.drawable.ic_launcher_foreground, "open", openReminderApp);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }

}