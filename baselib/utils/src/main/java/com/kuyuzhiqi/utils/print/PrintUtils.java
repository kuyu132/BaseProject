package com.kuyuzhiqi.utils.print;

import android.app.Activity;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.webkit.WebView;
import com.kuyuzhiqi.utils.R;
import com.kuyuzhiqi.utils.common.ToastUtils;

/**
 * 打印工具类
 */
public class PrintUtils {

    /**
     * 打印WebView内容
     *
     * @param printJobName 打印任务名称,用于任务识别
     * @param webView 目标页面
     */
    public static void printWebView(Activity activity, String printJobName, WebView webView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // Get a PrintManager instance
            PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);
            // Get a print adapter instance
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
            // Create a print job with name and adapter instance
            String jobName = printJobName;
            PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
            //处理printJob
            if (printJob == null) {
                ToastUtils.show(activity, activity.getString(R.string.add_print_job_failed));
            }
        } else {
            ToastUtils.show(activity, activity.getString(R.string.os_version_not_support_printer));
        }
    }
}
