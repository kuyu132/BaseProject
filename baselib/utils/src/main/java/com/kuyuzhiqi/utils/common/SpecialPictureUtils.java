package com.kuyuzhiqi.utils.common;

import android.annotation.TargetApi;
import android.graphics.*;
import android.media.ExifInterface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义修改图片使用的方法，区别BitmapUtils的通用方法
 */
public class SpecialPictureUtils {

    public static int DEFAULT_ROTATE = 90;
    private static int BITMAP_TARGET_SIZE = 600;

    public static Bitmap getSquareBitmap(String filePath, int rotateValue) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        Bitmap loadBitmap = BitmapUtils.decodeSampledBitmapFromFile(filePath, BITMAP_TARGET_SIZE, BITMAP_TARGET_SIZE);
        if (loadBitmap == null) {
            return null;
        }

        //裁剪正方形
        Bitmap squareBitmap = cutSquareBitmapFromMiddle(loadBitmap);

        //旋转预设角度
        Bitmap bitmap = BitmapUtils.rotateBitmap(squareBitmap, DEFAULT_ROTATE + rotateValue);
        return bitmap;
    }

    /**
     * 从中间截出一个正方形的图片
     */
    public static Bitmap cutSquareBitmapFromMiddle(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int difference = Math.abs((w - h) / 2);

        if (w > h) {
            return Bitmap.createBitmap(bitmap, difference, 0, h, h, null, true);
        } else {
            return Bitmap.createBitmap(bitmap, 0, difference, w, w, null, true);
        }
    }

    /**
     * 查看图片是否旋转过，如某些三星手机拍照完自动旋转,获取旋转的度数
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90 + DEFAULT_ROTATE;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180 + DEFAULT_ROTATE;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270 + DEFAULT_ROTATE;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = DEFAULT_ROTATE;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 传入一张图片和要保存到的路径，将图片替换保存
     * 保存为jpeg格式
     */
    public static boolean savePictureWithJpegFormat(Bitmap bm, String savePath) {
        return savePicture(bm, savePath, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 传入一张图片和要保存到的路径，将图片替换保存
     * 保存为webP格式
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean savePictureWithWebPFormat(Bitmap bm, String savePath) {
        return savePicture(bm, savePath, Bitmap.CompressFormat.WEBP);
    }

    /**
     * 传入一张图片和要保存到的路径，将图片替换保存
     *
     * @param compressFormat 保存格式
     */
    public static boolean savePicture(Bitmap bm, String savePath, Bitmap.CompressFormat compressFormat) {
        if (bm == null)
            return false;
        InputStream is = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            baos = new ByteArrayOutputStream();
            bm.compress(compressFormat, 65, baos);
            //bm.compress(Bitmap.CompressFormat.JPEG, 65, baos);
            is = new ByteArrayInputStream(baos.toByteArray());
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (len != -1) {
                len = is.read(buffer);
                if (len > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                baos = null;
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is = null;
            }

            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream = null;
            }
        }
        return true;
    }

    /**
     * 水印内容
     */
    public static String buildMarkText(String userName) {
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.MINUTE_DATE_FORMAT);
        String timeStr = sdf.format(new Date(TimeUtils.getCurrentTimeInLong()));
        return userName + "   " + timeStr;
    }

}
