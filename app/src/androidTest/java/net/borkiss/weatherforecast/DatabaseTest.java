package net.borkiss.weatherforecast;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.db.WeatherBaseHelper;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.dto.ForecastFiveDayDTO;
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
        insertSelectCurrentWeatherDTO();
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


    private void insertSelectCurrentWeatherDTO() throws Exception {
        CurrentWeatherDTO dto = new CurrentWeatherDTO();
        dto.setDocument("dto");
        dto.setTime(123456789);
        dto.setPlaceId(111);

        CurrentWeatherDTO dto2 = new CurrentWeatherDTO();
        dto2.setDocument("dto2");
        dto2.setTime(99999999L);
        dto2.setPlaceId(222);


        sqLiteDatabase.addCurrentWeatherDTO(dto);
        sqLiteDatabase.addCurrentWeatherDTO(dto2);

        List<CurrentWeatherDTO> list = sqLiteDatabase.getListCurrentWeatherDTO();

        assertThat(list.size(), is(2));

        assertTrue(list.get(1).getDocument().equals("dto2"));
        assertThat(list.get(1).getTime(), is(99999999L));
        assertThat(list.get(1).getPlaceId(), is(222));

    }

    private void insertSelectForecastFiveDayDTO() throws Exception {
        ForecastFiveDayDTO dto = new ForecastFiveDayDTO();
        dto.setDocument("dto");
        dto.setTime(1111L);
        dto.setPlaceId(111);

        ForecastFiveDayDTO dto2 = new ForecastFiveDayDTO();
        dto2.setDocument("{\"coord\":{\"lon\":32,\"lat\":46.97},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"base\":\"stations\",\"main\":{\"temp\":297.915,\"pressure\":1014.89,\"humidity\":76,\"temp_min\":297.915,\"temp_max\":297.915,\"sea_level\":1018.96,\"grnd_level\":1014.89},\"wind\":{\"speed\":2.67,\"deg\":27.5058},\"rain\":{\"3h\":0.675},\"clouds\":{\"all\":76},\"dt\":1467067225,\"sys\":{\"message\":0.0382,\"country\":\"UA\",\"sunrise\":1466992774,\"sunset\":1467049855},\"id\":700569,\"name\":\"Mykolayiv\",\"cod\":200}");
        dto2.setTime(2222L);
        dto2.setPlaceId(222);


        sqLiteDatabase.addForecastFiveDayDTO(dto);
        sqLiteDatabase.addForecastFiveDayDTO(dto2);

        List<ForecastFiveDayDTO> list = sqLiteDatabase.getListForecastFiveDayDTO();

        assertThat(list.size(), is(2));

        assertTrue(list.get(1).getDocument().subSequence(33, 40).equals("weather"));
        assertThat(list.get(1).getTime(), is(2222L));
        assertThat(list.get(1).getPlaceId(), is(222));

    }
}