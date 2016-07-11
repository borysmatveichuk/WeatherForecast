package net.borkiss.weatherforecast.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.SystemClock;
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
import net.borkiss.weatherforecast.util.Prefs;

import java.util.Date;
import java.util.List;

public class WeatherService extends IntentService {

    private static final String TAG = WeatherService.class.getSimpleName();

    public static final int INTERVAL = 1000 * 60 * 10; // 15 MINUTES
    private static final int ID_NOTIF = 1;

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
    private NotificationManager nm;

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
        setIntentRedelivery(true);
        instance = WeatherStation.getInstance(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

        Prefs.setLastUpdateTime(this, new Date().getTime());

        sendNotification();
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
            //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
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
                .setDefaults(notifDefaultsSettings)
                .getNotification();

        nm.notify(ID_NOTIF, notif);
    }
}
