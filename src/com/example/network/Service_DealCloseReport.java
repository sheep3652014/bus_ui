package com.example.network;

import java.util.HashMap;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.bus_ui_demo.R;
import com.example.bus_ui_demo.aty_ThirdPartyPay_Circle;
import com.example.config.Global_Config;
import com.example.database.ChargeHolder;
import com.example.database.CustomTypeException;
import com.example.database.DbManager;
import com.example.database.DbManager_Third_Human;
import com.example.database.DbOpenHelper_Third_Human;
import com.example.database.DbOpenHelper_charge;
import com.example.database.Holder_Third_Human;
import com.example.network.Service_NetworkConnect.myBinder;
import com.google.zxing.oned.rss.FinderPattern;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class Service_DealCloseReport extends Service
{
	private static final String TAG = "Service_DealCloseReport";
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int REPORT_SUCCESS = Global_Config.INNER_MSG_START + 1;
	private static final int REPORT_FAIL = Global_Config.INNER_MSG_START + 2;

	private final IBinder binder = new myBinder();

	public class myBinder extends Binder
	{
		public Service_DealCloseReport getService()
		{
			return Service_DealCloseReport.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		System.out.println("onbind");

		SendReportThread();

		return binder;
	}

	private void SendReportThread()
	{
		// TODO Auto-generated method stub
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Looper.prepare();
				ReportCircleCompelet();
				ReportCircleCompelet_Third_Human();
				Looper.loop();
			}
		}).start();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		System.out.println("onstartcommand dealclose");
		SendReportThread();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		System.out.println("ondestory");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO Auto-generated method stub
		System.out.println("onunbind");
		return super.onUnbind(intent);
	}

	/**
	 * 上报圈存完成，上报成功更改交易关闭状态
	 */
	private void ReportCircleCompelet()
	{
		DbManager dm = new DbManager(this);
		Cursor c = dm.QueryByTransferenceFail();
		// c.moveToNext();
		while (c.moveToNext())
		{
			HashMap<String, String> map = new HashMap<String, String>();
			String orderSeq = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE));
			String tac = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.TAC_NFCCARD));
			map.put(PayParams.ORDERSEQ, orderSeq);
			map.put(PayParams.TAC_CARD, tac);

			Message msg = mHandler.obtainMessage();

			if (protocol.SendCircleCompleteReport(Service_DealCloseReport.this,
					map, mHandler))
			{
				UpdateReportCompeletStatus(dm, c, true);
				msg.what = REPORT_SUCCESS;
				mHandler.sendMessage(msg);
			} else
			{
				UpdateReportCompeletStatus(dm, c, false);
				msg.what = REPORT_FAIL;
				mHandler.sendMessage(msg);
			}
		}

		c.close();
		dm.closeDB();
	}

	/**
	 * 补登，人工充值 圈存成功上报，上报成功更新圈存成功上报状态
	 */
	private void ReportCircleCompelet_Third_Human()
	{
		DbManager_Third_Human dm = new DbManager_Third_Human(this);
		Cursor c = dm.QueryByReportFail();
		// c.moveToNext();
		while (c.moveToNext())
		{
			HashMap<String, String> map = new HashMap<String, String>();
			String orderSeq = c.getString(c
					.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO));
			String tac = c.getString(c
					.getColumnIndex(DbOpenHelper_Third_Human.TAC));
			map.put(PayParams.ORDERSEQ, orderSeq);
			map.put(PayParams.TAC_CARD, tac);

			Message msg = null;

			if (protocol.SendCircleCompleteReport(Service_DealCloseReport.this,
					map, mHandler))
			{
				// UpdateReportCompeletStatus(dm, c, true);
				UpdateReportCompeletSuccess_Third_Human(dm, c, true);
				msg = mHandler.obtainMessage();
				msg.what = REPORT_SUCCESS;
				mHandler.sendMessage(msg);
			} else
			{
				// UpdateReportCompeletStatus(dm, c, false);
				UpdateReportCompeletSuccess_Third_Human(dm, c, false);
				msg = mHandler.obtainMessage();
				msg.what = REPORT_FAIL;
				mHandler.sendMessage(msg);
			}
		}

		c.close();
		dm.closeDB();
	}

	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(Service_DealCloseReport.this,
							getString(R.string.network_error));
					break;
				case REPORT_SUCCESS:
					myAlert.ShowToast(Service_DealCloseReport.this,
							getString(R.string.circle_report_success));
					break;
				case REPORT_FAIL:
					myAlert.ShowToast(Service_DealCloseReport.this,
							getString(R.string.circle_report_fail));
					break;
				default:
					break;
			}
		}

	};

	/**
	 * 更新交易完成状态
	 * 
	 * @param DbManager
	 *            dm
	 * @param Cursor
	 *            c
	 * @param CompeletStatus
	 *            true：圈存完成上报成功 false：圈存完成上报失败
	 */
	private void UpdateReportCompeletStatus(DbManager dm, Cursor c,
			boolean CompeletStatus)
	{
		String OrderTime = "";
		String Order_Seq = "";
		String Order_Reqtranse = "";
		String Order_Amount = "";
		String Order_Publish_CardID = "";
		String payMethod = "";
		String tac = "";

		OrderTime = c.getString(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_TIME));
		Order_Seq = c
				.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ));
		Order_Reqtranse = c.getString(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE));
		Order_Amount = c.getString(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT));
		Order_Publish_CardID = c.getString(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID));
		payMethod = c
				.getString(c.getColumnIndex(DbOpenHelper_charge.PAYMETHOD));
		tac = c.getString(c.getColumnIndex(DbOpenHelper_charge.TAC_NFCCARD));

		int Charge_Status = c.getInt(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_STATUS));
		int ChargeNFC_Status = c.getInt(c
				.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS));
		// int TransferenceClose_Status = c
		// .getInt(c
		// .getColumnIndex(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS));
		int report_Status = 0;
		if (CompeletStatus)
		{
			report_Status = 1;
		}

		try
		{
			ChargeHolder ch = new ChargeHolder(OrderTime, Order_Seq,
					Order_Reqtranse, Order_Amount, Order_Publish_CardID,
					payMethod, Charge_Status, ChargeNFC_Status, report_Status,
					tac);

			dm.UpdateOrderbyReqTranse(ch);
		} catch (CustomTypeException e)
		{
			Log.d(TAG, "更新状态出错");
			// 出错先关闭数据库资源
			c.close();
			dm.closeDB();
			e.printStackTrace();
		}
	}

	/**
	 * 更新交易完成状态
	 * @param db
	 * @param c
	 * @param isCompelet
	 */
	private void UpdateReportCompeletSuccess_Third_Human(
			DbManager_Third_Human db, Cursor c, boolean isCompelet)
	{
		Holder_Third_Human holder = new Holder_Third_Human(c);
		
		try
		{
			holder.set_isStatus_Circle_Report(isCompelet);
			System.out.println("in --- "+ isCompelet);
			System.out.println("status = " + holder.getStatus_Request()+"_"+holder.getStatus_GotCircle() 
					+"_"+holder.getStatus_Circle()+"_"+holder.getStatus_Circle_Report());
			db.UpdateOrderbyHolderItem(holder);
		}
		catch(NullPointerException e)
		{
			System.out.println("null " + e.getMessage());
		}
		finally{
			c.close();
			db.closeDB();
		}
		

	}
}
