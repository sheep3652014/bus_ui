package com.example.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
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
	
}
