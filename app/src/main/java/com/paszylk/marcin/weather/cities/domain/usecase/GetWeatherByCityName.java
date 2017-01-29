package com.paszylk.marcin.weather.cities.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetWeatherByCityName extends UseCase<GetWeatherByCityName.RequestValues, WeatherResponseValue.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public GetWeatherByCityName(@NonNull CitiesRepository citiesRepository){
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        citiesRepository.getCity(requestValues.getCityName(), new CitiesDataSource.GetCityCallback() {
            @Override
            public void onCityLoaded(CityDaoMapper city) {
                WeatherResponseValue.ResponseValue responseValue = new WeatherResponseValue.ResponseValue(city);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String cityName;

        public RequestValues(String cityName) {
            this.cityName = cityName;
        }

        public String getCityName(){
            return cityName;
        }
    }
}
