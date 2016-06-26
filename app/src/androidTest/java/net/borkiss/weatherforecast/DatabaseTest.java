package net.borkiss.weatherforecast;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.db.WeatherDbSchema;
import net.borkiss.weatherforecast.model.Place;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class DatabaseTest {

    private WeatherStation sqLiteDatabase;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(WeatherBaseHelper.DATABASE_NAME);
        sqLiteDatabase = WeatherStation.getInstance(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        //database.close();
    }

    @Test
    public void insertSelectPlaces() throws Exception {

        Place place = new Place();ContentValues values = new ContentValues();
        place.setName("Nikolaev");
        place.setCountry("UA");
        place.setId(123456);
        place.setLatitude(123.456f);
        place.setLongitude(789.012f);

        sqLiteDatabase.addPlace(place);

        List<Place> places = sqLiteDatabase.getPlaces();
        for (Place p : places) {
            assertTrue(p.getName().equals("Nikolaev"));
            assertTrue(p.getCountry().equals("UA"));

            assertThat(p.getId(), is(123456));
            assertThat(p.getLatitude(), is(123.456f));
            assertThat(p.getLongitude(), is(789.012f));
        }

    }
}