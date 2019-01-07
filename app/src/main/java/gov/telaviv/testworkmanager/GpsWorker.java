package gov.telaviv.testworkmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

//import com.google.common.util.concurrent.ListenableFuture;

import java.util.Calendar;

//import androidx.work.ListenableWorker;
//import androidx.work.WorkerParameters;
//import androidx.work.impl.utils.futures.SettableFuture;

public class GpsWorker /*extends ListenableWorker*/ {

//    private final static String TAG = GpsWorker.class.getSimpleName();
//    private LocationManager mLocationManager;
//    private SettableFuture<Result> future;
//
//    public GpsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @SuppressLint("RestrictedApi")
//    @NonNull
//    @Override
//    public ListenableFuture<Result> startWork() {
//        future = SettableFuture.create();
//        Log.i(TAG, "startWork Thread =" + Thread.currentThread().getId());
//        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            if(isProviderEnabled(getApplicationContext())) {
//
//                if(mLocationManager == null)
//                    mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//                Log.i(TAG, "startWork  ==== LocationManager ====" );
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
//            }
//            else  {
//                future.set(Result.failure());
//            }
//        } else {
//
//            future.set(Result.failure());
//
//        }
//        return future;
//    }
//
//    @Override
//    public void onStopped() {
//        super.onStopped();
//        Log.i(TAG, "onStopped" );
//        mLocationManager.removeUpdates(mLocationListener);
//    }
//
//    static boolean isProviderEnabled(Context context) {
//        Log.i(TAG, "isProviderEnabled" );
//        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
//                    && lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
//    }
//
//    @SuppressLint("RestrictedApi")
//    private LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.i(TAG, "Update GPS Location: accuracy="+location.getAccuracy());
//            mLocationManager.removeUpdates(mLocationListener);
//            String str = String.format("%f : %f | %s\n", location.getLatitude(),
//            location.getLongitude(), AppUtils.dateFormat(Calendar.getInstance().getTime(), "dd/MM HH:mm"));
//            Log.i(TAG, str);
//            String log = AppUtils.getPreferences(GpsWorker.this.getApplicationContext(),MainActivity.POINTS_LOG);
//            AppUtils.setPreferences(GpsWorker.this.getApplicationContext(), MainActivity.POINTS_LOG, str+log);
//
//            future.set(Result.success());
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
//            Log.d(TAG, "onStatusChanged: "+s);
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//            Log.d(TAG, "onProviderEnabled: "+s);
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//            Log.i(TAG, "onProviderDisabled: "+s);
//            mLocationManager.removeUpdates(mLocationListener);
//            future.set(Result.failure());
//        }
//    };

}
