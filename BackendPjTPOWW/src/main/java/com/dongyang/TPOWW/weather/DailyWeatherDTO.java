package com.dongyang.TPOWW.weather;

public class DailyWeatherDTO {
    private String date; // YYYYMMDD
    private String dayOfWeek; // 요일 (월, 화...)
    private String minTemp; // 최저기온
    private String maxTemp; // 최고기온
    private String weatherState; // sunny, cloudy, rainy, snowy
    private String weatherDesc; // 맑음, 흐림, 비...

    public DailyWeatherDTO(String date, String dayOfWeek, String minTemp, String maxTemp, String weatherState,
            String weatherDesc) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherState = weatherState;
        this.weatherDesc = weatherDesc;
    }

    public String getDate() {
        return date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getWeatherState() {
        return weatherState;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }
}
