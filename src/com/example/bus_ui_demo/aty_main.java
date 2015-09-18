package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.List;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.listview.listCard_Item;
import com.example.listview.listCard_adapter;
import com.example.network.Service_DealCloseReport;
import com.example.network.Service_NetworkConnect;
import com.example.network.SocketClientCls;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class aty_main extends Activity implements OnItemClickListener
{
	private static final String NetStatus = Global_Config.NETWORK_STATUS;
	private static final String NETWORK_STATUS_ACTION = Global_Config.NETWORK_STATUS_ACTION;
	
	private ListView mList;
	private listCard_adapter mAdapter;
	private List<listCard_Item> mlistData = new ArrayList<listCard_Item>();
	
	private static final String SIMCARD_STATUS = Global_Config.SIMCARD_STATUS;
	private static final String SIMCARD_APPLET_STATUS = Global_Config.SIMCARD_APPLET_STATUS;
	
	private RelativeLayout Rel_NetStatus;
	private Intent NetworkConn_intent = null;
	private BroadcastReceiver Networkreceiver = null;
	private boolean SIM_ready = false;//是否找到有效sim卡，找到true，否则false
	private boolean hasSWP_applet = false;//是否在sim卡中找到公交应用，找到true，否则false
	private myApplication myApp = null;
	private Intent Service_Intent = null;
	
	private long lasttime = 0;
	private long rightnow = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		myApp = (myApplication)getApplicationContext();
		
		StartDealReportService();
		FindView();
		InitView();
		
		//SIM_ready = getIntent().getBooleanExtra(SIMCARD_STATUS, true);
		//hasSWP_applet = getIntent().getBooleanExtra(SIMCARD_APPLET_STATUS, false);
		SIM_ready = myApp.isHasSIMCard();
		hasSWP_applet = myApp.getHasSIMCard_applet();
		
		System.out.println("sim ready = " + SIM_ready + " swp applet find " + hasSWP_applet);
	}
	
	/**
	 * 启动圈存完成上报服务
	 */
	private void StartDealReportService()
	{
		Service_Intent = new Intent(aty_main.this, Service_DealCloseReport.class);
		startService(Service_Intent);
	}
	private void FindView()
	{
		mList = (ListView) findViewById(R.id.list);
		
		Rel_NetStatus = (RelativeLayout) findViewById(R.id.Rel_Netstatus);
	}
	
	private void InitView()
	{
		mAdapter = new listCard_adapter(getData());
		mList.setAdapter(mAdapter);
		mList.invalidate();
		
		mList.setOnItemClickListener(this);
		
		Rel_NetStatus.setVisibility(View.GONE);
		
		InitNetService();
	}
	
	
	/**
	 * 初始化网络状态判断服务，并注册广播接收器
	 * 根据网络状态设置网络状态提醒区域的可见性
	 */
	private void InitNetService()
	{
		NetworkConn_intent = new Intent(aty_main.this, Service_NetworkConnect.class);
		startService(NetworkConn_intent);
		Networkreceiver = new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context context, Intent intent)
			{
				// TODO Auto-generated method stub
				System.out.println("-->"+intent.getAction());
				if(intent.getExtras().getBoolean(NetStatus))
				{
					Rel_NetStatus.setVisibility(View.GONE);
				}
				else
				{
					Rel_NetStatus.setVisibility(View.VISIBLE);
					myAlert.ShowToast(aty_main.this, getString(R.string.network_error_notice));
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(NETWORK_STATUS_ACTION);
		registerReceiver(Networkreceiver, filter);
	}
	
	
	
	@Override
	protected void onDestroy()
	{
		DestoryRes();
		if(null != Service_Intent)//停止圈存完成上报服务
		{
			stopService(Service_Intent);
		}
		super.onDestroy();
	}
	
	/**
	 * 销毁资源
	 */
	private void DestoryRes()
	{
		unregisterReceiver(Networkreceiver);
	}
	
	/**
	 * 获取listcard数据
	 * @return
	 */
	private List<listCard_Item> getData()
	{
		Resources rs = getResources();
		
		List<listCard_Item> listData = new ArrayList<listCard_Item>();
		
		//大卡
		listCard_Item bigCard = new listCard_Item(R.drawable.simcard, rs.getString(R.string.bigCard_funcation), rs.getString(R.string.bigCard_funcation_Des));
		listData.add(bigCard);
		//swp卡
		listCard_Item swpCard = new listCard_Item(R.drawable.simcard, rs.getString(R.string.swpCard_funcation), rs.getString(R.string.swpCard_funcation_Des));
		listData.add(swpCard);
		//补登
		listCard_Item thirdPartyPay = new listCard_Item(R.drawable.simcard, rs.getString(R.string.thirdPartyPay_funcation), rs.getString(R.string.thirdPartyPay_Des));
		listData.add(thirdPartyPay);
		//在线补登记录
		listCard_Item thirdPartyPay_record = new listCard_Item(R.drawable.simcard, rs.getString(R.string.thirdPartyPay_rcord_funcation), rs.getString(R.string.thirdPartyPay_record_Des));
		listData.add(thirdPartyPay_record);
		//本地补登记录
		listCard_Item thirdPartyPay_localrecord = new listCard_Item(R.drawable.simcard, rs.getString(R.string.thirdPartyPay_Localrcord_funcation), rs.getString(R.string.thirdPartyPay_Localrecord_Des));
		listData.add(thirdPartyPay_localrecord);
		//人工收银圈存
		listCard_Item humanPay = new listCard_Item(R.drawable.simcard, rs.getString(R.string.humanPay_funcation), rs.getString(R.string.humanPay_Des));
		listData.add(humanPay);
		//其它
		listCard_Item other = new listCard_Item(R.drawable.simcard, rs.getString(R.string.other_funcation), rs.getString(R.string.other_funcation_Des));
		listData.add(other);
		return listData;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		// TODO Auto-generated method stub
		switch (position)
		{
			case 0:
				//bigcard
				myApp.setBigCard(true);//大卡
				Intent intent0 = new Intent(aty_main.this, aty_NFC_bigCard.class);
				startActivity(intent0);
				//MainActivity.this.finish();
				break;
			case 1:
				//swp-nfc
				if(!SIM_ready)
				{
					myAlert.ShowToast(aty_main.this, getString(R.string.NoSimCard));
				}
				else if ((!hasSWP_applet))
				{
					myAlert.ShowToast(aty_main.this, getString(R.string.NotFind_applet));
				}
				else
				{
					 myApp.setBigCard(false);//小卡
					 Intent intent1 = new Intent(aty_main.this, aty_SWP_main.class);
					 startActivity(intent1);
					 //aty_main.this.finish();
				}
				break;
			case 2:
				//补登
				//myAlert.ShowToast(aty_main.this, "补登暂未开通");
				StartThirdPartyPay(aty_main.this);
				break;
			case 3://在线补登记录
				StartThirdPartyPay_NetRecord(aty_main.this);
				break;
			case 4://本地补登记录
				StartThirdPartyPay_localRecord(aty_main.this);
				break;
			case 5:
				//人工收银圈存
				//myAlert.ShowToast(aty_main.this, "人工充值暂未开通");
				StartHumanPay(aty_main.this);
				break;
			case 6:
				//other
				myAlert.ShowToast(aty_main.this, "其它功能");
				break;
			default:
				break;
		}
	}
	
	private void StartThirdPartyPay(Context context)
	{
		Intent intent = new Intent(context, aty_ThirdPartyPayRequest_Init.class);
		startActivity(intent);
	}
	
	private void StartHumanPay(Context context)
	{
		Intent intent = new Intent(context, aty_HumanPay_init.class);
		startActivity(intent);
	}

	private void StartThirdPartyPay_NetRecord(Context context)
	{
		Intent intent = new Intent(context, aty_ThirdPartyPay_NetRecord_Init.class);
		startActivity(intent);
	}
	
	private void StartThirdPartyPay_localRecord(Context context)
	{
		Intent intent = new Intent(context, aty_ThirdPartyPay_LocalRecord.class);
		startActivity(intent);
	}
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		if(lasttime == rightnow)
		{
			lasttime = System.currentTimeMillis();
			//cnt = 1;
			myAlert.ShowToast(aty_main.this, "再按一次退出！");
			return;
		}
		else
		{
			rightnow = System.currentTimeMillis();
			if((rightnow - lasttime) > 2000)
			{
				//cnt = 0;
				//myAlert.ShowToast(aty_main.this, "click 123");
				lasttime = rightnow;
				return;
			}
			else {
				myAlert.ShowToast(aty_main.this, "退出！");
			}
			
		}
		
		super.onBackPressed();
	}


	
	
	
	
}
