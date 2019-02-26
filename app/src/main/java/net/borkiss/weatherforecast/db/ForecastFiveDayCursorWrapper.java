package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.model.ForecastFiveDay;

import java.util.Date;

import static net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable.Cols.*;

public class ForecastFiveDayCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ForecastFiveDayCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ForecastFiveDay getForecastFiveDay() {

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

        ForecastFiveDay forecast = new ForecastFiveDay();
        forecast.setPlaceId(placeId);
        forecast.setTime(new Date(time));
        forecast.setWeatherConditionId(conditionId);
        forecast.setWeatherMain(weatherMine);
        forecast.setWeatherDescription(weatherDescription);
        forecast.setTemperature(temperature);
        forecast.setPressure(pressure);
        forecast.setHumidity(humidity);
        forecast.setMinTemperature(minTemperature);
        forecast.setMaxTemperature(maxTemperature);
        forecast.setWindSpeed(windSpeed);
        forecast.setWindDegree(windDegree);
        forecast.setClouds(clouds);

        return forecast;
    }
}
