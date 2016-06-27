package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.PlacesTable;
import net.borkiss.weatherforecast.model.Place;

public class PlaceCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PlaceCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Place getPlace() {

        String name = getString(getColumnIndex(PlacesTable.Cols.NAME));
        int id = getInt(getColumnIndex(PlacesTable.Cols.CITY_ID));
        String country = getString(getColumnIndex(PlacesTable.Cols.COUNTRY));
        float longitude = getFloat(getColumnIndex(PlacesTable.Cols.COORD_LON));
        float latitude = getFloat(getColumnIndex(PlacesTable.Cols.COORD_LAT));

        Place place = new Place();
        place.setName(name);
        place.setId(id);
        place.setCountry(country);
        place.setLongitude(longitude);
        place.setLatitude(latitude);

        return place;
    }

}
