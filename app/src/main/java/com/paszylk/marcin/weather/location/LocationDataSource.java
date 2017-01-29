package com.paszylk.marcin.weather.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class LocationDataSource implements LocationDataSourceInterface {

    private static final float DESIRED_ACCURACY = 100; //accuracy in meters
    private static final long DESIRED_TIME_SPAN = TimeUnit.MINUTES.toMillis(5);
    private static final long LOCATION_ACQUISITION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    private static final int LOCATION_REFRESH_TIME = 200;   //refresh time in milliseconds
    private static final int LOCATION_REFRESH_DISTANCE = 10;    //distance in meters

    private final Context context;

    private final LocationManager locationManager;

    private CurrentLocationListener locationListener;

    private boolean locationAcquired;

    private synchronized boolean isLocationAcquired() {
        return locationAcquired;
    }

    private synchronized void setLocationAcquired(boolean locationAcquired) {
        this.locationAcquired = locationAcquired;
    }
    public LocationDataSource(Context context){
        this.context = context;
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void getLocation(@NonNull final GetLocationCallback callback) {
        try{
            for (String provider : locationManager.getAllProviders()) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
                if(lastKnownLocation != null && isGoodEnough(lastKnownLocation)){
                    //cached location is good enough
                    callback.onLocationAcquired(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    return;
                }
            }
            locationListener = new CurrentLocationListener(callback);
            //try to get fresh location
            for (String provider : locationManager.getAllProviders()) {
                locationManager.requestLocationUpdates(provider,
                        LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE,
                        locationListener, Looper.getMainLooper());

            }
            //set location acquisition timeout
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryToRemoveLocationCallback();
                    if(!isLocationAcquired()) {
                        callback.onLocationUnavailable();
                    }
                }
            }, LOCATION_ACQUISITION_TIMEOUT);
        }catch (SecurityException e){
            callback.onLocationUnavailable();
        }

    }

    private void tryToRemoveLocationCallback(){
        try {
            locationManager.removeUpdates(locationListener);
        }catch (SecurityException e){
            Log.w(getClass().getSimpleName(), "GPS permission rejected.");
        }
    }
    @Override
    public void checkProvidersAvailability(@NonNull CheckProvidersAvailabilityCallback callback) {
        int locationMode = Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
        );
        //check if Settings -> Location is ON
        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            callback.onProviderUnavailable();
            return;
        }
        //check if NETWORK and/or GPS provider is selected
        if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            callback.onProviderAvailable();
        }else {
            callback.onProviderUnavailable();
        }
    }


    private class CurrentLocationListener implements LocationListener{

        private final GetLocationCallback callback;

        private CurrentLocationListener(@NonNull final GetLocationCallback callback){
            this.callback = callback;
        }

        @Override
        public void onLocationChanged(Location location) {
            if(isGoodEnough(location)){
                setLocationAcquired(true);
                tryToRemoveLocationCallback();
                callback.onLocationAcquired(location.getLatitude(), location.getLongitude());
            }
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
    }

    private boolean isGoodEnough(Location location){
        return isFromRealProvider(location) && isFresh(location) && isAccurate(location);
    }

    private boolean isFromRealProvider(Location location){
        return !location.isFromMockProvider();
    }

    private boolean isAccurate(Location location){
        return location.getAccuracy() <= DESIRED_ACCURACY;
    }

    private boolean isFresh(Location location){
        return System.currentTimeMillis() <= location.getTime() + DESIRED_TIME_SPAN;
    }
}
