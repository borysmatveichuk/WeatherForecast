package net.borkiss.weatherforecast.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.support.v4.os.ResultReceiver;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.service.WeatherService;
import net.borkiss.weatherforecast.ui.PlacesActivity;
import net.borkiss.weatherforecast.util.Prefs;

import java.util.Date;
import java.util.List;

public class PageContainerFragment extends Fragment {

    private static final int REQUEST_PLACES = 1;
    private static final String TAG = PageContainerFragment.class.getSimpleName();
    private static final String SAVE_RESULT_RECEIVER = "saveResultReceiver";

    private List<Place> places;
    private ViewPager viewPager;
    private ResultReceiver resultReceiver;

    public static PageContainerFragment newInstance() {

        Bundle args = new Bundle();

        PageContainerFragment fragment = new PageContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!WeatherService.isServiceAlarmOn(getActivity())) {
            WeatherService.setServiceAlarm(getActivity(), true);
        }

        if ((savedInstanceState != null)
                && (savedInstanceState.getParcelable(SAVE_RESULT_RECEIVER) != null)) {
            resultReceiver = savedInstanceState.getParcelable(SAVE_RESULT_RECEIVER);
            Log.d(TAG, "Restored receiver" + resultReceiver);
        } else {
            resultReceiver = new ServiceResultReceiver(new Handler());
            Log.d(TAG, "Created new receiver" + resultReceiver);
        }

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

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    private void updateUI() {

        places = WeatherStation.getInstance(getActivity()).getPlaces();

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return WeatherFragment.newInstance(places.get(position));
            }

            @Override
            public int getCount() {
                return places.size();
            }
        };
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
        Log.d(TAG, "Service is ON = " + WeatherService.isServiceAlarmOn(getActivity()));

        if (needUpdateImmediately()) {
            Intent intent = WeatherService.newIntent(getActivity(), resultReceiver);
            getActivity().startService(intent);
        } else {
            updateUI();
        }
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
                startActivityForResult(intent, REQUEST_PLACES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACES) {
            Intent intent = WeatherService.newIntent(getActivity());
            getActivity().startService(intent);

            Log.d(TAG, "onActivityResult");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_RESULT_RECEIVER, resultReceiver);
    }

    private boolean needUpdateImmediately() {
        return (new Date().getTime() - Prefs.getLastUpdateTime(getActivity())
                >= WeatherService.INTERVAL);
    }

    @SuppressLint({"RestrictedApi", "ParcelCreator"})
    private class ServiceResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */

        public ServiceResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //super.onReceiveResult(resultCode, resultData);
            Log.d(TAG, "Received result from service: " + resultCode);
            updateUI();
        }
    }
}
