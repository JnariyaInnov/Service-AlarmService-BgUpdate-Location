package gov.telaviv.testworkmanager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

   // PowerManager.WakeLock screenWakeLock = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadCastReceiver", "onReceive ");
//        if (screenWakeLock == null) {
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
//                    "ScreenLock tag from AlarmListener");
//        }
//
//        screenWakeLock.acquire();
//
//        Log.d("AlarmBroadCastReceiver", "onReceive: screenWakeLock acquire");

        Intent service = new Intent(context, BackgroundOperationsManagerService.class);
       // startWakefulService(context, service);
        context.startService(service);


//        if (screenWakeLock != null)
//            screenWakeLock.release();

    }
}
