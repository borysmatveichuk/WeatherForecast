package net.borkiss.weatherforecast.db;

public class WeatherDbSchema {
    public static final class CurrentWeatherTable {
        public static final String NAME = "currentWeather";

        public static final class Cols {
            public static final String PLACE_ID = "place";
            public static final String TIME = "time";
            public static final String WEATHER_MAIN = "weatherMain";
            public static final String WEATHER_DESCRIPTION = "weatherDescription";
            public static final String TEMPERATURE = "temperature";
            public static final String PRESSURE = "pressure";
            public static final String HUMIDITY = "humidity";
            public static final String MIN_TEMPERATURE = "minTemperature";
            public static final String MAX_TEMPERATURE = "maxTemperature";
            public static final String WIND_SPEED = "windSpeed";
            public static final String WIND_DEGREE = "windDegree";
            public static final String CLOUDS = "clouds";
        }
    }

    public static final class ForecastFiveDayTable {
        public static final String NAME = "forecastFiveDay";

        public static final class Cols {
            public static final String PLACE_ID = "place";
            public static final String TIME = "time";
            public static final String DOCUMENT = "document";
        }
    }

    public static final class PlacesTable {
        public static final String NAME = "places";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String CITY_ID = "cityId";
            public static final String COUNTRY = "country";
            public static final String COORD_LON = "coordLon";
            public static final String COORD_LAT = "coordLat";
        }
    }
}
