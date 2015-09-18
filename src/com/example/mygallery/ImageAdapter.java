package com.example.mygallery;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.bus_ui_demo.R;



public class ImageAdapter extends BaseAdapter
{
	private List<Bitmap> list;
	private LayoutInflater inflater;
	
	public ImageAdapter(List<Bitmap> list, Context context)
	{
		// TODO Auto-generated constructor stub
		this.list = list;
		inflater = LayoutInflater.from(context);
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
		if(null == convertView)
		{
			convertView = inflater.inflate(R.layout.item_horscrollview, parent, false);
		}
		
		ImageView img = (ImageView) convertView.findViewById(R.id.iv_img);
		img.setImageBitmap(list.get(position));
		
		return convertView;
		//return null;
	}
	
}