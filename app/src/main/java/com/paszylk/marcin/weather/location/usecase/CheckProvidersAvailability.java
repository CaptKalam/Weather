package com.paszylk.marcin.weather.location.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.location.LocationDataSourceInterface;

import static com.google.common.base.Preconditions.checkNotNull;

public class CheckProvidersAvailability extends UseCase<CheckProvidersAvailability.RequestValues, CheckProvidersAvailability.ResponseValue> {

    private final LocationDataSourceInterface locationManager;

    public CheckProvidersAvailability(@NonNull LocationDataSourceInterface locationManager){
        this.locationManager = checkNotNull(locationManager, "locationManager cannot be null!");

    }
    @Override
    protected void executeUseCase(RequestValues requestValues) {
        locationManager.checkProvidersAvailability(new LocationDataSourceInterface.CheckProvidersAvailabilityCallback() {
            @Override
            public void onProviderAvailable() {
                getUseCaseCallback().onSuccess(new ResponseValue());
            }

            @Override
            public void onProviderUnavailable() {
                getUseCaseCallback().onError();
            }
        });
    }


    public static final class RequestValues implements UseCase.RequestValues {

        public RequestValues() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}