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
	private static final int ORDERTO_PAY_PLATFROMS = Global_Config.INNER_MSG_START + 0;//��ʼ���ֵƽ̨�ύ����
	
	
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
			//��һ�ΰ����ؼ� ������ʾ
			myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.ClickBackAgain));
		}
		else {
			//�ڶ��ΰ����ؼ� �˳���ǰҳ�棬������һҳ��
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
					tv_notice.setText(getString(R.string.PayRequest2platfrom));//�����ύ������ʾ
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
				//���ͳ�ֵ����
				if(null != Ack)
				{
					String terminalCode = Ack.substring(4, 16);//4-15�ն˺� Ȧ��ʹ��
					String Url = Ack.substring(16, Ack.length());
					String Ip = Url.replace("http://", "").split(":")[0];
					myApplication.setServerIP(Ip);//���ú�̨����IP
					myApp.setTerminalCode(terminalCode);//�����ն˺�
					myApplication.setVersionControlUrl(Url);//���ð汾���µ�ַ
					myApplication.setApkDownloadUrl(Url);//���ð汾�������ص�ַ
					
					//���ö�Ӧ֧��ƽ̨���ύ����
					Message msg = new Message();
					msg.what = ORDERTO_PAY_PLATFROMS; //�����ύ������ʾ
					mHandler.sendMessage(msg);
					
					
				}	
			}
		}
	});
	
	/**
	 * ƴ������֧��֡����������֧������
	 * @return ��ֵ���󷵻�ֵ�����󷵻�null���ɹ�����Э�鶨���������Ϣ
	 */
	private String RequestPay()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
//		Card_number	16	N	���п���
//		IMEI	15	N	�ն�Ψһ��ʶ��ȫ��Ψһ��
//		orderNo	30	AN	�������
//		Orderseq	30	AN	������ˮ��
//		orderMount	8	N	������� 10���� ��λ�֣���������Ϊ������ ��λԪ�� 
//		OldMount	8	HEX	��Ƭ��� ��λ��
//		pubLen	2	H	������Ϣ����
//		PublicData	N	H	������Ϣ
//		PayType	2	N	֧��;��
//		01 ��֧��
//		02 ֧����
//		03 ��������
//		04 ��������
//		05 
		long time_ms = System.currentTimeMillis();
		Date date = new Date(time_ms);
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
		//������Ψһ  30λ
		String OrderNO = time + myApp.getRandom() + myApp.getRandom() +myApp.getRandom();//4λ�����
		//������ˮ��Ψһ 30λ
		String OrderSeq = myApp.getRandom() + myApp.getRandom() +myApp.getRandom() + time;
		//��Ƭ���
		String hexMoney = Integer.toHexString(myApp.getBeforeChargeMoney()); 
		hexMoney = to8String(hexMoney);
		
		String orderAmount = ""+myApp.getChargeMoney();
		orderAmount = to8String(orderAmount);
		
		String publishMsg_len;
		if(myApp.getPublishMsg().length()>0xff)
		{//���ȳ���0xff��ֱ����Ϊ����Ϊ0���÷���������ͨ������
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
	 * ����֮ǰѡ���֧����ʽ�����ö�Ӧ��֧��ƽ̨
	 */
	private void CallPay_Platfroms()
	{
		
	}
	
	/**
	 * ��֧��֡ƴ�ӣ�֮�������֧��֧��ƽ̨
	 */
	private void BestPay()
	{
		
	}
	
	/**
	 * ���ַ���С��8λ�Ĳ���Ϊ8λ�����뷽��������
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
