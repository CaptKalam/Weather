package com.paszylk.marcin.weather.cities.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fetches the list of cities.
 */
public class GetCities extends UseCase<GetCities.RequestValues, GetCities.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public GetCities(@NonNull CitiesRepository citiesRepository) {
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            citiesRepository.refreshCities();
        }else {
            citiesRepository.dontRefresh();
        }

        citiesRepository.getCities(new CitiesDataSource.LoadCitiesCallback() {
            @Override
            public void onCitiesLoaded(List<CityDaoMapper> cities) {
                getUseCaseCallback().onSuccess(new ResponseValue(cities));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final boolean forceUpdate;

        public RequestValues(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public boolean isForceUpdate() {
            return forceUpdate;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<CityDaoMapper> cities;

        public ResponseValue(@NonNull List<CityDaoMapper> cities) {
            this.cities = checkNotNull(cities, "cities cannot be null!");
        }

        public List<CityDaoMapper> getCities() {
            return cities;
        }
    }
}
