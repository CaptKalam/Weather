package com.paszylk.marcin.weather.cities.domain.usecase;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class WeatherResponseValue {

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final CityDaoMapper city;

        public ResponseValue(@NonNull CityDaoMapper city) {
            this.city = checkNotNull(city, "cities cannot be null!");
        }

        public CityDaoMapper getCity() {
            return city;
        }
    }
}
