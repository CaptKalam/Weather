package com.paszylk.marcin.weather.citydetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.paszylk.marcin.weather.Injection;
import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.util.ActivityUtils;

/**
 * Displays city details screen.
 */
public class CityDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CITY_ID = "CITY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.city_detail_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        // Get the requested city id
        long cityId = getIntent().getLongExtra(EXTRA_CITY_ID, -1);

        CityDetailFragment cityDetailFragment = (CityDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (cityDetailFragment == null) {
            cityDetailFragment = CityDetailFragment.newInstance(cityId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    cityDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new CityDetailPresenter(
                Injection.provideUseCaseHandler(),
                cityId,
                cityDetailFragment,
                Injection.provideGetCity(getApplicationContext()),
                Injection.provideDeleteCity(getApplicationContext()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
