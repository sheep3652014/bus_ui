package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.List;

import com.example.Alert.myAlert;
import com.example.config.Global_Config;
import com.example.listview_main.listCard_Item;
import com.example.listview_main.listCard_adapter;
import com.example.network.NetworkConnect_Service;

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
import android.os.IBinder;
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
	
	private RelativeLayout Rel_NetStatus;
	private Intent NetworkConn_intent = null;
	private BroadcastReceiver Networkreceiver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		FindView();
		InitView();
		
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
		NetworkConn_intent = new Intent(aty_main.this, NetworkConnect_Service.class);
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
				Intent intent0 = new Intent(aty_main.this, aty_NFC_bigCard.class);
				startActivity(intent0);
				//MainActivity.this.finish();
				break;
			case 1:
				//swp-nfc
				Toast.makeText(aty_main.this, "swp-nfc", Toast.LENGTH_SHORT).show();
//				Intent intent1 = new Intent(MainActivity.this, cls);
//				startActivity(intent1);
//				MainActivity.this.finish();
				break;
			case 2:
				//other
				Toast.makeText(aty_main.this, "other", Toast.LENGTH_SHORT).show();
//				Intent intent2 = new Intent(MainActivity.this, cls);
//				startActivity(intent2);
//				MainActivity.this.finish();
				break;
			default:
				break;
		}
	}
}
