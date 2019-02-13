package net.borkiss.weatherforecast.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.fragment.PageContainerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment container = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (container == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, PageContainerFragment.newInstance())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
