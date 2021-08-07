package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.model.LatLng;

/**
 * 测试：https://lbs.amap.com/api/javascript-api/example/calcutation/calculate-distance-between-two-markers
 *
 * @author youfang
 * @version [1.0.0, 2020/4/9]
 **/
public class GpsUtil {
    // 地球赤道半径
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 得到两个经纬度点的距离
     *
     * @param lat1 lat1
     * @param lng1 lng1
     * @param lat2 lat2
     * @param lng2 lng2
     * @return double 距离（米）
     */
    public static int getDistanceInt(double lat1, double lng1, double lat2, double lng2) {
        LatLng from = new LatLng(lat1, lng1);
        LatLng to = new LatLng(lat2, lng2);
        double distance = getDistance(from, to);
        return new Double(distance).intValue();
    }

    /**
     * 得到两个经纬度点的距离
     * <pre>
     *      getDistanceInt(new LatLng(39.923423, 116.368904), new LatLng(39.922501, 116.387271));
     *      =   1571
     * </pre>
     *
     * @param from 从
     * @param to   来
     * @return double 距离（米）
     */
    public static int getDistanceInt(LatLng from, LatLng to) {
        double distance = getDistance(from, to);
        return new Double(distance).intValue();
    }

    /**
     * 得到两个经纬度点的距离
     * <pre>
     *      getDistance(39.923423, 116.368904, 39.922501, 116.387271);
     *      =   1571.3999999999999
     *  </pre>
     *
     * @param lat1 lat1
     * @param lng1 lng1
     * @param lat2 lat2
     * @param lng2 lng2
     * @return double 距离（米）
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        LatLng from = new LatLng(lat1, lng1);
        LatLng to = new LatLng(lat2, lng2);
        return getDistance(from, to);
    }

    /**
     * 得到两个经纬度点的距离
     *
     * @param from 从
     * @param to   来
     * @return double 距离（米）
     */
    public static double getDistance(LatLng from, LatLng to) {
        double radLat1 = rad(from.getLat());
        double radLat2 = rad(to.getLat());
        double a = radLat1 - radLat2;
        double b = rad(from.getLng()) - rad(to.getLng());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    public static void main(String[] args) {
        //116.368904, 39.923423
        //116.387271, 39.922501
        double distance = getDistance(39.923423, 116.368904, 39.922501, 116.387271);
        int distanceInt = getDistanceInt(39.923423, 116.368904, 39.922501, 116.387271);
        System.out.println(distance);
        System.out.println(distanceInt);
        int distanceInt1 = getDistanceInt(new LatLng(39.923423, 116.368904), new LatLng(39.922501, 116.387271));
        System.out.println(distanceInt1);
    }


}