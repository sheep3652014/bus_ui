package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.Frgpager_adapter;
import com.example.bus_ui_demo.R;

public class aty_SWP_main extends FragmentActivity
{
	private static final int RIGHT = 1;
	private static final int LEFT = 2;
	private ViewPager viewPager;
	private PagerTabStrip PagerTab;
	private FrameLayout frame_Title;
	private FrameLayout frame_content;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	private List<String> titlelist = new ArrayList<String>();
	private List<Fragment> fragmentlist = new ArrayList<Fragment>();
	
	private GestureDetector gesture = null;
	
	private TextView tv_tab0, tv_tab1, tv_tab2;
	private ImageView iv_img;
	private int offset = 0;// 动画图片偏移量 
	private int bitmapwidth;// 图片宽度
	private int currIndex = 0;// 当前页卡编号 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_swp_main);
		
		FindView();
		Init();
		
	}
	
	
	private void FindView()
	{
		frame_Title = (FrameLayout) findViewById(R.id.frame_Title);
		viewPager = (ViewPager) findViewById(R.id.ViewPager);
		//PagerTab = (PagerTabStrip) findViewById(R.id.PagerTab);
		
		tv_tab0 = (TextView) findViewById(R.id.tv_tab0);
		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
		
		tv_tab0.setOnClickListener(new MyOnClickListener(0));
		tv_tab1.setOnClickListener(new MyOnClickListener(1));
		tv_tab2.setOnClickListener(new MyOnClickListener(2));
	}
	
	private void Init()
	{
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		
		transaction.replace(R.id.frame_Title, new frg_SWP_title_home());
		//transaction.addToBackStack(null);//添加至backstack栈
		
		InitImageView();
		InitViewPager(fm);
		
		transaction.commit();
		
	}
	
	private void InitImageView() {  
		  iv_img = (ImageView) findViewById(R.id.iv_img);  
	      
		  bitmapwidth = BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();// 获取图片宽度  
	      DisplayMetrics dm = new DisplayMetrics();  
	      getWindowManager().getDefaultDisplay().getMetrics(dm);  
	      int screenW = dm.widthPixels;// 获取分辨率宽度  
	      offset = (screenW / 3 - bitmapwidth) / 2;// 计算偏移量  
	      Matrix matrix = new Matrix();  
	      matrix.postTranslate(offset, 0);  
	      iv_img.setImageMatrix(matrix);// 设置动画初始位置  
	  }  
	
	
	private void InitViewPager(FragmentManager fm)
	{
		String[] titles = getResources().getStringArray(R.array.Title);
		
		//PagerTab.setTextColor(getResources().getColor(R.color.l_blue));
		//PagerTab.setTabIndicatorColor(getResources().getColor(R.color.l_blue));
		//PagerTab.setBackgroundColor(getResources().getColor(R.color.white));
		//PagerTab.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//newAPI
		
		
		if(titles.length < 3)
		{
			titles[0] = "--";
			titles[1] = "--";
			titles[2] = "--";
		}
		titlelist.add(titles[0]);
		titlelist.add(titles[1]);
		titlelist.add(titles[2]);
		
		Fragment frg_home = new frg_SWP_home();
		Fragment frg_consume = new frg_SWP_consume();
		Fragment frg_record = new frg_SWP_record();
		
		fragmentlist.add(frg_home);
		fragmentlist.add(frg_consume);
		fragmentlist.add(frg_record);
		
		viewPager.setAdapter(new Frgpager_adapter(fm, null, fragmentlist));
		viewPager.setCurrentItem(0);
		//viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.addOnPageChangeListener(new MyOnPageChangeListener());;
	}
	
	/**
	 * 标题点击事件监听器
	 * @author tom
	 *
	 */
	private class MyOnClickListener implements OnClickListener{  
        private int index=0;  
        public MyOnClickListener(int i){  
            index=i;  
        }  
        public void onClick(View v) {  
            viewPager.setCurrentItem(index);              
        }  
          
    }
	
	private class MyOnPageChangeListener implements OnPageChangeListener
	{
		int one = offset * 2 + bitmapwidth;// 页卡1 -> 页卡2 偏移量  
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
			   /*两种方法，这个是一种，下面还有一种，显然这个比较麻烦 
            Animation animation = null; 
            switch (arg0) { 
            case 0: 
                if (currIndex == 1) { 
                    animation = new TranslateAnimation(one, 0, 0, 0); 
                } else if (currIndex == 2) { 
                    animation = new TranslateAnimation(two, 0, 0, 0); 
                } 
                break; 
            case 1: 
                if (currIndex == 0) { 
                    animation = new TranslateAnimation(offset, one, 0, 0); 
                } else if (currIndex == 2) { 
                    animation = new TranslateAnimation(two, one, 0, 0); 
                } 
                break; 
            case 2: 
                if (currIndex == 0) { 
                    animation = new TranslateAnimation(offset, two, 0, 0); 
                } else if (currIndex == 1) { 
                    animation = new TranslateAnimation(one, two, 0, 0); 
                } 
                break; 
                 
            } 
            */  
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。  
            currIndex = arg0;  
            animation.setFillAfter(true);// True:图片停在动画结束位置  
            animation.setDuration(300);  
            iv_img.startAnimation(animation);  
		}
		
	}
	
	
	
	
}
