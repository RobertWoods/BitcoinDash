package edu.temple.buttcoin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;

/**
 * Created by rober_000 on 12/8/2016.
 */

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int hour = 1000 * 60 * 60;
        Log.d("StartupReceiver", "We started the receiver real good m80");
        context.startService(new Intent(context, UpdaterService.class));
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmManager alarmManager = (AlarmManager) context.
                    getSystemService(Context.ALARM_SERVICE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(cal.getTime());
            cal.add(Calendar.HOUR, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Intent serviceIntent = new Intent(context, UpdaterService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 1,
                    serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    hour, pendingIntent);
        }
    }



}
