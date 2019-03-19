package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ym on 19-3-19.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String upDate;
    }
}
