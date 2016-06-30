package net.borkiss.weatherforecast.api;

import android.util.Log;

import net.borkiss.weatherforecast.dto.JSONAdapterFactory;
import net.borkiss.weatherforecast.dto.PlaceDTO;
import net.borkiss.weatherforecast.dto.WrapperSearchResultDTO;
import net.borkiss.weatherforecast.util.Utils;
import net.borkiss.weatherforecast.util.WeatherApplication;

import java.util.ArrayList;
import java.util.List;

public class WeatherApi extends WeatherRestAbs {

    private static final String TAG = WeatherApi.class.getSimpleName();

    public void getCurrentWeather(final int cityId) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGet(URIBuildHelper.createUriCurrentWeather(cityId));

                switch (result.getHttpCode()) {
                    case HTTP_OK:
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

    public void getPlacesByName(final String placeName, final ApiCallback<List<PlaceDTO>> callback) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                final HTTPResult result = doGet(URIBuildHelper.createUriFindCityByName(placeName));

                switch (result.getHttpCode()) {
                    case HTTP_OK:
                        callback.onSuccess(parseFromResult(result));
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
//    public HTTPResult doGet(String url) {
//        HTTPResult testResult = new HTTPResult();
//        testResult.setHttpCode(HTTP_OK);
//        testResult.setContent(Utils.loadAssetTextAsString(WeatherApplication.getInstance().getApplicationContext(), "find_test_result"));
//        return testResult;
//    }

    private List<PlaceDTO> parseFromResult(HTTPResult result) {

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
}
