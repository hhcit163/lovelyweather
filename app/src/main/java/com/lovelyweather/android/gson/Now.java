package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ym on 19-3-19.
 */

public class Now {
    @SerializedName("tmp")
    public String tempperature;
    @SerializedName("cond_txt")
    public String info;

}
