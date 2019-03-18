package com.lovelyweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * @author HcIt on 2019/3/18 0018.
 * @Email hhc_wrok@163.com
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private int weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
