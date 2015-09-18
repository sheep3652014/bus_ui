package com.example.bus_ui_demo;

import java.util.HashMap;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.Alert.myAlert.ShowOneClickDialog_Interface;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.database.ChargeHolder;
import com.example.database.CustomTypeException;
import com.example.database.DbManager;
import com.example.database.DbOpenHelper_charge;
import com.example.network.PayParams;
import com.example.network.Service_DealCloseReport;
import com.example.network.protocol;
import com.example.network.Service_DealCloseReport.myBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class aty_NFC_bigCard_Circle extends FragmentActivity
{
	private static final String TAG = "aty_NFC_bigCard_Circle"; 
	private ImageButton ib_check;
	private FrameLayout frame_notice;
	private static boolean isResultBack = false;//�Ƿ񷵻ر�־
	private FragmentManager fm = null;
	private FragmentTransaction transaction = null;
	private myApplication myApp;
	
	private static final String BEFORECHARGE_MONEY = Global_Config.BEFORECHARGE_MONEY;
	private static final String CHARGE_MONEY = Global_Config.CHARGE_MONEY;
	private static final String AFTERCHARGE_MONEY = Global_Config.AFTERCHARGE_MONEY;
	private static final String TAC_CODE = Global_Config.TAC_CODE;
	
	//���ڴ󿨳�ֵȦ�沽���ʶ
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	//private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	//private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
	
	private static final int REQUESTCODE_CIRCLE = 1;
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int CIRCLE_COMPELETE_REPORT_SUCCESS = Global_Config.INNER_MSG_START + 1;
	private static final int CIRCLE_COMPELETE_REPORT_FAIL = Global_Config.INNER_MSG_START + 2;
	private ServiceConnection serconn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_nfc_bigcard_circle_layout);
		
		myApp = (myApplication) getApplicationContext();
		FindView();
		InitView();
	}
	
	private void FindView()
	{
		ib_check = (ImageButton) findViewById(R.id.ib_check);
		frame_notice = (FrameLayout) findViewById(R.id.frame_notice);
		
	}
	
	private void InitView()
	{
		ib_check.setVisibility(View.VISIBLE);
		ib_check.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(aty_NFC_bigCard_Circle.this, aty_NFC_bigCard_Init.class);
				myApp.setCircleStep(CIRCLE_INIT_STEP);
				startActivityForResult(intent, REQUESTCODE_CIRCLE);
			}
		});
		
		frame_notice.setVisibility(View.GONE);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		AddCircleCompleteNoticeFrame();
		super.onResume();
	}
	
	/**
	 * ���frg_NFC_bigCard_CircleComplete_notice fragment
	 */
	private void AddCircleCompleteNoticeFrame()
	{	
		if(!isResultBack) return;//Ȧ��û�ɹ����˳�
		isResultBack = false;
		
		ib_check.setVisibility(View.GONE);//Ȧ��ɹ�������Ȧ����ð�ť
		frame_notice.setVisibility(View.VISIBLE);
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.replace(R.id.frame_notice, new frg_NFC_bigCard_CircleComplete_notice());
		transaction.commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		System.out.println("resultcode = "+resultCode + "_" + Activity.RESULT_OK);
		if(REQUESTCODE_CIRCLE == requestCode)
		{
			//Bundle bundle = data.getExtras();
			//System.out.println("--bundle is "+bundle.toString());
			int before = myApp.getBeforeChargeMoney();//bundle.getInt(BEFORECHARGE_MONEY);
			int charge = myApp.getChargeMoney();//bundle.getInt(CHARGE_MONEY);
			int after = myApp.getAfterChargeMoney();//bundle.getInt(AFTERCHARGE_MONEY);
			String TAC = myApp.getTAC_CARD();//bundle.getString(TAC_CODE);
			
			myApp.setCircleStep(INIT_STEP);//����Ϊ��ʼ����״̬
			
			if(charge == (after - before))
			{
				//Ȧ��ɹ��������ɹ�����
				isResultBack = true;
				//����Ȧ�����״̬
				UpdateCircleStatus(myApp.getOrderSeq(), true, TAC);
				CircleSuccess(aty_NFC_bigCard_Circle.this, TAC);
			}
			else {
				//Ȧ��ʧ�ܣ�������ʾ��������ҳ��
				myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_fail));
				myAlert.ShowOneClickDialog(aty_NFC_bigCard_Circle.this, getString(R.string.circle_fail_notice), new ShowOneClickDialog_Interface()
				{
					@Override
					public void DoSomething()
					{
						// TODO Auto-generated method stub
						BackHome(aty_NFC_bigCard_Circle.this);
					}
				});
			}
			super.onActivityResult(requestCode, requestCode, data);
		}
		
	}
	
	
	/**
	 * Ȧ��ɹ�����
	 * @param context
	 * @param TAC
	 */
	private void CircleSuccess(Context context,String TAC)
	{
		myAlert.ShowToast(context, getString(R.string.circle_success));
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put(PayParams.ORDERSEQ, myApp.getOrderSeq());
		map.put(PayParams.TAC_CARD, TAC);
		
		BindSerive(aty_NFC_bigCard_Circle.this);
		/*//����Ȧ��ɹ�ָ����������
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if(protocol.SendCircleCompleteReport(aty_NFC_bigCard_Circle.this, map, mHandler))
				{
					Message msg = mHandler.obtainMessage();
					msg.what = CIRCLE_COMPELETE_REPORT_SUCCESS;
					mHandler.sendMessage(msg);
				}
				else
				{
					Message msg = mHandler.obtainMessage();
					msg.what = CIRCLE_COMPELETE_REPORT_FAIL;
					mHandler.sendMessage(msg);
				}
			}
		}).start();*/
	}
	
	/**
	 * ��Ȧ������ϱ�����
	 * @param context
	 */
	private void BindSerive(Context context)
	{
		serconn = new ServiceConnection()
		{
			
			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				// TODO Auto-generated method stub
				System.out.println("disconnect");
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				// TODO Auto-generated method stub
				System.out.println("connect");
				Service_DealCloseReport mService = ((myBinder)service).getService();
			}
		};
		
		Intent intent = new Intent(context, Service_DealCloseReport.class);
		bindService(intent, serconn, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		BackHome(aty_NFC_bigCard_Circle.this);
		super.onBackPressed();
	}
	/**
	 * ������ҳ
	 */
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.network_error));
					BackHome(aty_NFC_bigCard_Circle.this);
					break;
//				case CIRCLE_COMPELETE_REPORT_FAIL:
//					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_report_fail));
//					//BackHome(aty_NFC_bigCard_Circle.this);
//					break;
//				case CIRCLE_COMPELETE_REPORT_SUCCESS:
//					myAlert.ShowToast(aty_NFC_bigCard_Circle.this, getString(R.string.circle_report_success));
//					//BackHome(aty_NFC_bigCard_Circle.this);
//					break;
				default:
					break;
			}
		}
		
	};
	
	/**
	 * ������ҳ�����رյ�ǰҳ��ջ���ϵ�����ҳ��
	 * @param context
	 */
	private void BackHome(Context context)
	{
		myApp.setCircleStep(INIT_STEP);//����Ϊ��ʼ����״̬
		Intent intent = new Intent(aty_NFC_bigCard_Circle.this, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity)context).finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		if(null != serconn)
		{
			unbindService(serconn);
		}
		super.onDestroy();
	}
	
	/**
	 * ����Ȧ��״̬ 
	 * @param OrderSeq ������ˮ��
	 * @param CircleStatus true��Ȧ��ɹ� false��Ȧ��ʧ��
	 * @param tac Ȧ��ɹ�Ӧ��TAC
	 */
	private void UpdateCircleStatus(String OrderSeq, boolean CircleStatus, String tac)
	{
		String OrderTime = "";
		String Order_Seq = "";
		String Order_Reqtranse = "";
		String Order_Amount = "";
		String Order_Publish_CardID = "";
		String payMethod = "";

		DbManager dm = new DbManager(this);
		Cursor c = dm.QueryOrderbyReqTranse(OrderSeq);
		c.moveToNext();
		if (0 != c.getCount())// ���ݿ��д��ڸö�����ˮ�ŵĶ�����Ϣ
		{
			OrderTime = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_TIME));
			Order_Seq = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ));
			Order_Reqtranse = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE));
			Order_Amount = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT));
			Order_Publish_CardID = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID));
			payMethod = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.PAYMETHOD));

			int Charge_Status = c
					.getInt(c
							.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_STATUS));
			// int ChargeNFC_Status =
			// c.getInt(c.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS));
			int TransferenceClose_Status = c
					.getInt(c
							.getColumnIndex(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS));
			int ChargeNFC_Status = 0;
			if (CircleStatus)
			{
				ChargeNFC_Status = 1;
			}
			//�����ֵ״̬���ԣ����������Ȧ��״̬
			if(ChargeHolder.TRUE_HERE!=Charge_Status)
			{
				c.close();
				dm.closeDB();
				return;
			}
			try
			{
				ChargeHolder ch = new ChargeHolder(OrderTime, Order_Seq,
						Order_Reqtranse, Order_Amount, Order_Publish_CardID,
						payMethod, Charge_Status, ChargeNFC_Status,
						TransferenceClose_Status, tac);

				dm.UpdateOrderbyReqTranse(ch);
			} catch (CustomTypeException e)
			{

				Log.d(TAG, "����״̬����aty_NFC_bigCard_Circle");
				// �����ȹر����ݿ���Դ
				c.close();
				dm.closeDB();
				e.printStackTrace();
			}
		}
		c.close();
		dm.closeDB();
	}
}
