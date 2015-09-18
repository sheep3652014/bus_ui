package com.example.bus_ui_demo;

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

	private static final String TAG = "aty_NFC_bigCard";
	private FrameLayout frame_bigCard;
	
	private FragmentManager fm = null;
	private FragmentTransaction transaction = null;
	private ImageButton ib_check;
	private TextView tv_help_3;
	private TextView tv_help_6;
	
	private LinearLayout Lin_help;
	private LinearLayout Lin_whyClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_nfc_bigcard_layout);
		
		FindView();
		InitView();
		
		
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
		
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.replace(R.id.frame_bigCardInit, new frg_NFC_bigCard_SelectMoney());
		transaction.commit();
		
		ib_check.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_NFC_bigCard.this, aty_NFC_bigCard_Init.class);
				startActivity(intent);
				
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
	
	
}
