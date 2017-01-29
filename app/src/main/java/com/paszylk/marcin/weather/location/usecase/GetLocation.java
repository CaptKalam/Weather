package com.paszylk.marcin.weather.location.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.location.LocationDataSourceInterface;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetLocation extends UseCase<GetLocation.RequestValues, GetLocation.ResponseValue> {

    private final LocationDataSourceInterface locationDataSource;
    public GetLocation(@NonNull LocationDataSourceInterface locationDataSource){
        this.locationDataSource = checkNotNull(locationDataSource, "locationDataSource cannot be null!");

    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        locationDataSource.getLocation(new LocationDataSourceInterface.GetLocationCallback() {
            @Override
            public void onLocationAcquired(double latitude, double longitude) {
                ResponseValue responseValue = new ResponseValue(latitude, longitude);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onLocationUnavailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        public RequestValues() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final double latitude;
        private final double longitude;

        public ResponseValue(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude(){
            return this.latitude;
        }

        public double getLongitude(){
            return this.longitude;
        }
    }
}
