package com.feikuan.streakreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by fei on 1/19/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();
            Intent makeNotif = new Intent(context, MakeNotification.class);
            context.startService(makeNotif);
        }
        //don't think can call notification setter from here
}
