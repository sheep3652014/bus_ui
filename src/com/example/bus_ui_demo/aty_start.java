package com.example.bus_ui_demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.tybus.swp.UICC_swp;
import com.tybus.swp.UICC_swp.UICC_Interface;
import com.tybus.swp.swp_dataPrase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;

public class aty_start extends Activity
{
	private Handler delayHandler = null;
	private static Runnable delay = null;
	private static final long delayinMs = Global_Config.CONNECT_OUT_TIME + 1*1000;
	
	private static final String SIMCARD_STATUS = Global_Config.SIMCARD_STATUS;
	private static final String MONEY_CMD = Global_Config.MONEY_CMD;
	private static final int MAX_RECORD = Global_Config.MAX_RECORD;
	private static final String READ_RECORD_HEAD = Global_Config.READ_RECORD_HEAD;
	private static final String READ_RECORD_TAIL = Global_Config.READ_RECORD_TAIL;
	private static final String READ_COMMON = Global_Config.READ_COMMON;
	private static final String GET_RANDOM =  Global_Config.GET_RANDOM;//��ȡ4byte �����
	private static final String VERIFY = Global_Config.VERIFY;
	private static final String VERIFY_PIN = Global_Config.VERIFY_PIN;
	
	private myApplication myApp = null;
	//private SEService _service = null;
	//private Session _session = null;
	
	private static final String TAG = "aty_start";
	
	private static final String AID = Global_Config.AID;//swp-nfc aid
	private static final String APPLET = Global_Config.APPLET;//swp-nfc mf
	private static final String SIMCARD_APPLET_STATUS = Global_Config.SIMCARD_APPLET_STATUS;
	
	private UICC_swp m_swp = null;
	private boolean hasSIMCard = false; //�Ƿ��ҵ���Чsim�����ҵ�true������false
	private boolean hasSWP_applet = false; //�Ƿ���sim�����ҵ�����Ӧ�ã��ҵ�true������false
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_start);
		
		delayHandler = new Handler();
		delay = new Runnable() //
		{

			@Override
			public void run()
			{
				//System.out.println("ack is = " +m_swp.UICC_TRANS(AID));
				hasSWP_applet = m_swp.isHasSWP_NFC_applet();
				System.out.println("back result = " + hasSWP_applet +"_" + m_swp.isHasSWP_NFC_applet());
				//ReadSWP(myApp);
				System.out.println("swp in mian = " + m_swp);
				Intent intent = new Intent(aty_start.this, aty_main.class);
				myApp.setHasSIMCard(hasSIMCard);
				myApp.setHasSIMCard_applet(hasSWP_applet);
				//intent.putExtra(SIMCARD_STATUS, hasSIMCard);
				//intent.putExtra(SIMCARD_APPLET_STATUS, hasSWP_applet);
				
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				aty_start.this.finish();//���Ե���OnDestory
			}
		};
		delayHandler
				.postDelayed(delay, delayinMs);
		
		myApp = (myApplication) getApplicationContext();
		myApp.setIMEI(GetIMEI());
		
		CheckSimCardStatus();
		
		m_swp = new UICC_swp(aty_start.this, new UICC_Interface()
		{
			
			@Override
			public void UICCTranSomeCmd()
			{
				// TODO Auto-generated method stub
				System.out.println("here");
				ReadSWP(myApp);
				
				//m_swp.UICCClose();
			}
		});
		
		if(m_swp.UICCInit())
		{
			System.out.println("uicc init ok");
//			System.out.println("ack is = " +m_swp.UICC_TRANS(AID));
//			hasSWP_applet = m_swp.isHasSWP_NFC_applet();
//			System.out.println("back result = " + hasSWP_applet +"_" + m_swp.isHasSWP_NFC_applet());
		
		}
	}

	/**
	 * �ж�SIM��״̬����SIM ����hasSIMCard = true������false
	 */
	private void CheckSimCardStatus()
	{
		TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		System.out.println("sim status = "+tm.getSimState());
		if(TelephonyManager.SIM_STATE_READY == tm.getSimState())
		{
			//�˲������������ܣ���Ҫ����SIM���������Ҳ����ϵͳ
			//�˴���Ҫ����ֻ���û��SIM�� 
			hasSIMCard = true;
		}
	}
	

	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		//�ر������Դ
		if(null != m_swp)
			m_swp.UICCClose();
		else
		{
			System.out.println("is null");
		}
		super.onDestroy();
	}

	/**
	 * ��ȡIMEI��  ���ȣ�15λ
	 * �в��ִ��ڲ���15λ���ֻ�IMEI����ȡ��0����
	 */
	private String GetIMEI()
	{
		String IMEI;
		
		TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI = tm.getDeviceId();
		System.out.println("imei = " + IMEI);
		if(null == IMEI)
		{
			IMEI = "123456789012345";
		}
		int len = IMEI.length();
		if(len < 15)
		{
			for(int i = 0; i < (15-len); i++)
			{
				IMEI = IMEI + "0";
			}
		}
		System.out.println("imei = "+IMEI);
		return IMEI;
	}
	
	/**
	 * ��ȡС��������Ϣ
	 * @param myApp
	 */
	private void ReadSWP(myApplication myApp)
	{
		//У�� 
		String verify = VERIFY + VERIFY_PIN;
		if(null == m_swp.UICC_TRANS(verify))
		{
			System.out.println("verify error! ");
			return;
		}
		System.out.println("swp in interface = " + m_swp);
		String money = m_swp.UICC_TRANS(MONEY_CMD);
		System.out.println("----");
		myApp.setSwp_balance(Integer.parseInt(money, 16));//(Integer.parseInt(money));
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		//���Ѽ�¼
		String record = "";
		for(int i = 0; i < MAX_RECORD; i++)
		{
			String hex = "0"+Integer.toHexString(i);
			String cmd = READ_RECORD_HEAD + hex + READ_RECORD_TAIL;
			
			if(null != (record = m_swp.UICC_TRANS(cmd)))
			{
				map = swp_dataPrase.PraseRecord(aty_start.this, record);
				if(null != map)
				{
					list.add(map);
				}
			}
		}
		myApp.setSwp_listLog(list);
		
		//������Ϣ
		String publishMsg = m_swp.UICC_TRANS(READ_COMMON);
		myApp.setSwp_PublicMsg(publishMsg);
		swp_dataPrase.PrasePublicMsg(myApp, publishMsg);
		
		myApp.setSwp_physicsCardNO("12345678");//С����ʱ���ܶ������ţ��ü����ݴ���
	}
	
	
	
}
