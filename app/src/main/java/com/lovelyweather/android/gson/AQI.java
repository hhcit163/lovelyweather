package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ym on 19-3-19.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
