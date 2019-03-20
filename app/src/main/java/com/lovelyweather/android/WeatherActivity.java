package com.lovelyweather.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovelyweather.android.gson.Forecast;
import com.lovelyweather.android.gson.Weather;
import com.lovelyweather.android.util.HttpUtil;
import com.lovelyweather.android.util.Utility;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public static final String TAG = "WeatherActtivity";


    private final String KEY_HEWEATHER="c0e22ad35b004312a4cba9634727ca5a";
   private final String URL_HEWEATHER="https://free-api.heweather.net/s6/weather?";


    @BindView(R.id.sv_weather)
     ScrollView weatherSv;
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
        closeAndroidPDialog();
        ButterKnife.bind(this);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr=prefs.getString("weather",null);
//        if (weatherStr !=null){
//            Weather weather= Utility.handleWeatherResponse(weatherStr);
//            showWeather(weather);
//        }else{
            String weaherId=getIntent().getStringExtra("weather_id");
            weatherSv.setVisibility(View.INVISIBLE);
            requesWeather(weaherId);
//        }
    }

    private void requesWeather(String weaherId) {
        String weatherUrl=URL_HEWEATHER+"location="+weaherId+"&key="+KEY_HEWEATHER;
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"请求失败！",Toast.LENGTH_LONG);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr=response.body().string();
                Log.i(TAG, "onResponse: "+responseStr);
                final Weather weather=Utility.handleWeatherResponse(responseStr);
                Log.i(TAG, "onResponse: status="+weather.status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(
                                    WeatherActivity.this).edit();
                            editor.putString("weather",responseStr);
                            editor.apply();
                            showWeather(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"请求失败！",Toast.LENGTH_LONG);

                        }

                    }
                });

            }
        });



    }


    private void showWeather(Weather weather) {
        String cityName=weather.basic.cityName;
        String upDateTime=weather.update.upDate;
        String degree=weather.now.tempperature+"℃";
        String nowInfo=weather.now.info;

        titleCityTv.setText(cityName);
        titleUpdateTimeTv.setText(upDateTime);
        nowDegreeTv.setText(degree);
        nowWeatherInfoTv.setText(nowInfo);

        forecastLl.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLl,false);
            TextView dateTv=view.findViewById(R.id.tv_forecast_date);
            TextView infoTv=view.findViewById(R.id.tv_forecast_info);
            TextView maxTv=view.findViewById(R.id.tv_forecast_max);
            TextView minTv=view.findViewById(R.id.tv_forecast_min);

            dateTv.setText(forecast.date);
            infoTv.setText(forecast.info);
            maxTv.setText(forecast.tmp_max);
            minTv.setText(forecast.tmp_min);
            forecastLl.addView(view);
        }

        weatherSv.setVisibility(View.VISIBLE);
    }
    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
