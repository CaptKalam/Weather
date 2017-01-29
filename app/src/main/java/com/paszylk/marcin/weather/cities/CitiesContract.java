package com.paszylk.marcin.weather.cities;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.BasePresenter;
import com.paszylk.marcin.weather.BaseView;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CitiesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showCities(List<CityDaoMapper> cities);

        void showAddCity();

        void showCityDetailsUi(long cityId);

        void showLoadingCitiesError();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadCities(boolean forceUpdate);

        void addNewCity();

        void openCityDetails(@NonNull CityDaoMapper requestedCity);
    }
}
