package net.borkiss.weatherforecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.adapter.ForecastAdapter;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherFragment extends Fragment {

    private static final String ARG_PLACE_ID = "placeId";

    private Place place;
    private TextView placeName;
    private TextView txtWeather;
    private TextView txtWeatherDescription;
    private TextView txtDate;
    private TextView txtTemperature;

    private RecyclerView recyclerView;
    private ForecastAdapter adapter;


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
        placeName.setText(place.getName());

        txtWeather = (TextView) view.findViewById(R.id.txtWeather);
        txtWeatherDescription = (TextView) view.findViewById(R.id.txtWeatherDescription);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

    private void updateUI() {
        CurrentWeather weather = WeatherStation.getInstance(getActivity())
                .getCurrentWeatherByCityId(place.getCityId());

        List<ForecastFiveDay> forecastFiveDayList = WeatherStation.getInstance(getActivity())
                .getListForecastFiveDayByCityId(place.getCityId());

        txtWeather.setText(weather.getWeatherMain());
        txtWeatherDescription.setText(weather.getWeatherDescription());
        txtDate.setText(Utils.formatDate(weather.getTime()));

        String temperature = Utils.formatTemperature(getActivity(), weather.getTemperature());
        txtTemperature.setText(temperature);

        if (adapter == null) {
            adapter = new ForecastAdapter(getActivity(), forecastFiveDayList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setForecastList(forecastFiveDayList);
            adapter.notifyDataSetChanged();
        }
    }

}
