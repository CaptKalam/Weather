package com.paszylk.marcin.weather.addcity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the add city screen.
 */
public class AddCityFragment extends Fragment implements AddCityContract.View, View.OnClickListener {

    public static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private AddCityContract.Presenter mPresenter;

    private GridLayout weatherDetails;

    private TextView find_city;

    private TextView cityName;
    private TextView temperature;
    private TextView latitude;
    private TextView longitude;
    private TextView cloudiness;
    private TextView humidity;

    private Button getWeatherByCityName;

    private ImageButton getWeatherByLocation;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    public AddCityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddCityContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_city_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveCity();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_city_frag, container, false);
        weatherDetails = (GridLayout)root.findViewById(R.id.weather_details_grid_layout);

        find_city = (TextView) root.findViewById(R.id.find_city);
        cityName = (TextView) root.findViewById(R.id.city_name);
        temperature = (TextView) root.findViewById(R.id.temperature);
        latitude = (TextView) root.findViewById(R.id.latitude);
        longitude = (TextView) root.findViewById(R.id.longitude);
        cloudiness = (TextView) root.findViewById(R.id.cloudiness);
        humidity = (TextView) root.findViewById(R.id.humidity);


        getWeatherByCityName = (Button) root.findViewById(R.id.get_weather_by_city_button);
        getWeatherByLocation = (ImageButton) root.findViewById(R.id.get_weather_by_location_button);

        getWeatherByCityName.setOnClickListener(this);
        getWeatherByLocation.setOnClickListener(this);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void showCitiesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    public void setFindCity(String findCity) {
        this.find_city.setText(findCity);
    }

    @Override
    public void showWeatherDetails(CityDaoMapper city) {
        cityName.setText(city.getName());
        temperature.setText(String.format(Locale.getDefault(), "%s \u00b0C", city.getTemperatureValue()));
        latitude.setText(String.valueOf(city.getLatitude()));
        longitude.setText(String.valueOf(city.getLongitude()));
        cloudiness.setText(String.format(Locale.getDefault(), "%s%%",city.getCloudsValue()));
        humidity.setText(String.format(Locale.getDefault(), "%s%s", city.getHumidityValue(), city.getHumidityUnit()));

        weatherDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWeatherDetails() {
        weatherDetails.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLocationProviderDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_weather_by_city_button:
                mPresenter.getWeather(find_city.getText().toString());
                break;
            case R.id.get_weather_by_location_button:
                checkPermissions();
                break;
            default:
                Log.d(this.getClass().getSimpleName(), "Unknown button pressed");

        }
    }

    private void checkPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }else{
            mPresenter.getLocation();
        }

    }
}
