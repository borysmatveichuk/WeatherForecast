package net.borkiss.weatherforecast.util;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import net.borkiss.weatherforecast.service.WeatherService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherApplication extends Application {

    private volatile static WeatherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

//        Intent intent = WeatherService.newIntent(getActivity());
//        getActivity().startService(intent);

        if (!WeatherService.isServiceAlarmOn(instance)) {
            WeatherService.setServiceAlarm(instance, true);
        }
    }

    public static WeatherApplication getInstance() {
        return instance;
    }

}
