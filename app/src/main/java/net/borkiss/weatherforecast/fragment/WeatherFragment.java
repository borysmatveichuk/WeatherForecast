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
import android.widget.ImageView;
import android.widget.TextView;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.adapter.DividerItemDecoration;
import net.borkiss.weatherforecast.adapter.ForecastAdapter;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.CurrentWeather;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.util.Utils;

import java.util.Date;
import java.util.List;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_PLACE_ID = "placeId";

    private Place place;
    private TextView placeName;
    private ImageView imgWeatherIcon;
    //private TextView txtWeather;
    private TextView txtWeatherDescription;
    private TextView txtDate;
    private TextView txtTemperature;
    private TextView txtTemperatureMinMax;
    private TextView txtWind;
    private TextView txtHumidity;
    private TextView txtClouds;
    private TextView txtPressure;
    private TextView txtSunriseSunset;

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

        //txtWeather = (TextView) view.findViewById(R.id.txtWeather);
        txtWeatherDescription = (TextView) view.findViewById(R.id.txtWeatherDescription);
        imgWeatherIcon = (ImageView) view.findViewById(R.id.imgWeatherIcon);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);
        txtTemperatureMinMax = (TextView) view.findViewById(R.id.txtTemperatureMinMax);
        txtWind = (TextView) view.findViewById(R.id.txtWind);
        txtHumidity = (TextView) view.findViewById(R.id.txtHumidity);
        txtClouds = (TextView) view.findViewById(R.id.txtClouds);
        txtPressure = (TextView) view.findViewById(R.id.txtPressure);
        txtSunriseSunset = (TextView) view.findViewById(R.id.txtSunriseSunset);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);

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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "on Start " + place.getName());
    }

    private void updateUI() {
        CurrentWeather weather = WeatherStation.getInstance(getActivity())
                .getCurrentWeatherByCityId(place.getCityId());

        List<ForecastFiveDay> forecastFiveDayList = WeatherStation.getInstance(getActivity())
                .getListForecastFiveDayByCityId(place.getCityId());

        placeName.setText(place.getName() + " "+ new Date().toString());
        //txtWeather.setText(weather.getWeatherMain());
        txtWeatherDescription.setText(weather.getWeatherDescription());
        txtDate.setText(Utils.formatDate(weather.getTime()));

        imgWeatherIcon.setImageResource(Utils.getIconResourceForWeatherCondition(
                weather.getWeatherConditionId()));

        String temperature = Utils.formatTemperature(getActivity(), weather.getTemperature());
        txtTemperature.setText(temperature);

        String temperatureMin = Utils.formatTemperature(getActivity(), weather.getMinTemperature());
        String temperatureMax = Utils.formatTemperature(getActivity(), weather.getMaxTemperature());

        txtTemperatureMinMax.setText(String.format(getString(R.string.format_temperature_min_max),
                temperatureMin, temperatureMax));

        txtWind.setText(Utils.getFormattedWind(getActivity(),
                weather.getWindSpeed(), weather.getWindDegree()));

        txtHumidity.setText(String.format(getString(R.string.format_humidity),
                weather.getHumidity()));
        txtClouds.setText(String.format(getString(R.string.format_clouds),
                weather.getClouds()));

        txtPressure.setText(String.format(getString(R.string.format_pressure),
                weather.getPressure()));

        String sunrise = Utils.formatTime(weather.getSunrise());
        String sunset = Utils.formatTime(weather.getSunset());
        txtSunriseSunset.setText(String.format(getString(R.string.format_sunrise_sunset),
                sunrise, sunset));

        if (adapter == null) {
            adapter = new ForecastAdapter(getActivity(), forecastFiveDayList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setForecastList(forecastFiveDayList);
            adapter.notifyDataSetChanged();
        }
    }

}
