package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable;
import net.borkiss.weatherforecast.model.CurrentWeather;

import java.util.Date;

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

        int placeId = getInt(getColumnIndex(CurrentWeatherTable.Cols.PLACE_ID));
        long time = getLong(getColumnIndex(CurrentWeatherTable.Cols.TIME));
        int conditionId = getInt(getColumnIndex(CurrentWeatherTable.Cols.CONDITION_ID));
        String weatherMine = getString(getColumnIndex(CurrentWeatherTable.Cols.WEATHER_MAIN));
        String weatherDescription = getString(getColumnIndex(CurrentWeatherTable.Cols.WEATHER_DESCRIPTION));
        float temperature = getFloat(getColumnIndex(CurrentWeatherTable.Cols.TEMPERATURE));
        float pressure = getFloat(getColumnIndex(CurrentWeatherTable.Cols.PRESSURE));
        int humidity = getInt(getColumnIndex(CurrentWeatherTable.Cols.HUMIDITY));
        float minTemperature = getFloat(getColumnIndex(CurrentWeatherTable.Cols.MIN_TEMPERATURE));
        float maxTemperature = getFloat(getColumnIndex(CurrentWeatherTable.Cols.MAX_TEMPERATURE));
        float windSpeed = getFloat(getColumnIndex(CurrentWeatherTable.Cols.WIND_SPEED));
        float windDegree = getFloat(getColumnIndex(CurrentWeatherTable.Cols.WIND_DEGREE));
        int clouds = getInt(getColumnIndex(CurrentWeatherTable.Cols.CLOUDS));
        long sunrise = getLong(getColumnIndex(CurrentWeatherTable.Cols.SUNRISE));
        long sunset = getLong(getColumnIndex(CurrentWeatherTable.Cols.SUNSET));

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
