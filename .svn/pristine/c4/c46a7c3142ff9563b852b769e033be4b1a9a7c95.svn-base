package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.List;
import android.R.anim;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class frg_NFC_bigCard_SelectMoney extends Fragment
{
	private GridView gv_list = null;
	private BaseAdapter adapter = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frg_nfc_bigcard_selectmoney, container, false);
		
		FindView(view);
		InitView(view);
		
		return view;
	}
	
	private void FindView(View view)
	{
		gv_list = (GridView) view.findViewById(R.id.gv_list);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void InitView(View view)
	{
		adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_single_choice, getData());
		
		gv_list.setAdapter(adapter);
		gv_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		gv_list.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				switch (position)
				{
					case 0:
						System.out.println("click " + 10);
						break;
					case 1:
						System.out.println("click " + 20);
						break;
					case 2:
						System.out.println("click " + 50);
						break;
					case 3:
						System.out.println("click " + 100);
						break;
					case 4:
						System.out.println("click " + 200);
						break;
					case 5:
						System.out.println("click " + 0.01);
						break;

					default:
						break;
				}
			}
		});
	}
	
	/**
	 * 获取充值数据
	 * @return
	 */
	private List<String> getData()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("10元");
		list.add("20元");
		list.add("50元");
		list.add("100元");
		list.add("200元");
		list.add("0.01元");
		
		return list;
	}
}
