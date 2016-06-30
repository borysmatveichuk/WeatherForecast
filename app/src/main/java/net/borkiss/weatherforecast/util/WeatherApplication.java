package net.borkiss.weatherforecast.util;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherApplication extends Application {

    private volatile static WeatherApplication instance;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static WeatherApplication getInstance() {
        return instance;
    }

    /**
     * @return the executor
     */
    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * @return the uiHandler
     */
    public Handler getUiHandler() {
        return uiHandler;
    }
}
