package com.example.bus_ui_demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.config.Global_Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class aty_NFC_bigCard extends FragmentActivity
{
	private static final String BEFORECHARGE_MONEY = Global_Config.BEFORECHARGE_MONEY;
	private static final String AFTERCHARGE_MONEY = Global_Config.AFTERCHARGE_MONEY;
	private static final String CONSUME = Global_Config.CONSUME;
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String PUBLISHMSG = Global_Config.PUBLISHMSG;
	private static final String PUBLISHCARDNO = Global_Config.PUBLISHCARDNO;
	private static final String RANDOM = Global_Config.RANDOM;
	private static final String LOGLIST = Global_Config.LOGLIST;
	private static final String CIRCLEINIT_MSG = Global_Config.CICLEINIT_MSG;
	
	public static final int INIT_STEP = 0;
	public static final int CIRCLE_INIT_STEP = 1;
	public static final int CIRCLEING_STEP = 2;
	public static final int CIRCLE_COMPELTE_STEP = 3;
	
	private static final String TAG = "aty_NFC_bigCard";
	private FrameLayout frame_bigCard;
	
	private FragmentManager fm = null;
	private FragmentTransaction transaction = null;
	private ImageButton ib_check;
	private TextView tv_help_3;
	private TextView tv_help_6;
	
	private LinearLayout Lin_help;
	private LinearLayout Lin_whyClick;
	
	private static final int REQUESTCODE = 0;//调用aty_NFC_bigCard_Init 请求码
	private myApplication myApp;
	
	private static boolean isResultBack = false;//是否返回标志
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_nfc_bigcard_layout);
		
		myApp = (myApplication) getApplicationContext();
		
		FindView();
		InitView();
		
		if(!GetNFCStatus())
			myAlert.ShowToast(aty_NFC_bigCard.this, getString(R.string.bigCardInit_notice_error));
	}
	
	private void FindView()
	{
		frame_bigCard = (FrameLayout) findViewById(R.id.frame_bigCardInit);
		
		ib_check = (ImageButton) findViewById(R.id.ib_check);		
		
		Lin_help = (LinearLayout) findViewById(R.id.Lin_help);
		tv_help_3 = (TextView) findViewById(R.id.tv_help_3);
		tv_help_6 = (TextView) findViewById(R.id.tv_help_6);
		
		Lin_whyClick = (LinearLayout) findViewById(R.id.Lin_whyClick);
	}
	
	private void InitText()
	{
		String help3 = getResources().getString(R.string.bigCard_help_3);  
		String help6 = getResources().getString(R.string.bigCard_help_6);  
		
        tv_help_3.setText(Html.fromHtml(help3));
        tv_help_6.setText(Html.fromHtml(help6));
        
        tv_help_6.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String str = tv_help_6.getText().toString();
				String phoneNO = str.split("：")[1];//注意此处为中文的冒号
				System.out.println("no: "+phoneNO+"_"+str);
				//<uses-permission android:name="android.permission.CALL_PHONE"/>
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNO));
				startActivity(intent);
			}
		});
	}
	
	private void InitView()
	{
		InitText();
		
		Lin_whyClick.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(aty_NFC_bigCard.this);
				builder.setTitle(R.string.bigCard_whyClick_dialog_title);
				builder.setMessage(R.string.bigCard_whyClick_dialog_msg);
				builder.setPositiveButton(R.string.bigCard_whyClick_dialog_ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
					}
				});
				builder.create().show();
			}
		});
		
		frame_bigCard.setVisibility(View.GONE);//圈存测试后再显示
		//AddSeletMoneyFrame();
		
		ib_check.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_NFC_bigCard.this, aty_NFC_bigCard_Init.class);
				//startActivity(intent);
				myApp.setCircleStep(INIT_STEP);
				startActivityForResult(intent, REQUESTCODE);
			}
		});
	}
	
	/**
	 * 获取NFC开启状态
	 * @return 开启返回 true 否则返回false
	 */
	private boolean GetNFCStatus()
	{
		boolean result = false;
		
		//获取NFC状态
		NfcManager nm = (NfcManager)getSystemService(Context.NFC_SERVICE);
		
		if(null != nm)
		{
			NfcAdapter nfcAdapter = nm.getDefaultAdapter();
			
			try
			{
				result = nfcAdapter.isEnabled();
			} catch (Exception e)
			{
				// TODO: handle exception
				Log.d(TAG, "无NFC设备 " + e.getMessage());
				result = false;
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		System.out.println("got request");
		// TODO Auto-generated method stub
		if(data==null)
			System.out.println("data is null");
		else
			System.out.println("--" + data.toString());
		if(REQUESTCODE == requestCode)
		{
			System.out.println("resultcode = "+resultCode + "_" + Activity.RESULT_OK);
			if(Activity.RESULT_OK == resultCode)
			{
				
				//解析记录
//				bundle.putString(PUBLISHCARDNO, bean.getPublishCardNO());
//				bundle.putString(PUBLISHMSG, bean.getPublishMsg());
//				bundle.putString(RANDOM, bean.getRandom());
//				bundle.putSerializable(LOGLIST, (Serializable)bean.getListLog());//要求实现Serializable接口
				Bundle bundle = data.getExtras();//getIntent().getExtras();
				System.out.println("--bundle is "+bundle.toString());
				myApp.setPublishCardNO(bundle.getString(PUBLISHCARDNO));
				myApp.setPublishMsg(bundle.getString(PUBLISHMSG));
				myApp.setBeforeChargeMoney(bundle.getInt(BEFORECHARGE_MONEY));
				myApp.setRandom(bundle.getString(RANDOM));
				myApp.setListLog((ArrayList<Map<String, String>>)bundle.getSerializable(LOGLIST));
				frame_bigCard.setVisibility(View.VISIBLE);
				frame_bigCard.invalidate();
				Lin_help.setVisibility(View.GONE);
				Lin_whyClick.setVisibility(View.GONE);
				System.out.println("got request--");
				
				isResultBack = true;//设置返回标志
			}
		}
		super.onActivityResult(requestCode, requestCode, data);
		
	}
	
	/**
	 * 添加frg_NFC_bigCard_SelectMoney fragment
	 */
	private void AddSeletMoneyFrame()
	{	
		if(!isResultBack) return;//不是返回状态，退出
		isResultBack = false;
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.replace(R.id.frame_bigCardInit, new frg_NFC_bigCard_SelectMoney());
		transaction.commit();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		System.out.println("onresume");
		AddSeletMoneyFrame();//恢复后再添加fragment，避免卡数据没更新
		super.onResume();
	}
	
	
}
