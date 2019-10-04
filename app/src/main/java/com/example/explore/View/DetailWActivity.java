package com.example.explore.View;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.explore.ForecastUtils;
import com.example.explore.R;
import com.example.explore.WeatherModel.Temperature;

public class DetailWActivity extends AppCompatActivity {

    public static final String EXTRA_TEMPERATURE = "TEMPERATURE";
    public static final String EXTRA_DAY_INDEX = "DAY_INDEX";
    public static final String EXTRA_PRESSURE = "PRESSURE";
    public static final String EXTRA_HUMIDITY = "HUMIDITY";
    public static final String EXTRA_WEATHER_CONDITION = "WEATHER_CONDITION";
    public static final String EXTRA_CLOUDS = "CLOUDS";

    private TextView mDateTV, mDayTempTV, mNightTempTV, mHumidityTV, mPressureTV, mConditionTV, mCloudsTV;
    private TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_w);

        findViews();
        //if (getIntent().getParcelableExtra(EXTRA_FORECAST) != null && getIntent().getIntExtra(EXTRA_DAY_INDEX, -1) != -1){
        populateUI((Temperature)getIntent().getParcelableExtra(EXTRA_TEMPERATURE),
                getIntent().getIntExtra(EXTRA_DAY_INDEX, 0),
                getIntent().getDoubleExtra(EXTRA_PRESSURE, 0),
                getIntent().getDoubleExtra(EXTRA_HUMIDITY, 0),
                getIntent().getStringExtra(EXTRA_WEATHER_CONDITION),
                getIntent().getDoubleExtra(EXTRA_CLOUDS, 0));
        // }
    }

    private void populateUI(Temperature temperature, int day, double pressure, double humidity,
                            String weatherCondition, double clouds) {
        mDateTV.setText(ForecastUtils.getDay(day));
        mDayTempTV.setText(getString(R.string.text_day_time)
                +ForecastUtils.getCelsiusTempFromKelvin(temperature.getDayTemperature()));
        mNightTempTV.setText(getString(R.string.text_night_time)
                +ForecastUtils.getCelsiusTempFromKelvin(temperature.getNightTemperature()));
        mHumidityTV.setText(getString(R.string.text_humidity) + humidity+"%");
        mPressureTV.setText(getString(R.string.text_pressure)+pressure+" hPa");
        mConditionTV.setText(getString(R.string.text_weather_condition)+weatherCondition);
        mCloudsTV.setText(getString(R.string.text_clouds)+clouds+"%");
    }

    private void findViews() {
        mDateTV = findViewById(R.id.text_date);
        mDayTempTV = findViewById(R.id.text_day_temp);
        mNightTempTV  = findViewById(R.id.text_night_temp);
        mHumidityTV = findViewById(R.id.text_humidity);
        mPressureTV = findViewById(R.id.text_pressure);
        mConditionTV  = findViewById(R.id.text_condition);
        mCloudsTV = findViewById(R.id.text_clouds);
    }
}
