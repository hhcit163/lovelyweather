package com.lovelyweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ym on 19-3-19.
 */

public class Suggesion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public Carwash carWash;

    public Sport sport;


    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class Carwash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }


}
