package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.application.myApplication;
import com.example.mygallery.ImageAdapter;
import com.example.mygallery.myGallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class frg_SWP_home extends Fragment
{

	private Button btn_SwpCard;
	private TextView tv_Balance;
	private TextView tv_PublishCardNO;
	private TextView tv_ValidDay;
	private TextView tv_PhoneNO;
	
	private Context context;
	private HorizontalScrollView hScrollView;
	//private myGallery hScrollView;
	private List<Bitmap> bitmap_list = new ArrayList<Bitmap>();
	
	private LinearLayout gallery;
	
	private myApplication myApp = null;
	
	private BaseAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.context = container.getContext();
		
		View view = inflater.inflate(R.layout.frg_swp_home_layout, container, false);
		myApp = (myApplication)context.getApplicationContext();
		
		FindView(view);
		InitView();

		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	/**
	 * 初始化控件
	 * @param view
	 */
	private void FindView(View view)
	{
		btn_SwpCard = (Button) view.findViewById(R.id.btn_swpcard);
		
		tv_Balance = (TextView) view.findViewById(R.id.tv_balance);
		tv_PublishCardNO = (TextView) view.findViewById(R.id.tv_PublishCardNO);
		tv_ValidDay = (TextView) view.findViewById(R.id.tv_ValidDay);
		tv_PhoneNO = (TextView) view.findViewById(R.id.tv_PhoneNO);
		
		hScrollView = (HorizontalScrollView) view.findViewById(R.id.hscrollview);
		//hScrollView = (myGallery) view.findViewById(R.id.hscrollview);
		
		InitHorScrollView(context, view);
	}
	
	/**
	 * 初始化按钮点击事件
	 */
	private void InitView()
	{	
		int money = myApp.getSwp_balance();
		float balance = new BigDecimal((double)money/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_Balance.setText(balance + getString(R.string.YUAN));
		tv_PublishCardNO.setText(myApp.getSwp_PublishCardNO());
		tv_ValidDay.setText(ParseValidDate(myApp.getSwp_PublicMsg()));
		
		btn_SwpCard.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Toast.makeText(context, "click swpcard", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, aty_SWP_ChargeMoneySelect.class);
				startActivity(intent);
			}
		});
		
	}
	
	private void InitHorScrollView(Context context, View view)
	{
		gallery = (LinearLayout) view.findViewById(R.id.gallery);
		
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
		
		int[] imgs = new int[]{R.drawable.bg_deep_blue,R.drawable.bg_deep_green,R.drawable.bg_deep_purple};
		for(int i=0; i<imgs.length; i++)
		{
			//inflater.inflate(R.layout.item_horscrollview, gallery) == inflater.inflate(R.layout.item_horscrollview, gallery,true
			View view1 = inflater.inflate(R.layout.item_horscrollview, gallery,false);
			ImageView img = (ImageView) view1.findViewById(R.id.iv_img);
			//img.setImageResource(imgs[i]);//
			img.setScaleType(ImageView.ScaleType.CENTER);
			img.setBackgroundResource(imgs[i]);
			gallery.addView(view1);
		}
		/*List<Bitmap> list = new ArrayList<Bitmap>();
		Bitmap b1 = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.bg_deep_blue));
		Bitmap b2 = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.bg_deep_green));
		Bitmap b3 = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.bg_deep_purple));
		list.add(b1);
		list.add(b2);
		list.add(b3);
		ImageAdapter adapter = new ImageAdapter(list, context);
		hScrollView.setAdapter(adapter);*/
		
	}
	
	
	/**
	 * 从SWP卡公共信息中解析出卡有效日期
	 * @param publicMsg
	 * @return
	 */
	private String ParseValidDate(String pubicMsg)
	{
		String date = "";

		// 34-36 年审日期 (34-1)*2 = 66 (36-34)*2*2 =16
		String year = "20" + pubicMsg.substring(66, 68)
				+ getString(R.string.YEAR);
		String month = pubicMsg.substring(68, 70) + getString(R.string.MONTH);
		String day = pubicMsg.substring(70, 72) + getString(R.string.DAY);

		date = year + month + day;
		return date;
	}
}
