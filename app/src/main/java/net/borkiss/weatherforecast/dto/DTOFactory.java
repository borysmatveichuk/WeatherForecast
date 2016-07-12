package net.borkiss.weatherforecast.dto;

import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
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

    public List<Place> createPlaceList(List<PlaceDTO> dtoList) {
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
        currentWeather.setWeatherConditionId(dto.getWeatherConditionId());
        currentWeather.setTemperature(dto.getTemperature());
        currentWeather.setPressure(dto.getPressure());
        currentWeather.setHumidity(dto.getHumidity());
        currentWeather.setMinTemperature(dto.getMinTemperature());
        currentWeather.setMaxTemperature(dto.getMaxTemperature());
        currentWeather.setWindSpeed(dto.getWindSpeed());
        currentWeather.setWindDegree(dto.getWindDegree());
        currentWeather.setClouds(dto.getClouds());
        currentWeather.setSunrise(dto.getSunrise());
        currentWeather.setSunset(dto.getSunset());

        return currentWeather;
    }

    public ForecastFiveDay createForecastFiveDay(ForecastFiveDayDTO dto) {
        if (dto == null)
            return null;

        ForecastFiveDay forecast = new ForecastFiveDay();
        forecast.setTime(dto.getTime());
        forecast.setPlaceId(dto.getPlaceId());
        forecast.setWeatherConditionId(dto.getWeatherConditionId());
        forecast.setWeatherMain(dto.getWeatherMain());
        forecast.setWeatherDescription(dto.getWeatherDescription());
        forecast.setTemperature(dto.getTemperature());
        forecast.setPressure(dto.getPressure());
        forecast.setHumidity(dto.getHumidity());
        forecast.setMinTemperature(dto.getMinTemperature());
        forecast.setMaxTemperature(dto.getMaxTemperature());
        forecast.setWindSpeed(dto.getWindSpeed());
        forecast.setWindDegree(dto.getWindDegree());
        forecast.setClouds(dto.getClouds());

        return forecast;
    }

    public List<ForecastFiveDay> createForecastFiveDayList(List<ForecastFiveDayDTO> dtoList) {
        if (dtoList == null)
            return null;

        List<ForecastFiveDay> list = new ArrayList<>();
        for (ForecastFiveDayDTO dto : dtoList) {
            list.add(createForecastFiveDay(dto));
        }

        return list;
    }

}
