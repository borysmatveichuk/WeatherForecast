package net.borkiss.weatherforecast.dto;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JSONAdapterFactory {

    private static JSONPlaceAdapter placeAdapter;
    private static JSONSearchResultWrapperAdapter searchResultWrapperAdapter;

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

}
