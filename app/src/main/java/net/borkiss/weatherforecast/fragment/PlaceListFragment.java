package net.borkiss.weatherforecast.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.adapter.DividerItemDecoration;
import net.borkiss.weatherforecast.adapter.PlaceAdapter;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.Place;
import net.borkiss.weatherforecast.ui.PlacesActivity;

import java.util.List;

public class PlaceListFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    private static final String TAG = PlaceListFragment.class.getSimpleName();
    private Handler handler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private List<Place> places;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        places = WeatherStation.getInstance(getActivity()).getPlaces();
        if (adapter == null) {
            adapter = new PlaceAdapter(getActivity(), places);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {
                        case R.id.btnDelete:
                            WeatherStation.getInstance(getActivity()).deletePlace(places.get(position));
                            break;
                        default:
                            Toast.makeText(getActivity(), "Place is " + places.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            adapter.setPlaces(places);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place_list, menu);

        menu.findItem(R.id.menu_item_add).setOnMenuItemClickListener(this);
        menu.findItem(R.id.menu_item_edit).setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_item_add:

                Intent intent = PlacesActivity.newIntent(getActivity(), PlacesActivity.TYPE.ADD);
                startActivity(intent);
                return true;

            case R.id.menu_item_edit:

                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}