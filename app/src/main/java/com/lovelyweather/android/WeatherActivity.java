package com.lovelyweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovelyweather.android.gson.Forecast;
import com.lovelyweather.android.gson.Lifestyle;
import com.lovelyweather.android.gson.Weather;
import com.lovelyweather.android.util.HttpUtil;
import com.lovelyweather.android.util.Utility;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.suggestion_ll)
    LinearLayout suggestionLl;
    @BindView(R.id.bg_img)
    ImageView bgImg;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.btn_cities)
    Button citiesBtn;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    public  static String mWeatherId;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        closeAndroidPDialog();
        ButterKnife.bind(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr=prefs.getString("weather",null);
        String bgStr=prefs.getString("bing_pic",null);
//        final String weatherId;
        if (bgStr !=null){
            Glide.with(this).load(bgStr).into(bgImg);
        }else{
            loadBingPic();
        }
        if (weatherStr !=null){
            Weather weather= Utility.handleWeatherResponse(weatherStr);
            mWeatherId=weather.basic.weatherId;
            showWeather(weather);
        }else{
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherSv.setVisibility(View.INVISIBLE);
            requesWeather(mWeatherId);
            loadBingPic();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requesWeather(mWeatherId);
            }
        });
    }

    private void loadBingPic() {
        final String requestBgStr="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBgStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              final String responseStr=response.body().string();
              SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
              editor.putString("bing_pic",responseStr);
              editor.apply();
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Glide.with(WeatherActivity.this).load(requestBgStr).into(bgImg);
                  }
              });
            }
        });
    }

    public void requesWeather(final String weaherId) {
        String weatherUrl=URL_HEWEATHER+"location="+weaherId+"&key="+KEY_HEWEATHER;
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"请求失败！",Toast.LENGTH_LONG);
                 swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(
                                    WeatherActivity.this).edit();
                            editor.putString("weather",responseStr);
                            editor.apply();
                            mWeatherId=weather.basic.weatherId;
                            showWeather(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"请求失败！",Toast.LENGTH_LONG);

                        }
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });

            }
        });



    }

    @OnClick(R.id.btn_cities)
    public void showCityChooseFragment(){
        drawerLayout.openDrawer(GravityCompat.START);
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
        suggestionLl.removeAllViews();
        for (Lifestyle lifestyle:weather.lifestyleList){
            View view = LayoutInflater.from(this).inflate(R.layout.suggestion_item, suggestionLl, false);
            TextView typeTv=view.findViewById(R.id.tv_suggestion_type);
            TextView brfTv=view.findViewById(R.id.tv_suggestion_brf);
            TextView txtTv=view.findViewById(R.id.tv_suggestion_txt);
            typeTv.setText(lifestyle.type);
            brfTv.setText(lifestyle.brf);
            txtTv.setText(lifestyle.txt);
            suggestionLl.addView(view);
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
