package net.borkiss.weatherforecast.db;

public class WeatherDbSchema {
    public static final class CurrentWeatherTable {
        public static final String NAME = "currentWeather";

        public static final class Cols {
            public static final String PLACE = "place";
            public static final String TIME = "time";
            public static final String DOCUMENT = "document";
        }
    }

    public static final class ForecastFiveDayTable {
        public static final String NAME = "forecastFiveDay";

        public static final class Cols {
            public static final String PLACE = "place";
            public static final String TIME = "time";
            public static final String DOCUMENT = "document";
        }
    }

    public static final class PlacesTable {
        public static final String NAME = "places";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String ID = "id";
            public static final String COUNTRY = "country";
            public static final String COORD_LON = "coordLon";
            public static final String COORD_LAT = "coordLat";
        }
    }
}
