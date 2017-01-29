package com.paszylk.marcin.weather.addcity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paszylk.marcin.weather.UseCase;
import com.paszylk.marcin.weather.UseCaseHandler;
import com.paszylk.marcin.weather.addcity.domain.usecase.SaveCity;
import com.paszylk.marcin.weather.addcity.exceptions.CoordinatesParseException;
import com.paszylk.marcin.weather.location.usecase.CheckProvidersAvailability;
import com.paszylk.marcin.weather.location.usecase.GetLocation;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;
import com.paszylk.marcin.weather.cities.domain.usecase.GetWeatherByCityName;
import com.paszylk.marcin.weather.cities.domain.usecase.GetWeatherByLocation;
import com.paszylk.marcin.weather.cities.domain.usecase.WeatherResponseValue;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddCityFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public class AddCityPresenter implements AddCityContract.Presenter {

    private static final String COORDINATES_JOINER = ":";

    private final AddCityContract.View addCityView;

    private final GetLocation getLocation;
    private final CheckProvidersAvailability checkProvidersAvailability;
    private final GetWeatherByCityName getWeatherByCityName;
    private final GetWeatherByLocation getWeatherByLocation;
    private final SaveCity saveCity;

    private final UseCaseHandler useCaseHandler;

    @Nullable
    private CityDaoMapper currentlyDownloadedCity;

    public AddCityPresenter(@NonNull UseCaseHandler useCaseHandler,
                            @NonNull AddCityContract.View addCityView,
                            @NonNull GetWeatherByCityName getWeatherByCityName,
                            @NonNull GetWeatherByLocation getWeatherByLocation,
                            @NonNull SaveCity saveCity, @NonNull GetLocation getLocation,
                            @NonNull CheckProvidersAvailability checkProvidersAvailability) {
        this.useCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null!");
        this.addCityView = checkNotNull(addCityView, "addCityView cannot be null!");
        this.getLocation = checkNotNull(getLocation, "getLocation cannot be null!");
        this.checkProvidersAvailability = checkNotNull(checkProvidersAvailability, "checkProvidersAvailability cannot be null!");

        this.getWeatherByCityName = checkNotNull(getWeatherByCityName, "getWeatherByCityName cannot be null!");
        this.getWeatherByLocation = checkNotNull(getWeatherByLocation, "getWeatherByLocation cannot be null!");
        this.saveCity = checkNotNull(saveCity, "saveCity cannot be null!");

        this.addCityView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void saveCity() {
        createCity();
    }

    @Override
    public void getWeather(String city) {
        try{
            Coordinates coordinates = tryToParseCoordinates(city);
            useCaseHandler.execute(getWeatherByLocation,
                    new GetWeatherByLocation.RequestValues(coordinates),
                    weatherCallback);

        } catch (CoordinatesParseException e){
            useCaseHandler.execute(getWeatherByCityName, new GetWeatherByCityName.RequestValues(city),
                    weatherCallback);
        }
    }

    private Coordinates tryToParseCoordinates(String city) throws CoordinatesParseException {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            String[] coordinates = city.split(COORDINATES_JOINER);
            if (coordinates.length == 2) {
                return new Coordinates(numberFormat.parse(coordinates[0]).floatValue(),
                        numberFormat.parse(coordinates[1]).floatValue());
            }
        }catch (ParseException e){
            Log.d(getClass().getSimpleName(), "Couldn't parse coordinates, we're dealing with city name.");
        }
        throw new CoordinatesParseException();
    }

    private final UseCase.UseCaseCallback<WeatherResponseValue.ResponseValue> weatherCallback
            = new UseCase.UseCaseCallback<WeatherResponseValue.ResponseValue>() {
        @Override
        public void onSuccess(WeatherResponseValue.ResponseValue response) {
            currentlyDownloadedCity = response.getCity();
            showCity(currentlyDownloadedCity);
        }

        @Override
        public void onError() {
            currentlyDownloadedCity = null;
            hideWeatherDetails();
            setTitle("location not found");
            Log.w(getClass().getSimpleName(), "Something went wrong");
        }
    };

    @Override
    public void getLocation() {
        useCaseHandler.execute(checkProvidersAvailability, new CheckProvidersAvailability.RequestValues(), new UseCase.UseCaseCallback<CheckProvidersAvailability.ResponseValue>() {
            @Override
            public void onSuccess(CheckProvidersAvailability.ResponseValue response) {
                setTitle("checking your location...");
                useCaseHandler.execute(getLocation, new GetLocation.RequestValues(), new UseCase.UseCaseCallback<GetLocation.ResponseValue>() {
                    @Override
                    public void onSuccess(GetLocation.ResponseValue response) {
                        setTitle(String.format(Locale.getDefault(), "%f%s%f", response.getLatitude(), COORDINATES_JOINER, response.getLongitude()));
                    }

                    @Override
                    public void onError() {
                        setTitle("location not acquired");
                    }
                });
            }

            @Override
            public void onError() {
                if(addCityView.isActive()){
                    addCityView.showLocationProviderDialog();
                }
            }
        });

    }

    private void showCity(CityDaoMapper city) {
        if (addCityView.isActive()) {
            addCityView.setFindCity(city.getName());
            addCityView.showWeatherDetails(city);
        }
    }

    private void hideWeatherDetails(){
        if(addCityView.isActive()){
            addCityView.hideWeatherDetails();
        }
    }
    private void setTitle(String title){
        if(addCityView.isActive()){
            addCityView.setFindCity(title);
        }
    }

    private void createCity() {
        if(currentlyDownloadedCity != null) {
            useCaseHandler.execute(saveCity, new SaveCity.RequestValues(currentlyDownloadedCity),
                    new UseCase.UseCaseCallback<SaveCity.ResponseValue>() {
                        @Override
                        public void onSuccess(SaveCity.ResponseValue response) {
                            addCityView.showCitiesList();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }

    }
}
