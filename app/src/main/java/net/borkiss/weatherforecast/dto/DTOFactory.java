package net.borkiss.weatherforecast.dto;

import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.Place;

import java.util.ArrayList;
import java.util.List;

public enum DTOFactory {
     INSTANCE;

    public Place createPlace(PlaceDTO dto) {
        if (dto == null)
            return null;

        Place place = new Place();
        place.setCityId(dto.getCityId());
        place.setName(dto.getName());
        place.setCountry(dto.getCountry());
        place.setLatitude(dto.getLatitude());
        place.setLongitude(dto.getLongitude());

        return place;
    }

    public List<Place> createPlaces(List<PlaceDTO> dtoList) {
        if (dtoList == null)
            return null;

        List<Place> list = new ArrayList<>();
        for (PlaceDTO dto : dtoList) {
            list.add(createPlace(dto));
        }

        return list;
    }

    public CurrentWeather createCurrentWeather(CurrentWeatherDTO dto) {
        if (dto == null)
            return null;

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTime(dto.getTime());
        currentWeather.setPlaceId(dto.getPlaceId());
        currentWeather.setWeatherMain(dto.getWeatherMain());
        currentWeather.setWeatherDescription(dto.getWeatherDescription());
        currentWeather.setTemperature(dto.getTemperature());
        currentWeather.setPressure(dto.getPressure());
        currentWeather.setHumidity(dto.getHumidity());
        currentWeather.setMinTemperature(dto.getMinTemperature());
        currentWeather.setMaxTemperature(dto.getMaxTemperature());
        currentWeather.setWindSpeed(dto.getWindSpeed());
        currentWeather.setWindDegree(dto.getWindDegree());
        currentWeather.setClouds(dto.getClouds());

        return currentWeather;
    }
}
