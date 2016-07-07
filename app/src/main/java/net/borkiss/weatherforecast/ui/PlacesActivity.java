package net.borkiss.weatherforecast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.fragment.PlaceListFragment;
import net.borkiss.weatherforecast.fragment.PlaceSearchFragment;

/***
 *
 */

public class PlacesActivity extends AppCompatActivity {

    private static final String EXTRA_TYPE = "fragmentType";

    public enum TYPE {
        EDIT, ADD
    }

    public static Intent newIntent(Context context, @NonNull TYPE type) {
        Intent intent = new Intent(context, PlacesActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.place_activity_title));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            TYPE fragmentType = (TYPE) extras.getSerializable(EXTRA_TYPE);

            Fragment container = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            if (container == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, getFragment(fragmentType))
                        .commit();
            }
        }
    }

    private Fragment getFragment(TYPE type) {

        switch (type) {
            case ADD:
                return PlaceSearchFragment.newInstance();
            case EDIT:
                return PlaceListFragment.newInstance();
            default:
                return null;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
