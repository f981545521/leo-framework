package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.model.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Data
@EqualsAndHashCode(callSuper = true)
public class LatLng extends DTO {

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

}