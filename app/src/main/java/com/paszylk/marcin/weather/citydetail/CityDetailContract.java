package com.paszylk.marcin.weather.citydetail;

import com.paszylk.marcin.weather.BasePresenter;
import com.paszylk.marcin.weather.BaseView;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CityDetailContract {

    interface View extends BaseView<Presenter> {

        void showWeatherInfo(CityDaoMapper city);

        void showCityDeleted();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void deleteCity();
    }
}
