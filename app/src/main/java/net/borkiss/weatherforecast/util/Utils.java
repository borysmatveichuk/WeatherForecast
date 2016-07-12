package net.borkiss.weatherforecast.util;

import android.content.Context;

import android.util.Log;

import net.borkiss.weatherforecast.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static String formatTemperature(Context context, double temperature) {
        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    public static String formatDate(Date date) {
        if (date == null)
            return "";

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(tz);

        return sdf.format(date);
    }

    public static String formatTime(Date date) {
        if (date == null)
            return "";

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(tz);

        return sdf.format(date);
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {

        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }

        return String.format(context.getString(R.string.format_wind), windSpeed, direction);
    }

    // Based on weather code data found at:
    // http://openweathermap.org/weather-conditions
    public static int getIconResourceForWeatherCondition(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232) { //Group 2xx: Thunderstorm
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) { //Group 3xx: Drizzle
//            return R.drawable.ic_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {  // Rain
//            return R.drawable.ic_rain;
//        } else if (weatherId == 511) { //	freezing rain
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {  //shower rain
//            return R.drawable.ic_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {  // Group 6xx: Snow
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 701 && weatherId <= 741) {  //fog
//            return R.drawable.ic_fog;
//        } else if (weatherId == 741 || weatherId == 781) {  //  sand, dust
//            return R.drawable.ic_storm;
        } else if (weatherId == 800) {      //	clear sky
            return R.mipmap.ic_weather_clear;
//        } else if (weatherId == 801) {   	// few clouds
//            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {  //	cloudy
            return R.mipmap.ic_weather_cloudy;
        }
        return R.mipmap.ic_launcher;
    }


    public static String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing asset " + name);
                }
            }
        }

        return null;
    }
}
