package com.example.bus_ui_demo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.nfc.BigCardBean;
import com.example.nfc.CardManager;
import com.example.nfc.Iso7816;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class aty_ThirdPartyPay_NetRecord extends FragmentActivity
{
	private static final String ORDERNO = Global_Config.ORDERNO;// "orderNO";
	private static final String PUBLISHCARDNO = Global_Config.PUBLISHCARDNO;
	private static final String ORDER_AMOUNT = Global_Config.ORDER_AMOUNT;// "orderAmount";
	
	private Iso7816.Tag tag = null;
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private Resources res;
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int GOT_THIRDPARTYPAY_NETLOG_BALANCE = Global_Config.INNER_MSG_START + 9;
	private static final int GOT_THIRDPARTYPAY_NETLOG = Global_Config.INNER_MSG_START + 10;
	
	private static final String THIRDPARTYPAY_NETLOG = Global_Config.THIRDPARTYPAY_NETLOG;
	
	private myApplication myApp = null;
	private int balance;
	private myAdapter adapter = null;
	
	//private TextView tv_ThirdPartyPayNetLog_Notice;
	private TextView tv_ThirdPartyPayNetLog_help;
	private TextView tv_ThirdPartyPayNetLog_Balance;
	private FrameLayout frame;
	private ListView lv_NetLog;
	private frg_SWP_CircleWait frg_wait = null;
	private Button btn_back;
	private TextView tv_record;
	
	private static final String TAG = "aty_ThirdPartyPay_NetRecord";
	private static final String FRG_WAIT_CALL_FROM = Global_Config.FRG_WAIT_CALL_FROM;
	private static final String FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG = Global_Config.FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG;
	
	private static final String LOGLIST = Global_Config.LOGLIST;
	private FragmentTransaction transaction = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_thirdpartypay_netlog_layout);
		
		FindView();
		Init();
	}

	private void FindView()
	{
		frame = (FrameLayout) findViewById(R.id.frame_notice);
		//tv_ThirdPartyPayNetLog_Notice = (TextView) findViewById(R.id.tv_ThirdPartyPayNetLog_notice);
		tv_ThirdPartyPayNetLog_help = (TextView) findViewById(R.id.tv_ThirdPartyPayNetLog_help);
		lv_NetLog = (ListView) findViewById(R.id.lv_NetLog);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_ThirdPartyPayNetLog_Balance = (TextView) findViewById(R.id.tv_ThirdPartyPayNetLog_Balance);
		tv_record = (TextView) findViewById(R.id.tv_record);
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
	
	private void InitView()
	{
		tv_ThirdPartyPayNetLog_Balance.setText(getString(R.string.Balance));
		
		tv_record.setVisibility(View.GONE);
		tv_record.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				transaction = getSupportFragmentManager().beginTransaction();
				frg_PopLog popLog = new frg_PopLog();
				popLog.show(transaction, "log");
			}
		});
	}
	
	private void Init()
	{
		InitView();
		
		res = getResources();
		myApp = (myApplication) getApplicationContext();
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		onNewIntent(getIntent());
		
		btn_back.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_ThirdPartyPay_NetRecord.this, aty_main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				aty_ThirdPartyPay_NetRecord.this.finish();
			}
		});
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
			//tip = getString(R.string.tip_nfc_enabled);
			tip1 = getString(R.string.bigCardInit_notice);
		}
		else
		{
			//tip = getString(R.string.tip_nfc_disabled);
			tip1 = getString(R.string.bigCardInit_notice_error);
			
		}
		
		//tv_NFC_notice.setText(tip);
		tv_ThirdPartyPayNetLog_help.setText(tip1);
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
			if(null != (bean = CardManager.load(p, res, myApp, aty_ThirdPartyPay_NetRecord.this, mHandler)) )
			{
				System.out.println("back in ui ");
			}
		}
		else
			System.out.println("didn't find any nfc tag!");
	}
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case CONNECT_ERROR:
					
					break;
				case GOT_THIRDPARTYPAY_NETLOG_BALANCE:
					//获取余额，此时可以载入等待fragment
					AddDialog();
					GotBalance(msg);
					break;
				case GOT_THIRDPARTYPAY_NETLOG:
					//获取在线补登记录
					DisMissDialog();
					adapter = new myAdapter(GetData(msg));
					adapter.notifyDataSetChanged();
					lv_NetLog.setAdapter(adapter);
					setListViewHeightBasedOnChildren(lv_NetLog);
					lv_NetLog.invalidate();
				default:
					break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 加载等待fragment
	 */
	private void AddDialog()
	{
		frg_wait = new frg_SWP_CircleWait();
		Bundle args = new Bundle();
		args.putString(FRG_WAIT_CALL_FROM, FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG);
		frg_wait.setArguments(args);
		frg_wait.show(getSupportFragmentManager(), TAG);
	}
	
	private void DisMissDialog()
	{
		if(null != frg_wait)
		{
			frg_wait.dismiss();
		}
	}
	
	/**
	 * 获取余额，并加载过度fragment
	 * @param msg
	 */
	private void GotBalance(Message msg)
	{
		synchronized (mHandler)
		{
			tv_record.setVisibility(View.VISIBLE);//显示消费记录触发textview
			myApp.setListLog((ArrayList<Map<String,String>>)msg.getData().getSerializable(LOGLIST));
			
			balance = msg.arg1;
			float m = new BigDecimal((double)balance/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			String money = getString(R.string.Balance) + m + getString(R.string.YUAN);
			tv_ThirdPartyPayNetLog_Balance.setText(money);
			myAlert.ShowToast(aty_ThirdPartyPay_NetRecord.this, getString(R.string.ThirdPartyPay_readcard_compelete));
			
			mHandler.notify();
		}
	}
	
	/**
	 * 重新根据listview元素个数计算高度，设置listview adapter后调用
	 * 解决scrollview listview嵌套 时listview高度的问题
	 * 子ListView的每个Item必须是LinearLayout，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，
	 * 所以会在onMeasure()时抛出异常
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		 
		myAdapter listAdapter = (myAdapter) listView.getAdapter();
		if (listAdapter == null) {
		return;
		}
		 
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount()-1; i++) //有多个item时少算一个item高度 预留空间给button
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		if(1 == listAdapter.getCount()) //只有一个item时，高度即为该item高度
		{
			View listItem = listAdapter.getView(0, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}	

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
						+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	private List<Map<String, String>> GetData(Message msg)
	{
		return (List<Map<String, String>>) msg.getData().getSerializable(THIRDPARTYPAY_NETLOG);
	}
	
	class myAdapter extends BaseAdapter
	{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		public myAdapter(List<Map<String, String>> list)
		{
			this.list = list;
		}
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			Holder holder = null;
			
			if(null == convertView)
			{
				holder = new Holder();
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.item_thirdpartypay_netlog, parent, false);
				//订单号
				//发行卡号
				//补登金额
				holder.tv_OrderNO = (TextView) convertView.findViewById(R.id.tv_OrderNO);
				holder.tv_PublishCardNO = (TextView) convertView.findViewById(R.id.tv_PublishCardNO);
				holder.tv_Money = (TextView) convertView.findViewById(R.id.tv_OrderMoney);
				convertView.setTag(holder);
			}
			
			holder = (Holder)convertView.getTag();
			
			int m = Integer.parseInt(list.get(position).get(ORDER_AMOUNT), 10);
			float m1 =  new BigDecimal((double)m/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			String money = getString(R.string.thirdPartyPay_money_head) + m1 + getString(R.string.YUAN);
			String orderNO = getString(R.string.OrderNO_head) + list.get(position).get(ORDERNO);
			String publishCardNO = getString(R.string.PublishcardID_head) + list.get(position).get(PUBLISHCARDNO);
			holder.tv_Money.setText(money);
			holder.tv_OrderNO.setText(orderNO);
			holder.tv_PublishCardNO.setText(publishCardNO);
			return convertView;
		}
		
	}
	
	class Holder
	{
		TextView tv_OrderNO;
		TextView tv_PublishCardNO;
		TextView tv_Money;
	}
}
