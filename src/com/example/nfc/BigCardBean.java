package com.example.nfc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BigCardBean
{
	private String random; //随机数
	private String CircleInitMsg;//圈存初始化信息
	private String PublishMsg;//公共信息
	private String PublishCardNO;//发行卡号
	private int BeforeChargeMoney;//充值前金额 单位分
	private int AfterChargeMoney;//充值后金额 单位分
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
	 * 获取随机数
	 * @return
	 */
	public String getRandom()
	{
		return random;
	}

	/**
	 * 设置随机数
	 * @param random
	 */
	public void setRandom(String random)
	{
		this.random = random;
	}


	/**
	 * 获取大卡充值前金额
	 * @return
	 */
	public int getBeforeChargeMoney()
	{
		return BeforeChargeMoney;
	}

	/**
	 * 设置大卡充值前金额
	 * @param beforeChargeMoney
	 */
	public void setBeforeChargeMoney(int beforeChargeMoney)
	{
		BeforeChargeMoney = beforeChargeMoney;
	}

	/**
	 * 获取大卡公共信息
	 * @return
	 */
	public String getPublishMsg()
	{
		return PublishMsg;
	}

	/**
	 * 设置大卡公共信息
	 * @param publishMsg
	 */
	public void setPublishMsg(String publishMsg)
	{
		PublishMsg = publishMsg;
	}

	/**
	 * 获取大卡消费记录
	 * @return
	 */
	public List<Map<String, String>> getListLog()
	{
		return listLog;
	}

	/***
	 * 设置大卡消费记录
	 * @param listLog
	 */
	public void setListLog(ArrayList<Map<String, String>> listLog)
	{
		this.listLog = listLog;
	}

	/**
	 * 设置圈存初始化信息
	 * @return
	 */
	public String getCircleInitMsg()
	{
		return CircleInitMsg;
	}

	/**
	 * 获取圈存初始化信息
	 * @param circleInitMsg
	 */
	public void setCircleInitMsg(String circleInitMsg)
	{
		CircleInitMsg = circleInitMsg;
	}

	/**
	 * 获取圈存后金额
	 * @return
	 */
	public int getAfterChargeMoney()
	{
		return AfterChargeMoney;
	}

	/**
	 * 设置圈存后金额
	 * @param afterChargeMoney
	 */
	public void setAfterChargeMoney(int afterChargeMoney)
	{
		AfterChargeMoney = afterChargeMoney;
	}

	/**
	 * 获取发行卡号
	 * @return
	 */
	public String getPublishCardNO()
	{
		return PublishCardNO;
	}

	/**
	 * 设置发行卡号
	 * @param publishCardNO
	 */
	public void setPublishCardNO(String publishCardNO)
	{
		PublishCardNO = publishCardNO;
	}

}
