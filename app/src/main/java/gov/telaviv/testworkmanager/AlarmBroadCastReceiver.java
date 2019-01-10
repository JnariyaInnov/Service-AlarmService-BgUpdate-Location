package gov.telaviv.testworkmanager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBroadCastReceiver", "onReceive ");

        Intent service = new Intent(context, BackgroundOperationsManagerService.class);
        context.startService(service);

    }
}
