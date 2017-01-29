package com.paszylk.marcin.weather.addcity.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesRepository;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.City;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Updates or creates a new {@link City} in the {@link CitiesRepository}.
 */
public class SaveCity extends UseCase<SaveCity.RequestValues, SaveCity.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public SaveCity(@NonNull CitiesRepository citiesRepository) {
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        CityDaoMapper city = values.getCity();
        citiesRepository.saveCity(city);
        getUseCaseCallback().onSuccess(new ResponseValue(city));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final CityDaoMapper city;

        public RequestValues(@NonNull CityDaoMapper city) {
            this.city = checkNotNull(city, "city cannot be null!");
        }

        public CityDaoMapper getCity() {
            return city;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final CityDaoMapper city;

        public ResponseValue(@NonNull CityDaoMapper city) {
            this.city = checkNotNull(city, "city cannot be null!");
        }

        public CityDaoMapper getCity() {
            return city;
        }
    }
}
