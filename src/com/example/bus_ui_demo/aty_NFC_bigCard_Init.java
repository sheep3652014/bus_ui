package com.example.bus_ui_demo;

import com.example.nfc.CardManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Window;
import android.widget.TextView;

public class aty_NFC_bigCard_Init extends Activity
{
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private Resources res;
	private TextView tv_NFC_notice;
	
	
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
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		// TODO Auto-generated method stub
		//super.onNewIntent(intent);
		
		final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if(null != p)
		{
			CardManager.load(p, res);
		}
		else
			System.out.println("didn't find any nfc tag!");
	}
	
	private void RefreshStatus()
	{

	    final String tip;
		if (nfcAdapter == null)
			tip = getString(R.string.tip_nfc_notfound);
		else if (nfcAdapter.isEnabled())
			tip = getString(R.string.tip_nfc_enabled);
		else
			tip = getString(R.string.tip_nfc_disabled);
		
		tv_NFC_notice.setText(tip);
	}
	
}
