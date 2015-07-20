package com.example.config;

import android.R.integer;

public class Global_Config
{
	//消息常量定义
	public static final int NETWORK_DISCONNECT = 0;
	public static final int NETWORK_CONNECT = 1;
	
	public static final String NETWORK_STATUS = "network_status";
	public static final String NETWORK_STATUS_ACTION = "com.example.network.NetworkConnect_action";//自定义广播action
	
	public static final int CONNECT_OUT_TIME = 2000;//网络超时时间
	
	public static final String BIG_AID = "a00000000386980701";//
	public static final String CIRCLE_INIT = "805000020B01";
	public static final String CIRCLE_WRITE = "805200000B";
}
