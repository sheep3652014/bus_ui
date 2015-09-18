package com.example.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.config.Global_Config;
import com.example.database.CustomTypeException;

import android.app.Application;

public class myApplication extends Application
{
	private String IMEI;//�ֻ�IMEI��
	private String phoneNO = "12345678901";//�ֻ�����
	private int ChargeMoney;//��ֵ���
	private String BigCard_CircleCmd;
	private String random; //�����
	private String CircleInitMsg;//Ȧ���ʼ����Ϣ
	private String PublicMsg;//������Ϣ
	private String swp_PublicMsg;//С��������Ϣ
	private String PublishCardNO;//���п���
	private String swp_PublishCardNO;//С�����п���
	private int BeforeChargeMoney;//��ֵǰ��� ��λ��
	private int AfterChargeMoney;//��ֵ���� ��λ��
	private int swp_balance;//С�����
	private List<Map<String, String>> listLog = new ArrayList<Map<String,String>>();//�����Ѽ�¼
	private List<Map<String, String>> swp_listLog = new ArrayList<Map<String,String>>();//С�����Ѽ�¼
	private int CircleStep;//��Ȧ�沽�� 0:��ʼ����1:Ȧ���ʼ����2:Ȧ�棬3:Ȧ�����
	private String physicsCardNO;//������
	private String swp_physicsCardNO;//С��������
	private String TAC_CARD;//��Ȧ��ɹ�����
	private boolean swp_isNewCard;//С���¾ɿ���־
	private boolean isBigCard = false;//�Ƿ�Ϊ�󿨣�true���󿨣�false��С��
	private boolean hasSIMCard = false;//�Ƿ������ЧSIM����true������  false��������
	private boolean hasSIMCard_applet = false;//�Ƿ����ֻ�����Ӧ�ã� true���ҵ�����Ӧ��  false��û�ҵ�����Ӧ��
	

	//private enum CircleStep {INIT, CIRCLE_INIT, CIRCLEING, CIRCLE_COMPLETE};
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	//private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	//private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
	
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String CONSUME_TERMINAL = Global_Config.CONSUME_TERMINAL;
	
	public static String VersionControlUrl = Global_Config.VersionControlUrl;
	public static String ApkDownloadUrl = Global_Config.ApkDownloadUrl;
	
	public static String SERVER_IP = Global_Config.SERVER_IP;
	public static int SERVER_PORT = Global_Config.SERVER_PORT;
	
	private String PayMethod;//��ֵ��ʽ
	private String TerminalCode = "100000000001";//�ն˺� 
	
	private int MoneyPosition;//���ڸ���ѡ���ֵ������Ȧ���ʼ������
	
	
	//��ֵ��ʽ ����
	 //        01:��֧��
	 //        02:֧����
	 //        03:��������
	 //        04:��������
	 //        05:ũҵ����
	public static final int PAYMETHOD_BESTPAY = 01;
	public static final int PAYMETHOD_ALIPAY = 02;
	public static final int PAYMETHOD_CCBPAY = 03;
	public static final int PAYMETHOD_ICBCPAY = 04;
	public static final int PAYMETHOD_ABCPAY = 05;
	
	
	//֧��������Ϣ
	private String OrderNO;//������
	private String OrderSeq;//������ˮ��
	private long OrderTime;//����ʱ��
	private String OrderAmount;//�������
	
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		setCircleStep(INIT_STEP);
	}
	
	/**
	 * ��ȡ�Ƿ��ҵ���Чsim��
	 * @return true������  false��������
	 */
	public boolean isHasSIMCard()
	{
		return hasSIMCard;
	}
	/**
	 * �����Ƿ��ҵ���Чsim�� true������  false��������
	 * @param hasSIMCard
	 */
	public void setHasSIMCard(boolean hasSIMCard)
	{
		this.hasSIMCard = hasSIMCard;
	}
	/**
	 * ��ȡsim�����Ƿ��й���Ӧ��
	 * @return  true���ҵ�����Ӧ��  false��û�ҵ�����Ӧ��
	 */
	public boolean getHasSIMCard_applet()
	{
		return hasSIMCard_applet;
	}
	/**
	 * �����Ƿ��ҵ�SIM���еĹ���Ӧ��   true���ҵ�����Ӧ��  false��û�ҵ�����Ӧ��
	 * @param hasSIMCard_applet
	 */
	public void setHasSIMCard_applet(boolean hasSIMCard_applet)
	{
		this.hasSIMCard_applet = hasSIMCard_applet;
	}
	
	/**
	 * ��ȡ������ 30λ
	 * @return
	 */
	public String getOrderNO()
	{
		return OrderNO;
	}
	/**
	 * ���ö����� 
	 * @param orderNO 30λ
	 */
	public void setOrderNO(String orderNO)
	{
		OrderNO = orderNO;
	}
	/**
	 * ��ȡ������ˮ�� 30λ
	 * @return
	 */
	public String getOrderSeq()
	{
		return OrderSeq;
	}
	/**
	 * ���ö�����ˮ�� 30λ
	 * @param orderSeq
	 */
	public void setOrderSeq(String orderSeq)
	{
		OrderSeq = orderSeq;
	}
	/**
	 * ��ȡ����ʱ�� ms��ʽ long��
	 * @return
	 */
	public long getOrderTime()
	{
		return OrderTime;
	}
	/**
	 * ���ö���ʱ��  ms��ʽ long��
	 * @param orderTime
	 */
	public void setOrderTime(long orderTime)
	{
		OrderTime = orderTime;
	}
	/**
	 * ��ȡ������� ��λ����
	 * @return
	 */
	public String getOrderAmount()
	{
		return OrderAmount;
	}
	/**
	 * ���ö������ ��λ����
	 * @param orderAmount
	 */
	public void setOrderAmount(String orderAmount)
	{
		OrderAmount = orderAmount;
	}
	
	
	public String getIMEI()
	{
		return IMEI;
	}

	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
	}
	/**
	 * ��ȡ�绰����
	 * @return
	 */
	public String getPhoneNO()
	{
		return phoneNO;
	}
	/**
	 * ���õ绰����
	 * @param phoneNO
	 */
	public void setPhoneNO(String phoneNO)
	{
		this.phoneNO = phoneNO;
	}
	/**
	 * ��ȡ��Ȧ��ָ��
	 * @return
	 */
	public String getBigCard_CircleCmd()
	{
		return BigCard_CircleCmd;
	}

	/**
	 * ���ô�Ȧ��ָ��
	 * @param CircleCmd
	 */
	public void setBigCard_CircleCmd(String Cmd)
	{
		BigCard_CircleCmd = Cmd;
	}
	
	/**
	 * ��ȡ��ֵ��� ��λ�� 
	 * @return
	 */
	public int getChargeMoney()
	{
		return ChargeMoney;
	}
	
	/**
	 * ���ó�ֵ��� ��λ��
	 * @param chargeMoney
	 */
	public void setChargeMoney(int chargeMoney)
	{
		ChargeMoney = chargeMoney;
	}

	/**
	 * �󿨳�ֵȦ�沽���ȡ
	 * @return
	 */
	public int getCircleStep()
	{
		return CircleStep;
	}
	
	/**
	 * �󿨳�ֵȦ�沽������
	 * @param circleStep
	 */
	public void setCircleStep(int circleStep)
	{
		CircleStep = circleStep;
	}

	/**
	 * ��ȡ�ն˺�
	 * @return
	 */
	public String getTerminalCode()
	{
		return TerminalCode;
	}
	
	/**
	 * �����ն˺�
	 * @param terminalCode
	 */
	public void setTerminalCode(String terminalCode)
	{
		TerminalCode = terminalCode;
	}

	
	/**
	 * ��ȡ�����
	 * @return
	 */
	public String getRandom()
	{
		return random;
	}

	/**
	 * ���������
	 * @param random
	 */
	public void setRandom(String random)
	{
		this.random = random;
	}


	/**
	 * ��ȡ�󿨳�ֵǰ���
	 * @return
	 */
	public int getBeforeChargeMoney()
	{
		return BeforeChargeMoney;
	}

	/**
	 * ���ô󿨳�ֵǰ���
	 * @param beforeChargeMoney
	 */
	public void setBeforeChargeMoney(int beforeChargeMoney)
	{
		BeforeChargeMoney = beforeChargeMoney;
	}

	/**
	 * ��ȡ�󿨹�����Ϣ
	 * @return
	 */
	public String getPublicMsg()
	{
		return PublicMsg;
	}

	/**
	 * ���ô󿨹�����Ϣ
	 * @param PublicMsg
	 */
	public void setPublicMsg(String PublicMsg)
	{
		this.PublicMsg = PublicMsg;
	}
	
	/**
	 * ��ȡС��������Ϣ
	 * @return
	 */
	public String getSwp_PublicMsg()
	{
		return swp_PublicMsg;
	}
	/**
	 * ����С��������Ϣ
	 * @param swp_PublicMsg
	 */
	public void setSwp_PublicMsg(String swp_PublicMsg)
	{
		this.swp_PublicMsg = swp_PublicMsg;
	}
	/**
	 * ��ȡ�����Ѽ�¼
	 * @return
	 */
	public List<Map<String, String>> getListLog()
	{
		return listLog;
	}

	/***
	 * ���ô����Ѽ�¼
	 * @param listLog
	 */
	public void setListLog(ArrayList<Map<String, String>> listLog)
	{
		this.listLog = listLog;
	}
	
	/**
	 * ��ȡС�����Ѽ�¼
	 * @return
	 */
	public List<Map<String, String>> getSwp_listLog()
	{
		return swp_listLog;
	}
	/**
	 * ����С�����Ѽ�¼,�����ȡ��¼Ϊ�գ����ʾ������
	 * @param swp_listLog
	 */
	public void setSwp_listLog(List<Map<String, String>> swp_listLog)
	{
		if((null == swp_listLog) || (swp_listLog.isEmpty()))
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(CONSUME_TYPE, "02");//�������� 0X02:��ֵ  0x06:���� 0x09��������
			map.put(CONSUME_MONEY, "1.00Ԫ");//
			map.put(CONSUME_TIME, "2015��08��04�� 15:03:21");
			map.put(CONSUME_TERMINAL, "�ն˺ţ�123456789012");
			this.swp_listLog.add(map);
		}
		else {
			this.swp_listLog = swp_listLog;
		}
	}
	/**
	 * ����Ȧ���ʼ����Ϣ
	 * @return
	 */
	public String getCircleInitMsg()
	{
		return CircleInitMsg;
	}

	/**
	 * ��ȡȦ���ʼ����Ϣ
	 * @param circleInitMsg
	 */
	public void setCircleInitMsg(String circleInitMsg)
	{
		CircleInitMsg = circleInitMsg;
	}

	/**
	 * ��ȡȦ�����
	 * @return
	 */
	public int getAfterChargeMoney()
	{
		return AfterChargeMoney;
	}

	/**
	 * ����Ȧ�����
	 * @param afterChargeMoney
	 */
	public void setAfterChargeMoney(int afterChargeMoney)
	{
		AfterChargeMoney = afterChargeMoney;
	}

	/**
	 * ��ȡ���п���
	 * @return
	 */
	public String getPublishCardNO()
	{
		return PublishCardNO;
	}

	/**
	 * ���÷��п���
	 * @param publishCardNO
	 */
	public void setPublishCardNO(String publishCardNO)
	{
		PublishCardNO = publishCardNO;
	}
	/**
	 * ��ȡС�����п���
	 * @return
	 */
	public String getSwp_PublishCardNO()
	{
		return swp_PublishCardNO;
	}
	/**
	 * ����С�����п���
	 * @param swp_PublishCardNO
	 */
	public void setSwp_PublishCardNO(String swp_PublishCardNO)
	{
		this.swp_PublishCardNO = swp_PublishCardNO;
	}
	/**
	 * ��ȡС�����
	 * @return
	 */
	public int getSwp_balance()
	{
		return swp_balance;
	}
	/**
	 * ����С�����
	 * @param swp_balance
	 */
	public void setSwp_balance(int swp_balance)
	{
		this.swp_balance = swp_balance;
	}
	/**
	 * ��ȡ��ֵ��ʽ
	 * //		 01:��֧��
	 * //        02:֧����
	 * //        03:��������
	 * //        04:��������
	 * //        05:ũҵ����
	 * @return String
	 */
	public String getPayMethod()
	{
		return PayMethod;
	}
	/**
	 * ���ó�ֵ��ʽ
	 * //		 01:��֧��
	 * //        02:֧����
	 * //        03:��������
	 * //        04:��������
	 * //        05:ũҵ����
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod)
	{
		PayMethod = payMethod;
	}
	
	public static String getVersionControlUrl()
	{
		return VersionControlUrl;
	}
	public static void setVersionControlUrl(String versionControlUrl)
	{
		VersionControlUrl = versionControlUrl + "/app/update.xml";
	}
	public static String getApkDownloadUrl()
	{
		return ApkDownloadUrl;
	}
	public static void setApkDownloadUrl(String apkDownloadUrl)
	{
		ApkDownloadUrl = apkDownloadUrl  + "/app/huangshi_demo.apk";
	}
	
	public static String getServerIP()
	{
		return SERVER_IP;
	}
	
	public static void setServerIP(String ip)
	{
		SERVER_IP = ip;
	}
	
	public static int getServerPort()
	{
		return SERVER_PORT;
	}
	
	public static void setServerPort(int port)
	{
		SERVER_PORT = port;
	}
	/**
	 * ��ȡ��ֵ���ѡ��λ��
	 * @return
	 */
	public int getMoneyPosition()
	{
		return MoneyPosition;
	}
	/**
	 * ���ó�ֵ���ѡ��λ��
	 * @param moneyPosition
	 */
	public void setMoneyPosition(int moneyPosition)
	{
		MoneyPosition = moneyPosition;
	}
	
	/**
	 * ��ȡ������ ���� 4byte
	 * @return
	 */
	public String getPhysicsCardNO()
	{
		return physicsCardNO;
	}
	/**
	 * ���������� ����4byte
	 * ����ֵΪnull �� ���ַ��� �� ����С��8���ַ������ȣ�4byte�� ������������Ϊ"00000000" 
	 * @param physicsCardNO
	 */
	public void setPhysicsCardNO(String physicsCardNO)
	{
		if((null == physicsCardNO) || (physicsCardNO.isEmpty()) ||(8 != physicsCardNO.length()))
		{
			this.physicsCardNO = "00000000";
		}
		else
		{	
			this.physicsCardNO = physicsCardNO;
	
		}
	}
	/**
	 * ��ȡС��������
	 * @return
	 */
	public String getSwp_physicsCardNO()
	{
		return swp_physicsCardNO;
	}
	/**
	 * ����С��������
	 * @param swp_physicsCardNO
	 */
	public void setSwp_physicsCardNO(String swp_physicsCardNO)
	{
		this.swp_physicsCardNO = swp_physicsCardNO;
	}
	/**
	 * ��ȡ��Ȧ��ɹ�Ӧ�𷵻�ֵ������4byte
	 * @return ����4byte
	 */
	public String getTAC_CARD()
	{
		return TAC_CARD;
	}
	/**
	 * ���ô�Ȧ��ɹ�Ӧ�𷵻�ֵ ����4byte
	 * @param tAC_CARD
	 */
	public void setTAC_CARD(String tAC_CARD)
	{
		TAC_CARD = tAC_CARD;
	}
	
	/**
	 * ��ȡС���¾ɿ���־
	 * @return
	 */
	public boolean isSwp_isNewCard()
	{
		return swp_isNewCard;
	}
	
	/**
	 * ����С���¾ɿ���־������Ϊ�ַ����������ַ��������¾ɿ�״̬
	 * ���ñ�־,�Ƿ�Ϊ�¿� 00:�¿� 01:�ɿ�
	 * @param String NewCard
	 * �������ǡ�00������01�� �׳��쳣CustomTypeException
	 */
	public void setSwp_isNewCard(String NewCard) throws CustomTypeException
	{
		if("00".endsWith(NewCard))
		{
			this.swp_isNewCard = true;
		}
		else if("01".equals(NewCard))
		{
			this.swp_isNewCard = false;
		}
		else {
			throw new CustomTypeException("�¾ɿ���־ֻ��Ϊ00��01���Ƿ��ȡ������Ϣ����");
		}
		
	}
	/**
	 * ��ȡ��С����־
	 * @return true���󿨣�false��С��
	 */
	public boolean isBigCard()
	{
		return isBigCard;
	}
	/**
	 * ���ô�С����־  true���󿨣�false��С��
	 * @param isBigCard
	 */
	public void setBigCard(boolean isBigCard)
	{
		this.isBigCard = isBigCard;
	}
	
}
