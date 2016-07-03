package net.borkiss.weatherforecast.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.WeatherApi;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.ui.PlacesActivity;

import java.util.List;

public class PageContainerFragment extends Fragment {

    private List<Place> places;
    private ViewPager viewPager;
    private Place currentPlace;

    public static PageContainerFragment newInstance() {

        Bundle args = new Bundle();

        PageContainerFragment fragment = new PageContainerFragment();
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
        return inflater.inflate(R.layout.fragment_page_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                currentPlace = places.get(position);
                return WeatherFragment.newInstance(places.get(position));
            }

            @Override
            public int getCount() {
                return places.size();
            }
        });
    }

    private void updateUI() {
        places = WeatherStation.getInstance(getActivity()).getPlaces();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();

        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_page_container, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_places:
                Intent intent = PlacesActivity.newIntent(getActivity(), PlacesActivity.TYPE.EDIT);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
