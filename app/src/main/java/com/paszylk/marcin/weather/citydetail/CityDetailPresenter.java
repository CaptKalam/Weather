package com.paszylk.marcin.weather.citydetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.UseCaseHandler;
import com.paszylk.marcin.weather.addcity.domain.usecase.DeleteCity;
import com.paszylk.marcin.weather.addcity.domain.usecase.GetCity;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CityDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class CityDetailPresenter implements CityDetailContract.Presenter {

    private final CityDetailContract.View cityDetailView;
    private final UseCaseHandler useCaseHandler;
    private final GetCity getCity;
    private final DeleteCity deleteCity;

    @Nullable
    private long cityId;

    public CityDetailPresenter(@NonNull UseCaseHandler useCaseHandler,
                               @Nullable long cityId,
                               @NonNull CityDetailContract.View cityDetailView,
                               @NonNull GetCity getCity,
                               @NonNull DeleteCity deleteCity) {
        this.cityId = cityId;
        this.useCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null!");
        this.cityDetailView = checkNotNull(cityDetailView, "cityDetailView cannot be null!");
        this.getCity = checkNotNull(getCity, "getCity cannot be null!");
        this.deleteCity = checkNotNull(deleteCity, "deleteCity cannot be null!");
        this.cityDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openCity();
    }

    private void openCity() {
        useCaseHandler.execute(getCity, new GetCity.RequestValues(cityId),
                new UseCase.UseCaseCallback<GetCity.ResponseValue>() {
                    @Override
                    public void onSuccess(GetCity.ResponseValue response) {
                        CityDaoMapper city = response.getCity();

                        // The view may not be able to handle UI updates anymore
                        if (!cityDetailView.isActive()) {
                            return;
                        }
                        showWeatherInfo(city);
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public void deleteCity() {
        useCaseHandler.execute(deleteCity, new DeleteCity.RequestValues(cityId),
                new UseCase.UseCaseCallback<DeleteCity.ResponseValue>() {
                    @Override
                    public void onSuccess(DeleteCity.ResponseValue response) {
                        cityDetailView.showCityDeleted();
                    }

                    @Override
                    public void onError() {
                        // Show error, log, etc.
                    }
                });
    }


    private void showWeatherInfo(@NonNull CityDaoMapper city) {
        if(cityDetailView.isActive()) {
            cityDetailView.showWeatherInfo(city);
        }
    }
}
