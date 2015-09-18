package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.myApplication;
import com.example.bus_ui_demo.R;
import com.example.config.Global_Config;
import com.example.listview.ListView_dispatch;

public class frg_SWP_consume extends Fragment
{
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String CONSUME_TERMINAL = Global_Config.CONSUME_TERMINAL;
	private static final String TYPE_CHARGE = "0"+Global_Config.TYPE_CHARGE;//"2"; 
	private static final String TYPE_CONSUME_NORMAL = "0"+Global_Config.TYPE_CONSUME_NORMAL;//"6";
	private static final String TYPE_CONSUME = "0"+Global_Config.TYPE_CONSUME;//"9";
	//private ListView lv_consume;
	private Context context;
	
	private ListView_dispatch lv_consume;
	
	private myAdapter adapter = null;
	
	private View view = null;
	
	private myApplication myApp = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.context = container.getContext();
		myApp = (myApplication)context.getApplicationContext();
		
		if(null == view)
		{
			view = inflater.inflate(R.layout.frg_swp_consume_layout, container, false);
			
			FindView(view);
			InitView();
			System.out.println("2");
		}
		
		ViewGroup parent = (ViewGroup) view.getParent();
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
		//lv_consume = (ListView) view.findViewById(R.id.lv_consume);
		lv_consume = (ListView_dispatch) view.findViewById(R.id.lv_consume);
	}
	
	/**
	 * 初始化控件事件
	 */
	private void InitView()
	{
		//GetData();
		adapter = new myAdapter(GetData());
		lv_consume.setAdapter(adapter);
		lv_consume.invalidate();
	}
	
	/**
	 * 获取adapter数据
	 */
	private List<Map<String, String>> GetData()
	{	
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		list = myApp.getSwp_listLog();
		return list;
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

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Holder holder = null;
			
			if(null == convertView)
			{
				holder = new Holder();
				System.out.println("consume" + parent.getContext());
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frg_swp_consume, parent, false);
				
				holder.tv_consume = (TextView) convertView.findViewById(R.id.tv_consume);
				holder.im_type = (ImageView) convertView.findViewById(R.id.im_type);
				
				convertView.setTag(holder);
			}
			holder = (Holder) convertView.getTag();
			
			//根据交易类型设置图片
			if(TYPE_CHARGE.equals(list.get(position).get(CONSUME_TYPE)) )
			{
				holder.im_type.setBackgroundResource(R.drawable.charge_text);
			}
			if(TYPE_CONSUME_NORMAL.equals(list.get(position).get(CONSUME_TYPE)) )
			{
				holder.im_type.setBackgroundResource(R.drawable.consume_text1);
			}
			if(TYPE_CONSUME.equals(list.get(position).get(CONSUME_TYPE)) )
			{
				holder.im_type.setBackgroundResource(R.drawable.consume_text2);
			}
			
			String text = list.get(position).get(CONSUME_MONEY) + "," 
						 + list.get(position).get(CONSUME_TIME) + ","
						 + list.get(position).get(CONSUME_TERMINAL);
			holder.tv_consume.setText(text);
			
			return convertView;
		}
		
	}
	
	class Holder 
	{
		TextView tv_consume;
		ImageView im_type;
	}
}
