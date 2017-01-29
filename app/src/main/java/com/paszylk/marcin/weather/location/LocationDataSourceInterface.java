package com.paszylk.marcin.weather.location;

import android.support.annotation.NonNull;

public interface LocationDataSourceInterface {

    void getLocation(@NonNull GetLocationCallback callback);

    void checkProvidersAvailability(@NonNull CheckProvidersAvailabilityCallback callback);

    interface GetLocationCallback {

        void onLocationAcquired(double latitude, double longitude);

        void onLocationUnavailable();
    }

    interface CheckProvidersAvailabilityCallback {
        void onProviderAvailable();

        void onProviderUnavailable();
    }
}
