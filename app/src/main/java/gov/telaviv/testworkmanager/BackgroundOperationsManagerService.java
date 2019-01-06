package gov.telaviv.testworkmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Calendar;

import androidx.work.ListenableWorker;

public class BackgroundOperationsManagerService extends Service {


    private final static String TAG = BackgroundOperationsManagerService.class.getSimpleName();
    private LocationManager mLocationManager;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if(isProviderEnabled(getApplicationContext())) {


                if(mLocationManager == null)
                    mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Log.i(TAG, "startWork  ==== LocationManager ====" );
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @SuppressLint("RestrictedApi")
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Update GPS Location: accuracy="+location.getAccuracy());
            mLocationManager.removeUpdates(mLocationListener);
            String str = String.format("%f : %f | %s\n", location.getLatitude(),
                    location.getLongitude(), AppUtils.dateFormat(Calendar.getInstance().getTime(), "dd/MM HH:mm"));
            Log.i(TAG, str);
            String log = AppUtils.getPreferences(BackgroundOperationsManagerService.this.getApplicationContext(),MainActivity.POINTS_LOG);
            AppUtils.setPreferences(BackgroundOperationsManagerService.this.getApplicationContext(), MainActivity.POINTS_LOG, str+log);



        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d(TAG, "onStatusChanged: "+s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d(TAG, "onProviderEnabled: "+s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i(TAG, "onProviderDisabled: "+s);
            mLocationManager.removeUpdates(mLocationListener);
        }
    };
    static boolean isProviderEnabled(Context context) {
        Log.i(TAG, "isProviderEnabled" );
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                && lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }
}
