package com.example.listview_main;

import java.util.ArrayList;
import java.util.List;

import com.example.bus_ui_demo.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listCard_adapter extends BaseAdapter
{
	private List<listCard_Item> listData = new ArrayList<listCard_Item>();
	
	public listCard_adapter(List<listCard_Item> listData)
	{
		this.listData = listData;
		
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return listData.get(position);
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
		Holder holder = null;
		
		if(null == convertView)
		{
			holder = new Holder();
			
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listcard, parent, false);
			
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_Title);
			holder.tv_des = (TextView) convertView.findViewById(R.id.tv_Des);
			
			convertView.setTag(holder);
			
		}
		holder = (Holder) convertView.getTag();
		
		InitView(holder, position);
		
		return convertView;
	}
	
	private void InitView(Holder holder, int position)
	{
		holder.img.setImageResource(listData.get(position).getmImgID());
		holder.tv_title.setText(listData.get(position).getmType());
		holder.tv_des.setText(listData.get(position).getmDescreption());
	}
	
	class Holder
	{
		ImageView img = null;
		TextView tv_title = null;
		TextView tv_des = null;
		
	}
}
