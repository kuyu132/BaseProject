package com.kuyuzhiqi.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

public class GPSUtils {

    /**
     * 将gps的经纬度变成度分秒
     */
    public static String degressToString(double digitalDegree) {
        double num = 60;
        int degree = (int) digitalDegree;
        double tmp = (digitalDegree - degree) * num;
        int minute = (int) tmp;
        int second = (int) (10000 * (tmp - minute) * num);
        return degree + "/1," + minute + "/1," + second + "/10000";
    }

    /**
     * @param rationalString 度分秒格式的经纬度字符串,形如: 114/1,23/1,538547/10000 或 30/1,28/1,432120/10000
     * @param ref 东西经 或 南北纬 的标记 S南纬 W西经
     * @return double格式的 经纬度
     */
    public static double convertRationalLatLonToFloat(String rationalString, String ref) {
        if (TextUtils.isEmpty(rationalString) || TextUtils.isEmpty(ref)) {
            return 0;
        }

        try {
            String[] parts = rationalString.split(",");

            String[] pair;
            pair = parts[0].split("/");
            double degrees = parseDouble(pair[0].trim(), 0)
                    / parseDouble(pair[1].trim(), 1);

            pair = parts[1].split("/");
            double minutes = parseDouble(pair[0].trim(), 0)
                    / parseDouble(pair[1].trim(), 1);

            pair = parts[2].split("/");
            double seconds = parseDouble(pair[0].trim(), 0)
                    / parseDouble(pair[1].trim(), 1);

            double tmp = degrees + (minutes / 60.0) + (seconds / 3600.0);
            BigDecimal bd = new BigDecimal(tmp);
            double result = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (("S".equals(ref) || "W".equals(ref))) {
                return -result;
            }
            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static double parseDouble(String doubleValue, double defaultValue) {
        try {
            return Double.parseDouble(doubleValue);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * 七牛存储的格式：113, 22, 22.9800转成113/1,22/1,229800/10000
     */
    public static double convertCoordinate(String qiniuGPS, String ref) {
        try {
            String[] parts = qiniuGPS.split(",");
            StringBuilder sb = new StringBuilder();
            sb.append(parts[0] + "/1,");
            sb.append(parts[1] + "/1,");
            sb.append(Double.valueOf(parts[2]) * 10000 + "/10000");
            return convertRationalLatLonToFloat(sb.toString(), ref);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
