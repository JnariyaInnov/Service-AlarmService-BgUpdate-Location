package gov.telaviv.testworkmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

//@SuppressLint("RestrictedApi")
public class MyForegroundService extends Service {

    private final static int NOTIFICATION_ID = 445;
    private final static String NOTIFICATION_CHANNEL_ID ="_test_channel_id";
    private final static String NOTIFICATION_CHANNEL_NAME ="_test_channel_name";
    private final static String NOTIFICATION_CHANNEL_DESC ="_test_channel_desc";
    private final static String TAG = MyForegroundService.class.getSimpleName();


    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand" );
        startInForeground();
        if(timer!=null)
            timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "scheduleAtFixedRate ---" );
               startService(new Intent(getApplicationContext(), BackgroundOperationsManagerService.class));
            }
        }, 5000, 60000);
        return START_STICKY;
        // return super.onStartCommand(intent, flags, startId);
    }

    private void startInForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setContentTitle("TEST")
                .setContentText("HELLO")
                .setTicker("TICKER")
                .setContentIntent(pendingIntent);
        Notification notification=builder.build();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        Log.d(TAG, "startForeground ..........." );
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" );
        if(timer!=null)
            timer.cancel();
        timer=null;
    }

}
