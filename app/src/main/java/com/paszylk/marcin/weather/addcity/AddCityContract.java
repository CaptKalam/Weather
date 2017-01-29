package com.paszylk.marcin.weather.addcity;

import com.paszylk.marcin.weather.BasePresenter;
import com.paszylk.marcin.weather.BaseView;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddCityContract {

    interface View extends BaseView<Presenter> {

        void showCitiesList();

        void setFindCity(String findCity);

        void showWeatherDetails(CityDaoMapper city);

        void hideWeatherDetails();

        boolean isActive();

        void showLocationProviderDialog();
    }

    interface Presenter extends BasePresenter {

        void saveCity();

        void getWeather(String city);

        void getLocation();
    }
}
