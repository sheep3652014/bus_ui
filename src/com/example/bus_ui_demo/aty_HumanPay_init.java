package com.example.bus_ui_demo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Alert.myAlert;
import com.example.config.Global_Config;
import com.example.network.SocketClientCls;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosSDK;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class aty_HumanPay_init extends FragmentActivity implements OnClickListener
{
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	
	//private PosSDK  mPosSDK;
	private LinearLayout Lin_circle;
	private TextView tv_money;
	private Button btn_submit;
	private Button btn_print;
	private SimpleDateFormat FormatDate = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	
	private static SocketClientCls socket = null;//避免重复
	private boolean isLogin = false;//登录状态标记
	//private Handler handler = null;
	private static frg_login frg_login_dialog = null;//避免重复
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_humanpay_init_layout);
	
		InitSocket();
		FindView();
		InitView();
		//InitPsamCard();
		InitLoginDialog();
		HeartBeat();
	}
	
	private void InitSocket()
	{
		if(null != getSocket()) return;//避免重复
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{	
				SocketClientCls socketClientCls = new SocketClientCls();
				setSocket(socketClientCls);
			}
		}).start();
	}
	
	
	private void FindView()
	{
		Lin_circle = (LinearLayout) findViewById(R.id.Lin_circle);
		tv_money = (TextView) findViewById(R.id.tv_money);
		btn_print = (Button) findViewById(R.id.btn_print);
		btn_submit = (Button) findViewById(R.id.btn_submit);
	}
	
	private void InitView()
	{
		btn_submit.setOnClickListener(this);
		btn_print.setOnClickListener(this);
	}
	
	private void InitLoginDialog()
	{
		if(null != frg_login_dialog) return;//避免重复
		frg_login_dialog = new frg_login();
		frg_login_dialog.show(getSupportFragmentManager(), "login dialog");
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_submit:
				
				break;

			case R.id.btn_print:
				//Print(100, 200, "201508171056341234789078907890");
				break;
			default:
				break;
		}
	}
	
	Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_HumanPay_init.this, getString(R.string.network_error));
					break;
				case 100:
					synchronized (handler)
					{
						System.out.println("got it");
						handler.notify();//唤醒frg_login中的子线程
					}
					
					break;
				default:
					break;
			}
			
			super.handleMessage(msg);
		}
		
	};
	
	public Handler getHandler()
	{
		return handler;
	}
	/**
	 * 打印收据
	 * @param charge 充值金额
	 * @param receive 实收金额
	 * @param OrderNO 订单号
	 */
	/*private void Print(int charge, int receive,String OrderNO)
	{
		StringBuilder sb =new StringBuilder();
		//sb.append("   惠州市合众智远电子科技     ");
		sb.append("\n");
		sb.append("\n");
		sb.append("        收 银 凭 据                 ");
		sb.append("\n");
		sb.append("时间   : ");
		Date time = new Date();
		time.setTime(System.currentTimeMillis());
		sb.append(FormatDate.format(time));
		sb.append("\n");
		sb.append("操作员:admin");
		sb.append("\n");
		sb.append("收据单号：" + OrderNO);//订单编号
		sb.append("\n");
		sb.append("编号  数量  单价  折扣  小计");
		sb.append("\n");
		sb.append("-----------------------------");
		sb.append("\n");
		sb.append("-----------------------------");
		sb.append("\n");
		sb.append("共销售数量: 1 ");
		sb.append("\n");
		float chargeMoney = new BigDecimal((double)charge/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		float receiveMoney = new BigDecimal((double)receive/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		float changeMoney = new BigDecimal((double)(receive-charge)/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		sb.append("售价合计(RMB): " + chargeMoney);
		sb.append("\n");
		sb.append("实收金额(RMB): " + receiveMoney);
		sb.append("\n");
		sb.append("找零金额(RMB): " + changeMoney);
		sb.append("\n");
		sb.append("-----------------------------");
		sb.append("\n");
		sb.append("谢谢惠顾    请保留好小票！");
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		
		byte [] printData1 = null;
		try {
			//if(isZh()){
				printData1 =sb.toString().getBytes("GBK");
			//}else{
			//	printData1 =sben.toString().getBytes("GBK");
			//}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		mPosSDK.printText(10,printData1, printData1.length);
		sb=null;
	}*/
	
	
	/*private void InitPsamCard()
	{
		mPosSDK = PosSDK.getInstance(aty_HumanPay_init.this);//(getApplicationContext());
		mPosSDK.init(PosSDK.PRODUCT_MODEL_IPM01);
		
		IntentFilter filter  = new IntentFilter();
		filter.addAction(PosSDK.ACTION_POS_COMM_STATUS);
		filter.addAction(PosSDK.ACTION_POS_KEY_EVENT);
		registerReceiver(mPosSDKReceiver, filter);
		
	}*/
	
	
	public static SocketClientCls getSocket()
	{
		return socket;
	}

	private void setSocket(SocketClientCls socket)
	{
		this.socket = socket;
	}


	public boolean isLogin()
	{
		return isLogin;
	}

	public void setLogin(boolean isLogin)
	{
		this.isLogin = isLogin;
	}


	BroadcastReceiver mPosSDKReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action =  intent.getAction();
			System.out.println("--> action = " + action);
			if(action.equalsIgnoreCase(PosSDK.ACTION_POS_KEY_EVENT)){
				String value = intent.getStringExtra(PosSDK.KEY_KEY_VALUE);
				tv_money.setText("");
				tv_money.setText(value);
			}
			if(action.equalsIgnoreCase(PosSDK.ACTION_POS_COMM_STATUS)){
				int cmdFlag =intent.getIntExtra(PosSDK.KEY_CMD_FLAG, -1);
				int status	=intent.getIntExtra(PosSDK.KEY_CMD_STATUS , -1); 
				int bufferLen = intent.getIntExtra(PosSDK.KEY_CMD_DATA_LENGTH, 0);
				byte [] buffer =intent.getByteArrayExtra(PosSDK.KEY_CMD_DATA_BUFFER);

				switch(cmdFlag){
					case PosSDK.POS_PRINT_BARCODE:
						switch(status){
						case PosSDK.ERR_POS_PRINT_SUCC:
							//打印成功
							//mTestView.setText(getString(R.string.print_success));
							break;
						case PosSDK.ERR_POS_PRINT_NO_PAPER:
							//打印缺纸
							//mTestView.setText(getString(R.string.print_no_paper));
							break;
						case PosSDK.ERR_POS_PRINT_FAILED:
							//打印失败
							//mTestView.setText(getString(R.string.print_failed));
							break;
						case PosSDK.ERR_POS_PRINT_VOLTAGE_LOW:
							//电压过低
							//mTestView.setText(getString(R.string.print_voltate_low));
							break;
						case PosSDK.ERR_POS_PRINT_VOLTAGE_HIGH:
							//电压过高
							//mTestView.setText(getString(R.string.print_voltate_high));
							break;
						}
				}
			}
		}
	};

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		System.out.println("onbackpressed");
		BackHome(aty_HumanPay_init.this);
		super.onBackPressed();
	}
	
	/**
	 * 返回首页
	 * @param context
	 */
	public static void BackHome(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		((Activity)context).finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		if(null != frg_login_dialog) frg_login_dialog = null;//一定要，因为定义的是static，要不下次调用，无法加载（见frg_login_dialog加载）
		if(!socket.isClose())//(null != getSocket())
		{
			socket.CloseLink();
			socket = null;
		}
		System.out.println("socket = " + (null == getSocket()) );
		super.onDestroy();
	}
	
	private void HeartBeat()
	{
		TimerTask timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				socket.SendText("111");
			}
		};		
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000, 1000);
		
	}
}

