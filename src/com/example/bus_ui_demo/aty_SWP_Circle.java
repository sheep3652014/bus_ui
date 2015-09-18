package com.example.bus_ui_demo;

import com.example.Alert.myAlert;
import com.example.Alert.myAlert.ShowTwoClickDialog_Interface;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.database.ChargeHolder;
import com.example.database.CustomTypeException;
import com.example.database.DbManager;
import com.example.database.DbOpenHelper_charge;
import com.example.network.Service_DealCloseReport;
import com.example.network.Service_DealCloseReport.myBinder;
import com.example.network.protocol;
import com.example.nfc.Util;
import com.tybus.swp.UICC_swp;
import com.tybus.swp.UICC_swp.UICC_Interface;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;

public class aty_SWP_Circle extends FragmentActivity
{
	// 805000020B01+czje+12345678 //cmd + 金额 + 终端号
	// czje是8位16进制数字，分结尾。充1分钱，是00000001，充1元钱是0000000A
	private static final String CIRCLE_INIT = Global_Config.CIRCLE_INIT;
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final String MONEY_CMD = Global_Config.MONEY_CMD;  //读余额
	private static final int RESTART_CIRCLE = Global_Config.INNER_MSG_START + 1;
	private static final int CIRCLE_COMPELETE = Global_Config.INNER_MSG_START + 2;
	
	private static final String TAG = "aty_SWP_Circle";
	private ServiceConnection serconn = null;
	private static final String FRG_WAIT_CALL_FROM = Global_Config.FRG_WAIT_CALL_FROM;
	private static final String FRG_WAIT_CALL_FROM_SWP = Global_Config.FRG_WAIT_CALL_FROM_SWP;
	private UICC_swp m_swp = null;
	private FrameLayout frame_notice;

	private FragmentManager fm = null;
	private FragmentTransaction transaction = null;
	private frg_SWP_CircleWait circleWait = null;
	private myApplication myApp = null;
	private Thread mThread;
	
	final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_SWP_Circle.this, getString(R.string.network_error));
					break;
				case RESTART_CIRCLE:
					//关闭资源，重新发起圈存过程
					m_swp.UICCClose();
					InitSWP(mHandler);
					break;
				case CIRCLE_COMPELETE:
					//启用服务发送完成报告
					BindSerive(aty_SWP_Circle.this);
					//添加圈存完成界面
					AddCircleCompelete();
					break;
				default:
					break;
			}
		}

	};
	
	/**
	 * 绑定圈存完成上报服务
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
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_swp_circle_layout);

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();

		myApp = (myApplication) getApplicationContext();

		FindView();
		InitView();


		InitSWP(mHandler);

	}

	private void InitSWP(final Handler mHandler)
	{
		m_swp = new UICC_swp(aty_SWP_Circle.this, new UICC_Interface()
		{

			@Override
			public void UICCTranSomeCmd()
			{
				Runnable mRunnable = new Runnable()
				{
					
					@Override
					public void run()
					{
						System.out.println("here");
						Looper.prepare();
						// 圈存初始化
						String CircleInitmsg = CircleInit(m_swp, myApp);
						myApp.setCircleInitMsg(CircleInitmsg);
						// 请求圈存指令
						String ack = RequestCircle(aty_SWP_Circle.this, myApp, mHandler);
						if(null != ack)
						{
							String TAC = Circle(m_swp, myApp, ack, mHandler);
							if(null != TAC)
							{
								//更新订单状态
								UpdateCircleStatus(myApp.getOrderSeq(), true, TAC);
								//保存TAC
								myApp.setTAC_CARD(TAC);
								//发送msg， 
								//圈存，更新状态，发送圈存完成报告
								//ui线程中加入圈存成功提示
								Message msg = mHandler.obtainMessage();
								msg.what = CIRCLE_COMPELETE;
								mHandler.sendMessage(msg);
							}
						}
						
						Looper.loop();
					}
				};
			mThread = new Thread(mRunnable);
			//mThread.start();
			try
			{
				Thread.sleep(1*1000);
				mThread.start();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("sleep error " + e.getMessage());
			}
			}
		});

		if (m_swp.UICCInit())
		{
			System.out.println("uicc init ok");
		}
	}

	private void FindView()
	{
		frame_notice = (FrameLayout) findViewById(R.id.frame_notice);
	}

	/**
	 * 加入圈存提醒等待
	 */
	private void InitView()
	{
		// transaction.replace(R.id.frame_notice, new frg_SWP_CircleWait());
		// transaction.commit();
		// FragmentTransaction transaction =
		// getSupportFragmentManager().beginTransaction();
		circleWait = new frg_SWP_CircleWait();
		Bundle args = new Bundle();
		args.putString(FRG_WAIT_CALL_FROM, FRG_WAIT_CALL_FROM_SWP);
		circleWait.setArguments(args);
		circleWait.show(transaction, "swp_circle_notice");
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		// 关闭相关资源
		if (null != m_swp)
			m_swp.UICCClose();
		else
		{
			System.out.println("is null");
		}
		if(null != serconn)
		{
			unbindService(serconn);
		}
		
		super.onDestroy();
	}

	/**
	 * 添加圈存完成后界面
	 */
	private void AddCircleCompelete()
	{
		// transaction.remove(circleWait);
		circleWait.dismiss();

		FragmentTransaction transaction2 = getSupportFragmentManager()
				.beginTransaction();
		transaction2.replace(R.id.frame_notice,
				new frg_NFC_bigCard_CircleComplete_notice());
		transaction2.commit();
	}

	/**
	 * 圈存初始化
	 * 
	 * @param m_swp
	 * @param myApp
	 * @return
	 */
	private String CircleInit(UICC_swp m_swp, myApplication myApp)
	{
		// 805000020B01+czje+12345678 //cmd + 金额 + 终端号
		// czje是8位16进制数字，分结尾。充1分钱，是00000001，充1元钱是0000000A
		String m = Integer.toHexString(myApp.getChargeMoney());
		String money = Util.to8String(m);
		String cmd = CIRCLE_INIT + money + myApp.getTerminalCode();
		String ack = m_swp.UICC_TRANS(cmd);
		if (null == ack)
		{
			myAlert.ShowToast(aty_SWP_Circle.this,
					getString(R.string.swp_circle_init_fail));
			BackHomeActivity(aty_SWP_Circle.this);

		}
		return ack;
	}

	/**
	 * 申请圈存指令
	 * 成功返回圈存指令，否则null
	 * @param context
	 * @param myApp
	 * @param mHandler
	 */
	private String RequestCircle(Context context, myApplication myApp, final Handler mHandler)
	{
		String cmd = protocol.RequestCircle(context, myApp, mHandler);
		if(null != cmd)
		{
			return cmd;
		}
		else
		{
			myAlert.ShowTwoClickDialog(aty_SWP_Circle.this, getString(R.string.swp_requestcircle_fail_notice), 
					getString(R.string.swp_circle_cancel), getString(R.string.swp_circle_retry),
					new ShowTwoClickDialog_Interface()
					{
						
						@Override
						public void DoPositive()
						{
							//再次发送圈存请求
							//关闭资源
							Message msg = mHandler.obtainMessage();
							msg.what = RESTART_CIRCLE;
							mHandler.sendMessage(msg);
						}
						
						@Override
						public void DoNegative()
						{
							//放弃圈存，返回主界面，给出退款提示
							myAlert.ShowToast(aty_SWP_Circle.this, getString(R.string.circle_fail_notice));
							BackHomeActivity(aty_SWP_Circle.this);
						}
					});
		}	
		return null;
	}

	/**
	 * 圈存，成功返回TAC，失败返回null
	 * @param m_swp
	 * @param myApplication
	 * @param cmd
	 * @return
	 */
	private String Circle(UICC_swp m_swp, myApplication myApp, String cmd, final Handler mHandler)
	{
		//读取圈存前金额
		String before = m_swp.UICC_TRANS(MONEY_CMD);
		//圈存
		String TAC = m_swp.UICC_TRANS(cmd);
		
		if(null != TAC)
		{
			//比对充值前后金额，确认是否充值成功
			//成功显示充值圈存完成窗口，并尝试方式交易完成报告
			
			//读圈存后金额
			String after = m_swp.UICC_TRANS(MONEY_CMD);
			//比对精确判断是否圈存成功
			int before_money = Integer.parseInt(before, 16);
			int after_money = Integer.parseInt(after,16);
			int charge_money = myApp.getChargeMoney();
			
			if(charge_money == (after_money - before_money))
			{//成功
				myApp.setBeforeChargeMoney(before_money);
				myApp.setAfterChargeMoney(after_money);
				//保存小卡余额
				myApp.setSwp_balance(after_money);
				return TAC;
			}
			else {
				//失败，给出提示
				//重试或取消
				myAlert.ShowTwoClickDialog(aty_SWP_Circle.this, getString(R.string.swp_circle_fail), 
						getString(R.string.swp_circle_cancel), getString(R.string.swp_circle_retry),
						new ShowTwoClickDialog_Interface()
						{
							
							@Override
							public void DoPositive()
							{
								//再次发送圈存请求
								//关闭资源
								Message msg = mHandler.obtainMessage();
								msg.what = RESTART_CIRCLE;
								mHandler.sendMessage(msg);
							}
							
							@Override
							public void DoNegative()
							{
								//放弃圈存，返回主界面，给出退款提示
								myAlert.ShowToast(aty_SWP_Circle.this, getString(R.string.circle_fail_notice));
								BackHomeActivity(aty_SWP_Circle.this);
							}
						});
				return null;
			}
			
		}
		
		return null;
	}
	
	/**
	 * 更新圈存状态 
	 * @param OrderSeq 订单流水号
	 * @param CircleStatus true：圈存成功 false：圈存失败
	 * @param tac 圈存成功应答TAC
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
		if (0 != c.getCount())// 数据库中存在该订单流水号的订单信息
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
			//如果充值状态不对，不允许更新圈存状态
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

				Log.d(TAG, "更新状态出错，onActivityResult");
				// 出错先关闭数据库资源
				c.close();
				dm.closeDB();
				e.printStackTrace();
			}
		}
		c.close();
		dm.closeDB();
	}
	
	/**
	 * 返回首页面
	 * 
	 * @param context
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity) context).finish();
	}
}
