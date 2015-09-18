package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.bus_ui_demo.R.string;
import com.example.config.Global_Config;
import com.example.database.DbManager_Third_Human;
import com.example.database.DbOpenHelper_Third_Human;
import com.example.database.Holder_Third_Human;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.AndroidCharacter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class aty_ThirdPartyPay_LocalRecord extends Activity
{

	private ListView lv_record;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_thirdpartypay_record);

		FindView();
		InitView();

	}

	private void FindView()
	{
		lv_record = (ListView) findViewById(R.id.lv_record);
	}

	private void InitView()
	{
		myAdapter adapter = new myAdapter(GetData());
		adapter.notifyDataSetChanged();
		lv_record.setAdapter(adapter);
		lv_record.invalidate();
	}

	private List<Map<String, String>> GetData()
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		DbManager_Third_Human dm = new DbManager_Third_Human(
				aty_ThirdPartyPay_LocalRecord.this);
		Cursor c = dm
				.QueryOrderbyUserName(Holder_Third_Human.THIRDPAY_USERNAME);

		int cnt = 0;
		String OrderAmonut;
		String OrderNO;
		String publishCardNO;
		String bankID;
		String time;
		int Status_Circle;
		System.out.println("no = " + c.getCount());
		if (0 == c.getCount())// 当前没有补登记录，给出示例数据
		{
			OrderAmonut = getString(R.string.sample_money);// 1.00元
			OrderNO = getString(R.string.sample_order);
			publishCardNO = getString(R.string.PulbishCardNO_record);
			bankID = getString(R.string.sample_BankID);
			time = getString(R.string.sample_time);
			Status_Circle = 0;

			Map<String, String> map = new HashMap<String, String>();
			map.put(DbOpenHelper_Third_Human.ORDER_AMOUNT, OrderAmonut);
			map.put(DbOpenHelper_Third_Human.ORDER_NO, OrderNO);
			map.put(DbOpenHelper_Third_Human.PUBLISH_CARDNO, publishCardNO);
			map.put(DbOpenHelper_Third_Human.BANKCARDID, bankID);
			map.put(DbOpenHelper_Third_Human.STATUS_CIRCLE, "" + Status_Circle);
			map.put(DbOpenHelper_Third_Human.ORDER_TIME, time);
			list.add(map);
		} else
		{
			//do
			while ((cnt++ < 10) && (c.moveToNext()))
			{
				//c.moveToNext();
				OrderAmonut = c.getString(c
						.getColumnIndex(DbOpenHelper_Third_Human.ORDER_AMOUNT));
				OrderNO = c.getString(c
						.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO));
				publishCardNO = c
						.getString(c
								.getColumnIndex(DbOpenHelper_Third_Human.PUBLISH_CARDNO));
				bankID = c.getString(c
						.getColumnIndex(DbOpenHelper_Third_Human.BANKCARDID));
				time = c.getString(c
						.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME));
				Status_Circle = c
						.getInt(c
								.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE));

				Map<String, String> map = new HashMap<String, String>();
				map.put(DbOpenHelper_Third_Human.ORDER_AMOUNT, OrderAmonut);
				map.put(DbOpenHelper_Third_Human.ORDER_NO, OrderNO);
				map.put(DbOpenHelper_Third_Human.PUBLISH_CARDNO, publishCardNO);
				map.put(DbOpenHelper_Third_Human.BANKCARDID, bankID);
				map.put(DbOpenHelper_Third_Human.STATUS_CIRCLE, ""
						+ Status_Circle);
				list.add(map);
			} //while ((cnt++ < 10) && (c.moveToNext()));// 最多显示10条记录
		}
		c.close();
		dm.closeDB();

		return list;
	}

	class myAdapter extends BaseAdapter
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		public myAdapter(List<Map<String, String>> list)
		{
			this.list = list;
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
			Holder holder = null;
			if (null == convertView)
			{
				holder = new Holder();

				LayoutInflater inflater = LayoutInflater.from(parent
						.getContext());
				convertView = inflater.inflate(
						R.layout.item_thirdpartypay_record, parent, false);

				holder.ib_delete = (ImageButton) convertView
						.findViewById(R.id.ib_delete);
				holder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				holder.tv_OrderNO = (TextView) convertView
						.findViewById(R.id.tv_OrderNO);
				holder.tv_PulbishCardNO = (TextView) convertView
						.findViewById(R.id.tv_PublishCardNO);
				holder.tv_Status = (TextView) convertView
						.findViewById(R.id.tv_status);
				holder.tv_BankID = (TextView) convertView
						.findViewById(R.id.tv_BankID);
				holder.tv_Time = (TextView) convertView
						.findViewById(R.id.tv_time);

				convertView.setTag(holder);
			}
			holder = (Holder) convertView.getTag();
			System.out.println("holder is = " + (holder == null) + "_"
					+ holder.toString());

			InitView(holder, position, list);
			InitButton(holder);

			return convertView;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		private void InitView(Holder holder, int position,
				List<Map<String, String>> list)
		{
			int m = Integer.parseInt(list.get(position).get(
					DbOpenHelper_Third_Human.ORDER_AMOUNT));
			float m1 = new BigDecimal((double) m / 100).setScale(2,
					BigDecimal.ROUND_HALF_UP).floatValue();
			String money = getString(R.string.thirdPartyPay_money_head) + m1
					+ getString(R.string.YUAN);
			String orderNo = getString(R.string.OrderNO_head)
					+ list.get(position).get(DbOpenHelper_Third_Human.ORDER_NO);
			String publishCardNO = getString(R.string.PublishcardID_head)
					+ list.get(position).get(
							DbOpenHelper_Third_Human.PUBLISH_CARDNO);
			int status = Integer.parseInt(list.get(position).get(
					DbOpenHelper_Third_Human.STATUS_CIRCLE));
			String bankID = getString(R.string.thirdPartyPay_BankID_head)
					+ list.get(position).get(
							DbOpenHelper_Third_Human.BANKCARDID);
			String t = list.get(position).get(
					DbOpenHelper_Third_Human.ORDER_TIME);
			String time;
			/*if (android.os.Build.VERSION.SDK_INT >= 18)
			{
				time = getString(R.string.Time_head)
						+ DateFormat.getBestDateTimePattern(Locale.CHINA, t);
			} else
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				time = getString(R.string.Time_head) + sdf.format(new Date(t));
			}*/
			time = getString(R.string.Time_head) + t;
			System.out.println("time = " + time);
			holder.tv_money.setText(money);
			holder.tv_OrderNO.setText(orderNo);
			holder.tv_PulbishCardNO.setText(publishCardNO);
			holder.tv_Status.setText("" + status);
			holder.tv_Time.setText(time);
			holder.tv_BankID.setText(bankID);
		}

		private void InitButton(Holder holder)
		{
			holder.ib_delete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub

				}
			});
		}
	}

	class Holder
	{
		ImageButton ib_delete;
		TextView tv_money;
		TextView tv_OrderNO;
		TextView tv_PulbishCardNO;
		TextView tv_Status;
		TextView tv_BankID;
		TextView tv_Time;
	}
}
