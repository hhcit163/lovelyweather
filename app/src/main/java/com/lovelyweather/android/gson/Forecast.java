package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ym on 19-3-19.
 */

public class Forecast {

    public String date;

    @SerializedName("cond_txt_d")
    public String info;
    public String tmp_max;
    public String tmp_min;


}
