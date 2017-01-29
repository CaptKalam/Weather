package com.paszylk.marcin.weather.cities.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class CityDaoMapper {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private float latitude;

    private float longitude;

    private float temperatureValue;

    private float humidityValue;

    private String humidityUnit;

    private float cloudsValue;

    @Keep
    public CityDaoMapper(Long id, String name, float latitude, float longitude,
            float temperatureValue, float humidityValue, String humidityUnit,
            float cloudsValue) {
        this(name, latitude, longitude, temperatureValue, humidityValue, humidityUnit, cloudsValue);
        this.id = id;
    }

    public CityDaoMapper(String name, float latitude, float longitude,
                         float temperatureValue, float humidityValue, String humidityUnit,
                         float cloudsValue) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperatureValue = temperatureValue;
        this.humidityValue = humidityValue;
        this.humidityUnit = humidityUnit;
        this.cloudsValue = cloudsValue;
    }

    @Generated(hash = 1299689741)
    public CityDaoMapper() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CityDaoMapper that = (CityDaoMapper) o;

        return new EqualsBuilder()
                .append(latitude, that.latitude)
                .append(longitude, that.longitude)
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("temperatureValue", temperatureValue)
                .append("humidityValue", humidityValue)
                .append("humidityUnit", humidityUnit)
                .append("cloudsValue", cloudsValue)
                .toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getTemperatureValue() {
        return this.temperatureValue;
    }

    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public float getHumidityValue() {
        return this.humidityValue;
    }

    public void setHumidityValue(float humidityValue) {
        this.humidityValue = humidityValue;
    }

    public String getHumidityUnit() {
        return this.humidityUnit;
    }

    public void setHumidityUnit(String humidityUnit) {
        this.humidityUnit = humidityUnit;
    }

    public float getCloudsValue() {
        return this.cloudsValue;
    }

    public void setCloudsValue(float cloudsValue) {
        this.cloudsValue = cloudsValue;
    }
}
