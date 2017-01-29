package com.paszylk.marcin.weather.addcity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.paszylk.marcin.weather.Injection;
import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.util.ActivityUtils;

import static com.paszylk.marcin.weather.addcity.AddCityFragment.ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE;

/**
 * Displays an add city screen.
 */
public class AddCityActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CITY = 1;

    private AddCityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        AddCityFragment addCityFragment =
                (AddCityFragment) getSupportFragmentManager().findFragmentById(
                        R.id.contentFrame);

        if (addCityFragment == null) {
            addCityFragment = AddCityFragment.newInstance();

            if(actionBar != null){
                actionBar.setTitle(R.string.add_city);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addCityFragment, R.id.contentFrame);
        }

        // Create the presenter
        presenter = new AddCityPresenter(Injection.provideUseCaseHandler(),
                addCityFragment,
                Injection.provideGetWeatherByCityName(getApplicationContext()),
                Injection.provideGetWeatherByLocation(getApplicationContext()),
                Injection.provideSaveCity(getApplicationContext()),
                Injection.provideGetLocation(getApplicationContext()),
                Injection.providersAvailability(getApplicationContext())
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 1
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                presenter.getLocation();
            }else{
                Toast.makeText(this, "Please grant permission to this feature.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
