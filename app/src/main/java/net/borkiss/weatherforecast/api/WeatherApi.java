package net.borkiss.weatherforecast.api;

import android.util.Log;

import java.io.IOException;

/**
 * Created by Boris on 25.06.2016.
 */
public class WeatherApi extends WeatherRestAbs {

    private static final String TAG = WeatherApi.class.getSimpleName();

    public void getCurrentWeather() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGet(URIBuildHelper.createUriCurrentWeather(700569));

                switch (result.getHttpCode()) {
                    case 200:
                        Log.d(TAG, "OK! " + result.getContent());
                        break;
                    case IO_EXCEPTION_ERROR:
                        Log.e(TAG, "IO_EXCEPTION_ERROR: " + result.getException());
                        break;
                    default:
                        Log.e(TAG, "ERROR: " + result.getException());
                }
            }
        };

        execService.submit(task);
    }
}
