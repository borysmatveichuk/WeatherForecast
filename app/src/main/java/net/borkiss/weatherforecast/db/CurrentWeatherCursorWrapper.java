package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;

public class CurrentWeatherCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CurrentWeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CurrentWeatherDTO getCurrentWeatherDTO() {

        int placeId = getInt(getColumnIndex(CurrentWeatherTable.Cols.PLACE_ID));
        long time = getLong(getColumnIndex(CurrentWeatherTable.Cols.TIME));
        String document = getString(getColumnIndex(CurrentWeatherTable.Cols.DOCUMENT));

        CurrentWeatherDTO dto = new CurrentWeatherDTO();
        dto.setPlaceId(placeId);
        dto.setTime(time);
        dto.setDocument(document);

        return dto;
    }
}
