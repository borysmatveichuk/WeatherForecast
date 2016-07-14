package net.borkiss.weatherforecast.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.List;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_PLACE_ID = "placeId";

    private Place place;
    private TextView placeName;
    private ImageView imgWeatherIcon;
    private TextView txtWeather;
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
    private CurrentWeather weather;
    private List<ForecastFiveDay> forecastFiveDayList;


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
        setHasOptionsMenu(true);
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
        txtWeather.setVisibility(View.GONE);
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

        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weather, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getWeatherReport())
                        .setSubject(getString(R.string.weather_report_subject))
                        .getIntent();
                startActivity(Intent.createChooser(intent, getActivity().getString(R.string.send_report)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateUI() {

        weather = WeatherStation.getInstance(getActivity())
                .getCurrentWeatherByCityId(place.getCityId());

        forecastFiveDayList = WeatherStation.getInstance(getActivity())
                .getListForecastFiveDayByCityId(place.getCityId());

        Log.d(TAG, "updateUI " + place.getCityId() + " " + place.getName());
        Log.d(TAG, "Forecast is " + forecastFiveDayList);
        Log.d(TAG, "Weather is " + weather);

        placeName.setText(String.format(getString(R.string.format_place),
                place.getName(), place.getCountry()));

        txtWeatherDescription.setText(weather.getWeatherDescription());
        txtDate.setText(Utils.formatDate(weather.getTime()));

        int iconId = Utils.getIconResourceForWeatherCondition(
                weather.getWeatherConditionId());
        if (iconId >= 0) {
            imgWeatherIcon.setImageResource(Utils.getIconResourceForWeatherCondition(
                    weather.getWeatherConditionId()));
            txtWeather.setVisibility(View.GONE);
        } else {
            imgWeatherIcon.setVisibility(View.GONE);
            txtWeather.setVisibility(View.VISIBLE);
            txtWeather.setText(weather.getWeatherMain());
        }

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

    private String getWeatherReport() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format(getString(R.string.format_place),
                place.getName(), place.getCountry()));
        sb.append(" ");
        sb.append(Utils.formatDate(weather.getTime()));
        sb.append("\n");
        sb.append(String.format(getString(R.string.format_place),
                weather.getWeatherMain(), weather.getWeatherDescription()));
        sb.append("\n");
        sb.append(Utils.formatTemperature(getActivity(), weather.getTemperature()));
        sb.append("\n");
        sb.append(getString(R.string.title_wind));
        sb.append(": ");
        sb.append(Utils.getFormattedWind(getActivity(),
                weather.getWindSpeed(), weather.getWindDegree()));

        return sb.toString();

    }
}
