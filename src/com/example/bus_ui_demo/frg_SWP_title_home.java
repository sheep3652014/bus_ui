package com.example.bus_ui_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class frg_SWP_title_home extends Fragment
{
	private ImageButton ib_update;
	private ImageButton ib_setting;
	private Context parentContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
	
		View view = inflater.inflate(R.layout.frg_swp_title_home, container, false);
		
		this.parentContext = container.getContext();
		
		FindView(view);
		Init();
		
		return view;
	}
	
	
	private void FindView(View view)
	{
		ib_update = (ImageButton) view.findViewById(R.id.ib_update);
		ib_setting = (ImageButton) view.findViewById(R.id.ib_setting);
		
	}
	
	private void Init()
	{
		ib_update.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(parentContext, "update!", Toast.LENGTH_SHORT).show();
			}
		});
		
		ib_setting.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Toast.makeText(parentContext, "setting!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
