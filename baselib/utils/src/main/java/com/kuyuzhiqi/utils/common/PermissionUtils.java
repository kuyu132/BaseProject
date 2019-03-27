package com.kuyuzhiqi.utils.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * 检查是否有录音权限
     */
    public static boolean checkRecordAudioPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }
}
