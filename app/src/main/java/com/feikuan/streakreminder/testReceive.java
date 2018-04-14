package com.feikuan.streakreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by fei on 3/8/18.
 */

public class testReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        String action=intent.getStringExtra("action");
        if(action.equals("actionname")){
            performAction1(context);
        }

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);

    }
    public void performAction1(Context context) {
        Toast.makeText(context,"sup",Toast.LENGTH_SHORT).show();
    }
}
