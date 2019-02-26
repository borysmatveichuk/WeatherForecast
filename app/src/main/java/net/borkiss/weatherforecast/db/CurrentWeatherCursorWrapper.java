package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.model.CurrentWeather;

import java.util.Date;

import static net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable.Cols.*;

public class CurrentWeatherCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CurrentWeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CurrentWeather getCurrentWeather() {

        int placeId = getInt(getColumnIndex(PLACE_ID));
        long time = getLong(getColumnIndex(TIME));
        int conditionId = getInt(getColumnIndex(CONDITION_ID));
        String weatherMine = getString(getColumnIndex(WEATHER_MAIN));
        String weatherDescription = getString(getColumnIndex(WEATHER_DESCRIPTION));
        float temperature = getFloat(getColumnIndex(TEMPERATURE));
        float pressure = getFloat(getColumnIndex(PRESSURE));
        int humidity = getInt(getColumnIndex(HUMIDITY));
        float minTemperature = getFloat(getColumnIndex(MIN_TEMPERATURE));
        float maxTemperature = getFloat(getColumnIndex(MAX_TEMPERATURE));
        float windSpeed = getFloat(getColumnIndex(WIND_SPEED));
        float windDegree = getFloat(getColumnIndex(WIND_DEGREE));
        int clouds = getInt(getColumnIndex(CLOUDS));
        long sunrise = getLong(getColumnIndex(SUNRISE));
        long sunset = getLong(getColumnIndex(SUNSET));

        CurrentWeather weather = new CurrentWeather();
        weather.setPlaceId(placeId);
        weather.setTime(new Date(time));
        weather.setWeatherConditionId(conditionId);
        weather.setWeatherMain(weatherMine);
        weather.setWeatherDescription(weatherDescription);
        weather.setTemperature(temperature);
        weather.setPressure(pressure);
        weather.setHumidity(humidity);
        weather.setMinTemperature(minTemperature);
        weather.setMaxTemperature(maxTemperature);
        weather.setWindSpeed(windSpeed);
        weather.setWindDegree(windDegree);
        weather.setClouds(clouds);
        weather.setSunrise(new Date(sunrise));
        weather.setSunset(new Date(sunset));

        return weather;
    }
}
