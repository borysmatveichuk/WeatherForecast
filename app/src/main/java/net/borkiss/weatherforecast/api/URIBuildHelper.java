package net.borkiss.weatherforecast.api;

import android.net.Uri;

/**
 * Helper class with static methods to build URIs for REST service methods
 *
 */

public class URIBuildHelper {

    public static String SCHEMA = "http";

    // QA
    public static final String QA_SERVER = "api.openweathermap.org";
    // STAGE
    public static final String STAGE_SERVER = "api.openweathermap.org";
    // PRODUCTION
    public static final String PRODUCTION_SERVER = "api.openweathermap.org";

    public static final String SERVER = QA_SERVER;

    private static final String API_KEY = "46c8b7efcac89c7a3f1b4e463e6fe010";

    private static final String DATA_SECTION = "data/2.5";

    private static final String KEY_CURRENT_WEATHER = "weather";
    private static final String KEY_FIVE_DAY_WEATHER = "forecast";
    private static final String KEY_FIND = "find";
    private static final String KEY_SEARCH_PARAM = "type";
    private static final String KEY_CITY_NAME = "q";
    private static final String KEY_LATITUDE = "lat";
    private static final String KEY_LONGITUDE = "lon";
    private static final String KEY_CITY_ID = "id";
    private static final String KEY_API_ID = "appid";
    private static final String KEY_UNITS = "units";

    private static final String VALUE_SEARCH_PARAM_LIKE = "like";
    private static final String VALUE_UNITS_METRIC = "metric";
    private static final String VALUE_UNITS_IMPERIAL = "imperial";

    public static String createUriFindCityByName(String cityName) {
        return createCommonUri()
                .appendEncodedPath(KEY_FIND)
                .appendQueryParameter(KEY_CITY_NAME, cityName)
                .appendQueryParameter(KEY_SEARCH_PARAM, VALUE_SEARCH_PARAM_LIKE)
                .appendQueryParameter(KEY_API_ID, API_KEY)
                .toString();
    }

    public static String createUriCurrentWeather(String cityName) {
        return createCommonUri()
                .appendEncodedPath(KEY_CURRENT_WEATHER)
                .appendQueryParameter(KEY_CITY_NAME, cityName)
                .appendQueryParameter(KEY_API_ID, API_KEY)
                .appendQueryParameter(KEY_UNITS, VALUE_UNITS_METRIC)
                .toString();
    }

    public static String createUriCurrentWeather(int cityId) {
        return createCommonUri()
                .appendEncodedPath(KEY_CURRENT_WEATHER)
                .appendQueryParameter(KEY_CITY_ID, Integer.toString(cityId))
                .appendQueryParameter(KEY_API_ID, API_KEY)
                .appendQueryParameter(KEY_UNITS, VALUE_UNITS_METRIC)
                .toString();
    }

    public static String createUriCurrentWeather(int latitude, int longitude) {
        return createCommonUri()
                .appendEncodedPath(KEY_CURRENT_WEATHER)
                .appendQueryParameter(KEY_LATITUDE, Integer.toString(latitude))
                .appendQueryParameter(KEY_LONGITUDE, Integer.toString(longitude))
                .appendQueryParameter(KEY_API_ID, API_KEY)
                .appendQueryParameter(KEY_UNITS, VALUE_UNITS_METRIC)
                .toString();
    }

    public static String createUriFiveDayWeather(int cityId) {
        return createCommonUri()
                .appendEncodedPath(KEY_FIVE_DAY_WEATHER)
                .appendQueryParameter(KEY_CITY_ID, Integer.toString(cityId))
                .appendQueryParameter(KEY_API_ID, API_KEY)
                .appendQueryParameter(KEY_UNITS, VALUE_UNITS_METRIC)
                .toString();
    }

    private static Uri.Builder createCommonUri() {
        return new Uri.Builder().scheme(SCHEMA)
                .encodedAuthority(SERVER)
                .appendEncodedPath(DATA_SECTION);
    }



}
