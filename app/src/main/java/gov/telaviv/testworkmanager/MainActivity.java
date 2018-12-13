package gov.telaviv.testworkmanager;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private final static String GPS_WORK_TAG = "gps_work";
    public final static String UUID_TAG = "uuid_tag";
    public final static String POINTS_LOG = "points.log";

    private TextView logTv;

    private Button startButton;
    private Button stopButton;

    private UUID uniqueWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTv = findViewById(R.id.logTextView);
        logTv.setText(AppUtils.getPreferences(getApplicationContext(), POINTS_LOG));
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        startButton.setOnClickListener(startButtonListener);
        stopButton.setOnClickListener(stopButtonListener);
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
                                enableGpsTracking();
                            }
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                        }
                    })
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).check();
        } else {
            enableGpsTracking();
        }
    }

    private void enableGpsTracking() {
        String uuuidStr = AppUtils.getPreferences(getApplicationContext(), UUID_TAG);
        if (!TextUtils.isEmpty(uuuidStr))
            uniqueWorkRequest = UUID.fromString(uuuidStr);

        if (uniqueWorkRequest == null) {
            startButton.setEnabled(true);
        }
        else {

            observeWork();
        }

    }

    private void observeWork()  {

        WorkManager.getInstance().getWorkInfoByIdLiveData(uniqueWorkRequest).observe(ProcessLifecycleOwner.get(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    Log.i("WorkInfoById", "WorkInfo 2 State=" + workInfo.getState().name());
                    if (workInfo.getState().isFinished()) {
                        if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(MainActivity.this, "Finished state: " + workInfo.getState().name(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private View.OnClickListener startButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (uniqueWorkRequest == null) {

                PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(GpsWorker.class, 1, TimeUnit.MINUTES)
                        .addTag(GPS_WORK_TAG)
                        .build();

                WorkManager.getInstance().enqueue(myWorkRequest);

                uniqueWorkRequest = myWorkRequest.getId();

                AppUtils.setPreferences(getApplicationContext(), UUID_TAG, uniqueWorkRequest.toString());
                startButton.setEnabled(false);

                observeWork();
            }
        }
    };

    private View.OnClickListener stopButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (uniqueWorkRequest != null) {
                WorkManager.getInstance().cancelAllWorkByTag(GPS_WORK_TAG);
            }
            uniqueWorkRequest = null;
            AppUtils.setPreferences(getApplicationContext(), UUID_TAG, "");
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startButton.setEnabled(true);
            }
        }
    };

}
