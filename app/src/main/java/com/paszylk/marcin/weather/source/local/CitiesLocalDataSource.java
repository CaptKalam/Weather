package com.paszylk.marcin.weather.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.paszylk.marcin.weather.source.CitiesDataSource;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapperDao;
import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.paszylk.marcin.weather.WeatherApplication.getDaoSession;

/**
 * Concrete implementation of a data source as a db.
 */
public class CitiesLocalDataSource implements CitiesDataSource {

    private static CitiesLocalDataSource INSTANCE;

    private CitiesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);

    }

    public static CitiesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadCitiesCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getCities(@NonNull LoadCitiesCallback callback) {
        List<CityDaoMapper> cities = getDaoSession().getCityDaoMapperDao().loadAll();
        if (cities.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onCitiesLoaded(cities);
        }

    }

    @Override
    public void getCities(@NonNull List<Coordinates> coordinates, @NonNull LoadCitiesCallback callback) {
        //no need to implement it.
    }

    /**
     * Note: {@link GetCityCallback#onDataNotAvailable()} is fired if the {@link CityDaoMapper} isn't
     * found.
     */
    @Override
    public void getCity(@NonNull String cityName, @NonNull GetCityCallback callback) {
        List<CityDaoMapper> cities = getDaoSession().getCityDaoMapperDao().queryBuilder()
                .where(CityDaoMapperDao.Properties.Name.like(cityName)).limit(1).list();

        if(!cities.isEmpty()){
            callback.onCityLoaded(cities.get(0));
        }else{
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getCity(long cityId, @NonNull GetCityCallback callback) {
        List<CityDaoMapper> cities = getDaoSession().getCityDaoMapperDao().queryBuilder()
                .where(CityDaoMapperDao.Properties.Id.eq(cityId))
                .limit(1)
                .list();
        if(!cities.isEmpty()){
            callback.onCityLoaded(cities.get(0));
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getCity(@NonNull Coordinates coordinates, @NonNull GetCityCallback callback) {
        List<CityDaoMapper> cities = getDaoSession().getCityDaoMapperDao().queryBuilder()
                .where(CityDaoMapperDao.Properties.Latitude.eq(coordinates.getLatitude()),
                        CityDaoMapperDao.Properties.Longitude.eq(coordinates.getLongitude()))
                .limit(1)
                .list();
        if(!cities.isEmpty()){
            callback.onCityLoaded(cities.get(0));
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveCity(@NonNull CityDaoMapper city) {
        checkNotNull(city);
        QueryBuilder<CityDaoMapper> queryBuilder =  getDaoSession().getCityDaoMapperDao().queryBuilder();
        queryBuilder.where(CityDaoMapperDao.Properties.Name.eq(city.getName()),
                           CityDaoMapperDao.Properties.Name.notEq("")).limit(1);

        List<CityDaoMapper> cities = queryBuilder.list();
        if(!cities.isEmpty()){
            city.setId(cities.get(0).getId());
        }
        getDaoSession().getCityDaoMapperDao().insertOrReplace(city);
    }


    @Override
    public void refreshCities() {
        // Not required because the {@link CitiesRepository} handles the logic of refreshing the
        // cities from all the available data sources.
    }

    @Override
    public void dontRefresh() {
        // Not required because the {@link CitiesRepository} handles the logic of refreshing the
        // cities from all the available data sources.
    }

    @Override
    public void deleteCity(long cityId) {
        getDaoSession().getCityDaoMapperDao().deleteByKey(cityId);
    }

    @Override
    public void deleteAllCities() {
        getDaoSession().getCityDaoMapperDao().deleteAll();
    }
}
