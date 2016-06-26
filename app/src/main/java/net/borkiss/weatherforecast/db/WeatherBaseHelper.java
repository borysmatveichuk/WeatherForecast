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
                PlacesTable.Cols.ID + ", " +
                PlacesTable.Cols.COUNTRY + ", " +
                PlacesTable.Cols.COORD_LON + ", " +
                PlacesTable.Cols.COORD_LAT +
                ")"
        );

        sqLiteDatabase.execSQL("create table " + CurrentWeatherTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CurrentWeatherTable.Cols.PLACE + ", " +
                CurrentWeatherTable.Cols.TIME + ", " +
                CurrentWeatherTable.Cols.DOCUMENT +
                ")"
        );

        sqLiteDatabase.execSQL("create table " + ForecastFiveDayTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ForecastFiveDayTable.Cols.PLACE + ", " +
                ForecastFiveDayTable.Cols.TIME + ", " +
                ForecastFiveDayTable.Cols.DOCUMENT +
                ")"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
