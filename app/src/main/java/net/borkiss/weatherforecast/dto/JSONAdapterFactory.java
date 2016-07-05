package net.borkiss.weatherforecast.dto;

import com.google.gson.reflect.TypeToken;

import net.borkiss.weatherforecast.model.CurrentWeather;

import java.lang.reflect.Type;
import java.util.List;

public class JSONAdapterFactory {

    private static JSONPlaceAdapter placeAdapter;
    private static JSONSearchResultWrapperAdapter searchResultWrapperAdapter;
    private static JSONCurrentWeatherAdapter currentWeatherAdapter;
    private static JSONForecastFiveDayWrapperAdapter forecastFiveDayWrapperAdapter;

    public static class JSONPlaceAdapter extends JSONEntityAdapter<PlaceDTO> {

        private JSONPlaceAdapter() {
        }

        @Override
        protected Class<PlaceDTO> getType() {
            return PlaceDTO.class;
        }

        @Override
        protected Type getCollectionType() {
            return new TypeToken<List<PlaceDTO>>() {
            }.getType();
        }
    }

    public static class JSONSearchResultWrapperAdapter extends JSONEntityAdapter<WrapperSearchResultDTO> {

        private JSONSearchResultWrapperAdapter() {
        }

        @Override
        protected Class<WrapperSearchResultDTO> getType() {
            return WrapperSearchResultDTO.class;
        }

        @Override
        protected Type getCollectionType() {
            return new TypeToken<List<WrapperSearchResultDTO>>() {
            }.getType();
        }
    }

    public static class JSONCurrentWeatherAdapter extends JSONEntityAdapter<CurrentWeatherDTO> {

        private JSONCurrentWeatherAdapter() {
        }

        @Override
        protected Class<CurrentWeatherDTO> getType() {
            return CurrentWeatherDTO.class;
        }

        @Override
        protected Type getCollectionType() {
            return new TypeToken<List<CurrentWeatherDTO>>() {
            }.getType();
        }
    }

    public static class JSONForecastFiveDayWrapperAdapter extends JSONEntityAdapter<WrapperForecastFiveDayDTO> {

        private JSONForecastFiveDayWrapperAdapter() {
        }

        @Override
        protected Class<WrapperForecastFiveDayDTO> getType() {
            return WrapperForecastFiveDayDTO.class;
        }

        @Override
        protected Type getCollectionType() {
            return new TypeToken<List<WrapperForecastFiveDayDTO>>() {
            }.getType();
        }
    }

    public synchronized static JSONPlaceAdapter getJsonPlaceAdapter() {
        if (placeAdapter == null)
            placeAdapter = new JSONPlaceAdapter();
        return placeAdapter;
    }

    public synchronized static JSONSearchResultWrapperAdapter getJsonSearchResultWrapperAdapter() {
        if (searchResultWrapperAdapter == null)
            searchResultWrapperAdapter = new JSONSearchResultWrapperAdapter();
        return searchResultWrapperAdapter;
    }

    public synchronized static JSONCurrentWeatherAdapter getJsonCurrentWeatherAdapter() {
        if (currentWeatherAdapter == null)
            currentWeatherAdapter = new JSONCurrentWeatherAdapter();
        return currentWeatherAdapter;
    }

    public synchronized static JSONForecastFiveDayWrapperAdapter getForecastFiveDayWrapperAdapter() {
        if (forecastFiveDayWrapperAdapter == null)
            forecastFiveDayWrapperAdapter = new JSONForecastFiveDayWrapperAdapter();
        return forecastFiveDayWrapperAdapter;
    }

}
