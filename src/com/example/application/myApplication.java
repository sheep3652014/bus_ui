package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.config.Global_Config;

import android.app.Application;

public class myApplication extends Application
{
	private String IMEI;//�ֻ�IMEI��
	private int ChargeMoney;//��ֵ���
	private String BigCard_CircleCmd;
	private String random; //�����
	private String CircleInitMsg;//Ȧ���ʼ����Ϣ
	private String PublishMsg;//������Ϣ
	private String PublishCardNO;//���п���
	private int BeforeChargeMoney;//��ֵǰ��� ��λ��
	private int AfterChargeMoney;//��ֵ���� ��λ��
	private List<Map<String, String>> listLog = new ArrayList<Map<String,String>>();
	private int CircleStep;//��Ȧ�沽�� 0:��ʼ����1:Ȧ���ʼ����2:Ȧ�棬3:Ȧ�����
	//private enum CircleStep {INIT, CIRCLE_INIT, CIRCLEING, CIRCLE_COMPLETE};
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
	
	public static String VersionControlUrl = Global_Config.VersionControlUrl;
	public static String ApkDownloadUrl = Global_Config.ApkDownloadUrl;
	
	public static String SERVER_IP = Global_Config.SERVER_IP;
	public static int SERVER_PORT = Global_Config.SERVER_PORT;
	
	private String PayMethod;//��ֵ��ʽ
	private String TerminalCode;//�ն˺� 

	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		setCircleStep(INIT_STEP);
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
	public String getPublishMsg()
	{
		return PublishMsg;
	}

	/**
	 * ���ô󿨹�����Ϣ
	 * @param publishMsg
	 */
	public void setPublishMsg(String publishMsg)
	{
		PublishMsg = publishMsg;
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
	 * ��ȡ��ֵ��ʽ
	 * //		 01:��֧��
	 * //        02:֧����
	 * //        03:��������
	 * //        04:��������
	 * //        05:ũҵ����
	 * @return
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
	
}
