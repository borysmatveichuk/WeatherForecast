package net.borkiss.weatherforecast.model;

import java.io.Serializable;

public class Place implements Serializable {

    private static final long serialVersionUID = -5963581202041078412L;

    private String name;
    private int cityId;
    private String country;
    private float longitude;
    private float latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int id) {
        this.cityId = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
