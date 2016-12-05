package edu.temple.buttcoin;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UpdaterService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "edu.temple.buttcoin.action.FOO";
    public static final String ACTION_BAZ = "edu.temple.buttcoin.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "edu.temple.buttcoin.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "edu.temple.buttcoin.extra.PARAM2";

    public UpdaterService() {
        super("UpdaterService");
    }
    private int hour = 1000 * 60 * 60;
    private int day  = hour * 24;

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        cal.add(Calendar.HOUR, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), hour, null);

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleChartUpdate();
            } else if (ACTION_BAZ.equals(action)) {
                handleWalletsUpdate();
            }
        }
    }

    private void handleChartUpdate() {

    }

    private void handleWalletsUpdate() {

    }


}
