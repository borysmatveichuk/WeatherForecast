package net.borkiss.weatherforecast;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.dto.ForecastFiveDayDTO;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class DatabaseTest {

    private static final String TAG = DatabaseTest.class.getSimpleName();

    private WeatherStation sqLiteDatabase;

    @Before
    public void setUp() throws Exception {

        if (getTargetContext().deleteDatabase(WeatherBaseHelper.DATABASE_NAME))
            Log.d(TAG, "DB deleted");
        else
            Log.e(TAG, "Can't delete DB");

        sqLiteDatabase = WeatherStation.getInstance(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        //database.close();
    }

    @Test
    public void databaseInsertSelect() throws Exception {
        insertSelectPlaces();
        insertSelectCurrentWeather();
        insertSelectForecastFiveDayDTO();
    }

    private void insertSelectPlaces() throws Exception {

        Place place = new Place();
        place.setName("Nikolaev");
        place.setCountry("UA");
        place.setCityId(123456);
        place.setLatitude(123.456f);
        place.setLongitude(789.012f);

        sqLiteDatabase.addPlace(place);

        List<Place> places = sqLiteDatabase.getPlaces();
        for (Place p : places) {
            assertTrue(p.getName().equals("Nikolaev"));
            assertTrue(p.getCountry().equals("UA"));

            assertThat(p.getCityId(), is(123456));
            assertThat(p.getLatitude(), is(123.456f));
            assertThat(p.getLongitude(), is(789.012f));
        }

        Place placeFromDB = sqLiteDatabase.getPlaceByCityId(123456);
        assertThat(placeFromDB.getCityId(), is(123456));

        int delCount = sqLiteDatabase.deletePlace(placeFromDB);
        assertThat(delCount, is(1));

    }


    private void insertSelectCurrentWeather() throws Exception {
        CurrentWeather dto = new CurrentWeather();
        dto.setTime(new Date(1467651600));
        dto.setPlaceId(111);

        CurrentWeather dto2 = new CurrentWeather();
        dto2.setTime(new Date(1467651600));
        dto2.setPlaceId(222);


        sqLiteDatabase.addCurrentWeather(dto);
        sqLiteDatabase.addCurrentWeather(dto2);

        List<CurrentWeather> listAdded = sqLiteDatabase.getListCurrentWeather();

        assertThat(listAdded.size(), is(2));

        assertThat(listAdded.get(1).getTime(), is(new Date(1467651600)));
        assertThat(listAdded.get(1).getPlaceId(), is(222));

        CurrentWeather dto3 = new CurrentWeather();
        dto3.setTime(new Date(1467651600));
        dto3.setPlaceId(111);
        //must bu updated
        assertThat(sqLiteDatabase.addCurrentWeather(dto3), is(1));
        assertThat(listAdded.size(), is(2));

        List<CurrentWeather> listUpdated = sqLiteDatabase.getListCurrentWeather();
        assertThat(listUpdated.get(0).getTime(), is(new Date(1467651600)));
    }

    private void insertSelectForecastFiveDayDTO() throws Exception {
        ForecastFiveDay dto = new ForecastFiveDay();
        dto.setTime(new Date(1467651600));
        dto.setPlaceId(111);

        ForecastFiveDay dto2 = new ForecastFiveDay();
        dto2.setTime(new Date(1467651600));
        dto2.setPlaceId(222);


        sqLiteDatabase.addForecastFiveDayDTO(dto);
        sqLiteDatabase.addForecastFiveDayDTO(dto2);

        List<ForecastFiveDay> list = sqLiteDatabase.getListForecastFiveDay();

        assertThat(list.size(), is(2));

        assertThat(list.get(1).getTime(), is(new Date(1467651600)));
        assertThat(list.get(1).getPlaceId(), is(222));

        sqLiteDatabase.deleteFiveDayForecastByPlaceId(dto.getPlaceId());
        sqLiteDatabase.deleteFiveDayForecastByPlaceId(dto2.getPlaceId());

        List<ForecastFiveDay> listAfterDelete = sqLiteDatabase.getListForecastFiveDay();

        assertThat(listAfterDelete.size(), is(0));
    }
}