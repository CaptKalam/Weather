package com.paszylk.marcin.weather.source.remote;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.source.exceptions.DownloadWeatherException;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;
import com.paszylk.marcin.weather.weatherapirequests.WeatherMapUrl;
import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.City;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CitiesRemoteDataSource implements CitiesDataSource {

    private static CitiesRemoteDataSource INSTANCE;

    public static CitiesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private CitiesRemoteDataSource() {}

    @Override
    public void getCities(final @NonNull LoadCitiesCallback callback) {
    }

    @Override
    public void getCities(@NonNull List<Coordinates> coordinates, final @NonNull LoadCitiesCallback callback){
        if(coordinates == null || coordinates.isEmpty()){
            callback.onDataNotAvailable();
            return;
        }

        List<CityDaoMapper> cities = new ArrayList<>(coordinates.size());
        for (Coordinates coordinate :
                coordinates) {
            try {
                CityDaoMapper city = downloadWeather(WeatherMapUrl.Builder()
                        .coordinates(coordinate).build());
                cities.add(city);
            } catch (DownloadWeatherException e) {
                callback.onDataNotAvailable();
                return;
            }
        }
        callback.onCitiesLoaded(cities);
    }

    @Override
    public void getCity(@NonNull String cityName, final @NonNull GetCityCallback callback) {
        try {
            CityDaoMapper city = downloadWeather(WeatherMapUrl.Builder().city(cityName).build());
            callback.onCityLoaded(city);
        } catch (DownloadWeatherException e) {
            callback.onDataNotAvailable();
        }
    }

    private CityDaoMapper mapToDaoObject(City city) {
        return new CityDaoMapper(
                city.getLocation().getName(),
                city.getLocation().getCoordinates().getLatitude(),
                city.getLocation().getCoordinates().getLongitude(),
                city.getTemperature().getValue(),
                city.getHumidity().getValue(),
                city.getHumidity().getUnit(),
                city.getClouds().getValue()
        );
    }

    @Override
    public void getCity(@NonNull long cityId, @NonNull GetCityCallback callback) {
        //nothing to do here
    }

    @Override
    public void getCity(@NonNull Coordinates coordinates, @NonNull GetCityCallback callback) {
        try {
            CityDaoMapper city = downloadWeather(WeatherMapUrl.Builder().coordinates(coordinates).build());
            callback.onCityLoaded(city);
        } catch (DownloadWeatherException e) {
            callback.onDataNotAvailable();
        }
    }

    private CityDaoMapper downloadWeather(@NonNull String url) throws DownloadWeatherException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            City city = (City)getMapper().fromXML(response.body().byteStream());
            return mapToDaoObject(city);
        } catch (IOException | StreamException e) {
            throw new DownloadWeatherException(e);
        }
    }

    @NonNull
    private XStream getMapper() {
        XStream xStream = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        //skip unknown fields
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xStream.processAnnotations(City.class);
        xStream.autodetectAnnotations(true);
        return xStream;
    }

    @Override
    public void saveCity(@NonNull CityDaoMapper city) {

    }

    @Override
    public void refreshCities() {
    }

    @Override
    public void dontRefresh() {
    }

    @Override
    public void deleteCity(@NonNull long cityId) {
    }

    @Override
    public void deleteAllCities() {

    }
}
