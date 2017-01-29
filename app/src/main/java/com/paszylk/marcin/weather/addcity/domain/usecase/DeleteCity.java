package com.paszylk.marcin.weather.addcity.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.source.CitiesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteCity extends UseCase<DeleteCity.RequestValues, DeleteCity.ResponseValue> {

    private final CitiesRepository citiesRepository;

    public DeleteCity(@NonNull CitiesRepository citiesRepository) {
        this.citiesRepository = checkNotNull(citiesRepository, "citiesRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        citiesRepository.deleteCity(values.getCityId());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final long cityId;

        public RequestValues(@NonNull long cityId) {
            this.cityId = checkNotNull(cityId, "cityId cannot be null!");
        }

        public long getCityId() {
            return cityId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue { }
}
