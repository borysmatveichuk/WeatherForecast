package net.borkiss.weatherforecast.api;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherRestAbs  implements HTTPMethodInvoker {

    private final static String TAG = WeatherRestAbs.class.getSimpleName();
    protected static final int IO_EXCEPTION_ERROR = -2;

    protected static final int HTTP_OK = HttpURLConnection.HTTP_OK;

    protected static final ExecutorService execService = Executors.newCachedThreadPool();

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public HTTPResult doGet(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        HTTPResult result = new HTTPResult();
        try {
            Response response = client.newCall(request).execute();

            result.setHttpCode(response.code());
            if (response.isSuccessful()) {
                result.setContent(response.body().string());
            }
        } catch (IOException eio) {
            Log.i(TAG, "IOException in http connection", eio);
            result.setHttpCode(IO_EXCEPTION_ERROR);
            result.setException(eio);
        } catch (Exception e) {
            Log.e(TAG, "Error during GET " + url, e);
        }

        return result;
    }

}
