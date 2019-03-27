package com.kuyuzhiqi.utils.common;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorUtils {

    private static SensorManager sm;
    private static Sensor sensor;
    private static int rotateValue = 0;

    public static int getRotateValue(Context context) {
        try {
            sm = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
            sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            SensorEventListener sel = new SensorEventListener() {
                public void onSensorChanged(SensorEvent se) {
                    float x = se.values[SensorManager.DATA_X];
                    float y = se.values[SensorManager.DATA_Y];
                    float z = se.values[SensorManager.DATA_Z];
                    // x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西

                    if (y > 5) {
                        rotateValue = 0;
                    } else if (z > 5) {
                        rotateValue = 0;
                    } else if (x < -5) {
                        rotateValue = 90;
                    } else if (x > 5) {
                        rotateValue = 270;
                    } else {
                        rotateValue = 0;
                    }

                }

                public void onAccuracyChanged(Sensor arg0, int arg1) {
                }
            };
            // 注册Listener，SENSOR_DELAY_GAME为检测的精确度
            sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);
            return rotateValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotateValue;

    }

    /**
     * 计算出屏幕转过的角度
     * @param sensorEvent
     * @return
     */
    public static int getRotateDegree(SensorEvent sensorEvent) {
        int degree;
        float x = sensorEvent.values[SensorManager.DATA_X];
        // x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
        if (x < -5) {
            degree = 90;
        } else if (x > 5) {
            degree = 270;
        } else {
            degree = 0;
        }
        return degree;
    }
}
