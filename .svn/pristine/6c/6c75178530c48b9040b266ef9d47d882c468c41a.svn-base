package com.example.network;

import java.util.Timer;
import java.util.TimerTask;

import com.example.config.Global_Config;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

public class NetworkConnect_Service extends Service
{

	private static final String NETWORKSTATUS = Global_Config.NETWORK_STATUS;
	private static final String NETWORK_STATUS_ACTION = Global_Config.NETWORK_STATUS_ACTION;
	
	private static boolean NetStatus_now = true;
	private static boolean NetStatus_before = false;
	
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		System.out.println("oncreate");
		InitTimerTask();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO Auto-generated method stub
		System.out.println("onstart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		System.out.println("onstartcommand");
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 初始化网络连接状态检测任务
	 */
	private void InitTimerTask()
	{
		TimerTask timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				NetStatus_now = NetCon_Util.isNetConnect(getApplicationContext());
				
				if( (NetStatus_before != NetStatus_now) || ((false == NetStatus_before) && (false == NetStatus_now)) )
				{
					NetStatus_before = NetStatus_now;
					
					Intent intent = new Intent(NETWORK_STATUS_ACTION);
					Bundle bundle = new Bundle();
					bundle.putBoolean(NETWORKSTATUS, NetStatus_now);
					intent.putExtras(bundle);
					sendBroadcast(intent);
					System.out.println("Netstatus = " + NetStatus_now);
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000, 1*1000);
	}

	
}
