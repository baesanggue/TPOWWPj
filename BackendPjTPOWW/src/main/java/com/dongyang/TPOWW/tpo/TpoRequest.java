package com.dongyang.TPOWW.tpo;

public class TpoRequest {

    private String gender;
    private int age;
    private String region;   // 시/도
    private String sigungu;  // 시/군/구
    private boolean indoor;
    private boolean outdoor;
    private String date;     // yyyy-MM-dd
    private String time;     // HH:mm
    private String what;     // 활동 내용

    private String brand;
    private String color;
    private String pcolor;

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getSigungu() {
        return sigungu;
    }
    public void setSigungu(String sigungu) {
        this.sigungu = sigungu;
    }

    public boolean isIndoor() {
        return indoor;
    }
    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public boolean isOutdoor() {
        return outdoor;
    }
    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getWhat() {
        return what;
    }
    public void setWhat(String what) {
        this.what = what;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getPcolor() {
        return pcolor;
    }
    public void setPcolor(String pcolor) {
        this.pcolor = pcolor;
    }
}
