package com.paszylk.marcin.weather.citydetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the city detail screen.
 */
public class CityDetailFragment extends Fragment implements CityDetailContract.View {

    @NonNull
    private static final String ARGUMENT_CITY_ID = "CITY_ID";

    private CityDetailContract.Presenter presenter;

    private TextView cityName;
    private TextView temperature;
    private TextView latitude;
    private TextView longitude;
    private TextView cloudiness;
    private TextView humidity;

    private android.support.v7.widget.GridLayout weatherDetails;

    public static CityDetailFragment newInstance(@Nullable long cityId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_CITY_ID, cityId);
        CityDetailFragment fragment = new CityDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.city_detail_frag, container, false);
        setHasOptionsMenu(true);

        weatherDetails = (android.support.v7.widget.GridLayout) root.findViewById(R.id.weather_details_grid_layout);
        cityName = (TextView) root.findViewById(R.id.city_name);
        temperature = (TextView) root.findViewById(R.id.temperature);
        latitude = (TextView) root.findViewById(R.id.latitude);
        longitude = (TextView) root.findViewById(R.id.longitude);
        cloudiness = (TextView) root.findViewById(R.id.cloudiness);
        humidity = (TextView) root.findViewById(R.id.humidity);
        return root;
    }

    @Override
    public void setPresenter(@NonNull CityDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteCity();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.city_detail_fragment_menu, menu);
    }

    @Override
    public void showWeatherInfo(CityDaoMapper city) {
        cityName.setText(city.getName());
        temperature.setText(String.format(Locale.getDefault(), "%s \u00b0C", city.getTemperatureValue()));
        latitude.setText(String.valueOf(city.getLatitude()));
        longitude.setText(String.valueOf(city.getLongitude()));
        cloudiness.setText(String.format(Locale.getDefault(), "%s%%",city.getCloudsValue()));
        humidity.setText(String.format(Locale.getDefault(), "%s%s", city.getHumidityValue(), city.getHumidityUnit()));

        weatherDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCityDeleted() {
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
