package net.borkiss.weatherforecast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.Place;

import java.util.List;

public class PageContainerFragment extends Fragment {

    private List<Place> places;

    public static PageContainerFragment newInstance() {

        Bundle args = new Bundle();

        PageContainerFragment fragment = new PageContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        places = WeatherStation.getInstance(getActivity()).getPlaces();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return WeatherFragment.newInstance(places.get(position));
            }

            @Override
            public int getCount() {
                return places.size();
            }
        });
    }
}
