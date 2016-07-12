package net.borkiss.weatherforecast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.borkiss.weatherforecast.db.WeatherDbSchema.CurrentWeatherTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.ForecastFiveDayTable;
import net.borkiss.weatherforecast.db.WeatherDbSchema.PlacesTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "weatherBase.db";

    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + PlacesTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PlacesTable.Cols.NAME + ", " +
                PlacesTable.Cols.CITY_ID + " INTEGER , " +
                PlacesTable.Cols.COUNTRY + ", " +
                PlacesTable.Cols.COORD_LON + ", " +
                PlacesTable.Cols.COORD_LAT +
                ")"
        );

        sqLiteDatabase.execSQL("create table " + CurrentWeatherTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CurrentWeatherTable.Cols.PLACE_ID + " INTEGER , " +
                CurrentWeatherTable.Cols.TIME + " INTEGER , " +
                CurrentWeatherTable.Cols.CONDITION_ID + " INTEGER , " +
                CurrentWeatherTable.Cols.WEATHER_MAIN + ", " +
                CurrentWeatherTable.Cols.WEATHER_DESCRIPTION + ", " +
                CurrentWeatherTable.Cols.TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.PRESSURE + " REAL , " +
                CurrentWeatherTable.Cols.HUMIDITY + " INTEGER , " +
                CurrentWeatherTable.Cols.MIN_TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.MAX_TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.WIND_SPEED + " REAL , " +
                CurrentWeatherTable.Cols.WIND_DEGREE + " REAL , "+
                CurrentWeatherTable.Cols.CLOUDS + " INTEGER ," +
                CurrentWeatherTable.Cols.SUNRISE + " INTEGER , " +
                CurrentWeatherTable.Cols.SUNSET + " INTEGER " +
                ")"
        );

        sqLiteDatabase.execSQL("create table " + ForecastFiveDayTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ForecastFiveDayTable.Cols.PLACE_ID + " INTEGER , " +
                ForecastFiveDayTable.Cols.TIME + ", " +
                CurrentWeatherTable.Cols.CONDITION_ID + " INTEGER , " +
                CurrentWeatherTable.Cols.WEATHER_MAIN + ", " +
                CurrentWeatherTable.Cols.WEATHER_DESCRIPTION + ", " +
                CurrentWeatherTable.Cols.TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.PRESSURE + " REAL , " +
                CurrentWeatherTable.Cols.HUMIDITY + " INTEGER , " +
                CurrentWeatherTable.Cols.MIN_TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.MAX_TEMPERATURE + " REAL , " +
                CurrentWeatherTable.Cols.WIND_SPEED + " REAL , " +
                CurrentWeatherTable.Cols.WIND_DEGREE + " REAL , "+
                CurrentWeatherTable.Cols.CLOUDS + " INTEGER " +
                ")"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
