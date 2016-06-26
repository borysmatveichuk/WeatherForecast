package net.borkiss.weatherforecast.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.db.WeatherDbSchema;
import net.borkiss.weatherforecast.db.WeatherDbSchema.PlacesTable;
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
        values.put(PlacesTable.Cols.ID, place.getId());
        values.put(PlacesTable.Cols.COUNTRY, place.getCountry());
        values.put(PlacesTable.Cols.COORD_LAT, place.getLatitude());
        values.put(PlacesTable.Cols.COORD_LON, place.getLongitude());

        return values;
    }

    public void addPlace(Place place) {
        database.insert(PlacesTable.NAME, null, getPlaceContentValues(place));
    }

    public List<Place> getPlaces() {

        List<Place> places = new ArrayList<>();

        Cursor cursor = database.query(PlacesTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(PlacesTable.Cols.NAME));
                int id = cursor.getInt(cursor.getColumnIndex(PlacesTable.Cols.ID));
                String country = cursor.getString(cursor.getColumnIndex(PlacesTable.Cols.COUNTRY));
                float longitude = cursor.getFloat(cursor.getColumnIndex(PlacesTable.Cols.COORD_LON));
                float latitude = cursor.getFloat(cursor.getColumnIndex(PlacesTable.Cols.COORD_LAT));

                Place place = new Place();
                place.setName(name);
                place.setId(id);
                place.setCountry(country);
                place.setLongitude(longitude);
                place.setLatitude(latitude);

                places.add(place);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return places;
    }
}
