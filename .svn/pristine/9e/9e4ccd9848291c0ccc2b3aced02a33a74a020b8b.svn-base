package com.example.bus_ui_demo;

import java.math.BigDecimal;

import com.example.application.myApplication;
import com.example.config.Global_Config;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class frg_NFC_bigCard_CircleComplete_notice extends Fragment
{

	//用于大卡充值圈存步骤标识
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	//private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	//private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
		
	private Button btn_back_main;
	private Button btn_back_NFC_bigCard;
	private TextView tv_before;
	private TextView tv_charge;
	private TextView tv_after;
	
	private Context context;
	private myApplication myApp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frg_nfc_bigcard_circlecomplete_notice_layout, container, false);
		
		context = container.getContext();
		myApp = (myApplication) context.getApplicationContext();
		
		FindView(view);
		InitView();
		
		return view;
	}
	
	private void FindView(View view)
	{
		btn_back_main = (Button) view.findViewById(R.id.btn_backmain);
		//btn_back_NFC_bigCard = (Button) view.findViewById(R.id.btn_backChargeRecord);
		tv_before = (TextView) view.findViewById(R.id.tv_before);
		tv_charge = (TextView) view.findViewById(R.id.tv_charge);
		tv_after = (TextView) view.findViewById(R.id.tv_after);
	}
	
	private void InitView()
	{
		btn_back_main.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				myApp.setCircleStep(INIT_STEP);//设置读卡状态了初始状态
				Intent intent = new Intent(context, aty_main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		
		String before = new BigDecimal((double)myApp.getBeforeChargeMoney()/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + getString(R.string.YUAN);
		String charge = new BigDecimal((double)myApp.getChargeMoney()/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + getString(R.string.YUAN);
		String after = new BigDecimal((double)myApp.getAfterChargeMoney()/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + getString(R.string.YUAN);
		
		tv_before.setText(before);
		tv_charge.setText(charge);
		tv_after.setText(after);
		
	}
	
	
}
