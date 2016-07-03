package net.borkiss.weatherforecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.dto.CurrentWeatherDTO;
import net.borkiss.weatherforecast.model.Place;

public class WeatherFragment extends Fragment implements ApiCallback<CurrentWeatherDTO> {

    private static final String ARG_PLACE_ID = "placeId";

    private Place place;
    private TextView placeName;

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

        new WeatherApi().getCurrentWeather(place.getCityId(), this);
        Toast.makeText(getActivity(), "Get weather " + place.getCityId(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onSuccess(CurrentWeatherDTO result) {
        placeName.setText(result.getWeatherMain() );
    }

    @Override
    public void onError(ApiError error) {

    }
}
