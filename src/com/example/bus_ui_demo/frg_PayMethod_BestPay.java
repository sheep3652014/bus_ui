package com.example.bus_ui_demo;

import com.example.Alert.myAlert;
import com.example.bestpay.OrderParams;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

public class frg_PayMethod_BestPay extends Fragment
{
	private View view = null;
	private EditText et_PhoneNO;
	private EditText et_Password;
	private Button btn_next;
	private int choice = AdapterView.INVALID_POSITION;
	private Context context = null;
	private static int cnt = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.frg_paymethod_bestpay_layout, container, false);
		
		context = container.getContext();
		
		FindView(view);
		InitView();
		
		return view;
	}

	private void FindView(View view)
	{
		et_PhoneNO = (EditText) view.findViewById(R.id.et_PhoneNO);
		et_Password = (EditText) view.findViewById(R.id.et_password);
		
		btn_next = (Button) view.findViewById(R.id.btn_next);
	}
	
	private void InitView()
	{
		btn_next.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) //用于翼支付
			{
//				if(0 != cnt) return;
//				cnt = 1;
				
				if( (AdapterView.INVALID_POSITION != choice)
					&& (11 == et_PhoneNO.getText().toString().length()) )
				{
					OrderParams.setAccountID(et_PhoneNO.getText().toString());//保存翼支付账户
					Intent intent = new Intent(context, aty_PopRequestPay.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				else if( (et_PhoneNO.getText().toString().isEmpty()) ||(et_PhoneNO.getText().toString().length() < 11))
				{
					myAlert.ShowToast(context, getString(R.string.BestPay_account_error));
				}
			}
		});
	}
	
	public void SetChoice(int choice)
	{
		this.choice = choice;
		System.out.println("chocie = " + choice);
	}

	/*@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		System.out.println("orientation2 = " + newConfig.orientation);
		if( (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) || 
			(Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) )
		{
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			//setContentView(R.layout.aty_chargemethod_choice_layout);
			
		}
		
		super.onConfigurationChanged(newConfig);
	}*/
	
	
}
