package com.kuyuzhiqi.utils.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 显示Toast工具类
 */
public class ToastUtils {

    private ToastUtils() {
        /* Protect from instantiations */
    }

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context.getApplicationContext(), text, duration).show();
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text, Object... args) {
        show(context, String.format(text, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String text, int duration, Object... args) {
        show(context, String.format(text, args), duration);
    }

    /**
     * 指定位置显示toast
     *
     * @param xOffset x偏移量,相对左上角
     * @param yOffset y偏移量,相对左上角
     */
    public static void showAtLocation(Context context, String text, int duration, int xOffset, int yOffset) {
        Toast toast = Toast.makeText(context.getApplicationContext(), text, duration);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, xOffset, yOffset);
        toast.show();
    }

    public static void showWithTime(Context context, String text, int time) {
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        showWithTime(toast, time);
    }


    public static void showWithTime(Context context, int resId, int time) {
        final Toast toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
        showWithTime(toast, time);
    }

    private static void showWithTime(final Toast toast, int time) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, time);
    }
}
