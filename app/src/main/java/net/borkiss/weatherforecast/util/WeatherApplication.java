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
    }

    public static WeatherApplication getInstance() {
        return instance;
    }

}
