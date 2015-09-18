package com.example.bus_ui_demo;

import com.example.application.myApplication;
import com.example.config.Global_Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class aty_ThirdPartyPayRequest_Init extends Activity
{
	private ImageButton ib_check;
	private LinearLayout Lin_whyClick;
	private myApplication myApp;
	private TextView tv_title;
	private static final int THIRDPARTY_PAY_INIT = Global_Config.THIRDPARTY_PAY_INIT;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_thirdpartypayrequest_init_layout);
		
		myApp = (myApplication) getApplicationContext();
		
		FindView();
		InitView();
	}

	private void FindView()
	{
		ib_check = (ImageButton) findViewById(R.id.ib_check);
		Lin_whyClick = (LinearLayout) findViewById(R.id.Lin_whyClick);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	private void InitView()
	{
		tv_title.setText(getString(R.string.thirdPartyPay_Title));
		
		Lin_whyClick.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(aty_ThirdPartyPayRequest_Init.this);
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
		
		ib_check.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_ThirdPartyPayRequest_Init.this, aty_ThirdPartyPay_Circle.class);
				//startActivity(intent);
				myApp.setCircleStep(THIRDPARTY_PAY_INIT);
				myApp.setBigCard(true);//
				startActivity(intent);
			}
		});
	}
}
