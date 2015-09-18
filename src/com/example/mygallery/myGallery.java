package com.example.mygallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;

public class myGallery extends HorizontalScrollView implements OnItemClickListener
{
	private ImageAdapter adapter;
	
	public myGallery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public myGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public myGallery(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setAdapter(ImageAdapter adapter)
	{
		this.adapter = adapter;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		// TODO Auto-generated method stub
		Bitmap img = (Bitmap) adapter.getItem(position);
		
		((ImageView)view).setImageBitmap(img);
	}
	
	
}
