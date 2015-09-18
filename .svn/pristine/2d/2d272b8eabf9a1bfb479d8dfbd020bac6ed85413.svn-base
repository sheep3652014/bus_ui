package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.Alert.myAlert;
import com.example.application.myApplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

public class frg_NFC_bigCard_SelectMoney extends Fragment
{
	private GridView gv_list = null;
	private BaseAdapter adapter = null;
	private Context context;

	private TextView tv_publishCardNO;
	private TextView tv_money;
	private TextView tv_record;

	private myApplication myApp;
	private FragmentManager fm;
	private FragmentTransaction transaction;

	private Button btn_payMethod;
	private boolean isSelect = false;//金额选择标志

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.frg_nfc_bigcard_selectmoney,
				container, false);

		this.context = container.getContext();

		myApp = (myApplication) this.context.getApplicationContext();

		FindView(view);
		InitView(view);

		return view;
	}

	private void FindView(View view)
	{
		gv_list = (GridView) view.findViewById(R.id.gv_list);
		tv_publishCardNO = (TextView) view.findViewById(R.id.tv_publishCardNO);
		tv_money = (TextView) view.findViewById(R.id.tv_money);

		tv_record = (TextView) view.findViewById(R.id.tv_record);
		btn_payMethod = (Button) view.findViewById(R.id.btn_payMethod);

	}

	public void UpdateGridView(View view)
	{
		// 获取卡中余额
		float money = new BigDecimal((double)myApp.getBeforeChargeMoney() / 100)
				.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_money.setText(money + getString(R.string.YUAN));
		// 获取发行卡号
		tv_publishCardNO.setText(myApp.getPublishCardNO());

		// adapter = new ArrayAdapter<String>(view.getContext(),
		// android.R.layout.simple_list_item_single_choice, getData());
		adapter = new ArrayAdapter<String>(view.getContext(),
				R.layout.simple_list_item_single_choice, getData());

		gv_list.setAdapter(adapter);
		gv_list.invalidate();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void InitView(View view)
	{
		UpdateGridView(view);
		gv_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		gv_list.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				isSelect = true;
				myApp.setMoneyPosition(position);
				switch (position)
				{
					case 0:
						System.out.println("click " + 10);
						myApp.setChargeMoney(10 * 100);
						break;
					case 1:
						System.out.println("click " + 20);
						myApp.setChargeMoney(20 * 100);
						break;
					case 2:
						System.out.println("click " + 50);
						myApp.setChargeMoney(50 * 100);
						break;
					case 3:
						System.out.println("click " + 100);
						myApp.setChargeMoney(100 * 100);
						break;
					case 4:
						System.out.println("click " + 200);
						myApp.setChargeMoney(200 * 100);
						break;
					case 5:
						System.out.println("click " + 0.01);
						myApp.setChargeMoney(1);
						break;
					default:
						break;
				}
			}
		});

		tv_record.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				System.out.println("click record");
				fm = getFragmentManager();
				transaction = fm.beginTransaction();
				frg_PopLog popLog = new frg_PopLog();
				popLog.show(transaction, "log");
			}
		});

		btn_payMethod.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (!isSelect)
				{
					myAlert.ShowToast(context,
							getString(R.string.bigCard_youneedChoice));
				} else
				{
					Intent intent = new Intent(context,
							aty_ChargeMethod_Choice.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					isSelect = false;
				}
			}
		});
	}

	/**
	 * 获取充值数据
	 * 
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
