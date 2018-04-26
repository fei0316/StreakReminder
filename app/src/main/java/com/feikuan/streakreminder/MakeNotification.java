package com.feikuan.streakreminder;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class MakeNotification extends IntentService {

    public MakeNotification() {
        super("MakeNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent openApp = new Intent(this, MainActivity.class);
        Intent openSnap = getPackageManager().getLaunchIntentForPackage(getString(R.string.snapchat_package));
        Intent resetTime = new Intent(this, resetService.class);
        resetTime.setAction(resetService.ACTION1);
        PendingIntent pendingApp = PendingIntent.getActivity(this, 0, openApp, 0);
        PendingIntent pendingReset = PendingIntent.getService(this, 154, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingApp)
                .addAction(R.drawable.ic_stat_name, getString(R.string.just_now), pendingReset);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        if (openSnap != null) {
            openSnap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingSnap = PendingIntent.getActivity(this, 0, openSnap, 0);
            nBuilder.addAction(R.drawable.ic_stat_name, getString(R.string.open_snap), pendingSnap);
        }
        notificationManager.notify(1, nBuilder.build());
    }


}
