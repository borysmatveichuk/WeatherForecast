package net.borkiss.weatherforecast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.fragment.PlaceListFragment;

public class PlacesActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, PlacesActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment container = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (container == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, PlaceListFragment.newInstance())
                    .commit();
        }
    }
}
