package com.kuyuzhiqi.utils.device;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

/**
 * 获取屏幕宽高;
 * px和dp之间转换;
 *
 * @author chengkai
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }

    /**
     * get the width(in px) of screen
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * get the height(in px) of screen
     */
    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取屏幕的真实高度
     */
    public static Point getRealSizeOfScreen(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point realSize = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realSize.x = realMetrics.widthPixels;
            realSize.y = realMetrics.heightPixels;
        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realSize.x = (Integer) mGetRawW.invoke(display);
                realSize.y = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realSize.x = display.getWidth();
                realSize.y = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }
        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realSize.x = display.getWidth();
            realSize.y = display.getHeight();
        }
        return realSize;
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static Rect getAppContentRect(Activity activity) {
        Rect contentRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(contentRect);
        int offset = getStatusBarHeight(activity);
        contentRect.top = offset;
        return contentRect;
    }

    public static int getAppContentHeight(Activity activity) {
        Rect contentRect = getAppContentRect(activity);
        return contentRect.bottom - contentRect.top;
    }
}
