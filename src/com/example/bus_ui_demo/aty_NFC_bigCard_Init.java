package com.example.bus_ui_demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.nfc.BigCardBean;
import com.example.nfc.CardManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class aty_NFC_bigCard_Init extends Activity
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
	
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private Resources res;
	private TextView tv_NFC_notice;
	private TextView tv_NFC_help;
	private ImageView iv_img;
	
	private myApplication myApp = null;
	private static final int REQUESTCODE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_nfc_bigcard_init_layout);
		
		FindView();
		Init();
	}

	private void FindView()
	{
		tv_NFC_notice = (TextView) findViewById(R.id.tv_NFC_notice);
		tv_NFC_help = (TextView) findViewById(R.id.tv_NFC_help);
		iv_img = (ImageView) findViewById(R.id.iv_img);
		
	}
	
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		if (nfcAdapter != null)
			nfcAdapter.enableForegroundDispatch(this, pendingIntent,
					CardManager.FILTERS, CardManager.TECHLISTS);

		RefreshStatus();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		
		if (nfcAdapter != null)
			nfcAdapter.disableForegroundDispatch(this);
	}
	
	
	private void Init()
	{
		res = getResources();
		myApp = (myApplication) getApplicationContext();
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		onNewIntent(getIntent());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent)
	{
		// TODO Auto-generated method stub
		//super.onNewIntent(intent);
		
		final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if(null != p)
		{
			BigCardBean bean = null;
			System.out.println("got nfc tag");
			if(null != (bean = CardManager.load(p, res, myApp)) )
			{
				System.out.println("here");
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString(PUBLISHCARDNO, bean.getPublishCardNO());
				bundle.putString(PUBLISHMSG, bean.getPublishMsg());
				bundle.putString(RANDOM, bean.getRandom());
				bundle.putInt(BEFORECHARGE_MONEY, bean.getBeforeChargeMoney());
				bundle.putSerializable(LOGLIST, (Serializable)bean.getListLog());//要求实现Serializable接口
				data.putExtras(bundle);
				System.out.println("--->"+bundle.toString()
						+" data "+data.toString());
				setResult(RESULT_OK, data);//需要设置返回码和返回数据，否则不生效
				onActivityResult(REQUESTCODE, Activity.RESULT_OK , data);
				aty_NFC_bigCard_Init.this.finish();
			}
		}
		else
			System.out.println("didn't find any nfc tag!");
	}
	
	private void RefreshStatus()
	{

	    final String tip,tip1;
		if (nfcAdapter == null)
		{
			tip = getString(R.string.tip_nfc_notfound);
			tip1 = tip;
		}
		else if (nfcAdapter.isEnabled())
		{
			tip = getString(R.string.tip_nfc_enabled);
			tip1 = getString(R.string.bigCardInit_notice);
		}
		else
		{
			tip = getString(R.string.tip_nfc_disabled);
			tip1 = getString(R.string.bigCardInit_notice_error);
			
		}
		
		tv_NFC_notice.setText(tip);
		tv_NFC_help.setText(tip1);
	}

	
	
}
