package net.borkiss.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable;
import net.borkiss.weatherforecast.dto.ForecastFiveDayDTO;

public class ForecastFiveDayCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ForecastFiveDayCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ForecastFiveDayDTO getForecastFiveDayDTO() {

        int placeId = getInt(getColumnIndex(ForecastFiveDayTable.Cols.PLACE_ID));
        long time = getLong(getColumnIndex(ForecastFiveDayTable.Cols.TIME));
        String document = getString(getColumnIndex(ForecastFiveDayTable.Cols.DOCUMENT));

        ForecastFiveDayDTO dto = new ForecastFiveDayDTO();
        dto.setPlaceId(placeId);
        dto.setTime(time);
        dto.setDocument(document);

        return dto;
    }
}
