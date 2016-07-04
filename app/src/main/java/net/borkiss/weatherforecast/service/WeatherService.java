package net.borkiss.weatherforecast.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.borkiss.weatherforecast.adapter.PlaceAdapter;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.URIBuildHelper;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.dto.DTOFactory;
import net.borkiss.weatherforecast.dto.ForecastFiveDayDTO;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.Place;

import java.util.List;

public class WeatherService extends IntentService {

    private static final String TAG = WeatherService.class.getSimpleName();

    private ApiCallback<CurrentWeatherDTO> currentWeatherCallback = new ApiCallback<CurrentWeatherDTO>() {
        @Override
        public void onSuccess(CurrentWeatherDTO result) {
            if (result == null)
                return;

            final CurrentWeather currentWeather = DTOFactory.INSTANCE.createCurrentWeather(result);
            WeatherStation.getInstance(WeatherService.this).addCurrentWeather(currentWeather);
        }

        @Override
        public void onError(ApiError error) {
            Log.e(TAG, "Can't write to db! " + error);
        }
    };

    private ApiCallback<ForecastFiveDayDTO> fiveDayDTOCallback;


    public static Intent newIntent(Context context) {
        return new Intent(context, WeatherService.class);
    }

    public WeatherService() {
        this(TAG);
    }
    public WeatherService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        WeatherApi api = new WeatherApi();

        List<Place> places = WeatherStation.getInstance(this).getPlaces();
        for (Place place : places) {
            Log.i(TAG, place.getCityId() + " " + place.getName() + " " + place.getCountry());
            String url = URIBuildHelper.createUriCurrentWeather(place.getCityId());
            Log.i(TAG, url);

            api.getCurrentWeather(place.getCityId(), currentWeatherCallback);
        }

    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        return isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
    }

}
