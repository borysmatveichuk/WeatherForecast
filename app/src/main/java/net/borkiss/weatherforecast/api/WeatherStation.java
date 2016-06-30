package net.borkiss.weatherforecast.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.borkiss.weatherforecast.db.CurrentWeatherCursorWrapper;
import net.borkiss.weatherforecast.db.ForecastFiveDayCursorWrapper;
import net.borkiss.weatherforecast.db.PlaceCursorWrapper;
import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.PlacesTable;
import net.borkiss.weatherforecast.model.CurrentWeatherDTO;
import net.borkiss.weatherforecast.model.ForecastFiveDayDTO;
import net.borkiss.weatherforecast.model.Place;

import java.util.ArrayList;
import java.util.List;

public class WeatherStation {

    private static WeatherStation weatherStation;

    private Context appContext;
    private List<String> currentWeather;
    private List<String> forecastFiveDays;

    private SQLiteDatabase database;

    private WeatherStation(Context context) {
        appContext = context.getApplicationContext();
        database = new WeatherBaseHelper(appContext).getWritableDatabase();
        currentWeather = new ArrayList<>();
        forecastFiveDays = new ArrayList<>();
    }

    public static WeatherStation getInstance(Context context) {
        if (weatherStation == null) {
            weatherStation = new WeatherStation(context);
        }
        return weatherStation;
    }

    private static ContentValues getPlaceContentValues(Place place) {
        ContentValues values = new ContentValues();
        values.put(PlacesTable.Cols.NAME, place.getName());
        values.put(PlacesTable.Cols.CITY_ID, place.getId());
        values.put(PlacesTable.Cols.COUNTRY, place.getCountry());
        values.put(PlacesTable.Cols.COORD_LAT, place.getLatitude());
        values.put(PlacesTable.Cols.COORD_LON, place.getLongitude());

        return values;
    }

    private static ContentValues getForecastFiveDayDTOContentValues(ForecastFiveDayDTO dto) {
        ContentValues values = new ContentValues();
        values.put(ForecastFiveDayTable.Cols.TIME, dto.getTime());
        values.put(ForecastFiveDayTable.Cols.PLACE_ID, dto.getPlaceId());
        values.put(ForecastFiveDayTable.Cols.DOCUMENT, dto.getDocument());

        return values;
    }

    private ContentValues getCurrentWeatherDTOContentValues(CurrentWeatherDTO dto) {
        ContentValues values = new ContentValues();
        values.put(CurrentWeatherTable.Cols.TIME, dto.getTime());
        values.put(CurrentWeatherTable.Cols.PLACE_ID, dto.getPlaceId());
        values.put(CurrentWeatherTable.Cols.DOCUMENT, dto.getDocument());

        return values;
    }


    public void addPlace(Place place) {
        database.insert(PlacesTable.NAME, null, getPlaceContentValues(place));
    }

    public void addForecastFiveDayDTO(ForecastFiveDayDTO dto) {
        database.insert(ForecastFiveDayTable.NAME, null, getForecastFiveDayDTOContentValues(dto));
    }

    public void addCurrentWeatherDTO(CurrentWeatherDTO dto) {
        database.insert(CurrentWeatherTable.NAME, null, getCurrentWeatherDTOContentValues(dto));
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

        //places = getTestPlaces();

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

    private List<Place> getTestPlaces() {
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Place place = new Place();
            place.setId(i);
            place.setName("Place " + i);
            place.setCountry("UA");
            place.setLatitude(123.456f);
            place.setLongitude(456.789f);

            places.add(place);
        }
        return places;
    }

    public List<CurrentWeatherDTO> getListCurrentWeatherDTO() {

        List<CurrentWeatherDTO> dto = new ArrayList<>();
        CurrentWeatherCursorWrapper cursor = queryCurrentWeather(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dto.add(cursor.getCurrentWeatherDTO());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return dto;
    }

    public List<ForecastFiveDayDTO> getListForecastFiveDayDTO() {

        List<ForecastFiveDayDTO> dto = new ArrayList<>();
        ForecastFiveDayCursorWrapper cursor = queryForecastFiveDay(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dto.add(cursor.getForecastFiveDayDTO());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return dto;
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
