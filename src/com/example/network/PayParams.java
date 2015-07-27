package com.example.network;

import com.example.bus_ui_demo.R.string;

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

	public static final String CARD_NUMBER = "cardNO";
	public static final String IMEI_NO = "imei";
	public static final String ORDERNO = "orderNO";
	public static final String ORDERSEQ = "orderSeq";
	public static final String ORDER_AMOUNT = "orderAmount";
	public static final String OLDAMOUNT = "oldAmount";
	public static final String PUBLEN = "pubLen";
	public static final String PUBLIC_DATA = "publicdata";
	public static final String PAYTYPE = "payType";
	public static final String SOFTVERSION = "softversion";
	public static final String PHONENO = "phoneNO";
	
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
	
	
}
