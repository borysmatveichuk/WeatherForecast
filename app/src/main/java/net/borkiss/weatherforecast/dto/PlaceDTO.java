package net.borkiss.weatherforecast.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceDTO implements Serializable {

    private static final long serialVersionUID = -8396561409012387729L;

    @SerializedName("id")
    private int cityId;
    private String name;

    @SerializedName("coord")
    private Coordinates coordinates;

    @SerializedName("sys")
    private Sys sysInfo;

    public int getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public float getLongitude() {
        return this.coordinates.lon;
    }

    public float getLatitude() {
        return this.coordinates.lat;
    }

    public String getCountry() {
        return this.sysInfo.country;
    }

    private static class Sys {
        String country;
    }

    private class Coordinates {
        float lon;
        float lat;
    }
}
