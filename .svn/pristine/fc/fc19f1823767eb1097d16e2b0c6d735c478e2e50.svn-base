package com.example.application;

import android.app.Application;

public class myApplication extends Application
{
	private static boolean isNetConnect; 
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/**
	 * 获取网络连接状态
	 * @return 连接返回true 否则返回false
	 */
	public static boolean isNetConnect()
	{
		return isNetConnect;
	}

	/**
	 * 设置网络连接状态
	 * @param isNetConnect
	 */
	public static void setNetConnect(boolean isNetConnect)
	{
		myApplication.isNetConnect = isNetConnect;
	}

	
	
}
