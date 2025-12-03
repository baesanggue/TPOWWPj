package com.dongyang.TPOWW.weather;

public class MidTemp {
    private String day;   // 3~10일 후
    private String min;
    private String max;

    public MidTemp(String day, String min, String max) {
        this.day = day;
        this.min = min;
        this.max = max;
    }

    public String getDay() { return day; }
    public String getMin() { return min; }
    public String getMax() { return max; }
}
