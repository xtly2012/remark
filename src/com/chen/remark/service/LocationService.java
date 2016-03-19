package com.chen.remark.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.chen.remark.receiver.LocationReceiver;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenfayong on 16/3/15.
 */
public class LocationService extends Service {
    private Timer timer = new Timer();
    private Bundle bundle = new Bundle();
    private Intent locationIntent = new Intent();
    private LocationManager locationManager;
    private Criteria criteria = new Criteria();
    private static int minUpdateTime = 0;
    private static int minUpdateDistance = 0;
    private static final String TAG = "LocationService";

    @Override
    public void onCreate() {
        super.onCreate();

        // 获得对Location Manager的引用
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)this.getSystemService(svcName);

        // 指定Location Provider的条件
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        registerListener();
    }

    private void sendLocationChangeBroadcast() {

    }

    private String getLocation(Location location) {
        String locationStr = null;
        if (location != null) {
            locationStr = "Lat:" +location.getLatitude() +", lng:" +location.getLongitude();
        } else {
            locationStr = "no location info";
        }

        return locationStr;
    }

    private void registerListener() {
        String bestProvider = locationManager.getBestProvider(criteria, false);
        String bestAvailableProvider = locationManager.getBestProvider(criteria, true);

        if (bestProvider == null) {
            Log.e(TAG, "No Location providers exist on device.");
        } else if (bestProvider.equals(bestAvailableProvider)) {
            locationManager.requestLocationUpdates(bestAvailableProvider, minUpdateTime, minUpdateDistance,
                    bestAvailableProviderListener);
        } else {
            locationManager.requestLocationUpdates(bestProvider, minUpdateTime, minUpdateDistance,
                    bestProviderListener);
        }

        if (bestAvailableProvider != null) {
            locationManager.requestLocationUpdates(bestAvailableProvider, minUpdateTime, minUpdateDistance,
                    bestAvailableProviderListener);
        } else {
            List<String> allProviders = locationManager.getAllProviders();
            for (String provider : allProviders) {
                locationManager.requestLocationUpdates(provider, 0, 0, bestProviderListener);
                Log.e(TAG, "No Location Providers currently available.");
            }
        }
    }

    private void reactToLocationChange(Location location) {
        // TODO:响应位置变化
        this.bundle.putString("location", this.getLocation(location));
        locationIntent.putExtras(this.bundle);
        locationIntent.setAction(LocationReceiver.LOCATION_CHANGED_ACTION);
        this.sendBroadcast(locationIntent);
    }

    private void unregisterListener() {
        locationManager.removeUpdates(bestProviderListener);
        locationManager.removeUpdates(bestAvailableProviderListener);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterListener();
    }

    private LocationListener bestProviderListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            reactToLocationChange(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private LocationListener bestAvailableProviderListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            reactToLocationChange(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            registerListener();
        }
    };
}