package com.example.bus_ui_demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.network.PayParams;
import com.example.network.protocol;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class aty_PopRequestPay extends Activity
{
	private static int backpress_cnt = 0;
	private myApplication myApp;
	
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int ORDERTO_PAY_PLATFROMS = Global_Config.INNER_MSG_START + 0;//开始向充值平台提交订单
	
	
	private TextView tv_notice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_poprequetpay_layout);
		
		myApp = (myApplication) getApplicationContext();
		
		FindView();
	}

	private void FindView()
	{
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_notice.setText(getString(R.string.PayRequest));
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		if(backpress_cnt++ < 1)
		{
			//第一次按返回键 给出提示
			myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.ClickBackAgain));
		}
		else {
			//第二次按返回键 退出当前页面，返回上一页面
			backpress_cnt = 0;
			aty_PopRequestPay.this.finish();
			super.onBackPressed();
		}
	}
	
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
					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.ConnectServer_error));
					break;
				case ORDERTO_PAY_PLATFROMS:
					tv_notice.setText(getString(R.string.PayRequest2platfrom));//设置提交订单提示
					break;
				default:
					break;
			}
		}
		
	};
	
	Thread mThread = new Thread(new Runnable()
	{
		
		@Override
		public void run()
		{
			String Ack = RequestPay();
			if(null != Ack)
			{
				//发送充值申请
				if(null != Ack)
				{
					String terminalCode = Ack.substring(4, 16);//4-15终端号 圈存使用
					String Url = Ack.substring(16, Ack.length());
					String Ip = Url.replace("http://", "").split(":")[0];
					myApplication.setServerIP(Ip);//设置后台服务IP
					myApp.setTerminalCode(terminalCode);//设置终端号
					myApplication.setVersionControlUrl(Url);//设置版本更新地址
					myApplication.setApkDownloadUrl(Url);//设置版本更新下载地址
					
					//调用对应支付平台，提交订单
					Message msg = new Message();
					msg.what = ORDERTO_PAY_PLATFROMS; //设置提交订单提示
					mHandler.sendMessage(msg);
					
					
				}	
			}
		}
	});
	
	/**
	 * 拼接申请支付帧，调用申请支付函数
	 * @return 充值请求返回值，错误返回null，成功返回协议定义的配置信息
	 */
	private String RequestPay()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
//		Card_number	16	N	发行卡号
//		IMEI	15	N	终端唯一标识（全球唯一）
//		orderNo	30	AN	订单编号
//		Orderseq	30	AN	订单流水号
//		orderMount	8	N	订单金额 10进制 单位分，（至银行为浮点数 单位元） 
//		OldMount	8	HEX	卡片余额 单位分
//		pubLen	2	H	公共信息长度
//		PublicData	N	H	公共信息
//		PayType	2	N	支付途径
//		01 翼支付
//		02 支付宝
//		03 建设银行
//		04 工商银行
//		05 
		long time_ms = System.currentTimeMillis();
		Date date = new Date(time_ms);
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
		//订单号唯一  30位
		String OrderNO = time + myApp.getRandom() + myApp.getRandom() +myApp.getRandom();//4位随机数
		//订单流水号唯一 30位
		String OrderSeq = myApp.getRandom() + myApp.getRandom() +myApp.getRandom() + time;
		//卡片余额
		String hexMoney = Integer.toHexString(myApp.getBeforeChargeMoney()); 
		hexMoney = to8String(hexMoney);
		
		String orderAmount = ""+myApp.getChargeMoney();
		orderAmount = to8String(orderAmount);
		
		String publishMsg_len;
		if(myApp.getPublishMsg().length()>0xff)
		{//长度超出0xff则直接认为长度为0，让服务器不能通过申请
			publishMsg_len = "00";
		}
		else {
			publishMsg_len = Integer.toHexString(myApp.getPublishMsg().length());
		}
		
		map.put(PayParams.CARD_NUMBER, myApp.getPublishCardNO());
		map.put(PayParams.IMEI_NO, myApp.getIMEI());
		map.put(PayParams.ORDERNO, OrderNO);
		map.put(PayParams.ORDERSEQ, OrderSeq);
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.OLDAMOUNT, hexMoney);
		map.put(PayParams.PUBLEN, publishMsg_len);
		map.put(PayParams.PUBLIC_DATA, myApp.getPublishMsg());
		map.put(PayParams.PAYTYPE, myApp.getPayMethod());
	
		return(protocol.RequestPay(aty_PopRequestPay.this,map, mHandler));
		
	}
	
	/**
	 * 根据之前选择的支付方式，调用对应的支付平台
	 */
	private void CallPay_Platfroms()
	{
		
	}
	
	/**
	 * 翼支付帧拼接，之后调用翼支付支付平台
	 */
	private void BestPay()
	{
		
	}
	
	/**
	 * 将字符串小于8位的补齐为8位，补齐方法：左补零
	 * @param str
	 * @return
	 */
	private String to8String(String str)
	{
		if(str.length() < 8)
		{
			for(int i = 0; i < 8-str.length(); i++)
			{
				str = "0"+str;
			}
		}
		
		return str;
	}
}
