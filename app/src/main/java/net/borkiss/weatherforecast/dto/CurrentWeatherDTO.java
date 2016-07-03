package net.borkiss.weatherforecast.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CurrentWeatherDTO implements Serializable {
    private static final long serialVersionUID = 4262761557170039078L;

    private long time;

    @SerializedName("id")
    private int placeId;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("main")
    private Main mainIndicators;

    @SerializedName("wind")
    private Wind wind;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getWeatherMain() {
        if (weather != null
                && !weather.isEmpty()) {
            return weather.get(0).main;
        }
        return "";
    }

    public String getWeatherDescription() {

        if (weather != null
                && !weather.isEmpty()) {
            return weather.get(0).description;
        }
        return "";
    }

    public float getTemperature() {
        return mainIndicators.temp;
    }

    public float getPressure() {
        return mainIndicators.pressure;
    }

    public int getHumidity() {
        return mainIndicators.humidity;
    }

    public float getMinTemperature() {
        return mainIndicators.temp_min;
    }

    public float getMaxTemperature() {
        return mainIndicators.temp_max;
    }

    public float getWindSpeed() {
        return wind.speed;
    }

    public float getWindDegree() {
        return wind.deg;
    }


    private class Weather {
        String main;
        String description;
    }

    private class Main {
        float temp;
        float pressure;
        int humidity;
        float temp_min;
        float temp_max;
    }

    private class Wind {
        float speed;
        float deg;
    }
}

