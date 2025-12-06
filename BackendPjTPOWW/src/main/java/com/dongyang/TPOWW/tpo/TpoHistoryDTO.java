package com.dongyang.TPOWW.tpo;

import java.sql.Timestamp;

public class TpoHistoryDTO {
    private int hId;
    private int un;
    private String requestDate;
    private String requestTime;
    private String what;
    private String weatherSummary;
    private String aiRecommend;
    private String reasonSummary; // AI 추천 이유 추가
    private Timestamp createdAt;

    public int gethId() {
        return hId;
    }

    public void sethId(int hId) {
        this.hId = hId;
    }

    public int getUn() {
        return un;
    }

    public void setUn(int un) {
        this.un = un;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public String getAiRecommend() {
        return aiRecommend;
    }

    public void setAiRecommend(String aiRecommend) {
        this.aiRecommend = aiRecommend;
    }

    public String getReasonSummary() {
        return reasonSummary;
    }

    public void setReasonSummary(String reasonSummary) {
        this.reasonSummary = reasonSummary;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
