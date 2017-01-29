package com.paszylk.marcin.weather.source;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;

import java.util.List;

public interface CitiesDataSource {

    interface LoadCitiesCallback {

        void onCitiesLoaded(List<CityDaoMapper> cities);

        void onDataNotAvailable();
    }

    interface GetCityCallback {

        void onCityLoaded(CityDaoMapper city);

        void onDataNotAvailable();
    }

    void getCities(@NonNull LoadCitiesCallback callback);

    void getCities(@NonNull List<Coordinates> coordinates, @NonNull LoadCitiesCallback callback);

    void getCity(@NonNull String cityName, @NonNull GetCityCallback callback);

    void getCity(long cityId, @NonNull GetCityCallback callback);

    void getCity(@NonNull Coordinates coordinates, @NonNull GetCityCallback callback);

    void saveCity(@NonNull CityDaoMapper city);

    void refreshCities();

    void dontRefresh();

    void deleteCity(long cityId);

    void deleteAllCities();
}
