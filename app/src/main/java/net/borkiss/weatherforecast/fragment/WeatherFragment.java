package net.borkiss.weatherforecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.Place;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherFragment extends Fragment {

    private static final String ARG_PLACE_ID = "placeId";

    private Place place;
    private TextView placeName;
    private TextView txtWeather;
    private TextView txtDate;

    public static WeatherFragment newInstance(Place place) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE_ID, place);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place = (Place) getArguments().getSerializable(ARG_PLACE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placeName = (TextView) view.findViewById(R.id.txtPlace);
        placeName.setText(place.getName() + new Date().toString());

        txtWeather = (TextView) view.findViewById(R.id.txtWeather);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(tz);

        String localTime = sdf.format(date); // I assume your timestamp is in seconds and you're converting to milliseconds?
        return localTime;
    }

    private void updateUI() {
        CurrentWeather weather = WeatherStation.getInstance(getActivity()).getCurrentWeatherByCityId(place.getCityId());

        txtWeather.setText(weather.toString());
        txtDate.setText(formatDate(weather.getTime()));
    }

}
