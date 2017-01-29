package com.paszylk.marcin.weather.cities;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.UseCaseHandler;
import com.paszylk.marcin.weather.addcity.AddCityActivity;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.usecase.GetCities;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link CitiesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class CitiesPresenter implements CitiesContract.Presenter {

    private final CitiesContract.View citiesView;
    private final GetCities getCities;

    private boolean mFirstLoad = true;

    private final UseCaseHandler useCaseHandler;

    public CitiesPresenter(@NonNull UseCaseHandler useCaseHandler,
                           @NonNull CitiesContract.View citiesView, @NonNull GetCities getCities) {
        this.useCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        this.citiesView = checkNotNull(citiesView, "citiesView cannot be null!");
        this.getCities = checkNotNull(getCities, "getCity cannot be null!");
        this.citiesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCities(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a city was successfully added, show snackbar
        if (AddCityActivity.REQUEST_ADD_CITY == requestCode
                && Activity.RESULT_OK == resultCode) {
            citiesView.showSuccessfullySavedMessage();
        }
    }

    public void loadCities(boolean forceUpdate) {
        // Network reload on each application start
        loadCities(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link CitiesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadCities(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            citiesView.setLoadingIndicator(true);
        }

        GetCities.RequestValues requestValue = new GetCities.RequestValues(forceUpdate);

        useCaseHandler.execute(getCities, requestValue,
                new UseCase.UseCaseCallback<GetCities.ResponseValue>() {
                    @Override
                    public void onSuccess(GetCities.ResponseValue response) {
                        List<CityDaoMapper> cities = response.getCities();
                        // The view may not be able to handle UI updates anymore
                        if (!citiesView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            citiesView.setLoadingIndicator(false);
                        }

                        processCities(cities);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!citiesView.isActive()) {
                            return;
                        }
                        citiesView.showLoadingCitiesError();
                    }
                });
    }

    private void processCities(List<CityDaoMapper> cities) {
        if (cities.isEmpty()) {
        } else {
            // Show the list of cities
            citiesView.showCities(cities);
        }
    }

    @Override
    public void addNewCity() {
        citiesView.showAddCity();
    }

    @Override
    public void openCityDetails(@NonNull CityDaoMapper requestedCity) {
        checkNotNull(requestedCity, "requestedCity cannot be null!");
        citiesView.showCityDetailsUi(requestedCity.getId());
    }
}
