package com.paszylk.marcin.weather.cities.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetWeatherByLocation extends UseCase<GetWeatherByLocation.RequestValues, WeatherResponseValue.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public GetWeatherByLocation(@NonNull CitiesRepository citiesRepository){
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        citiesRepository.getCity(requestValues.getCoordinates(), new CitiesDataSource.GetCityCallback() {
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

        private final Coordinates coordinates;

        public RequestValues(@NonNull Coordinates coordinates) {
            this.coordinates = checkNotNull(coordinates, "coordinates cannot be null!");;
        }

        public Coordinates getCoordinates(){
            return coordinates;
        }
    }
}

