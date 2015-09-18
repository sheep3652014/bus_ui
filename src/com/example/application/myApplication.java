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
	private String IMEI;//手机IMEI码
	private String phoneNO = "12345678901";//手机号码
	private int ChargeMoney;//充值金额
	private String BigCard_CircleCmd;
	private String random; //随机数
	private String CircleInitMsg;//圈存初始化信息
	private String PublicMsg;//公共信息
	private String swp_PublicMsg;//小卡公共信息
	private String PublishCardNO;//发行卡号
	private String swp_PublishCardNO;//小卡发行卡号
	private int BeforeChargeMoney;//充值前金额 单位分
	private int AfterChargeMoney;//充值后金额 单位分
	private int swp_balance;//小卡余额
	private List<Map<String, String>> listLog = new ArrayList<Map<String,String>>();//大卡消费记录
	private List<Map<String, String>> swp_listLog = new ArrayList<Map<String,String>>();//小卡消费记录
	private int CircleStep;//大卡圈存步骤 0:初始化，1:圈存初始化，2:圈存，3:圈存完成
	private String physicsCardNO;//物理卡号
	private String swp_physicsCardNO;//小卡物理卡号
	private String TAC_CARD;//大卡圈存成功返回
	private boolean swp_isNewCard;//小卡新旧卡标志
	private boolean isBigCard = false;//是否为大卡，true：大卡，false：小卡
	private boolean hasSIMCard = false;//是否存在有效SIM卡，true：存在  false：不存在
	private boolean hasSIMCard_applet = false;//是否有手机公家应用， true：找到公交应用  false：没找到公交应用
	

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
	 * 获取是否找到有效sim卡
	 * @return true：存在  false：不存在
	 */
	public boolean isHasSIMCard()
	{
		return hasSIMCard;
	}
	/**
	 * 设置是否找到有效sim卡 true：存在  false：不存在
	 * @param hasSIMCard
	 */
	public void setHasSIMCard(boolean hasSIMCard)
	{
		this.hasSIMCard = hasSIMCard;
	}
	/**
	 * 获取sim卡中是否有公交应用
	 * @return  true：找到公交应用  false：没找到公交应用
	 */
	public boolean getHasSIMCard_applet()
	{
		return hasSIMCard_applet;
	}
	/**
	 * 设置是否找到SIM卡中的公交应用   true：找到公交应用  false：没找到公交应用
	 * @param hasSIMCard_applet
	 */
	public void setHasSIMCard_applet(boolean hasSIMCard_applet)
	{
		this.hasSIMCard_applet = hasSIMCard_applet;
	}
	
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
	
	
	public String getIMEI()
	{
		return IMEI;
	}

	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
	}
	/**
	 * 获取电话号码
	 * @return
	 */
	public String getPhoneNO()
	{
		return phoneNO;
	}
	/**
	 * 设置电话号码
	 * @param phoneNO
	 */
	public void setPhoneNO(String phoneNO)
	{
		this.phoneNO = phoneNO;
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
	public String getPublicMsg()
	{
		return PublicMsg;
	}

	/**
	 * 设置大卡公共信息
	 * @param PublicMsg
	 */
	public void setPublicMsg(String PublicMsg)
	{
		this.PublicMsg = PublicMsg;
	}
	
	/**
	 * 获取小卡公共信息
	 * @return
	 */
	public String getSwp_PublicMsg()
	{
		return swp_PublicMsg;
	}
	/**
	 * 设置小卡公共信息
	 * @param swp_PublicMsg
	 */
	public void setSwp_PublicMsg(String swp_PublicMsg)
	{
		this.swp_PublicMsg = swp_PublicMsg;
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
	 * 获取小卡消费记录
	 * @return
	 */
	public List<Map<String, String>> getSwp_listLog()
	{
		return swp_listLog;
	}
	/**
	 * 设置小卡消费记录,如果读取记录为空，添加示例数据
	 * @param swp_listLog
	 */
	public void setSwp_listLog(List<Map<String, String>> swp_listLog)
	{
		if((null == swp_listLog) || (swp_listLog.isEmpty()))
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(CONSUME_TYPE, "02");//交易类型 0X02:充值  0x06:消费 0x09复合消费
			map.put(CONSUME_MONEY, "1.00元");//
			map.put(CONSUME_TIME, "2015年08月04日 15:03:21");
			map.put(CONSUME_TERMINAL, "终端号：123456789012");
			this.swp_listLog.add(map);
		}
		else {
			this.swp_listLog = swp_listLog;
		}
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
	 * 获取小卡发行卡号
	 * @return
	 */
	public String getSwp_PublishCardNO()
	{
		return swp_PublishCardNO;
	}
	/**
	 * 设置小卡发行卡号
	 * @param swp_PublishCardNO
	 */
	public void setSwp_PublishCardNO(String swp_PublishCardNO)
	{
		this.swp_PublishCardNO = swp_PublishCardNO;
	}
	/**
	 * 获取小卡余额
	 * @return
	 */
	public int getSwp_balance()
	{
		return swp_balance;
	}
	/**
	 * 设置小卡余额
	 * @param swp_balance
	 */
	public void setSwp_balance(int swp_balance)
	{
		this.swp_balance = swp_balance;
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
	 * 获取小卡物理卡号
	 * @return
	 */
	public String getSwp_physicsCardNO()
	{
		return swp_physicsCardNO;
	}
	/**
	 * 设置小卡物理卡号
	 * @param swp_physicsCardNO
	 */
	public void setSwp_physicsCardNO(String swp_physicsCardNO)
	{
		this.swp_physicsCardNO = swp_physicsCardNO;
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
	
	/**
	 * 获取小卡新旧卡标志
	 * @return
	 */
	public boolean isSwp_isNewCard()
	{
		return swp_isNewCard;
	}
	
	/**
	 * 设置小卡新旧卡标志，输入为字符串，根据字符串设置新旧卡状态
	 * 启用标志,是否为新卡 00:新卡 01:旧卡
	 * @param String NewCard
	 * 如果输入非“00”，“01” 抛出异常CustomTypeException
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
			throw new CustomTypeException("新旧卡标志只能为00，01，是否读取公共信息出错？");
		}
		
	}
	/**
	 * 获取大小卡标志
	 * @return true：大卡，false：小卡
	 */
	public boolean isBigCard()
	{
		return isBigCard;
	}
	/**
	 * 设置大小卡标志  true：大卡，false：小卡
	 * @param isBigCard
	 */
	public void setBigCard(boolean isBigCard)
	{
		this.isBigCard = isBigCard;
	}
	
}
