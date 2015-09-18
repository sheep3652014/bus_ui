package com.example.bus_ui_demo;

import java.util.HashMap;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.network.PayParams;
import com.example.network.SocketClientCls;
import com.example.network.protocol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class frg_login extends DialogFragment implements OnClickListener
{
	private EditText et_UserName;
	private EditText et_password;
	private Button btn_login;
	private Button btn_dislogin;
	private Context context;
	private SocketClientCls socket = null;
	private String Username = null;
	private String password = null;
	private String imei = null;
	private aty_HumanPay_init aty_humanpay = null;
	private Handler handler = null;
	
	@Override
	public void onActivityCreated(Bundle arg0)
	{
		// TODO Auto-generated method stub
		System.out.println("onActivityCreated --");
		
		context = getActivity();
		aty_humanpay = ((aty_HumanPay_init)context);
		socket = aty_humanpay.getSocket();
		handler = aty_humanpay.getHandler();
		myApplication myApp = (myApplication)context.getApplicationContext();
		imei = myApp.getIMEI();
		
		super.onActivityCreated(arg0);
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		System.out.println("oncreatedialog --");
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Init();
		
		View view = inflater.inflate(R.layout.frg_login_layout, container, false);
		
		FindView(view);
		InitView(view);
		//return super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	private void Init()
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCancelable(false);
		getDialog().setCanceledOnTouchOutside(false);
		
		getDialog().setOnKeyListener(new OnKeyListener()
		{
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				if(KeyEvent.KEYCODE_BACK == keyCode)
				{
					aty_HumanPay_init.BackHome(context);
					return true;
				}
				return false;
			}
		});
	}
	
	private void FindView(View view)
	{
		et_UserName = (EditText) view.findViewById(R.id.et_UserName);
		et_password = (EditText) view.findViewById(R.id.et_password);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_dislogin = (Button) view.findViewById(R.id.btn_dislogin);
	}
	
	private void InitView(View view)
	{
		btn_login.setOnClickListener(this);
		btn_dislogin.setOnClickListener(this);
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btn_login:
				if( (!GetUsername()) || (!GetPassword()) ) return;
				
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Looper.prepare();
						Message msg = handler.obtainMessage();
						boolean isLogin = Login(context, handler, socket, imei);
						if(isLogin)
						{
							msg.what = 100;
							handler.sendMessage(msg);
							synchronized (handler)
							{
								try
								{
									handler.wait();//等待主线程唤醒
								} catch (InterruptedException e)
								{
									// TODO Auto-generated catch block
									System.out.println("error = "+e.getMessage());
								}
							}
							
						}
						
						System.out.println("i'm here can you see me!");
						dismiss();
						Looper.loop();
					}
				}).start();
				//aty_humanpay.setLogin(Login(context, handler, socket, imei));
				break;
			case R.id.btn_dislogin:
				aty_HumanPay_init.BackHome(context);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 返回首页
	 * @param context
	 *//*
	private void BackHome(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity)context).finish();
	}*/
	
	/**
	 * 用户登录
	 * @param context
	 * @param handler
	 * @param socket
	 * @param IMEI
	 * @return
	 */
	private boolean Login(Context context,
			 Handler handler, SocketClientCls socket, String IMEI)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(PayParams.IMEI_NO, IMEI);
		map.put(PayParams.USERNAME, Username);
		map.put(PayParams.PASSWROD, password);
		return (protocol.RequestLogin(context, map, handler, socket));
	}
	
	private boolean GetUsername()
	{
		String str = et_UserName.getText().toString();
		if(str.length() < 4)
		{
			myAlert.ShowToast(context, getString(R.string.username_input_error));
			return false;
		}
		
		Username = str;
		return true;
		
	}
	
	private boolean GetPassword()
	{
		String str = et_password.getText().toString();
		if(6 != str.length())
		{
			myAlert.ShowToast(context, getString(R.string.userpassword_input_error));
			return false;
		}
		
		Username = str;
		return true;
	}
	
	
}
