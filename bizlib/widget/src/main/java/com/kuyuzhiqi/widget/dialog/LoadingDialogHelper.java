package com.kuyuzhiqi.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.kuyuzhiqi.widget.R;

public class LoadingDialogHelper {

	private static LoadingDialogHelper sInstance;
	private ProgressDialog mProgressDialog;

	public static LoadingDialogHelper getInstance(){
		if(sInstance == null){
			sInstance = new LoadingDialogHelper();
		}
		return sInstance;
	}

	private LoadingDialogHelper() {
	}

	/**
	 * 关闭已经打开的加载对话框
	 */
	public void dismissLoadingDialog(){
		try {
			if(mProgressDialog != null && mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		} catch (final IllegalArgumentException e) {
			// 此时Activity已经结束
		} catch (final Exception e) {

		} finally {
			mProgressDialog = null;
		}
	}

	/**
	 * 显示默认的加载对话框,不能取消
	 * @param context
	 * @return
	 */
	public void showDefaultLoadingDialog(Context context){
		if(null == mProgressDialog){
			showProgressDialog(context, R.string.loading, false);
		}
	}

	/**
	 * 显示加载对话框
	 * @param context
	 * @param messageResourceId
	 * @param cancelable
	 */
	public void showLoadingDialog(Context context, int messageResourceId, boolean cancelable){
		if(null == mProgressDialog){
			showProgressDialog(context, messageResourceId, cancelable);
		}
	}

	private void showProgressDialog(Context context , int messageResourceId , boolean cancelable){
		showProgressDialog(context , context.getResources().getString(messageResourceId) ,cancelable);
	}

	private void showProgressDialog(Context context , String message , boolean cancelable){
		showProgressDialog(context, "", message, cancelable );
	}

	private void showProgressDialog(Context context ,String title , String message , boolean cancelable){
		mProgressDialog = ProgressDialog.show(context, title, message, true ,cancelable);
		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override public void onCancel(DialogInterface dialogInterface) {
				mProgressDialog = null;
			}
		});
	}
}
