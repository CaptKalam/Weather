package com.paszylk.marcin.weather.cities.domain.model.xmlmapper;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Humidity {

    @XStreamAsAttribute
    private float value;

    @XStreamAsAttribute
    private String unit;

    public float getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Humidity humidity = (Humidity) o;

        return new EqualsBuilder()
                .append(value, humidity.value)
                .append(unit, humidity.unit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(unit)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("unit", unit)
                .toString();
    }
}
