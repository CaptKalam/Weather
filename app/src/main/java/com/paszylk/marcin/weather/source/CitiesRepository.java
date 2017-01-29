package com.paszylk.marcin.weather.source;

import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitiesRepository implements CitiesDataSource {

    private static CitiesRepository INSTANCE = null;

    private final CitiesDataSource citiesRemoteDataSource;

    private final CitiesDataSource citiesLocalDataSource;

    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private CitiesRepository(@NonNull CitiesDataSource citiesRemoteDataSource,
                             @NonNull CitiesDataSource citiesLocalDataSource) {
        this.citiesRemoteDataSource = checkNotNull(citiesRemoteDataSource);
        this.citiesLocalDataSource = checkNotNull(citiesLocalDataSource);
    }

    public static CitiesRepository getInstance(CitiesDataSource citiesRemoteDataSource,
                                               CitiesDataSource citiesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRepository(citiesRemoteDataSource, citiesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Gets cities from local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadCitiesCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getCities(@NonNull final LoadCitiesCallback callback) {
        checkNotNull(callback);

        citiesLocalDataSource.getCities(new LoadCitiesCallback() {
            @Override
            public void onCitiesLoaded(final List<CityDaoMapper> cities) {
                if(mCacheIsDirty){
                    getCitiesFromRemoteDataSource(getCoordinates(cities), new LoadCitiesCallback() {
                        @Override
                        public void onCitiesLoaded(List<CityDaoMapper> cities) {
                            callback.onCitiesLoaded(cities);
                        }

                        @Override
                        public void onDataNotAvailable() {
                            callback.onCitiesLoaded(cities);
                        }
                    });
                }else {
                    callback.onCitiesLoaded(cities);
                }
            }

            @Override
            public void onDataNotAvailable() {
                callback.onCitiesLoaded(Collections.EMPTY_LIST);
            }
        });
    }

    @Override
    public void getCities(@NonNull List<Coordinates> coordinates, @NonNull LoadCitiesCallback callback) {

    }

    private List<Coordinates> getCoordinates(List<CityDaoMapper> cities) {
        List<Coordinates> coordinates = new ArrayList<>(cities.size());
        for (CityDaoMapper city : cities) {
            coordinates.add(new Coordinates(city.getLatitude(), city.getLongitude()));
        }
        return coordinates;
    }

    @Override
    public void saveCity(@NonNull CityDaoMapper city) {
        checkNotNull(city);
        citiesRemoteDataSource.saveCity(city);
        citiesLocalDataSource.saveCity(city);
    }
    /**
     * Gets cities from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source.
     * <p>
     * Note: {@link LoadCitiesCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getCity(@NonNull final String cityName, @NonNull final GetCityCallback callback) {
        checkNotNull(cityName);
        checkNotNull(callback);
        // Is the city in the local data source? If not, query the network.
        citiesLocalDataSource.getCity(cityName, new GetCityCallback() {
            @Override
            public void onCityLoaded(CityDaoMapper city) {
                callback.onCityLoaded(city);
            }

            @Override
            public void onDataNotAvailable() {
                citiesRemoteDataSource.getCity(cityName, new GetCityCallback() {
                    @Override
                    public void onCityLoaded(CityDaoMapper city) {
                        callback.onCityLoaded(city);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getCity(final long cityId, @NonNull final GetCityCallback callback) {
        checkNotNull(cityId);
        checkNotNull(callback);
        // Is the city in the local data source? If not, query the network.
        citiesLocalDataSource.getCity(cityId, new GetCityCallback() {
            @Override
            public void onCityLoaded(CityDaoMapper city) {
                callback.onCityLoaded(city);
            }

            @Override
            public void onDataNotAvailable() {
                citiesRemoteDataSource.getCity(cityId, new GetCityCallback() {
                    @Override
                    public void onCityLoaded(CityDaoMapper city) {
                        callback.onCityLoaded(city);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getCity(final @NonNull Coordinates coordinates, @NonNull final GetCityCallback callback) {
        checkNotNull(callback);
        checkNotNull(coordinates);
        // Is the city in the local data source? If not, query the network.
        citiesLocalDataSource.getCity(coordinates, new GetCityCallback() {
            @Override
            public void onCityLoaded(CityDaoMapper city) {
                callback.onCityLoaded(city);
            }

            @Override
            public void onDataNotAvailable() {
                citiesRemoteDataSource.getCity(coordinates, new GetCityCallback() {
                    @Override
                    public void onCityLoaded(CityDaoMapper city) {
                        callback.onCityLoaded(city);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshCities() {
        mCacheIsDirty = true;
    }

    @Override
    public void dontRefresh() {
        mCacheIsDirty = false;
    }

    @Override
    public void deleteCity(long cityId) {
        citiesRemoteDataSource.deleteCity(checkNotNull(cityId));
        citiesLocalDataSource.deleteCity(checkNotNull(cityId));
    }

    @Override
    public void deleteAllCities() {
        citiesRemoteDataSource.deleteAllCities();
        citiesLocalDataSource.deleteAllCities();
    }

    private void getCitiesFromRemoteDataSource(List<Coordinates> coordinates, @NonNull final LoadCitiesCallback callback) {
        citiesRemoteDataSource.getCities(coordinates, new LoadCitiesCallback() {
            @Override
            public void onCitiesLoaded(List<CityDaoMapper> cities) {
                refreshLocalDataSource(cities);
                callback.onCitiesLoaded(cities);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<CityDaoMapper> cities) {
        citiesLocalDataSource.deleteAllCities();
        for (CityDaoMapper city : cities) {
            citiesLocalDataSource.saveCity(city);
        }
    }
}
