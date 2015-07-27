package com.example.bus_ui_demo;

import com.example.application.myApplication;
import com.example.config.Global_Config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;

public class aty_start extends Activity
{
	private Handler delayHandler = null;
	private static Runnable delay = null;
	private static final long delayinMs = Global_Config.CONNECT_OUT_TIME + 1*1000;
	
	private myApplication myApp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_start);
		
		delayHandler = new Handler();
		delay = new Runnable() //
		{

			@Override
			public void run()
			{
				finish();
				Intent intent = new Intent(aty_start.this, aty_main.class);
				startActivity(intent);
			}
		};
		delayHandler
				.postDelayed(delay, delayinMs);
		
		myApp = (myApplication) getApplicationContext();
		myApp.setIMEI(GetIMEI());
	}

	/**
	 * 获取IMEI码  长度：15位
	 */
	private String GetIMEI()
	{
		String IMEI;
		
		TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI = tm.getDeviceId();
		return IMEI;
	}
}
