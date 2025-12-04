package com.dongyang.TPOWW.weather;


public class ForecastItem {
    private String fcstDate;
    private String fcstTime;
    private String category;
    private String fcstValue;

    public ForecastItem(String fcstDate, String fcstTime, String category, String fcstValue) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.category = category;
        this.fcstValue = fcstValue;
    }

    public String getFcstDate() { return fcstDate; }
    public String getFcstTime() { return fcstTime; }
    public String getCategory() { return category; }
    public String getFcstValue() { return fcstValue; }

    // 🔥 추가해야 하는 부분
    public String getKey() {
        return fcstDate + fcstTime;
    }
}

