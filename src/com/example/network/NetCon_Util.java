package com.example.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetCon_Util
{
	public static boolean isWifiConnect = false;
	public static boolean isCelluarConnect = false;
	
	/*public static boolean isCelluarConnect(Context context)
	{
		boolean result = false;
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo();
		return result;
	}
	
	public static boolean isWifiConnect(Context context)
	{
		boolean result = false;
		
		
		return result;
	}*/
	
	/**
	 * 检查网络连接状态，连接返回true 否则false
	 * @param context
	 * @return 连接返回true 否则false
	 */
	public static boolean isNetConnect(Context context)
	{
		boolean result = false;
		
		/*if(isCelluarConnect() || isWifiConnect)
		{
			result = true;
		}*/
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null != cm)
		{
			NetworkInfo info = cm.getActiveNetworkInfo();
			
			if(null != info)
			{
				result = info.isConnected();
			}
		}
		
		return result;
	}
	
	/**
	 * 网络故障，跳转至网络设置界面
	 */
	public static void setNetWorkConnect(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		context.startActivity(intent);
	}
	
}
