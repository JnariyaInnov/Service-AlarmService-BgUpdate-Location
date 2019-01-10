package gov.telaviv.testworkmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.Calendar;

public class BackgroundOperationsManagerService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private final static String TAG = BackgroundOperationsManagerService.class.getSimpleName();
    private LocationManager mLocationManager;
    final LocationRequest mLocationRequest = LocationRequest.create();
    private GoogleApiClient mGoogleApiClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand" );

        buildGoogleApiClient();
        MainActivity.setAlarm(getApplicationContext(), true);

        return  super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" );

        disconnectGoogleApiClient();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void buildGoogleApiClient() {

        Log.d(TAG, "buildGoogleApiClient # connect");

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();
    }

    private void disconnectGoogleApiClient() {
        if(mGoogleApiClient!=null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()))
            mGoogleApiClient.disconnect();
    }

    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Update GPS Location: accuracy="+location.getAccuracy());


        String str = String.format("%f : %f | %s\n",  location.getLatitude(),
                location.getLongitude(), AppUtils.dateFormat(Calendar.getInstance().getTime(), "dd/MM HH:mm"));
        Log.i(TAG, str);
        String log = AppUtils.getPreferences(getApplicationContext(),MainActivity.POINTS_LOG);
        AppUtils.setPreferences(getApplicationContext(), MainActivity.POINTS_LOG, str+log);
        stopSelf();
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
        if(mLocationManager!=null)
            mLocationManager.removeUpdates(this);
        stopSelf();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        boolean permissions = checkPermissions();
        Log.i(TAG, "onConnected: permissions="+permissions);
        if(mGoogleApiClient!=null && permissions) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i(TAG, "onConnected: === getLastLocation ===");
            onLocationChanged(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: "+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: "+connectionResult.getErrorMessage());
        stopSelf();
    }
}
