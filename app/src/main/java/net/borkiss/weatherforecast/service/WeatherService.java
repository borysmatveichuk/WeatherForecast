package net.borkiss.weatherforecast.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
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
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;

import java.util.List;

public class WeatherService extends IntentService {

    private static final String TAG = WeatherService.class.getSimpleName();

    private static final int INTERVAL = 1000 * 60 * 10; // 15 MINUTES

    private ApiCallback<CurrentWeatherDTO> currentWeatherCallback = new ApiCallback<CurrentWeatherDTO>() {
        @Override
        public void onSuccess(CurrentWeatherDTO result) {
            if (result == null)
                return;

            final CurrentWeather currentWeather = DTOFactory.INSTANCE.createCurrentWeather(result);
            int count = instance.addCurrentWeather(currentWeather);
            if (count > 0) {
                Log.d(TAG, "Added current weather " + count + " record(s) to DB.");
            } else {
                Log.e(TAG, "Can't add current weather to DB!");
        }
        }

        @Override
        public void onError(ApiError error) {
            Log.e(TAG, "Can't write to db! " + error);
        }
    };

    private ApiCallback<List<ForecastFiveDayDTO>> fiveDayDTOCallback = new ApiCallback<List<ForecastFiveDayDTO>>() {
        @Override
        public void onSuccess(List<ForecastFiveDayDTO> result) {
            if (result == null)
                return;

            final List<ForecastFiveDay> forecastList = DTOFactory.INSTANCE.createForecastFiveDayList(result);

            if (!forecastList.isEmpty()) {
                instance.deleteFiveDayForecastByPlaceId(forecastList.get(0).getPlaceId());


                int count = instance.addListFiveDayForecast(forecastList);

                /*
                int count = 0;

                for (ForecastFiveDay forecast : forecastList) {
                    instance.addForecastFiveDay(forecast);
                    count++;
                }
                */

                if (count > 0) {
                    Log.d(TAG, "Added forecast " + count + " record(s) to DB.");
                } else {
                    Log.e(TAG, "Can't add 5 day forecast to DB!");
                }
            }

        }

        @Override
        public void onError(ApiError error) {
            Log.e(TAG, "Can't write to db! " + error);
        }
    };

    private WeatherStation instance;

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
    public void onCreate() {
        super.onCreate();
        instance = WeatherStation.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(!isNetworkAvailableAndConnected())
            return;

        Log.i(TAG, "Received an intent: " + intent);

        WeatherApi api = new WeatherApi();

        List<Place> places = instance.getPlaces();
        for (Place place : places) {
            Log.i(TAG, "Get weather for: " + place.getName());

            api.getCurrentWeather(place.getCityId(), currentWeatherCallback);
            api.getFiveDayForecastByPlaceId(place.getCityId(), fiveDayDTOCallback);

        }

    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        return isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = WeatherService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = WeatherService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

}
