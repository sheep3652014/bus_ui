package com.example.nfc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BigCardBean
{
	private String random; //�����
	private String CircleInitMsg;//Ȧ���ʼ����Ϣ
	private String PublishMsg;//������Ϣ
	private String PublishCardNO;//���п���
	private int BeforeChargeMoney;//��ֵǰ��� ��λ��
	private int AfterChargeMoney;//��ֵ���� ��λ��
	private List<Map<String, String>> listLog = new ArrayList<Map<String,String>>();
	
	public BigCardBean()
	{
		setRandom(null);
		setCircleInitMsg(null);
		setPublishMsg(null);
		setPublishMsg(null);
		setBeforeChargeMoney(0);
		setAfterChargeMoney(0);
		setListLog(null);
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

}
