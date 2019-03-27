package com.kuyuzhiqi.utils.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetworkUtils {

	public static final String NETWORK_TYPE_WIFI = "wifi";
	public static final String NETWORK_TYPE_3G = "eg";
	public static final String NETWORK_TYPE_2G = "2g";
	public static final String NETWORK_TYPE_WAP = "wap";
	public static final String NETWORK_TYPE_UNKNOWN = "unknown";
	public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

	/**
	 * 网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context){
		return context != null && (!getNetworkTypeName(context).equals(NETWORK_TYPE_DISCONNECT));
	}
	
	/**
	 * 当前网络是否wifi
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		return context != null && (getNetworkTypeName(context).equals(NETWORK_TYPE_WIFI));
	}
	
	/**
	 * 当前网络是否移动网络
	 * @param context
	 * @return
	 */
	public static boolean isMoblieNetworkConnected(Context context){
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo;
		if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
			return false;
		}
		
		if (networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}
	
	/**
	 * 当前网络是否高速移动网络
	 * @param context
	 * @return
	 */
	public static boolean isFastMoblieNetworkConnected(Context context){
		return (getNetworkTypeName(context).equals(NETWORK_TYPE_3G));
	}
	
	/**
	 * Get network type
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager == null ? null
				: connectivityManager.getActiveNetworkInfo();
		return networkInfo == null ? -1 : networkInfo.getType();
	}

	/**
	 * Get network type name
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getNetworkTypeName(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo;
		String type = NETWORK_TYPE_DISCONNECT;
		if (manager == null
				|| (networkInfo = manager.getActiveNetworkInfo()) == null) {
			return type;
		}
		
		if (networkInfo.isConnected()) {
			String typeName = networkInfo.getTypeName();
			if ("WIFI".equalsIgnoreCase(typeName)) {
				type = NETWORK_TYPE_WIFI;
			} else if ("MOBILE".equalsIgnoreCase(typeName)) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G
						: NETWORK_TYPE_2G)
						: NETWORK_TYPE_WAP;
			} else {
				type = NETWORK_TYPE_UNKNOWN;
			}
		}
		return type;
	}

	/**
	 * Whether is fast mobile network
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager == null) {
			return false;
		}

		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true;
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true;
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

}
