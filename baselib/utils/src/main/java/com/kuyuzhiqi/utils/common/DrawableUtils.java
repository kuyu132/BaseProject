package com.kuyuzhiqi.utils.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableUtils {

    /**
     * 对Drawable着色
     * @param context
     * @param drawableResId
     * @param colorResId
     * @return
     */
    public static Drawable tintDrawable(Context context, int drawableResId, int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        Drawable.ConstantState state = drawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        DrawableCompat.setTint(drawable, context.getResources().getColor(colorResId));
        return drawable;
    }
}
