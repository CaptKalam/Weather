package com.paszylk.marcin.weather.addcity.domain.usecase;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetCity extends UseCase<GetCity.RequestValues, GetCity.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public GetCity(@NonNull CitiesRepository citiesRepository) {
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if(TextUtils.isEmpty(values.getCityName())) {
            citiesRepository.getCity(values.getCityId(), getCallback());
        } else {
            citiesRepository.getCity(values.getCityName(), getCallback());
        }
    }

    @NonNull
    private CitiesDataSource.GetCityCallback getCallback() {
        return new CitiesDataSource.GetCityCallback() {
            @Override
            public void onCityLoaded(CityDaoMapper city) {
                if (city != null) {
                    ResponseValue responseValue = new ResponseValue(city);
                    getUseCaseCallback().onSuccess(responseValue);
                } else {
                    getUseCaseCallback().onError();
                }
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        };
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private long cityId;
        private String cityName;

        public RequestValues(@NonNull long cityId) {
            this.cityId = checkNotNull(cityId, "cityId cannot be null!");
        }

        public RequestValues(@NonNull String cityName){
            this.cityName = checkNotNull(cityName, "cityName cannot be null!");
        }

        public long getCityId() {
            return cityId;
        }
        public String getCityName(){
            return cityName;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private CityDaoMapper city;

        public ResponseValue(@NonNull CityDaoMapper city) {
            this.city = checkNotNull(city, "city cannot be null!");
        }

        public CityDaoMapper getCity() {
            return city;
        }
    }
}
