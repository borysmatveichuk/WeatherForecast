package net.borkiss.weatherforecast.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import net.borkiss.weatherforecast.db.CurrentWeatherCursorWrapper;
import net.borkiss.weatherforecast.db.ForecastFiveDayCursorWrapper;
import net.borkiss.weatherforecast.db.PlaceCursorWrapper;
import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.PlacesTable;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;

import java.util.ArrayList;
import java.util.List;

public class WeatherStation {

    private static final String TAG = WeatherStation.class.getSimpleName();

    private static WeatherStation weatherStation;

    private Context appContext;

    private SQLiteDatabase database;

    private WeatherStation(Context context) {
        appContext = context.getApplicationContext();
        database = new WeatherBaseHelper(appContext).getWritableDatabase();
    }

    public static WeatherStation getInstance(Context context) {
        if (weatherStation == null) {
            weatherStation = new WeatherStation(context);
        }
        return weatherStation;
    }

    private static ContentValues getContentValues(Place place) {
        ContentValues values = new ContentValues();
        values.put(PlacesTable.Cols.NAME, place.getName());
        values.put(PlacesTable.Cols.CITY_ID, place.getCityId());
        values.put(PlacesTable.Cols.COUNTRY, place.getCountry());
        values.put(PlacesTable.Cols.COORD_LAT, place.getLatitude());
        values.put(PlacesTable.Cols.COORD_LON, place.getLongitude());

        return values;
    }

    private static ContentValues getContentValues(ForecastFiveDay forecast) {
        ContentValues values = new ContentValues();
        values.put(ForecastFiveDayTable.Cols.TIME, forecast.getTime().getTime());
        values.put(ForecastFiveDayTable.Cols.PLACE_ID, forecast.getPlaceId());
        values.put(ForecastFiveDayTable.Cols.CONDITION_ID, forecast.getWeatherConditionId());
        values.put(ForecastFiveDayTable.Cols.WEATHER_MAIN, forecast.getWeatherMain());
        values.put(ForecastFiveDayTable.Cols.WEATHER_DESCRIPTION, forecast.getWeatherDescription());
        values.put(ForecastFiveDayTable.Cols.TEMPERATURE, forecast.getTemperature());
        values.put(ForecastFiveDayTable.Cols.PRESSURE, forecast.getPressure());
        values.put(ForecastFiveDayTable.Cols.HUMIDITY, forecast.getHumidity());
        values.put(ForecastFiveDayTable.Cols.MIN_TEMPERATURE, forecast.getMinTemperature());
        values.put(ForecastFiveDayTable.Cols.MAX_TEMPERATURE, forecast.getMaxTemperature());
        values.put(ForecastFiveDayTable.Cols.WIND_SPEED, forecast.getWindSpeed());
        values.put(ForecastFiveDayTable.Cols.WIND_DEGREE, forecast.getWindDegree());
        values.put(ForecastFiveDayTable.Cols.CLOUDS, forecast.getClouds());
        return values;
    }

    private static ContentValues getContentValues(CurrentWeather weather) {
        ContentValues values = new ContentValues();
        values.put(CurrentWeatherTable.Cols.TIME, weather.getTime().getTime());
        values.put(CurrentWeatherTable.Cols.PLACE_ID, weather.getPlaceId());
        values.put(CurrentWeatherTable.Cols.CONDITION_ID, weather.getWeatherConditionId());
        values.put(CurrentWeatherTable.Cols.WEATHER_MAIN, weather.getWeatherMain());
        values.put(CurrentWeatherTable.Cols.WEATHER_DESCRIPTION, weather.getWeatherDescription());
        values.put(CurrentWeatherTable.Cols.TEMPERATURE, weather.getTemperature());
        values.put(CurrentWeatherTable.Cols.PRESSURE, weather.getPressure());
        values.put(CurrentWeatherTable.Cols.HUMIDITY, weather.getHumidity());
        values.put(CurrentWeatherTable.Cols.MIN_TEMPERATURE, weather.getMinTemperature());
        values.put(CurrentWeatherTable.Cols.MAX_TEMPERATURE, weather.getMaxTemperature());
        values.put(CurrentWeatherTable.Cols.WIND_SPEED, weather.getWindSpeed());
        values.put(CurrentWeatherTable.Cols.WIND_DEGREE, weather.getWindDegree());
        values.put(CurrentWeatherTable.Cols.CLOUDS, weather.getClouds());
        values.put(CurrentWeatherTable.Cols.SUNRISE, weather.getSunrise().getTime());
        values.put(CurrentWeatherTable.Cols.SUNSET, weather.getSunset().getTime());

        return values;
    }


    public void addPlace(Place place) {
        database.insert(PlacesTable.NAME, null, getContentValues(place));
    }

    public int deletePlace(Place place) {
        return database.delete(
                PlacesTable.NAME,
                PlacesTable.Cols.CITY_ID + " = ? ",
                new String[]{Integer.toString(place.getCityId())}
        );
    }

    public void addForecastFiveDay(ForecastFiveDay forecast) {
        database.insert(ForecastFiveDayTable.NAME, null, getContentValues(forecast));
    }

    public int addCurrentWeather(CurrentWeather weather) {

        int count = 0;
        CurrentWeatherCursorWrapper cursor = null;
        String whereClause = CurrentWeatherTable.Cols.PLACE_ID + " = ? ";
        String[] whereArgs = {Integer.toString(weather.getPlaceId())};

        try {
            cursor = queryCurrentWeather(whereClause, whereArgs);

            if (cursor.moveToFirst()) {
                count = database.update(CurrentWeatherTable.NAME,
                        getContentValues(weather),
                        whereClause,
                        whereArgs);
            } else {
                count = (int) database.insert(CurrentWeatherTable.NAME, null, getContentValues(weather));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return count;
    }

    public List<Place> getPlaces() {

        List<Place> places = new ArrayList<>();
        PlaceCursorWrapper cursor = queryPlaces(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                places.add(cursor.getPlace());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return places;
    }

    public Place getPlaceByCityId(int cityId) {

        PlaceCursorWrapper cursor = queryPlaces(
                PlacesTable.Cols.CITY_ID + " = " + cityId,
                null
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getPlace();
        } finally {
            cursor.close();
        }

    }

    public List<CurrentWeather> getListCurrentWeather() {

        List<CurrentWeather> weatherList = new ArrayList<>();
        CurrentWeatherCursorWrapper cursor = queryCurrentWeather(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                weatherList.add(cursor.getCurrentWeather());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return weatherList;
    }

    public CurrentWeather getCurrentWeatherByCityId(int cityId) {

        CurrentWeather weather = new CurrentWeather();

        String whereClause = CurrentWeatherTable.Cols.PLACE_ID + " = ? ";
        String[] whereArgs = {Integer.toString(cityId)};

        CurrentWeatherCursorWrapper cursor = queryCurrentWeather(whereClause, whereArgs);

        try {
            if (cursor.moveToFirst()) {
                weather = cursor.getCurrentWeather();
            };
        } finally {
            cursor.close();
        }

        return weather;
    }

    public List<ForecastFiveDay> getListForecastFiveDay() {

        List<ForecastFiveDay> forecastList = new ArrayList<>();
        ForecastFiveDayCursorWrapper cursor = queryForecastFiveDay(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                forecastList.add(cursor.getForecastFiveDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return forecastList;
    }

    public List<ForecastFiveDay> getListForecastFiveDayByCityId(int cityId) {

        List<ForecastFiveDay> forecastList = new ArrayList<>();

        String whereClause = ForecastFiveDayTable.Cols.PLACE_ID + " = ? ";
        String[] whereArgs = {Integer.toString(cityId)};

        ForecastFiveDayCursorWrapper cursor = queryForecastFiveDay(whereClause, whereArgs);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                forecastList.add(cursor.getForecastFiveDay());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return forecastList;
    }


    public void deleteFiveDayForecastByPlaceId(final int cityId) {
        final String ROUTE_TABLE_DELETE =
                "DELETE FROM " + ForecastFiveDayTable.NAME
                        + " WHERE "
                        + ForecastFiveDayTable.Cols.PLACE_ID
                        + " = ?";

        final SQLiteStatement statement = database.compileStatement(ROUTE_TABLE_DELETE);

        database.beginTransactionNonExclusive();
        try {
            statement.bindLong(1, cityId);
            statement.execute();
            statement.clearBindings();
            database.setTransactionSuccessful();
            Log.d(TAG, "FiveDayForecastByPlaceId deleted");
        } catch (Exception e) {
            Log.e(TAG, "Error when deleting FiveDayForecastByPlaceId " + e.getMessage());
        } finally {
            database.endTransaction();
        }
    }

    public int addListFiveDayForecast(List<ForecastFiveDay> forecastFiveDayList) {

        int count = 0;

        String sql = "INSERT OR REPLACE INTO " + ForecastFiveDayTable.NAME + " ( " +
                ForecastFiveDayTable.Cols.TIME + ", " +
                ForecastFiveDayTable.Cols.PLACE_ID + ", " +
                ForecastFiveDayTable.Cols.CONDITION_ID + ", " +
                ForecastFiveDayTable.Cols.WEATHER_MAIN + ", " +
                ForecastFiveDayTable.Cols.WEATHER_DESCRIPTION + ", " +
                ForecastFiveDayTable.Cols.TEMPERATURE + ", " +
                ForecastFiveDayTable.Cols.PRESSURE + ", " +
                ForecastFiveDayTable.Cols.HUMIDITY + ", " +
                ForecastFiveDayTable.Cols.MIN_TEMPERATURE + ", " +
                ForecastFiveDayTable.Cols.MAX_TEMPERATURE + ", " +
                ForecastFiveDayTable.Cols.WIND_SPEED + ", " +
                ForecastFiveDayTable.Cols.WIND_DEGREE + ", " +
                ForecastFiveDayTable.Cols.CLOUDS +
                " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        SQLiteStatement statement = database.compileStatement(sql);

        database.beginTransactionNonExclusive();

        try {
            for (ForecastFiveDay forecast : forecastFiveDayList) {

                statement.bindLong(1, forecast.getTime().getTime());
                statement.bindLong(2, forecast.getPlaceId());
                statement.bindLong(3, forecast.getWeatherConditionId());
                statement.bindString(4, forecast.getWeatherMain());
                statement.bindString(5, forecast.getWeatherDescription());
                statement.bindDouble(6, forecast.getTemperature());
                statement.bindDouble(7, forecast.getPressure());
                statement.bindLong(8, forecast.getHumidity());
                statement.bindDouble(9, forecast.getMinTemperature());
                statement.bindDouble(10, forecast.getMaxTemperature());
                statement.bindDouble(11, forecast.getWindSpeed());
                statement.bindDouble(12, forecast.getWindDegree());
                statement.bindLong(13, forecast.getClouds());

                statement.execute();
                statement.clearBindings();
                count++;
            }
            database.setTransactionSuccessful();

            Log.d(TAG, "ListFiveDayForecast inserted " + count + " records for " +
                    forecastFiveDayList.get(0).getPlaceId());

        } catch (Exception e) {
            Log.e(TAG, "Error when inserting ListFiveDayForecast " + e.getMessage());
        } finally {
            database.endTransaction();
        }

        return count;

    }

    private PlaceCursorWrapper queryPlaces(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                PlacesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PlaceCursorWrapper(cursor);
    }

    private CurrentWeatherCursorWrapper queryCurrentWeather(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                CurrentWeatherTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CurrentWeatherCursorWrapper(cursor);
    }

    private ForecastFiveDayCursorWrapper queryForecastFiveDay(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                ForecastFiveDayTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ForecastFiveDayCursorWrapper(cursor);
    }

}
