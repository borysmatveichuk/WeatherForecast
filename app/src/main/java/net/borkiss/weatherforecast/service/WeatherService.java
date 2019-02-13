package net.borkiss.weatherforecast.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.dto.DTOFactory;
import net.borkiss.weatherforecast.dto.ForecastFiveDayDTO;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.ui.MainActivity;
import net.borkiss.weatherforecast.util.Prefs;

import java.util.Date;
import java.util.List;

public class WeatherService extends IntentService {

    private static final String TAG = WeatherService.class.getSimpleName();

    public static final int INTERVAL = 1000 * 60 * 15; // 15 MINUTES
    public static final int STATUS_FINISHED = 1;

    private static final int ID_NOTIF = 1;
    private static final String EXTRA_RESULT_RECEIVER = "extraResultReceiver";


    private ApiCallback<CurrentWeatherDTO> currentWeatherCallback = new ApiCallback<CurrentWeatherDTO>() {
        @Override
        public void onSuccess(CurrentWeatherDTO result) {
            if (result == null)
                return;

            final CurrentWeather currentWeather = DTOFactory.INSTANCE.createCurrentWeather(result);
            int count = instance.addCurrentWeather(currentWeather);
            if (count > 0) {
                Log.d(TAG, "Added current weather " + count + " record(s) to DB.");
                Prefs.setLastUpdateTime(WeatherService.this, new Date().getTime());
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

    public static Intent newIntent(Context context, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        return intent;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WeatherService.class);
        return intent;
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
        setIntentRedelivery(true);
        instance = WeatherStation.getInstance(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(Intent intent) {

        if(!isNetworkAvailableAndConnected())
            return;

        Log.i(TAG, "Received an intent: " + intent);
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

        WeatherApi api = new WeatherApi();

        List<Place> places = instance.getPlaces();
        for (Place place : places) {
            Log.i(TAG, "Get weather for: " + place.getName());

            api.getCurrentWeather(place.getCityId(), currentWeatherCallback);
            api.getFiveDayForecastByPlaceId(place.getCityId(), fiveDayDTOCallback);

        }

        if (receiver != null)
            receiver.send(STATUS_FINISHED, Bundle.EMPTY);

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
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
