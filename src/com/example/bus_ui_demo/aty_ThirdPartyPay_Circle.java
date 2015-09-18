package com.example.bus_ui_demo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.bestpay.Util;
import com.example.bus_ui_demo.frg_PopLog.myAdapter;
import com.example.config.Global_Config;
import com.example.database.DbManager_Third_Human;
import com.example.database.DbOpenHelper_Third_Human;
import com.example.database.Holder_Third_Human;
import com.example.network.Service_DealCloseReport;
import com.example.network.Service_DealCloseReport.myBinder;
import com.example.nfc.BigCardBean;
import com.example.nfc.CardManager;
import com.example.nfc.Iso7816;
import com.example.nfc.PbocCard;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.YuvImage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nfc.YiChangTong;

public class aty_ThirdPartyPay_Circle extends FragmentActivity implements OnClickListener
{
	private static final String BEFORECHARGE_MONEY = Global_Config.BEFORECHARGE_MONEY;
	private static final String CHARGE_MONEY = Global_Config.CHARGE_MONEY;
	private static final String AFTERCHARGE_MONEY = Global_Config.AFTERCHARGE_MONEY;
	private static final String CONSUME = Global_Config.CONSUME;
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String PUBLISHMSG = Global_Config.PUBLISHMSG;
	private static final String PUBLISHCARDNO = Global_Config.PUBLISHCARDNO;
	private static final String RANDOM = Global_Config.RANDOM;
	private static final String LOGLIST = Global_Config.LOGLIST;
	private static final String CIRCLEINIT_MSG = Global_Config.CICLEINIT_MSG;
	//private static final String TAC_CODE = Global_Config.TAC_CODE;
	
	private static final int THIRDPARTY_PAY_INIT = Global_Config.THIRDPARTY_PAY_INIT;
	//private static final int THIRDPARTY_PAY_CIRCLE = Global_Config.THIRDPARTY_PAY_CIRCLE;
	protected static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int CIRCLE_START_MSG = Global_Config.INNER_MSG_START + 1;
	private static final int GOT_THIRDPARTYPAY_MSG = Global_Config.INNER_MSG_START + 2;
	private static final int THIRDPARTYPAY_REQUEST_CMD_FIAL_MSG = Global_Config.INNER_MSG_START + 3;
	private static final int THIRDPARTYPAY_CIRCLE_FAIL_MSG = Global_Config.INNER_MSG_START + 4;
	private static final int THIRDPARTYPAY_CIRCLE_SUCCESS_MSG = Global_Config.INNER_MSG_START + 5;
	private static final int THIRDPARTYPAY_REQUEST_CMD_SUCCESS_MSG = Global_Config.INNER_MSG_START + 6;
	private static final int GOT_THIRDPARTYPAY_FAIL_MSG = Global_Config.INNER_MSG_START + 7;
	private static final int GOT_TAG_SHOW_NOTICE = Global_Config.INNER_MSG_START + 8;
	private static final String NFC_TAG = Global_Config.NFC_TAG;
	private static final String REQUEST_CIRCLE_ACK = "ack";//申请圈存指令的应答
	private static final String THIRDPARTYPAY_LIST = Global_Config.THIRDPARTYPAY_LIST;
	private static final String ORDERNO = Global_Config.ORDERNO;// "orderNO";
	private static final String ORDERSEQ = Global_Config.ORDERSEQ;// "orderSeq";
	private static final String ORDER_AMOUNT = Global_Config.ORDER_AMOUNT;//
	private static final String ORDER_DATE = Global_Config.ORDER_DATE;
	private static final String BANKCARDNO = Global_Config.BANKCARDNO;
	private static final String FAKE_PUBLISHCARDNO = Global_Config.FAKE_PUBLISHCARDNO;
	
	private static final String FRG_WAIT_CALL_FROM = Global_Config.FRG_WAIT_CALL_FROM;
	private static final String FRG_WAIT_CALL_FROM_THIRDPARTY = Global_Config.FRG_WAIT_CALL_FROM_THIRDPARTY;
	
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private Resources res;
	//private TextView tv_NFC_notice;
	private TextView tv_NFC_help;
	private ImageView iv_img;
	
	private myApplication myApp = null;
	private static final int REQUESTCODE = 0;
	private static final int REQUESTCODE_CIRCLE = 1;
	
	//private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private LinearLayout Lin_select;
	
	private myAdapter adapter = null;
	private ListView lv_payrecord;
	private Button btn_circle;
	
	private int chargeMoney = 0;
	private Iso7816.Tag tag = null;
	private ServiceConnection serconn = null;
	private frg_SWP_CircleWait circleWait = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_thirdpartypay_circle);
		
		myApp = (myApplication)getApplicationContext();
		FindView();
		Init();
	}

	private void FindView()
	{
		//tv_NFC_notice = (TextView) findViewById(R.id.tv_NFC_notice);
		tv_NFC_help = (TextView) findViewById(R.id.tv_NFC_help);
		iv_img = (ImageView) findViewById(R.id.iv_img);
		Lin_select = (LinearLayout) findViewById(R.id.lin_select);
		lv_payrecord = (ListView) findViewById(R.id.lv_payrecord);
		btn_circle = (Button) findViewById(R.id.btn_circle);
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
		Lin_select.setVisibility(View.GONE);//没有申请到补登数据前为不可见
		btn_circle.setOnClickListener(this);
		
		lv_payrecord.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				Holder holder = (Holder)view.getTag();
				
				adapter.setSelectPosition(position);
				adapter.notifyDataSetInvalidated();
				
				String m = holder.tv_money.getText().toString()
						  .replace(getString(R.string.thirdPartyPay_money_head), "")
						  .replace(getString(R.string.YUAN), "");//list.get(position).get(ORDER_AMOUNT);
				int money = (int)(Float.parseFloat(m)*100);
				String orderNO = holder.tv_OrderNO.getText().toString()
								 .replace(getString(R.string.OrderNO_head), "");//list.get(position).get(ORDERNO);
				String orderSeq = orderNO;//list.get(position).get(ORDERSEQ);
				String BankID = holder.tv_BankID.getText().toString()
								.replace(getString(R.string.thirdPartyPay_BankID_head), "");//list.get(position).get(BANKCARDNO);
				/*int len = orderNO.length();
				for(int i = 0; i < (30-len); i++)
					orderNO = orderNO + " ";
				len = orderSeq.length();
				for(int i = 0; i < (30-len); i++)
					orderSeq = orderSeq + " ";*/
				myApp.setChargeMoney(money);
				myApp.setOrderNO(orderNO);
				myApp.setOrderSeq(orderSeq);
				
				float m2 = new BigDecimal((double)money/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getResources().getString(R.string.thirdPartyPay_money_head)+m2);
				
				if(BankID.equals(getString(R.string.sample_BankID)))
				{
					myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, "当前为示例数据,不能用于圈存！");
					btn_circle.setClickable(false);
				}
				else
				{
					btn_circle.setClickable(true);
					myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.ThirdPartyPayRecord_beginCircle));
				}
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
			if(null != (bean = CardManager.load(p, res, myApp, aty_ThirdPartyPay_Circle.this, mHandler)) )
			{
				System.out.println("back in ui ");
			}
		}
		else
			System.out.println("didn't find any nfc tag!");
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
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			System.out.println("got msg = " + msg.what);
			// TODO Auto-generated method stub
			if(CONNECT_ERROR == msg.what)
			{
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.network_error));
			}
			if(GOT_TAG_SHOW_NOTICE == msg.what)
			{
				synchronized (mHandler)
				{
					NoticeDialog();
					mHandler.notify();//唤醒yichangtong的补登圈存线程
				}
			}
			if(GOT_THIRDPARTYPAY_FAIL_MSG == msg.what)
			{
				circleWait.dismiss();//关闭补登申请提醒
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, "没有补登信息或获取补登信息失败");
				//BackHome(aty_ThirdPartyPay_Circle.this);
				//显示补登金额选择界面
				Lin_select.setVisibility(View.VISIBLE);//获取补登数据，设置可见
				adapter = new myAdapter(GetData(FAKE_PUBLISHCARDNO));//加载数据，显示补登订单列表
				adapter.notifyDataSetChanged();
				lv_payrecord.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
				lv_payrecord.setAdapter(adapter);
				lv_payrecord.invalidate();
			}
			if(GOT_THIRDPARTYPAY_MSG == msg.what)
			{
				circleWait.dismiss();//关闭补登申请提醒
				Tag nfcTag = (Tag)msg.getData().getParcelable(NFC_TAG);
				IsoDep iso = IsoDep.get(nfcTag);
				//Iso7816.Tag tag = new Iso7816.Tag(iso);
				tag = new Iso7816.Tag(iso);
				
				List<Map<String, String>> templist = new ArrayList<Map<String, String>>();
				templist = (ArrayList<Map<String, String>>) msg.getData().getSerializable(THIRDPARTYPAY_LIST);
				
				String publishCardNO;
				if(!templist.isEmpty())
				{
					InsertItems(templist);//插入补登表项
					publishCardNO = templist.get(0).get(PUBLISHCARDNO);//因为返回的list都是同一个发行卡号下的数据
				}
				else {
					publishCardNO = FAKE_PUBLISHCARDNO;
				}
				
				//显示补登金额选择界面
				Lin_select.setVisibility(View.VISIBLE);//获取补登数据，设置可见
				adapter = new myAdapter(GetData(publishCardNO));//加载数据，显示补登订单列表
				adapter.notifyDataSetChanged();
				lv_payrecord.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
				lv_payrecord.setAdapter(adapter);
				setListViewHeightBasedOnChildren(lv_payrecord);
				lv_payrecord.invalidate();
				
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.Select_ThirdPartyPayRecord));
				
			}
			if(THIRDPARTYPAY_REQUEST_CMD_FIAL_MSG == msg.what)
			{
				myApp.setCircleStep(THIRDPARTY_PAY_INIT);
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.RequestCirlce_fail));
			}
			if(THIRDPARTYPAY_REQUEST_CMD_SUCCESS_MSG == msg.what)
			{
				synchronized (mHandler)
				{
					myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.RequestCirlce_success));
					//更新状态
					DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
					Holder_Third_Human holder = db.QueryOrderStatus(myApp.getOrderNO().trim());
					System.out.println("orderno = " + myApp.getOrderNO() + "_");
					if(null == holder)
					{
						myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, "无订单信息");
						db.closeDB();
						return;//无订单信息
					}
					db.closeDB();
					//更新表
					UpdateSpecialItem(myApp.getOrderNO(), holder.isStatus_Request(), true, holder.isStatus_Circle(), holder.isStatus_Circle_Report(), holder.getTac());
					mHandler.notify();//唤醒yichangtong的补登圈存线程
				}
			}
			if(THIRDPARTYPAY_CIRCLE_FAIL_MSG == msg.what)
			{
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.circle_fail));
				
			}
			if(THIRDPARTYPAY_CIRCLE_SUCCESS_MSG == msg.what)
			{
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.circle_success));
				DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
				Holder_Third_Human holder = db.QueryOrderStatus(myApp.getOrderNO().trim());
				db.closeDB();
				if(null == holder) return;//无订单信息
				//更新表
				UpdateSpecialItem(myApp.getOrderNO().trim(), holder.isStatus_Request(), holder.isStatus_GotCircle(), true, false, myApp.getTAC_CARD());
				//绑定圈存结果上报
				BindSerive(aty_ThirdPartyPay_Circle.this);
				AddCircleCompelete();
				
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * 申请补登记录提醒
	 */
	private void NoticeDialog()
	{
		circleWait = new frg_SWP_CircleWait();
		Bundle args = new Bundle();
		args.putString(FRG_WAIT_CALL_FROM, FRG_WAIT_CALL_FROM_THIRDPARTY);
		circleWait.setArguments(args);
		circleWait.show(getSupportFragmentManager(), "aty_thirdparty_notice");
	}
	
	/**
	 * 返回首页
	 * @param context
	 */
	private static void BackHome(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		((Activity)context).finish();
	}
	/**
	 * 添加圈存完成后界面
	 */
	private void AddCircleCompelete()
	{
		// transaction.remove(circleWait);

		FragmentTransaction transaction2 = getSupportFragmentManager()
				.beginTransaction();
		transaction2.replace(R.id.frame_notice,
				new frg_NFC_bigCard_CircleComplete_notice());
		transaction2.commit();
	}
	
//	private void BackHomeActivity(Context context)
//	{
//		Intent intent = new Intent(context, aty_main.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//		((Activity)context).finish();
//	}
	
	/**
	 * 获取指定发行卡号下的补登数据，无补登数据，返回示例数据
	 * @param publishCardNO
	 * @return
	 */
	private List<Map<String, String>> GetData(String publishCardNO)
	{
		//List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		return QueryUnCircleItem(publishCardNO);
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
		tv_NFC_help.setText(tip1);
	}

	
	private class myAdapter extends BaseAdapter
	{
		private List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		private int selectPosition = -1;
		
		
		public myAdapter(List<Map<String, String>> list)
		{
			// TODO Auto-generated constructor stub
			this.list = list;
		}
		
		public void setSelectPosition(int selectPosition)
		{
			this.selectPosition = selectPosition;
		}
		
		public int getSelectPosition()
		{
			return selectPosition;
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
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thirdpartypay_listview, parent, false);
				holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				holder.tv_OrderNO = (TextView) convertView.findViewById(R.id.tv_OrderNO);
				holder.tv_OrderTime = (TextView) convertView.findViewById(R.id.tv_OrderTime);
				holder.tv_BankID = (TextView) convertView.findViewById(R.id.tv_BankID);
				convertView.setTag(holder);
			}
			else {
				holder = (Holder) convertView.getTag();
			}
//			map.put(ORDER_AMOUNT, ""+100);
//			map.put(ORDERNO, "12345667890123456"); //可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置相同
//			map.put(ORDERSEQ, "12345667890123456");
//			map.put(ORDER_DATE, "20150816");
//			map.put(BANKCARDNO, "0000000000000000000");
			Resources res = getResources();
			int m = Integer.parseInt(list.get(position).get(ORDER_AMOUNT));
			float m1 = new BigDecimal((double)m/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			String money = res.getString(R.string.thirdPartyPay_money_head) + m1
						  + res.getString(R.string.YUAN);
			String OrderNO = res.getString(R.string.OrderNO_head) + list.get(position).get(ORDERNO);
			String OrderTime = res.getString(R.string.Time_head) + list.get(position).get(ORDER_DATE);
			String BankID = res.getString(R.string.thirdPartyPay_BankID_head) + list.get(position).get(BANKCARDNO);
			holder.tv_money.setText(money);
			holder.tv_OrderNO.setText(OrderNO);
			holder.tv_OrderTime.setText(OrderTime);
			holder.tv_BankID.setText(BankID);
			
			SetColor(holder, position);
			
			return convertView;
		}
		
		private void SetColor(Holder holder, int position)
		{
			if(position == ((myAdapter)lv_payrecord.getAdapter()).getSelectPosition() )
			{
				holder.tv_money.setTextColor(getResources().getColor(R.color.black));
				holder.tv_OrderNO.setTextColor(getResources().getColor(R.color.black));
				holder.tv_OrderTime.setTextColor(getResources().getColor(R.color.black));
				holder.tv_BankID.setTextColor(getResources().getColor(R.color.black));
			}
			else
			{
				holder.tv_money.setTextColor(getResources().getColor(R.color.deep_gray));
				holder.tv_OrderNO.setTextColor(getResources().getColor(R.color.deep_gray));
				holder.tv_OrderTime.setTextColor(getResources().getColor(R.color.deep_gray));
				holder.tv_BankID.setTextColor(getResources().getColor(R.color.deep_gray));
			}	
		}
		
	}
	
	private class Holder
	{
		//	 		8 N orderMount 补登金额，十进制 分
		// 		30 AN orderNO
		//      8 N 订单时间 yyyymmdd
		//      19 N 银行卡号  无卡号 全0
		private TextView tv_money;
		private TextView tv_OrderNO;
		private TextView tv_OrderTime;
		private TextView tv_BankID;
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(R.id.btn_circle == v.getId())
		{
			if(lv_payrecord.getCheckedItemCount() < 1)
			{
				myAlert.ShowToast(aty_ThirdPartyPay_Circle.this, getString(R.string.ThirdPartyPay_youneedchocice));
				return;
			}
			
			//myApp.setChargeMoney(chargeMoney);
			YiChangTong.ThirdPartyPay_Circle(tag, res, myApp, mHandler, aty_ThirdPartyPay_Circle.this);
		}
	}
	
	
	/**
	 * 绑定圈存完成上报服务
	 * @param context
	 */
	private void BindSerive(Context context)
	{
		serconn = new ServiceConnection()
		{
			
			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				// TODO Auto-generated method stub
				System.out.println("disconnect");
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				// TODO Auto-generated method stub
				System.out.println("connect");
				Service_DealCloseReport mService = ((myBinder)service).getService();
			}
		};
		
		Intent intent = new Intent(context, Service_DealCloseReport.class);
		bindService(intent, serconn, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		if(null != serconn)
		{
			unbindService(serconn);
		}
		super.onDestroy();
	}
	
	/**
	 * 根据申请到的补登信息，插入表项
	 * @param list
	 */
	private void InsertItems(List<Map<String, String>> list)
	{
		DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
		
		for(int i = 0; i < list.size(); i++)
		{
//			map1.put(ORDER_AMOUNT, money);
//			map1.put(ORDERNO, orderNO); //可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置相同
//			map1.put(ORDERSEQ, orderNO);
//			map1.put(ORDER_DATE, date);
//			map1.put(BANKCARDNO, BankCardNO);
//			map1.put(PUBLISHCARDNO, map.get(PUBLISHCARDNO));
			String username = null;
			String OrderTime = list.get(i).get(ORDER_DATE);
			String OrderNO = list.get(i).get(ORDERNO);
			String OrderSeq = OrderNO;
			String OrderAmount = list.get(i).get(ORDER_AMOUNT);
			String OrderCash = null;
			String PublishCardNO = list.get(i).get(PUBLISHCARDNO);
			String BankCardID = list.get(i).get(BANKCARDNO);
			boolean isStatusRequest = true;
			boolean isStatusGotCircle = false;
			boolean isStatusCircle = false;
			boolean isStatusCircleReport = false;
			String Tac = null;
			Holder_Third_Human holder = new Holder_Third_Human(username, OrderTime, OrderNO,
					OrderSeq, OrderAmount, OrderCash, PublishCardNO, BankCardID,
					isStatusRequest, isStatusGotCircle, isStatusCircle, isStatusCircleReport,
					Tac);
			try
			{
				db.InsertItem(holder);
			} catch (Exception e)
			{
				// TODO: handle exception
				System.out.println("InsertItems " + e.getMessage());
				db.closeDB();
			}
		}
		db.closeDB();
	}
	
	/**
	 * 查找之前发行卡号对应的表项,
	 * @param publishCardNO 发行卡号
	 * @return 该卡号下的过往补登数据，无补登数据则返回示例数据
	 */
	private List<Map<String, String>> QueryUnCircleItem(String publishCardNO)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
		
		Cursor c = db.QueryOrderbyPublishCardNO(publishCardNO);
		
		if(0 == c.getCount()) 
		{
			//获取补登数据失败，或者没有补登数据,加入示例数据
			HashMap<String, String> map = new HashMap<String, String>();
			
			map.put(ORDER_AMOUNT, getString(R.string.sample_money));
			map.put(ORDERNO, getString(R.string.sample_order)); //可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置相同
			map.put(ORDERSEQ, getString(R.string.sample_order));
			map.put(ORDER_DATE, getString(R.string.sample_time));
			map.put(BANKCARDNO, getString(R.string.sample_BankID));
			map.put(PUBLISHCARDNO, FAKE_PUBLISHCARDNO);
			map.put(DbOpenHelper_Third_Human.STATUS_REQUEST, ""+0);
			map.put(DbOpenHelper_Third_Human.STATUS_CIRCLE, ""+0);
			map.put(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE, ""+0);
			map.put(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT, ""+0);
			list.add(map);
		}
		while(c.moveToNext())
		{
//			map.put(ORDER_AMOUNT, ""+100);
//			map.put(ORDERNO, "12345667890123456"); //可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置相同
//			map.put(ORDERSEQ, "12345667890123456");
//			map.put(ORDER_DATE, "20150816");
//			map.put(BANKCARDNO, "0000000000000000000");
			String OrderNO = c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO));
			String OrderTime = c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME));
			String OrderAmount = c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_AMOUNT));
			String BankCardNO = c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.BANKCARDID));
			
			Map<String, String> map = new HashMap<String, String>();
			map.put(ORDERNO, OrderNO);
			map.put(ORDER_DATE, OrderTime);
			map.put(BANKCARDNO, BankCardNO);
			map.put(ORDER_AMOUNT, OrderAmount);
			list.add(map);
		}
		
		c.close();
		db.closeDB();
		return list;
	}
	
	/**
	 * 根据订单号更新对应指定表内容
	 * @param OrderNO
	 * @param isStatusRequest
	 * @param isStatusGotCircle
	 * @param isStatusCircle
	 * @param isStatusCircleReport
	 * @param tac
	 */
	private void UpdateSpecialItem(String OrderNO, boolean isStatusRequest, boolean isStatusGotCircle,
								   boolean isStatusCircle, boolean isStatusCircleReport, String tac)
	{
		DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
		
		Cursor c = db.QueryOrderbyOrderNO(OrderNO);
		System.out.println("c.getcount = " + c.getCount() +"_");
		if(0 == c.getCount())
		{
			c.close();
			db.closeDB();
			return;
		}
		while(c.moveToNext())
		{
			Holder_Third_Human holder = new Holder_Third_Human(c);
			holder.set_isStatus_Request(isStatusRequest);
			holder.set_isStatus_GotCircle(isStatusGotCircle);
			holder.set_isStatus_Circle(isStatusCircle);
			holder.set_isStatus_Circle_Report(isStatusCircleReport);
			holder.setTac(tac);
			System.out.println("in status = " + isStatusRequest+"_"+isStatusGotCircle 
					+"_"+isStatusCircle+"_"+isStatusCircleReport);
			System.out.println("status = " + holder.getStatus_Request()+"_"+holder.getStatus_GotCircle() 
						+"_"+holder.getStatus_Circle()+"_"+holder.getStatus_Circle_Report());
			db.UpdateOrderbyHolderItem(holder);
		}
		c.close();
		db.closeDB();
		
	}
	
	/**
	 * 根据订单号查找订单状态
	 * @param OrderNO
	 * @return 返回
	 * 		   <p>补登信息申请状态
	 * 		   <p>获取圈存信息状态
	 * 		   <p>圈存成功状态
	 * 		   <p>圈存成功状态上报状态
	 *//*
	private Holder_Third_Human QueryOrderStatus(String OrderNO)
	{
		Holder_Third_Human holder = new Holder_Third_Human();
		DbManager_Third_Human db = new DbManager_Third_Human(aty_ThirdPartyPay_Circle.this);
		
		Cursor c = db.QueryOrderbyOrderNO(OrderNO);
		if(0 == c.getCount()) return null;
		
		holder.setStatus_Request(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_REQUEST)));
		holder.setStatus_GotCircle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE)));
		holder.setStatus_Circle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE)));
		holder.setStatus_Circle_Report(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT)));
		
		c.close();
		db.closeDB();
		return holder;
	}*/
}
