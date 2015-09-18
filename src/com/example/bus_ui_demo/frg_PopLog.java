package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.application.myApplication;
import com.example.config.Global_Config;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class frg_PopLog extends android.support.v4.app.DialogFragment
{
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String TYPE_CHARGE = Global_Config.TYPE_CHARGE;
	private static final String TYPE_CONSUME_NORMAL = Global_Config.TYPE_CONSUME_NORMAL;
	private static final String TYPE_CONSUME = Global_Config.TYPE_CONSUME;
	
	private ListView list_log;
	private myApplication myApp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		View view = inflater.inflate(R.layout.frg_poplog_layout, container, false);
		
		myApp = (myApplication) view.getContext().getApplicationContext();
	
		FindView(view);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//全屏
		//setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		
	}

	private void FindView(View view)
	{
		list_log = (ListView) view.findViewById(R.id.list_log);
		myAdapter adapter = new myAdapter(GetData());
		list_log.setAdapter(adapter);
		list_log.invalidate();
	}
	
	private List<Map<String,String>> GetData()
	{
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		
		list = myApp.getListLog();
		System.out.println("list size = " + list.isEmpty());
		if( (0 == list.size()) || (list.isEmpty())) //没有读取到大卡记录数据，添加示例数据
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(CONSUME_TYPE, "2");
			map.put(CONSUME_MONEY, "100");
			map.put(CONSUME_TIME, "2015.07.08 09:10:11示例");
			list.add(map);
		}
		System.out.println("list is " + list.toString());
		return list;
	}
	
	class myAdapter extends BaseAdapter
	{
		private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		public myAdapter(List<Map<String,String>> list)
		{
			// TODO Auto-generated constructor stub
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
			Holder holder;
			
			if(null == convertView)
			{
				holder = new Holder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frg_listlog, parent, false);
				//TextView tv_consumeType = (TextView) convertView.findViewById(R.id.tv_consumeType);
				holder.tv_consumeType = (TextView) convertView.findViewById(R.id.tv_consumeType);
				holder.tv_consumeTime = (TextView) convertView.findViewById(R.id.tv_consumeTime);
				holder.tv_consumeMoney = (TextView) convertView.findViewById(R.id.tv_consumeMoney);
				//TextView tv_consumeTime = (TextView) convertView.findViewById(R.id.tv_consumeTime);
				//TextView tv_consumeMoney = (TextView) convertView.findViewById(R.id.tv_consumeMoney);
				convertView.setTag(holder);
			}
			else {
				holder = (Holder)convertView.getTag();
			}
			
			String type="";
			int m = Integer.parseInt(list.get(position).get(CONSUME_MONEY));
			float m1 = new BigDecimal((double)m/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			String money = ""+m1 + getString(R.string.YUAN);
			System.out.println("money = " + m + "_" + m1 + "_" + money + " type = " + list.get(position).get(CONSUME_TYPE));
			if(TYPE_CHARGE.equals(list.get(position).get(CONSUME_TYPE)))//根据消费类型配置显示文字
			{
				type = getString(R.string.consume_type3);//充值
				money = "+"+money;
				holder.tv_consumeType.setTextColor(getResources().getColor(R.color.red));
			}	
			else if(TYPE_CONSUME_NORMAL.equals(list.get(position).get(CONSUME_TYPE)))
			{
				type = getString(R.string.consume_type1);//消费
				money = "-"+money;
				holder.tv_consumeType.setTextColor(getResources().getColor(R.color.black));
			}
			else if(TYPE_CONSUME.equals(list.get(position).get(CONSUME_TYPE)))
			{
				type = getString(R.string.consume_type2);//复合消费
				money = "-"+money;
				holder.tv_consumeType.setTextColor(getResources().getColor(R.color.l_blue));
			}
			
			holder.tv_consumeMoney.setText(money);
			holder.tv_consumeTime.setText(list.get(position).get(CONSUME_TIME));
			holder.tv_consumeType.setText(type);
			return convertView;
		}
		
	}

	private class Holder
	{
		TextView tv_consumeType;
		TextView tv_consumeTime;
		TextView tv_consumeMoney;
	}
}
