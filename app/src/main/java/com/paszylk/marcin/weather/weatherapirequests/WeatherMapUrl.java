package com.paszylk.marcin.weather.weatherapirequests;


import com.paszylk.marcin.weather.cities.domain.model.xmlmapper.Coordinates;

import static com.paszylk.marcin.weather.WeatherApplication.OPEN_WEATHER_MAP_APP_ID;

public class WeatherMapUrl {

    private static final String RESPONSE_FORMAT = "xml"; // formats: 'xml', 'html', 'json'

    private static final String UNIT_SYSTEM = "metric";    // 'metric', 'imperial' or standard if not defined

    private static final String CITY_SELECTION_MODE = "accurate";   //'like' - close result, 'accurate' - accurate result

    private StringBuilder builder;

    private WeatherMapUrl(){
        builder = new StringBuilder("http://api.openweathermap.org/data/2.5/weather")
                .append("?mode=").append(RESPONSE_FORMAT)
                .append("&units=").append(UNIT_SYSTEM)
                .append("&type=").append(CITY_SELECTION_MODE)
                .append("&APPID=").append(OPEN_WEATHER_MAP_APP_ID);
    }

    public static WeatherMapUrl Builder(){
        return new WeatherMapUrl();
    }

    public WeatherMapUrl city(String cityName){
        builder.append("&q=").append(cityName);
        return this;
    }

    public WeatherMapUrl coordinates(float latitude, float longitude){
        builder.append("&lat=").append(latitude).append("&lon=").append(longitude);
        return this;
    }

    public WeatherMapUrl coordinates(Coordinates coordinates){
        return this.coordinates(coordinates.getLatitude(), coordinates.getLongitude());
    }

    public String build(){
        return builder.toString();
    }
}
