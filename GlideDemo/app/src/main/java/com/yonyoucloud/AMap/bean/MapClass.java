package com.yonyoucloud.AMap.bean;


/**
 * 地图需要的内容
 * Created by yjbo on 17/9/13.
 */

public class MapClass {
    private String adressAll;//地址的全称
    private String adressSimple;//地址的简称
    private double latitude;//纬度

    public MapClass(String adressAll, String adressSimple, double latitude, double longitude) {
        this.adressAll = adressAll;
        this.adressSimple = adressSimple;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private double longitude;//经度

    public MapClass() {

    }

    public String getAdressAll() {
        return adressAll;
    }

    public void setAdressAll(String adressAll) {
        this.adressAll = adressAll;
    }

    public String getAdressSimple() {
        return adressSimple;
    }

    public void setAdressSimple(String adressSimple) {
        this.adressSimple = adressSimple;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MapClass{" +
                "adressAll='" + adressAll + '\'' +
                ", adressSimple='" + adressSimple + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
