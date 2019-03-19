package com.lovelyweather.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lovelyweather.android.gson.Weather;
import com.lovelyweather.android.util.Utility;

import butterknife.BindView;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.sv_weather)
    private ScrollView weatherSv;
    @BindView(R.id.title_city)
    TextView titleCityTv;
    @BindView(R.id.title_update)
    TextView titleUpdateTimeTv;

    @BindView(R.id.tv_now_degree)
    TextView nowDegreeTv;

    @BindView(R.id.tv_now_weather_info)
    TextView nowWeatherInfoTv;

    @BindView(R.id.forecast_ll)
    LinearLayout forecastLl;

    @BindView(R.id.tv_aqi)
    TextView aqiTv;

    @BindView(R.id.tv_pm25)
    TextView pm25Tv;

    @BindView(R.id.tv_comfort)
    TextView comfortTv;

    @BindView(R.id.tv_wash_car)
    TextView washCarTv;

    @BindView(R.id.tv_sport)
    TextView sportTv;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr=prefs.getString("weather",null);
        if (weatherStr !=null){
            Weather weather= Utility.handleWeatherResponse(weatherStr);
            showWeather(weather);
        }else{
            String weaherId=getIntent().getStringExtra("weather_id");
            weatherSv.setVisibility(View.INVISIBLE);
            requesWeather(weaherId);
        }
    }

    private void requesWeather(String weaherId) {


    }


    private void showWeather(Weather weather) {
    }

}
