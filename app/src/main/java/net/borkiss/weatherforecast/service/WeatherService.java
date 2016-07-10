package net.borkiss.weatherforecast.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
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
import net.borkiss.weatherforecast.ui.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WeatherService extends Service {

    private static final String TAG = WeatherService.class.getSimpleName();

    private static final int ID_NOTIF = 1;
    private static final int INTERVAL = 1000 * 60 * 15; // 15 MINUTES

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

    private final LogScheduledThreadPoolExecutor execService = new LogScheduledThreadPoolExecutor(1);
    private WeatherStation instance;
    private NotificationManager nm;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = WeatherStation.getInstance(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        execService.scheduleWithFixedDelay(task, 0, INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        execService.shutdownNow();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable task = new Runnable() {

        @Override
        public void run() {


            if (!isNetworkAvailableAndConnected())
                return;


            WeatherApi api = new WeatherApi();

            List<Place> places = instance.getPlaces();
            for (Place place : places) {
                Log.i(TAG, "Get weather for: " + place.getName());

                api.getCurrentWeather(place.getCityId(), currentWeatherCallback);
                api.getFiveDayForecastByPlaceId(place.getCityId(), fiveDayDTOCallback);

            }

            sendNotification();
        }

    };

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        return isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
    }

    private void sendNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int notifDefaultsSettings = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notif = new Notification.Builder(getApplicationContext())
                .setContentTitle(getApplicationContext().getResources().getString(R.string.app_name))
                        .setContentText("Get forecast")
                        .setContentIntent(pIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        //.setDefaults(notifDefaultsSettings)
                        .getNotification();

        nm.notify(ID_NOTIF, notif);
    }
}
