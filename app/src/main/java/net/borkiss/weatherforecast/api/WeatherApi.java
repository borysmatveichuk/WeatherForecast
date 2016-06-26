package net.borkiss.weatherforecast.api;

import android.util.Log;

import java.io.IOException;

public class WeatherApi extends WeatherRestAbs {

    private static final String TAG = WeatherApi.class.getSimpleName();

    public void getCurrentWeather(final int cityId) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGet(URIBuildHelper.createUriCurrentWeather(cityId));

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
