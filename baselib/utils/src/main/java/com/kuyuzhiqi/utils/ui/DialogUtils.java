package com.kuyuzhiqi.utils.ui;

import android.app.Dialog;

import java.lang.reflect.Field;

/**
 * 对话框工具类
 */
public class DialogUtils {

    /**
     * 设置对话框能否关闭
     * @param canClose 能否关闭
     */
    public static void setDialogCanClose(Dialog dialog, boolean canClose) {
        try {
            Field dialogShowingField = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            dialogShowingField.setAccessible(true);
            dialogShowingField.set(dialog, canClose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
