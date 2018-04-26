package com.feikuan.streakreminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

public class NotificationServiceSet extends IntentService {
    public NotificationServiceSet() {
        super("NotificationServiceSet");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
        // canceling notification only works in Activity not directly in notification
        long trigger_time = System.currentTimeMillis() + 396000000;
        Intent alarmInt = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmInt, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, trigger_time, pi);
    }

}
