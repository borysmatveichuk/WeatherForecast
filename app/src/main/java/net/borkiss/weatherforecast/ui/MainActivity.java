package net.borkiss.weatherforecast.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.URIBuildHelper;
import net.borkiss.weatherforecast.api.WeatherApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.testText);
        String tes1 = URIBuildHelper.createUriCurrentWeather("Nikolaev");
        String tes2 = URIBuildHelper.createUriCurrentWeather(123456);
        String tes3 = URIBuildHelper.createUriCurrentWeather(123, 456);
        String tes4 = URIBuildHelper.createUriFiveDayWeather(123456);

        textView.setText(tes1 + "\n"
                + tes2 + "\n"
                + tes3 + "\n"
                + tes4 + "\n");

        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherApi().getCurrentWeather();
            }
        });
    }
}
