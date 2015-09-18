package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.config.Global_Config;

import android.app.Application;

public class myApplication extends Application
{
	private String IMEI;//手机IMEI码
	private int ChargeMoney;//充值金额
	private String BigCard_CircleCmd;
	private String random; //随机数
	private String CircleInitMsg;//圈存初始化信息
	private String PublishMsg;//公共信息
	private String PublishCardNO;//发行卡号
	private int BeforeChargeMoney;//充值前金额 单位分
	private int AfterChargeMoney;//充值后金额 单位分
	private List<Map<String, String>> listLog = new ArrayList<Map<String,String>>();
	private int CircleStep;//大卡圈存步骤 0:初始化，1:圈存初始化，2:圈存，3:圈存完成
	private String physicsCardNO;//物理卡号
	private String TAC_CARD;//大卡圈存成功返回
	
	//private enum CircleStep {INIT, CIRCLE_INIT, CIRCLEING, CIRCLE_COMPLETE};
	private static final int INIT_STEP = Global_Config.INIT_STEP;
	private static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	//private static final int CIRCLEING_STEP = Global_Config.CIRCLEING_STEP;
	//private static final int CIRCLE_COMPELTE_STEP = Global_Config.CIRCLE_COMPELTE_STEP;
	
	public static String VersionControlUrl = Global_Config.VersionControlUrl;
	public static String ApkDownloadUrl = Global_Config.ApkDownloadUrl;
	
	public static String SERVER_IP = Global_Config.SERVER_IP;
	public static int SERVER_PORT = Global_Config.SERVER_PORT;
	
	private String PayMethod;//充值方式
	private String TerminalCode = "100000000001";//终端号 
	
	private int MoneyPosition;//用于根据选择充值金额查找圈存初始化数据
	
	
	//充值方式 常量
	 //        01:翼支付
	 //        02:支付宝
	 //        03:建设银行
	 //        04:工商银行
	 //        05:农业银行
	public static final int PAYMETHOD_BESTPAY = 01;
	public static final int PAYMETHOD_ALIPAY = 02;
	public static final int PAYMETHOD_CCBPAY = 03;
	public static final int PAYMETHOD_ICBCPAY = 04;
	public static final int PAYMETHOD_ABCPAY = 05;
	
	
	//支付订单信息
	private String OrderNO;//订单号
	private String OrderSeq;//订单流水号
	private long OrderTime;//订单时间
	private String OrderAmount;//订单金额
	
	/**
	 * 获取订单号 30位
	 * @return
	 */
	public String getOrderNO()
	{
		return OrderNO;
	}
	/**
	 * 设置订单号 
	 * @param orderNO 30位
	 */
	public void setOrderNO(String orderNO)
	{
		OrderNO = orderNO;
	}
	/**
	 * 获取订单流水号 30位
	 * @return
	 */
	public String getOrderSeq()
	{
		return OrderSeq;
	}
	/**
	 * 设置订单流水号 30位
	 * @param orderSeq
	 */
	public void setOrderSeq(String orderSeq)
	{
		OrderSeq = orderSeq;
	}
	/**
	 * 获取订单时间 ms形式 long型
	 * @return
	 */
	public long getOrderTime()
	{
		return OrderTime;
	}
	/**
	 * 设置订单时间  ms形式 long型
	 * @param orderTime
	 */
	public void setOrderTime(long orderTime)
	{
		OrderTime = orderTime;
	}
	/**
	 * 获取订单金额 单位：分
	 * @return
	 */
	public String getOrderAmount()
	{
		return OrderAmount;
	}
	/**
	 * 设置订单金额 单位：分
	 * @param orderAmount
	 */
	public void setOrderAmount(String orderAmount)
	{
		OrderAmount = orderAmount;
	}
	
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
	 * 获取大卡圈存指令
	 * @return
	 */
	public String getBigCard_CircleCmd()
	{
		return BigCard_CircleCmd;
	}

	/**
	 * 设置大卡圈存指令
	 * @param CircleCmd
	 */
	public void setBigCard_CircleCmd(String Cmd)
	{
		BigCard_CircleCmd = Cmd;
	}
	
	/**
	 * 获取充值金额 单位分 
	 * @return
	 */
	public int getChargeMoney()
	{
		return ChargeMoney;
	}
	
	/**
	 * 设置充值金额 单位分
	 * @param chargeMoney
	 */
	public void setChargeMoney(int chargeMoney)
	{
		ChargeMoney = chargeMoney;
	}

	/**
	 * 大卡充值圈存步骤获取
	 * @return
	 */
	public int getCircleStep()
	{
		return CircleStep;
	}
	
	/**
	 * 大卡充值圈存步骤设置
	 * @param circleStep
	 */
	public void setCircleStep(int circleStep)
	{
		CircleStep = circleStep;
	}

	/**
	 * 获取终端号
	 * @return
	 */
	public String getTerminalCode()
	{
		return TerminalCode;
	}
	
	/**
	 * 设置终端号
	 * @param terminalCode
	 */
	public void setTerminalCode(String terminalCode)
	{
		TerminalCode = terminalCode;
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
	/**
	 * 获取充值方式
	 * //		 01:翼支付
	 * //        02:支付宝
	 * //        03:建设银行
	 * //        04:工商银行
	 * //        05:农业银行
	 * @return String
	 */
	public String getPayMethod()
	{
		return PayMethod;
	}
	/**
	 * 设置充值方式
	 * //		 01:翼支付
	 * //        02:支付宝
	 * //        03:建设银行
	 * //        04:工商银行
	 * //        05:农业银行
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
	 * 获取充值金额选择位置
	 * @return
	 */
	public int getMoneyPosition()
	{
		return MoneyPosition;
	}
	/**
	 * 设置充值金额选择位置
	 * @param moneyPosition
	 */
	public void setMoneyPosition(int moneyPosition)
	{
		MoneyPosition = moneyPosition;
	}
	
	/**
	 * 获取物理卡号 长度 4byte
	 * @return
	 */
	public String getPhysicsCardNO()
	{
		return physicsCardNO;
	}
	/**
	 * 设置物理卡号 长度4byte
	 * 传入值为null 或 空字符串 或 长度小于8个字符串长度（4byte） 则物理卡号设置为"00000000" 
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
	 * 获取大卡圈存成功应答返回值，长度4byte
	 * @return 长度4byte
	 */
	public String getTAC_CARD()
	{
		return TAC_CARD;
	}
	/**
	 * 设置大卡圈存成功应答返回值 长度4byte
	 * @param tAC_CARD
	 */
	public void setTAC_CARD(String tAC_CARD)
	{
		TAC_CARD = tAC_CARD;
	}
	
}
