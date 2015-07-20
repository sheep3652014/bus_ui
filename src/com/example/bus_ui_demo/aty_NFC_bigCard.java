package com.example.bus_ui_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
	}
	
	private void InitView()
	{
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
