package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable;
import net.borkiss.weatherforecast.model.ForecastFiveDay;

import java.util.Date;

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

        int placeId = getInt(getColumnIndex(ForecastFiveDayTable.Cols.PLACE_ID));
        long time = getLong(getColumnIndex(ForecastFiveDayTable.Cols.TIME));
        String weatherMine = getString(getColumnIndex(ForecastFiveDayTable.Cols.WEATHER_MAIN));
        String weatherDescription = getString(getColumnIndex(ForecastFiveDayTable.Cols.WEATHER_DESCRIPTION));
        float temperature = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.TEMPERATURE));
        float pressure = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.PRESSURE));
        int humidity = getInt(getColumnIndex(ForecastFiveDayTable.Cols.HUMIDITY));
        float minTemperature = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.MIN_TEMPERATURE));
        float maxTemperature = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.MAX_TEMPERATURE));
        float windSpeed = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.WIND_SPEED));
        float windDegree = getFloat(getColumnIndex(ForecastFiveDayTable.Cols.WIND_DEGREE));
        int clouds = getInt(getColumnIndex(ForecastFiveDayTable.Cols.CLOUDS));

        ForecastFiveDay forecast = new ForecastFiveDay();
        forecast.setPlaceId(placeId);
        forecast.setTime(new Date(time));
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
