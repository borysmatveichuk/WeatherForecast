package net.borkiss.weatherforecast.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.ApiCallback;
import net.borkiss.weatherforecast.api.ApiError;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.dto.PlaceDTO;
import net.borkiss.weatherforecast.util.WeatherApplication;

import java.util.List;

public class PlaceListFragment extends Fragment implements ApiCallback<List<PlaceDTO>> {

    private static final String TAG = PlaceListFragment.class.getSimpleName();
    private Handler handler = new Handler(Looper.getMainLooper());

    private TextView textView;

    public static PlaceListFragment newInstance() {

        Bundle args = new Bundle();

        PlaceListFragment fragment = new PlaceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.txtPlace);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place_list, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                new WeatherApi().getPlacesByName(s, PlaceListFragment.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                textView.setText(s);
                return false;
            }
        });

    }

    @Override
    public void onSuccess(final List<PlaceDTO> result) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (result == null)
                    return;

                if (getActivity() == null)
                    return;

                PlaceDTO placeDTO = result.get(0);
                textView.setText(placeDTO.getName() + " " +
                placeDTO.getId() + " " +
                placeDTO.getCountry());
            }
        });
    }

    @Override
    public void onError(final ApiError error) {

    }
}
