package com.dongyang.TPOWW.weather;

public class MidForecastItem {
    private String date; // yyyyMMdd
    private String taMin; // 최저기온
    private String taMax; // 최고기온

    public MidForecastItem(String date, String taMin, String taMax) {
        this.date = date;
        this.taMin = taMin;
        this.taMax = taMax;
    }

    public String getDate() { return date; }
    public String getTaMin() { return taMin; }
    public String getTaMax() { return taMax; }
}
