package cn.acyou.leo.framework.model;

import java.io.Serializable;

/**
 * 经纬度
 * Lat  31.090867
 * Lng  121.817629
 * <p>
 * 国内：LNG比LAT大
 *
 * @author youfang
 * @version [1.0.0, 2020/4/9]
 **/
public class LatLng implements Serializable {
    private static final long serialVersionUID = -4840042005404521542L;

    private double lat;

    private double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng(String lat, String lng) {
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
    }

    public LatLng() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}