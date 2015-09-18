package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Frgpager_adapter extends FragmentPagerAdapter
{
	private List<String> arrayList = new ArrayList<String>();
	private List<Fragment> fragments = new ArrayList<Fragment>();
	
	public Frgpager_adapter(FragmentManager fm)
	{
		super(fm);
	}
	
	public Frgpager_adapter(FragmentManager fm, List<String> arrayList, List<Fragment> fragments)
	{
		super(fm);
		
		this.arrayList = arrayList;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		// TODO Auto-generated method stub
		return super.getPageTitle(position);
		//return arrayList.get(position);
	}

	
}
