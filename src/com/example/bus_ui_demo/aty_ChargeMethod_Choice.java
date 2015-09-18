package com.example.bus_ui_demo;

import java.math.BigDecimal;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.bestpay.OrderParams;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class aty_ChargeMethod_Choice extends FragmentActivity
{
	/*private EditText et_PhoneNO;
	private EditText et_Password;
	private LinearLayout Lin_bestpay;
	private Button btn_next;*/
	private Spinner Spinner_choice;
	private TextView tv_chargeMoney;
	private FrameLayout frame_payMethod;
	
	private static String[] payMethods;
	
	private myApplication myApp;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.aty_chargemethod_choice_layout);
		
		payMethods = getResources().getStringArray(R.array.payMethod);
		myApp = (myApplication) getApplicationContext();
		
		FindView();
		Init();
	}
	
	private void FindView()
	{
		tv_chargeMoney = (TextView) findViewById(R.id.tv_chargeMoney);
		/*et_PhoneNO = (EditText) findViewById(R.id.et_PhoneNO);
		et_Password = (EditText) findViewById(R.id.et_password);
		
		btn_next = (Button) findViewById(R.id.btn_next);
		Lin_bestpay = (LinearLayout) findViewById(R.id.Lin_bestpay);*/
		
		frame_payMethod = (FrameLayout) findViewById(R.id.frame_paymethod);
		Spinner_choice = (Spinner) findViewById(R.id.spinner_method);
	}
	
	private void Init()
	{
		float charge = new BigDecimal((double)myApp.getChargeMoney()/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_chargeMoney.setText(charge + getString(R.string.YUAN));
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty_ChargeMethod_Choice.this, android.R.layout.simple_spinner_item, payMethods);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty_ChargeMethod_Choice.this, R.layout.simple_spinner_item, payMethods);
		Spinner_choice.setAdapter(adapter);
		Spinner_choice.invalidate();

		Spinner_choice.setSelection(0);//默认选择翼支付
		//Lin_bestpay.setVisibility(View.VISIBLE);//选择翼支付时翼支付登录框可见
		
		/*btn_next.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) //用于翼支付
			{
				if( (AdapterView.INVALID_POSITION != Spinner_choice.getSelectedItemPosition())
					&& (11 == et_PhoneNO.getText().toString().length()) )
				{
					OrderParams.setAccountID(et_PhoneNO.getText().toString());//保存翼支付账户
					Intent intent = new Intent(aty_ChargeMethod_Choice.this, aty_PopRequestPay.class);
					startActivity(intent);
				}
				else if(AdapterView.INVALID_POSITION == Spinner_choice.getSelectedItemPosition())
				{
					myAlert.ShowToast(aty_ChargeMethod_Choice.this, getString(R.string.paymethod_need_select));
				}
				else if( (et_PhoneNO.getText().toString().isEmpty()) ||(et_PhoneNO.getText().toString().length() < 11))
				{
					myAlert.ShowToast(aty_ChargeMethod_Choice.this, getString(R.string.BestPay_account_error));
				}
			}
		});*/
		
		Spinner_choice.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, getString(R.string.paymethod_toast_notice)+payMethods[position]);
				/*if(0 != position)//翼支付登录框只有选择翼支付时可见
				{
					Lin_bestpay.setVisibility(View.GONE);
				}
				else if(0 == position)
				{
					Lin_bestpay.setVisibility(View.VISIBLE);
				}*/
				System.out.println("on item select 111");
				SetPayMethod(myApp, position);
				PayMethodNotice(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				System.out.println("nothing selected");
			}
			
		});
	}
	
	
	/**
	 * 所选择支付方式的开通提醒,当选择不支持的支付方式时，将付款按钮变为不可见
	 * @param choice
	 */
	private void PayMethodNotice(final int choice)
	{
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
//		0:翼支付
//      1:支付宝
//      2:建设银行
//      3:工商银行
//      4:农业银行
		switch (choice)
		{
			case 0://翼支付
			default:
				System.out.println("--- paymethodnotice 11");
				frame_payMethod.setVisibility(View.VISIBLE);
				frg_PayMethod_BestPay bestPay = new frg_PayMethod_BestPay();
				transaction.replace(R.id.frame_paymethod, bestPay);
				bestPay.SetChoice(choice);//传递支付方式给对应的fragment
				transaction.commit();
				//btn_next.setVisibility(View.VISIBLE);
				break;
			case 1://支付宝
				frame_payMethod.setVisibility(View.GONE);
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "暂不支持");
				//btn_next.setVisibility(View.GONE);
				break;	
			case 2://建行
				frame_payMethod.setVisibility(View.GONE);
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "暂不支持");
				//btn_next.setVisibility(View.GONE);
				break;
			case 3://工行
				frame_payMethod.setVisibility(View.GONE);
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "暂不支持");
				//btn_next.setVisibility(View.GONE);
				break;
			case 4://农行
				frame_payMethod.setVisibility(View.GONE);
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "暂不支持");
				//btn_next.setVisibility(View.GONE);
				break;
			
		}
	}

	/**
	 * 根据获取的支付方式保存为协议规定的支付方式格式
	 * @param myApp
	 * @param choice
	 */
	private void SetPayMethod(myApplication myApp, final int choice)
	{
		int mychoice = choice;
		mychoice = mychoice+1; //获取的值从0开始，协议中从1开始
		if((""+mychoice).length()<2)
			myApp.setPayMethod("0"+mychoice);
		else
			myApp.setPayMethod(""+mychoice);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		System.out.println("orientation = " + newConfig.orientation);
		if( (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) || 
			(Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) )
		{
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.aty_chargemethod_choice_layout);
			
			FindView();
		}
		
		super.onConfigurationChanged(newConfig);
	}
	
	
}
