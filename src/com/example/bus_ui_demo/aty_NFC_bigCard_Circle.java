package com.example.bus_ui_demo;

import java.util.HashMap;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.Alert.myAlert.ShowOneClickDialog_Interface;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.network.PayParams;
import com.example.network.protocol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class aty_NFC_bigCard_Circle extends FragmentActivity
{
	private ImageButton ib_check;
	private FrameLayout frame_notice;
	private static boolean isResultBack = false;//是否返回标志
	private FragmentManager fm = null;
	private FragmentTransaction transaction = null;
	private myApplication myApp;
	
	private static final String BEFORECHARGE_MONEY = Global_Config.BEFORECHARGE_MONEY;
	private static final String CHARGE_MONEY = Global_Config.CHARGE_MONEY;
	private static final String AFTERCHARGE_MONEY = Global_Config.AFTERCHARGE_MONEY;
	private static final String TAC_CODE = Global_Config.TAC_CODE;
	
	//用于大卡充值圈存步骤标识
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	//private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	//private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
	
	private static final int REQUESTCODE_CIRCLE = 1;
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int CIRCLE_COMPELETE_REPORT_SUCCESS = Global_Config.INNER_MSG_START + 1;
	private static final int CIRCLE_COMPELETE_REPORT_FAIL = Global_Config.INNER_MSG_START + 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_nfc_bigcard_circle_layout);
		
		myApp = (myApplication) getApplicationContext();
		FindView();
		InitView();
	}
	
	private void FindView()
	{
		ib_check = (ImageButton) findViewById(R.id.ib_check);
		frame_notice = (FrameLayout) findViewById(R.id.frame_notice);
		
	}
	
	private void InitView()
	{
		ib_check.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_NFC_bigCard_Circle.this, aty_NFC_bigCard_Init.class);
				myApp.setCircleStep(CIRCLE_INIT_STEP);
				startActivityForResult(intent, REQUESTCODE_CIRCLE);
			}
		});
		
		frame_notice.setVisibility(View.GONE);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		AddCircleCompleteNoticeFrame();
		super.onResume();
	}
	
	/**
	 * 添加frg_NFC_bigCard_CircleComplete_notice fragment
	 */
	private void AddCircleCompleteNoticeFrame()
	{	
		if(!isResultBack) return;//圈存没成功，退出
		isResultBack = false;
		
		frame_notice.setVisibility(View.VISIBLE);
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.replace(R.id.frame_notice, new frg_NFC_bigCard_CircleComplete_notice());
		transaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		System.out.println("resultcode = "+resultCode + "_" + Activity.RESULT_OK);
		if(REQUESTCODE_CIRCLE == requestCode)
		{
			//Bundle bundle = data.getExtras();
			//System.out.println("--bundle is "+bundle.toString());
			int before = myApp.getBeforeChargeMoney();//bundle.getInt(BEFORECHARGE_MONEY);
			int charge = myApp.getChargeMoney();//bundle.getInt(CHARGE_MONEY);
			int after = myApp.getAfterChargeMoney();//bundle.getInt(AFTERCHARGE_MONEY);
			String TAC = myApp.getTAC_CARD();//bundle.getString(TAC_CODE);
			
			myApp.setCircleStep(INIT_STEP);//设置为初始读卡状态
			
			if(charge == (after - before))
			{
				//圈存成功，弹出成功窗口
				isResultBack = true;
				CircleSuccess(aty_NFC_bigCard_Circle.this, TAC);
			}
			else {
				//圈存失败，给出提示，返回主页面
				myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_fail));
				myAlert.ShowOneClickDialog(aty_NFC_bigCard_Circle.this, getString(R.string.circle_fail_notice), new ShowOneClickDialog_Interface()
				{
					@Override
					public void DoSomething()
					{
						// TODO Auto-generated method stub
						BackHome(aty_NFC_bigCard_Circle.this);
					}
				});
			}
			super.onActivityResult(requestCode, requestCode, data);
		}
		
	}
	
	
	/**
	 * 圈存成功处理
	 * @param context
	 * @param TAC
	 */
	private void CircleSuccess(Context context,String TAC)
	{
		myAlert.ShowToast(context, getString(R.string.circle_success));
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put(PayParams.ORDERSEQ, myApp.getOrderSeq());
		map.put(PayParams.TAC_CARD, TAC);
		//发送圈存成功指令至服务器
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if(protocol.SendCircleCompleteReport(aty_NFC_bigCard_Circle.this, map, mHandler))
				{
					Message msg = mHandler.obtainMessage();
					msg.what = CIRCLE_COMPELETE_REPORT_SUCCESS;
					mHandler.sendMessage(msg);
				}
				else
				{
					Message msg = mHandler.obtainMessage();
					msg.what = CIRCLE_COMPELETE_REPORT_FAIL;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		BackHome(aty_NFC_bigCard_Circle.this);
		super.onBackPressed();
	}
	/**
	 * 返回首页
	 */
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.network_error));
					BackHome(aty_NFC_bigCard_Circle.this);
					break;
				case CIRCLE_COMPELETE_REPORT_FAIL:
					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_report_fail));
					//BackHome(aty_NFC_bigCard_Circle.this);
					break;
				case CIRCLE_COMPELETE_REPORT_SUCCESS:
					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_report_success));
					//BackHome(aty_NFC_bigCard_Circle.this);
					break;
				default:
					break;
			}
		}
		
	};
	
	/**
	 * 返回首页，并关闭当前页面栈顶上的所有页面
	 * @param context
	 */
	private void BackHome(Context context)
	{
		myApp.setCircleStep(INIT_STEP);//设置为初始读卡状态
		Intent intent = new Intent(aty_NFC_bigCard_Circle.this, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity)context).finish();
	}
}
