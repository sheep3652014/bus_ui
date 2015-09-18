package com.example.network;

import com.example.bus_ui_demo.R.string;
import com.example.config.Global_Config;

public class PayParams
{
//	Card_number	16	N	发行卡号
//	IMEI	15	N	终端唯一标识（全球唯一）
//	orderNo	30	AN	订单编号
//	Orderseq	30	AN	订单流水号
//	orderMount	8	N	订单金额 单位分，（至银行为浮点数 单位元）
//	OldMount	8	HEX	卡片余额 单位分
//	pubLen	2	H	公共信息长度
//	PublicData	N	H	公共信息
//	PayType	2	N	支付途径
//	01 翼支付
//	02 支付宝
//	03 建设银行
//	04 工商银行
//	version	1	N	软件版本号
//	Phone	11	N	手机号 //swp卡绑定时使用

	public static final String CARD_NUMBER = Global_Config.CARD_NUMBER;//"cardNO";
	public static final String PHYSICS_CARDNO = Global_Config.PHYSICS_CARDNO;//"physics_cardNO";
	public static final String IMEI_NO = Global_Config.IMEI_NO;//"imei";
	public static final String ORDERNO = Global_Config.ORDERNO;//"orderNO";
	public static final String ORDERSEQ = Global_Config.ORDERSEQ;//"orderSeq";
	public static final String ORDER_AMOUNT = Global_Config.ORDER_AMOUNT;//"orderAmount";
	public static final String OLDAMOUNT = Global_Config.OLDAMOUNT;//"oldAmount";
	public static final String PUBLEN = Global_Config.PUBLEN;//"pubLen";
	public static final String PUBLIC_DATA = Global_Config.PUBLIC_DATA;//"publicdata";
	public static final String PAYTYPE = Global_Config.PAYTYPE;//"payType";
	public static final String SOFTVERSION = Global_Config.SOFTVERSION;//"softversion";
	public static final String PHONENO = Global_Config.PHONENO;//"phoneNO";
	public static final String CIRCLEINIT_DATA = Global_Config.CIRCLEINIT_DATA;//"circleinit";
	public static final String TAC_CARD = Global_Config.TAC_CARD;//"tac";
	public static final String USERNAME = Global_Config.USERNAME;
	public static final String PASSWROD = Global_Config.PASSWORD;
	//public static final String USERNAME_LEN = Global_Config.USERNME_LEN;
	
	private String Card_number;
	private String IMEI;
	private String OrderNO;
	private String OrderAmount;
	private String OldAmount;
	private String PubLen;
	private String PublicData;
	private String PayType;
	private String SoftVersion;
	private String phoneNO;
	
	private boolean PayStatus;//支付状态
	private boolean CircleStatus;//圈存状态
	private boolean Transference_Close_Status;//支付、圈存、圈存上报均完成，即交易关闭状态
	
	/**
	 * 获取发行卡号
	 * @return
	 */
	public String getCard_number()
	{
		return Card_number;
	}
	/**
	 * 设置发行卡号
	 * @param card_number
	 */
	public void setCard_number(String card_number)
	{
		Card_number = card_number;
	}
	/**
	 * 设置IMEI码
	 * @return
	 */
	public String getIMEI()
	{
		return IMEI;
	}
	/**
	 * 获取IMEI码
	 * @param iMEI
	 */
	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
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
	 * @param orderNO
	 */
	public void setOrderNO(String orderNO)
	{
		OrderNO = orderNO;
	}
	/**
	 * 获取订单金额
	 * @return
	 */
	public String getOrderAmount()
	{
		return OrderAmount;
	}
	/**
	 * 设置订单金额
	 * @param orderAmount
	 */
	public void setOrderAmount(String orderAmount)
	{
		OrderAmount = orderAmount;
	}
	/**
	 * 获取充值前金额
	 * @return
	 */
	public String getOldAmount()
	{
		return OldAmount;
	}
	/**
	 * 设置充值前金额
	 * @param oldAmount
	 */
	public void setOldAmount(String oldAmount)
	{
		OldAmount = oldAmount;
	}
	/**
	 * 获取公共信息长度
	 * @return
	 */
	public String getPubLen()
	{
		return PubLen;
	}
	/**
	 * 设置公共信息长度
	 * @param pubLen
	 */
	private void setPubLen(String pubLen)
	{
		PubLen = pubLen;
	}
	/**
	 * 获取公共信息
	 * @return
	 */
	public String getPublicData()
	{
		return PublicData;
	}
	/**
	 * 设置公共信息,并自动保存公共信息长度
	 * 当公共信息长度大于0XFF时出错
	 * @param publicData
	 */
	public void setPublicData(String publicData)
	{
		PublicData = publicData;
		setPubLen(Integer.toHexString(publicData.length()));
	}
	/**
	 * 获取支付途径
	 * 01 翼支付
	 * 02 支付宝
	 * 03 建设银行
	 * 04 工商银行
	 * @return
	 */
	public String getPayType()
	{
		return PayType;
	}
	/**
	 * 设置支付途径
	 * 01 翼支付
	 * 02 支付宝
	 * 03 建设银行
	 * 04 工商银行
	 * @param payType
	 */
	public void setPayType(String payType)
	{
		PayType = payType;
	}
	/**
	 * 获取软件版本号
	 * @return
	 */
	public String getSoftVersion()
	{
		return SoftVersion;
	}
	/**
	 * 设置软件版本号
	 * @param softVersion
	 */
	public void setSoftVersion(String softVersion)
	{
		SoftVersion = softVersion;
	}
	/**
	 * 获取手机号码
	 * @return
	 */
	public String getPhoneNO()
	{
		return phoneNO;
	}
	/**
	 * 设置手机号码
	 * @param phoneNO
	 */
	public void setPhoneNO(String phoneNO)
	{
		this.phoneNO = phoneNO;
	}
	
	/**
	 * 获取支付成功状态 
	 * @return ture： 成功支付  false：支付失败
	 */
	public boolean isPayStatus()
	{
		return PayStatus;
	}
	/**
	 * 设置支付成功状态 ture： 成功支付  false：支付失败
	 * @param payStatus
	 */
	public void setPayStatus(boolean payStatus)
	{
		PayStatus = payStatus;
	}
	/**
	 * 获取圈存成功状态 
	 * @return ture： 成功  false：失败
	 */
	public boolean isCircleStatus()
	{
		return CircleStatus;
	}
	/**
	 * 设置圈存成功状态 ture： 成功  false：失败
	 * @param circleStatus
	 */
	public void setCircleStatus(boolean circleStatus)
	{
		CircleStatus = circleStatus;
	}
	/**
	 * 	获取交易关闭状态 
	 * @return ture： 完成 false：未完成
	 */
	public boolean isTransference_Close_Status()
	{
		return Transference_Close_Status;
	}
	/**
	 * 设置交易关闭状态  ture： 完成 false：未完成
	 * @param transference_Close_Status
	 */
	public void setTransference_Close_Status(boolean transference_Close_Status)
	{
		Transference_Close_Status = transference_Close_Status;
	}
	
	
}
