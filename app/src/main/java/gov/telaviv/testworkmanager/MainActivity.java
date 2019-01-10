package gov.telaviv.testworkmanager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


//import androidx.work.Data;
//import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String GPS_WORK_TAG = "gps_work";
    private final static String UNIQUE_PERIODIC_GPS = "uniquePeriodicGpsWork";
    public final static String LOG_POINT_DATA = "data_log__";
    public final static String UUID_TAG = "uuid_tag";
    public final static String POINTS_LOG = "points.log";
    private TextView logTv;
    private Button startButton;
    private Button stopButton;
    private UUID uuuidWorkRequest;
    public final static int ALARM_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTv = findViewById(R.id.logTextView);
        logTv.setMovementMethod(new ScrollingMovementMethod());
        String str = AppUtils.getPreferences(getApplicationContext(), POINTS_LOG);
        logTv.setText(str);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        startButton.setOnClickListener(startButtonListener);
        stopButton.setOnClickListener(stopButtonListener);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName)) {
                //     intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                // startActivity(intent);
            }
            else {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(getClass().getSimpleName(), "onStart Main Thread =" + Thread.currentThread().getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TedPermission.with(this)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            if (!isFinishing() && !isDestroyed()) {
                                enableGpsWorks();
                            }
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                        }
                    })
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION /*, Manifest.permission.WAKE_LOCK*/).check();
        } else {
            enableGpsWorks();
        }

    }

    private void enableGpsWorks() {


//        String uuuidStr = AppUtils.getPreferences(getApplicationContext(), UUID_TAG);
//        if (!TextUtils.isEmpty(uuuidStr))
//            uuuidWorkRequest = UUID.fromString(uuuidStr);
//        if (uuuidWorkRequest == null)
//            startButton.setEnabled(true);
//        else {
//            WorkManager.getInstance().getWorkInfoById(uuuidWorkRequest).addListener(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                WorkInfo workInfo = WorkManager.getInstance().getWorkInfoById(uuuidWorkRequest).get();
//                                if (workInfo != null) {
//                                    Log.i("Listener", "Run in Listener: WorkInfo 2 State=" + workInfo.getState().name());
//                                    if (workInfo.getState().isFinished()) {
//                                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                                            updateLogText(workInfo.getOutputData());
//                                        } else {
//                                            Toast.makeText(MainActivity.this, "Finished state: " + workInfo.getState().name(), Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                }
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    getMainExecutor()
//            );
//
//            WorkManager.getInstance().getWorkInfoByIdLiveData(uuuidWorkRequest).observe(ProcessLifecycleOwner.get(), new Observer<WorkInfo>() {
//                @Override
//                public void onChanged(@Nullable WorkInfo workInfo) {
//                    if (workInfo != null) {
//                        Log.i("WorkInfoById", "WorkInfo 2 State=" + workInfo.getState().name());
//                        if (workInfo.getState().isFinished()) {
//                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                                updateLogText(workInfo.getOutputData());
//                            } else {
//                                Toast.makeText(MainActivity.this, "Finished state: " + workInfo.getState().name(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                }
//            });
//        }

    }

    private View.OnClickListener startButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

//            Intent intentServer = new Intent(getApplicationContext(), MyForegroundService.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intentServer);
//            }
//            else {
//                startService(intentServer);
//            }

            setAlarm(MainActivity.this, false);

//            if (uuuidWorkRequest == null) {
//                Constraints constraints = new Constraints.Builder()
//                        .setRequiresBatteryNotLow(true)
//                        .build();
//                PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(GpsWorker.class, 16, TimeUnit.MINUTES) //, 15, TimeUnit.MINUTES)
//                        // .setConstraints(constraints)
//                        .addTag(GPS_WORK_TAG)
//                        .build();
//
//                // WorkManager.getInstance().enqueueUniquePeriodicWork(UNIQUE_PERIODIC_GPS, ExistingPeriodicWorkPolicy.REPLACE, myWorkRequest);
//                WorkManager.getInstance().enqueue(myWorkRequest);
//
//                uuuidWorkRequest = myWorkRequest.getId();
//                AppUtils.setPreferences(getApplicationContext(), UUID_TAG, uuuidWorkRequest.toString());
//                startButton.setEnabled(false);
//                WorkManager.getInstance().getWorkInfoById(uuuidWorkRequest).addListener(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    WorkInfo workInfo = WorkManager.getInstance().getWorkInfoById(uuuidWorkRequest).get();
//                                    if (workInfo != null) {
//                                        Log.i("Listener", "Run in Listener: WorkInfo State=" + workInfo.getState().name());
//                                        if (workInfo.getState().isFinished()) {
//                                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                                                updateLogText(workInfo.getOutputData());
//                                            } else {
//                                                Toast.makeText(MainActivity.this, "Finished state: " + workInfo.getState().name(), Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//                                    }
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        getMainExecutor()
//                );
//                WorkManager.getInstance().getWorkInfoByIdLiveData(myWorkRequest.getId()).observe(ProcessLifecycleOwner.get(), new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(@Nullable WorkInfo workInfo) {
//                        if (workInfo != null) {
//                            Log.i("WorkInfoById", "WorkInfo 1 State=" + workInfo.getState().name());
//                            if (workInfo.getState().isFinished()) {
//                                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                                    updateLogText(workInfo.getOutputData());
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Finished state: " + workInfo.getState().name(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                    }
//                });
//            }
        }
    };

//    private void updateLogText(Data data){
//        if(!isFinishing()) {
//            String str = data.getString(LOG_POINT_DATA);
//            logTv.setText(str + logTv.getText().toString());
//        }
//    }

    private View.OnClickListener stopButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            Intent intentServer = new Intent(getApplicationContext(), MyForegroundService.class);
//            stopService(intentServer);

            Intent intent = new Intent(getApplicationContext(), AlarmBroadCastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);


//            if (uuuidWorkRequest != null)
//                WorkManager.getInstance().cancelAllWorkByTag(GPS_WORK_TAG);
//            uuuidWorkRequest = null;
//            AppUtils.setPreferences(getApplicationContext(), UUID_TAG, "");
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                startButton.setEnabled(true);
        }
    };




    public static void setAlarm(Context context, boolean flag) {

        Log.d(TAG, "setAlarm ");

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if(flag) {
            calendar.add(Calendar.MINUTE, 1);
        }
        else {
            calendar.add(Calendar.SECOND, 15);
        }

//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        if (Build.VERSION.SDK_INT >= 23)
        {
            Log.d(TAG, "setAlarm setExactAndAllowWhileIdle: " + (calendar.getTimeInMillis() - System.currentTimeMillis()));
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            Log.d(TAG, "setAlarm setExact: "+ (calendar.getTimeInMillis() - System.currentTimeMillis()));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else
        {
            Log.d(TAG, "setAlarm setExact: " + (calendar.getTimeInMillis() - System.currentTimeMillis()));
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

}

/*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry


 // A [LifecycleOwner] which is always in a [Lifecycle.State.STARTED] state.

    class TestLifeCycleOwner : LifecycleOwner {
        private val registry = LifecycleRegistry(this)

        init {
            registry.markState(Lifecycle.State.STARTED)
        }

        override fun getLifecycle(): Lifecycle = registry
    }
*/

