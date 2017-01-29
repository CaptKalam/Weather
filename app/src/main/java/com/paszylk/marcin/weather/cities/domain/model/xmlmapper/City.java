package com.paszylk.marcin.weather.cities.domain.model.xmlmapper;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XStreamAlias("current")
public final class City {

    @XStreamAlias("city")
    private Location location;

    private Temperature temperature;

    private Humidity humidity;

    private Clouds clouds;

    public Location getLocation() {
        return location;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public Clouds getClouds() {
        return clouds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return new EqualsBuilder()
                .append(location, city.location)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(location)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("location", location)
                .append("temperature", temperature)
                .append("humidity", humidity)
                .append("clouds", clouds)
                .toString();
    }
}

