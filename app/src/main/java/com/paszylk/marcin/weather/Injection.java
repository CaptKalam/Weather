package com.paszylk.marcin.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.addcity.domain.usecase.DeleteCity;
import com.paszylk.marcin.weather.addcity.domain.usecase.GetCity;
import com.paszylk.marcin.weather.addcity.domain.usecase.SaveCity;
import com.paszylk.marcin.weather.location.LocationDataSource;
import com.paszylk.marcin.weather.location.LocationDataSourceInterface;
import com.paszylk.marcin.weather.location.usecase.CheckProvidersAvailability;
import com.paszylk.marcin.weather.location.usecase.GetLocation;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.source.local.CitiesLocalDataSource;
import com.paszylk.marcin.weather.source.remote.CitiesRemoteDataSource;
import com.paszylk.marcin.weather.cities.domain.usecase.GetCities;
import com.paszylk.marcin.weather.cities.domain.usecase.GetWeatherByCityName;
import com.paszylk.marcin.weather.cities.domain.usecase.GetWeatherByLocation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link CitiesDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    private static CitiesRepository provideCityRepositories(@NonNull Context context) {
        checkNotNull(context);
        return CitiesRepository.getInstance(CitiesRemoteDataSource.getInstance(),
                CitiesLocalDataSource.getInstance(context));
    }

    private static LocationDataSourceInterface provideLocationManager(@NonNull Context context){
        checkNotNull(context);
        return new LocationDataSource(context);
    }

    public static GetCities provideGetCities(@NonNull Context context) {
        return new GetCities(provideCityRepositories(context));
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static GetCity provideGetCity(@NonNull Context context) {
        return new GetCity(Injection.provideCityRepositories(context));
    }

    public static SaveCity provideSaveCity(@NonNull Context context) {
        return new SaveCity(Injection.provideCityRepositories(context));
    }

    public static DeleteCity provideDeleteCity(@NonNull Context context) {
        return new DeleteCity(Injection.provideCityRepositories(context));
    }

    public static GetWeatherByCityName provideGetWeatherByCityName(@NonNull Context context){
        return new GetWeatherByCityName(Injection.provideCityRepositories(context));
    }

    public static GetWeatherByLocation provideGetWeatherByLocation(@NonNull Context context){
        return new GetWeatherByLocation(Injection.provideCityRepositories(context));
    }
    public static GetLocation provideGetLocation(@NonNull Context context){
        return new GetLocation(Injection.provideLocationManager(context));
    }

    public static CheckProvidersAvailability providersAvailability(@NonNull Context context){
        return new CheckProvidersAvailability(Injection.provideLocationManager(context));
    }
}
