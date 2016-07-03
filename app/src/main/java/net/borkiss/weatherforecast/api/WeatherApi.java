package net.borkiss.weatherforecast.api;

import android.util.Log;

import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.dto.JSONAdapterFactory;
import net.borkiss.weatherforecast.dto.PlaceDTO;
import net.borkiss.weatherforecast.dto.WrapperSearchResultDTO;
import net.borkiss.weatherforecast.util.Utils;
import net.borkiss.weatherforecast.util.WeatherApplication;

import java.util.List;

public class WeatherApi extends WeatherRestAbs {

    private static final String TAG = WeatherApi.class.getSimpleName();

    public void getCurrentWeather(final int cityId, final ApiCallback<CurrentWeatherDTO> callback) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGetTest(URIBuildHelper.createUriCurrentWeather(cityId));

                switch (result.getHttpCode()) {
                    case HTTP_OK:
                        callback.onSuccess(parseCurrentWeatherFromResult(result));
                        Log.d(TAG, "OK! " + result.getContent());
                        break;
                    case IO_EXCEPTION_ERROR:
                        ApiError error = ApiError.IO_ERROR;
                        error.setMessage(result.getContent());
                        callback.onError(error);
                        Log.e(TAG, "IO_EXCEPTION_ERROR: " + result.getException());
                        break;
                    default:
                        callback.onError(ApiError.SERVER_ERROR);
                        Log.e(TAG, "ERROR: " + result.getException());
                }
            }
        };

        execService.submit(task);
    }

    public void getPlacesByName(final String placeName, final ApiCallback<List<PlaceDTO>> callback) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGet(URIBuildHelper.createUriFindCityByName(placeName));

                switch (result.getHttpCode()) {
                    case HTTP_OK:
                        callback.onSuccess(parsePlaceFromResult(result));
                        break;
                    case IO_EXCEPTION_ERROR:
                        ApiError error = ApiError.IO_ERROR;
                        error.setMessage(result.getContent());
                        callback.onError(error);
                        break;
                    default:
                        callback.onError(ApiError.SERVER_ERROR);
                }


            }
        };

        execService.submit(task);
    }

//    @Override
    public HTTPResult doGetTest(String url) {
        HTTPResult testResult = new HTTPResult();
        testResult.setHttpCode(HTTP_OK);
        //testResult.setContent(Utils.loadAssetTextAsString(WeatherApplication.getInstance().getApplicationContext(), "find_test_result"));
        testResult.setContent(Utils.loadAssetTextAsString(WeatherApplication.getInstance().getApplicationContext(), "nikolaev_test_result"));
        return testResult;
    }

    private List<PlaceDTO> parsePlaceFromResult(HTTPResult result) {

        List<PlaceDTO> places = null;

        if (result != null && result.getContent() != null) {
            try {
                WrapperSearchResultDTO wrapper = JSONAdapterFactory.getJsonSearchResultWrapperAdapter()
                        .createFromJSONString(result.getContent());
                if (wrapper != null)
                    places = wrapper.getPlaceDTOList();
            } catch (Exception e) {
                Log.e(TAG, "error parsing " + e);
                // error during parsing - wrong incoming data
            }
        }

        return places;
    }

    private CurrentWeatherDTO parseCurrentWeatherFromResult(HTTPResult result) {

        CurrentWeatherDTO weather = null;

        if (result != null && result.getContent() != null) {
            try {
                CurrentWeatherDTO adapter = JSONAdapterFactory.getJsonCurrentWeatherAdapter()
                        .createFromJSONString(result.getContent());
                if (adapter != null)
                    weather = adapter;
            } catch (Exception e) {
                Log.e(TAG, "error parsing " + e);
                // error during parsing - wrong incoming data
            }
        }

        return weather;
    }
}
