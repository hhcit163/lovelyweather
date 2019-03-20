package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ym on 19-3-19.
 */

public class Weather {
    public String status;

    public Basic basic;
//    public AQI aqi;
    public Now now;
    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String upDate24;
        @SerializedName("utc")
        public String upDate;
    }
}
