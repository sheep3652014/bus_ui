package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.bestpay.OrderParams;
import com.example.config.Global_Config;
import com.example.database.DbManager;
import com.example.database.DbOpenHelper_charge;
import com.example.network.PayParams;
import com.example.network.protocol;
import com.example.nfc.Util;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class frg_SWP_record extends Fragment
{
	private static final String PUBLISHCARDNO = "publishCardNO";
	private static final String MONEY = "Money";
	private static final String TIME = "Time";
	private static final String STATUS = "Status";
	private static final String ORDERNO = "OrderNO";
	private static final String ORDER_SEQ = "Order_Seq";
	private static final String MERCHANTID = "MerchantID";
	
	private static String ORDERNO_HEAD;
	private static String MONEY_HEAD;
	private static String PUBLISHCARDID_HEAD;
	private static String TIME_HEAD;
	private static String STATUS_HEAD;
	private static String ORDER_SEQ_HEAD;
	private static String MERCHANTID_HEAD;
	private static String MONEY_UNIT;
	
	private static final String Charge[] = new String[]{"支付-失败","支付-成功"};
	private static final String Transference[] = new String[]{"/圈存-失败","/圈存-成功"};
	
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int REFUND_SUCCESS = Global_Config.INNER_MSG_START + 1;
	private static final int REFUND_FAIL = Global_Config.INNER_MSG_START + 2;
	
	private ListView listView = null;
	private List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	private myAdapter adapter; 
	
	private View view = null;
	private Context context;
	private myApplication myApp;
	private String SWP_publishCardNO;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		context = container.getContext();
		myApp = (myApplication)context.getApplicationContext();
		SWP_publishCardNO = myApp.getSwp_PublishCardNO();
		InitConstant();
		
		if(null == view)
		{
			view = inflater.inflate(R.layout.frg_swp_record_layout, container, false);
			FindView(view);
			InitView();
			System.out.println("3");
		}
		
		ViewGroup parent = (ViewGroup)view.getParent();
		if(null != parent)
		{
			//parent.removeView(view);
		}
		//return super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}
	
	/**
	 * 初始化控件
	 * @param view
	 */
	private void FindView(View view)
	{
		listView = (ListView) view.findViewById(R.id.lv_record);
	}

	/**
	 * 初始化控件事件
	 */
	private void InitView()
	{
		adapter = new myAdapter(GetData(SWP_publishCardNO));
		listView.setAdapter(adapter);
		listView.invalidate();
		
	}
	
	/**
	 * 常量初始化
	 */
	private void InitConstant()
	{
		ORDER_SEQ_HEAD = getString(R.string.OrderSeq_head);
		MONEY_HEAD = getString(R.string.Money_head);
		PUBLISHCARDID_HEAD = getString(R.string.PublishcardID_head);
		TIME_HEAD = getString(R.string.Time_head);
		STATUS_HEAD = getString(R.string.Status_head);
		ORDERNO_HEAD = getString(R.string.OrderNO_head);
		MERCHANTID_HEAD = getString(R.string.MerchantID_head);
		MONEY_UNIT = getString(R.string.YUAN);
	}
	
	/**
	 * 获取数据
	 * @return List<Map<String, String>>
	 */
	private List<Map<String, String>> GetData(String PublishCardNO)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();;
		
		DbManager dm = new DbManager(context);
		Cursor c = dm.QueryOrderbyPublisCardNO(PublishCardNO);
		if(0 == c.getCount())
		{//本地数据库中没有交易记录，添加实例数据
			
			Map<String, String> map1 = new HashMap<String, String>();
			
			map1.put(ORDERNO, ORDERNO_HEAD + getString(R.string.sample_order));//订单号
			map1.put(ORDER_SEQ, ORDER_SEQ_HEAD + getString(R.string.sample_order));//订单流水号
			map1.put(MONEY, MONEY_HEAD + getString(R.string.charge_money_record));
			map1.put(PUBLISHCARDNO, PUBLISHCARDID_HEAD + getString(R.string.PulbishCardNO_record));
			map1.put(TIME, TIME_HEAD + getString(R.string.time_record));
			map1.put(STATUS, STATUS_HEAD + getString(R.string.status_record_sample));
			
			list.add(map1);
		}
		else {//载入真实交易记录，最近20条
			int i = 0;
			do{
				if(c.moveToNext())
				{
					String OrderNO = c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ));
					String OrderSeq = c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE));
					String M = c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT));
					String PublishCardID = c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID));
					String Time = c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_TIME));
					int ChargeStatus = c.getInt(c.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_STATUS));
					int ChargeNFC_Status = c.getInt(c.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS));
					String Status = Charge[ChargeStatus] + Transference[ChargeNFC_Status];
					
					Map<String, String> map1 = new HashMap<String, String>(); //每次新建一个，避免最终传出的都是同一个值,因为传出的是引用，不是值
					int m1 = Integer.parseInt(M);
					float money = new BigDecimal((double)m1/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
					map1.put(ORDERNO, ORDERNO_HEAD + OrderNO);//订单号
					map1.put(ORDER_SEQ, ORDER_SEQ_HEAD + OrderSeq);//订单流水号
					map1.put(MONEY, MONEY_HEAD + money + MONEY_UNIT);
					map1.put(PUBLISHCARDNO, PUBLISHCARDID_HEAD + PublishCardID);
					map1.put(TIME, TIME_HEAD + Time);
					map1.put(STATUS, STATUS_HEAD + Status);
					map1.put(MERCHANTID, MERCHANTID_HEAD + OrderParams.MERCHANTID);
					
					int id = c.getInt(c.getColumnIndex(DbOpenHelper_charge.ID));
					list.add(map1);
					Log.d("sqlite time", "_"+id+"_"+Time+"liushui:"+OrderNO+" dingdan:"+OrderSeq);
					System.out.println("sqlite :"+"_"+id+"_"+Time+"liushui:"+OrderSeq+" dingdan:"+OrderNO);
				}
			}while(i++ < 20);
		}	

		return list;
	}
	
	class Holder
	{
		ImageButton ib_delete = null;
		ImageButton ib_refund = null;
		TextView tv_charge = null;
		TextView tv_PublishCardNO = null;
		TextView tv_status = null;
		TextView tv_time = null;//订单时间
		TextView tv_OrderSeq = null;//订单流水号
		TextView tv_Money = null;//订单金额
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
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Holder holder = null;
			
			if(null == convertView)
			{
				holder = new Holder();
				
				System.out.println("record" + parent.getContext());
				//convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_record, parent, false);
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frg_swp_record, parent, false);
				
				holder.ib_delete = (ImageButton) convertView.findViewById(R.id.ib_delete);
				holder.ib_refund = (ImageButton) convertView.findViewById(R.id.ib_refund);
				holder.tv_charge = (TextView) convertView.findViewById(R.id.tv_charge);
				holder.tv_PublishCardNO = (TextView) convertView.findViewById(R.id.tv_PublishCardNO);
				holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

				convertView.setTag(holder);
			}
			
			holder = (Holder) convertView.getTag();
			
			SetView(holder, position);
			EnableView(holder, position, list.get(position).get(STATUS));
			InitClick(holder, position);
			
			return convertView;
		}
		
		/**
		 * 设置显示内容
		 * @param holder
		 * @param position
		 */
		private void SetView(Holder holder, int position)
		{
			holder.tv_charge.setText(list.get(position).get(MONEY));
			holder.tv_PublishCardNO.setText(list.get(position).get(PUBLISHCARDNO));
			holder.tv_status.setText(list.get(position).get(STATUS));
			holder.tv_time.setText(list.get(position).get(TIME));
			//holder.tv_OrderSeq.setText(list.get(position).get(ORDER_SEQ));
		}
		
		/**
		 * 根据状态设置 删除、退款 按钮有效状态 以及可见性
		 * @param holder
		 * @param position
		 * @param Status
		 */
		private void EnableView(Holder holder, int position, String Status)
		{
			String status = holder.tv_status.getText().toString();
			
			String ChargeStatus ="";// = status.split("/")[0];
			String CircleStatus ="";// = status.split("/")[1];
			
			if(!status.contains("没有充值记录"))
			{
				ChargeStatus = status.split("/")[0].split("：")[1]; //注意此处冒号是汉字下的冒号
				CircleStatus = "/"+status.split("/")[1];
			}
			
			//只有翼支付付款，但是没有圈存退款按钮才有效
			//翼支付成功，但圈存没成功，是不能删除记录的，用于退款
			if((Charge[1].equals(ChargeStatus)) && (Transference[0].equals(CircleStatus)))
			{
				holder.ib_refund.setVisibility(View.VISIBLE);
				holder.ib_refund.setEnabled(true);System.out.println("-->"+ChargeStatus+"_"+CircleStatus);
				holder.ib_delete.setEnabled(false);
				
			}	
			else
			{
				holder.ib_refund.setVisibility(View.GONE);
				holder.ib_refund.setEnabled(false);System.out.println("-->"+ChargeStatus+"+"+CircleStatus);
				holder.ib_delete.setEnabled(true);
			}
		}
		
		/**
		 * 初始化删除、退款 按钮 监听事件
		 * @param holder
		 */
		private void InitClick(final Holder holder, final int position)
		{
			holder.ib_delete.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					//获取的订单流水号格式为："订单流水号：201505161416123013"
					String OrderSeq = list.get(position).get(ORDER_SEQ);//holder.tv_OrderSeq.getText().toString();
					String OrderSeq_real = OrderSeq.replaceAll(ORDER_SEQ_HEAD, "");
					DbManager dm = new DbManager(context);
					int num = dm.DeletebyReqTranse(OrderSeq_real);
					dm.closeDB();
					Log.d("long click", OrderSeq_real+" 删除数："+num);
					
					list = GetData(SWP_publishCardNO);
					adapter.notifyDataSetChanged();
					listView.invalidate();
					
					if( 0!= num )
					{
						myAlert.ShowToast(context, "删除了订单");
					}
					else
					{
						myAlert.ShowToast(context, "当前没有订单可以删除！");
					}	
				}
			});
			
			holder.ib_refund.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							Looper.prepare();
							Message msg = mHandler.obtainMessage();
							HashMap<String, String> map = new HashMap<String, String>();
							// orderNO 30 AN 原订单号
							// orderSeq 30 AN 原订单流水号
							// orderAmount 8 N 原订单金额 十进制 分
							// phoneNO 11 N 手机号
							String orderNO = list.get(position).get(ORDERNO).replace(getString(R.string.OrderNO_head), "");
							String orderSeq = list.get(position).get(ORDER_SEQ).replace(getString(R.string.OrderSeq_head), "");
							String orderAmount = list.get(position).get(MONEY)
													 .replace(getString(R.string.Money_head), "")
													 .replace(getString(R.string.YUAN), "");
							float m1 = Float.parseFloat(orderAmount);
							int m2 = (int)(m1*100);
							String money = ""+m2;
							money = Util.to8String(money);
							
							String phoneNO = myApp.getPhoneNO();
							map.put(PayParams.ORDERNO, orderNO);
							map.put(PayParams.ORDERSEQ, orderSeq);
							map.put(PayParams.ORDER_AMOUNT, money);
							map.put(PayParams.PHONENO, phoneNO);
							if(protocol.Refund(context, map, mHandler))
							{
								msg.what = REFUND_SUCCESS;
								mHandler.sendMessage(msg);
							}
							else
							{
								msg.what = REFUND_FAIL;
								mHandler.sendMessage(msg);
							}
							Looper.loop();
						}
					}).start();
				}
			});
		}
	}
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(context, context.getString(R.string.network_error));
					break;
				case REFUND_FAIL:
					myAlert.ShowToast(context, context.getString(R.string.refund_fail));
					break;
				case REFUND_SUCCESS:
					myAlert.ShowToast(context, context.getString(R.string.refund_success));
					break;
				default:
					break;
			}
		}
		
	};
	
}
