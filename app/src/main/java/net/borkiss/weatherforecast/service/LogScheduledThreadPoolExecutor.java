package net.borkiss.weatherforecast.service;

import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LogScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {


    private static final String TAG = LogScheduledThreadPoolExecutor.class.getSimpleName();

    public LogScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }


    @Override
    public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }


    private Runnable wrapRunnable(Runnable command) {
        return new LogOnExceptionRunnable(command);
    }

    private class LogOnExceptionRunnable implements Runnable {
        private Runnable theRunnable;

        public LogOnExceptionRunnable(Runnable theRunnable) {
            super();
            this.theRunnable = theRunnable;
        }

        @Override
        public void run() {
            try {
                theRunnable.run();
            } catch (Exception e) {
                // LOG IT HERE!!!
                Log.e(TAG, "error in executing: " + theRunnable + ". It will no longer be run!");

                // and re throw it so that the Executor also gets this error so
                // that it can do what it would
                // usually do
                throw new RuntimeException(e);
            }
        }
    }

}
