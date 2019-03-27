package com.kuyuzhiqi.utils.common;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * 坐标转换工具
 * 服务器坐标,原点为左下角,按X轴最大值为 PLAN_SCALE_X_MAX = 1200 去缩放
 * 原始图片坐标,原点为左上角,不缩放
 */
public class CoordinateUtils {

    /** X轴缩放的最大坐标 */
    public static final float PLAN_SCALE_X_MAX = 1200;

    /**
     * 将服务器的x轴上的长度转化为原始图片上的长度
     */
    public static float serverXLengthToSourceXLength(float serverX, Point sourceImageSize) {
        float scaleMultiple = PLAN_SCALE_X_MAX / sourceImageSize.x;//缩放倍数
        return serverX / scaleMultiple;
    }

    /**
     * 将服务器的点坐标转化为原始图片上的坐标
     */
    public static PointF serverCoordToSourceCoord(Point serverCoordPoint , Point sourceImageSize){
        return serverCoordToSourceCoord(new PointF(serverCoordPoint.x, serverCoordPoint.y) ,sourceImageSize);
    }

    /**
     * 将服务器的点坐标转化为原始图片上的坐标
     */
    public static PointF serverCoordToSourceCoord(PointF serverCoordPoint , Point sourceImageSize){
        float scaleMultiple = PLAN_SCALE_X_MAX / sourceImageSize.x;//缩放倍数
        float sourceCoordX = serverCoordPoint.x / scaleMultiple;
        float sourceCoordY = sourceImageSize.y - serverCoordPoint.y / scaleMultiple;
        return new PointF(sourceCoordX , sourceCoordY);
    }

    /**
     * 将原始图片上的坐标转化为服务器的点坐标
     */
    public static Point sourceCoordToServerCoord(PointF sourceCoordPointF, Point sourceImageSize){
        float scaleMultiple = PLAN_SCALE_X_MAX / sourceImageSize.x;
        float serverCoordX = sourceCoordPointF.x * scaleMultiple;
        float serverCoordY = (sourceImageSize.y - sourceCoordPointF.y ) * scaleMultiple;
        return new Point((int)serverCoordX, (int)serverCoordY);
    }
}
