package com.example.network;

import java.util.Timer;
import java.util.TimerTask;

import com.example.config.Global_Config;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

/**
 * ����ͨ�Ϸ��񣬾���ͨ�Ϲ㲥���ܣ�Ҳ���Բ��ð󶨵ķ�ʽ��ȡ��ǰ����״̬
 * @author tom
 *
 */
public class Service_NetworkConnect extends Service
{

	private static final String NETWORKSTATUS = Global_Config.NETWORK_STATUS;
	private static final String NETWORK_STATUS_ACTION = Global_Config.NETWORK_STATUS_ACTION;
	private static final long NETWORKCHECK_GAP = Global_Config.NETWORKCHECK_GAP;
	
	private static boolean NetStatus_now = true;
	private static boolean NetStatus_before = false;
	
	private static boolean isNetConnect = false;//�����������״̬
	public static Service_NetworkConnect myService = null;
	
	private final IBinder binder = new myBinder();
	
	public class myBinder extends Binder
	{
		public Service_NetworkConnect getService()
		{
			return Service_NetworkConnect.this;
		}
	}
			
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		System.out.println("oncreate");
		InitTimerTask();
		super.onCreate();
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
		//return null;
		
		return binder;
	}

	/**
	 * ��ʼ����������״̬�������
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
				
				setNetConnect(NetStatus_now);
				
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
		timer.schedule(timerTask, 1000, NETWORKCHECK_GAP);
	}


	public static boolean isNetConnect()
	{
		return isNetConnect;
	}


	public static void setNetConnect(boolean isNetConnect)
	{
		Service_NetworkConnect.isNetConnect = isNetConnect;
	}

	
}
