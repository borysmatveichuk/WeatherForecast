package net.borkiss.weatherforecast.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ForecastFiveDayDTO implements Serializable {
    private static final long serialVersionUID = 6178146749953918577L;

    private int cityId;

    @SerializedName("dt")
    private Date time;

    @SerializedName("weather")
    private List<CurrentWeatherDTO.Weather> weather;

    @SerializedName("main")
    private CurrentWeatherDTO.Main mainIndicators;

    @SerializedName("wind")
    private CurrentWeatherDTO.Wind wind;

    @SerializedName("clouds")
    private CurrentWeatherDTO.Clouds clouds;

    public int getPlaceId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public Date getTime() {
        return time;
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

    public int getWeatherConditionId() {
        if (weather != null
                && !weather.isEmpty()) {
            return weather.get(0).id;
        }
        return 0;
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

    public int getClouds() {
        return clouds.all;
    }

}
