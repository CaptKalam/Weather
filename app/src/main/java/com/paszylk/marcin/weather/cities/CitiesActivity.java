package com.paszylk.marcin.weather.cities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.paszylk.marcin.weather.Injection;
import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.util.ActivityUtils;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_act);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CitiesFragment citiesFragment =
                (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (citiesFragment == null) {
            // Create the fragment
            citiesFragment = CitiesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), citiesFragment, R.id.contentFrame);
        }

        // Create the presenter
        new CitiesPresenter(
                Injection.provideUseCaseHandler(),
                citiesFragment,
                Injection.provideGetCities(getApplicationContext())
        );
    }
}
