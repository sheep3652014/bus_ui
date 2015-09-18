package com.example.bus_ui_demo;

import com.example.config.Global_Config;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class frg_SWP_CircleWait extends android.support.v4.app.DialogFragment
{
	private static final String FRG_WAIT_CALL_FROM = Global_Config.FRG_WAIT_CALL_FROM;
	private static final String FRG_WAIT_CALL_FROM_SWP = Global_Config.FRG_WAIT_CALL_FROM_SWP;
	private static final String FRG_WAIT_CALL_FROM_THIRDPARTY = Global_Config.FRG_WAIT_CALL_FROM_THIRDPARTY;
	private static final String FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG = Global_Config.FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG;
	private View view = null;
	private Button btn_exit;
	private Context context = null;
	private TextView tv_notice;
	private TextView tv_alert;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setCancelable(false); getDialog().setCanceledOnTouchOutside(false); 均可设置窗口是否点击消失
		getDialog().setCanceledOnTouchOutside(false);
		view = inflater.inflate(R.layout.frg_swp_circlewait_layout, container, false);
		
		//this.context = container.getContext();
		
		FindView(view);
		InitView();
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		this.context = getActivity();
	}

	private void FindView(View view)
	{
		btn_exit = (Button) view.findViewById(R.id.btn_exitCircle);
		tv_notice = (TextView) view.findViewById(R.id.tv_notice);
		tv_alert = (TextView) view.findViewById(R.id.tv_alert);
	}

	private void InitView()
	{
		InitTextView();
		
		btn_exit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				BackHomeActivity(context);
			}
		});
	}
	
	private void InitTextView()
	{
		String from = getArguments().getString(FRG_WAIT_CALL_FROM);
		if(from.equals(FRG_WAIT_CALL_FROM_SWP))
		{
			tv_notice.setText(getString(R.string.swp_circle_notice));
			tv_alert.setText(getString(R.string.swp_circle_alert));
			btn_exit.setVisibility(View.VISIBLE);
		}
		if(from.equals(FRG_WAIT_CALL_FROM_THIRDPARTY))
		{
			tv_notice.setText(getString(R.string.ThirdPartyPay_notice));
			tv_alert.setText(getString(R.string.ThirdPartyPay_alert));
			btn_exit.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 返回首页面
	 * @param context
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity)context).finish();
	}
}
