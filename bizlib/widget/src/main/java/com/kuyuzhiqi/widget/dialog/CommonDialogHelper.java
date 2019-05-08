package com.kuyuzhiqi.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import com.kuyuzhiqi.widget.R;

/**
 * 常用的对话框
 */
public class CommonDialogHelper {

    /**
     * 显示无网络提示
     */
    public static void showNetworkDisconnectedHintDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle(R.string.hint);
        builder.setMessage(R.string.dialog_no_network_hint_message);
        builder.setPositiveButton(R.string.network_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    //3.0以上打开设置界面
                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                } else {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

}
