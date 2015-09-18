package com.example.bestpay;

import com.example.config.Global_Config;

public class OrderParams {

	public static final String TEST_PHONENO = "17702746753";//测试用账�?
	
	// 商户代码, 必填
	//public static final String MERCHANTID = "02420105024032000";//"MERCHANTID";黄石
	public static final String MERCHANTID = Global_Config.MERCHANTID;//"MERCHANTID";宜昌

	// 子商户号，�?�填
	public static final String SUBMERCHANTID = "";//"SUBMERCHANTID";

	// 商户支付密码,必填
	//public static final String MERCHANTPWD = "840194";//"MERCHANTPWD";黄石
	public static final String MERCHANTPWD = Global_Config.MERCHANTPWD;//"MERCHANTPWD";宜昌

	// 订单号，必填
	//public static final String ORDERSEQ = "ORDERSEQ";
	private static String OrderSeq;
	// 订单请求交易流水号，必填
	//public static final String ORDERREQTRANSEQ = "ORDERREQTRANSEQ";
	private static String Order_ReqTranseq;

	// 订单日期，必�??
	//public static final String ORDERTIME = "ORDERTIME";
	private static String OrderTime;

	// 订单有效时间，�?�填
	//public static final String ORDERVALIDITYTIME = "ORDERVALIDITYTIME";
	private static String Order_validTime;// = "ORDERVALIDITYTIME";

	// 用户ID，在商户系统的登录名，银联WAP�??要，选填�??
	public static final String CUSTOMERID = "CUSTOMERID";

	// 订单总金额，单位元，订单总金�??=产品金额+附加金额，必填
	public static final String ORDERAMOUNT = "ORDERAMOUNT";

	// 产品金额，选填
	public static final String PRODUCTAMOUNT = "PRODUCTAMOUNT";

	// 附加金额，选填
	public static final String ATTACHAMOUNT = "ATTACHAMOUNT";

	// 币种, 默认填RMB，必填
	public static final String CURTYPE = "RMB";//"CURTYPE";

	// 后台返回地址，商户提供的用于接收交易返回的后台url，用于实际的业务处理，必�??
	//public static final String BACKMERCHANTURL = "BACKMERCHANTURL";
	private static String BackMerchantURL="";// = "BACKMERCHANTURL";

	// 附加信息，商户附加信息，选填
	public static final String ATTACH = "ATTACH";

	// 业务标识，银联WAP�??要，选填
	public static final String PRODUCTID = "PRODUCTID";

	// 翼支�?   用户IP,必填
	//public static final String USERIP = "USERIP";
	private static String UserIP;// = "USERIP";

	// 产品描述，普通WAP，必�??
	public static final String PRODUCTDESC = "PRODUCTDESC";

	//业务类型，来自于SDK文档附录
	public static final String BUSITYPE = "04";//"BUSITYPE";//01:只能用可体现余额，04：全部可用

	public static final String MAC = "MAC";

	// 分账明细，�?�填
	public static final String DIVDETAILS = "DIVDETAILS";

	// MAC校验域，默认�??0，当加密方式�??1时有意义，采用标准的MD5算法，由商户实现
	//public static final String KEY = "01CAEEDBF2B4F7464BB5DDEE1BB2FDED72A4601DB9930C77";//"KEY";黄石
	public static final String KEY = Global_Config.KEY_BESTPAY;//"KEY";宜昌

	// 翼支付账户ID
	//public static final String ACCOUNTID = "ACCOUNTID";
	private static String AccountID;// = "ACCOUNTID";

	// 翼支付账户密�??
	public static final String ACCOUNTPWD = "ACCOUNTPWD";
	
	//充�?�金�?
	private static String ChargeMoney;
	
	//翼支付充值成功状�?
	private static Boolean isChargeSucces = false;
	
	//向服务器申请圈存指令成功状�??
	private static Boolean isRequestTransferenceCMD = false;
	
	//圈存流程完成状�??
	private static Boolean isTransferenceSucces = false;
	
	//圈存终端�?
	private static String Terminal_Code = "";//12�?  "123456789012"
	
	/**
	 * 设置订单号
	 * @param seq
	 */
	public static void setOrder_Seq(String seq)
	{
		OrderSeq = seq;
	}
	/**
	 * 获取订单号
	 * @return
	 */
	public static String getOrder_Seq()
	{
		return OrderSeq;
	}
	
	/**
	 * 设置订单流水号
	 * @param seq
	 */
	public static void setOrder_Transeq(String seq)
	{
		Order_ReqTranseq = seq;
	}
	
	/**
	 * 获取订单流水号
	 * @return
	 */
	public static String getOrder_Transeq()
	{
		return Order_ReqTranseq;
	}
	
	public static void setOrderTime(String time)
	{
		OrderTime = time;
	}
	/**
	 * 获取订单时间
	 * @return
	 */
	public static String getOrderTime()
	{
		return OrderTime;
	}
	
	public static void setBackMerchantURL(String url)
	{
		System.out.println("url = " + url);
		OrderParams.BackMerchantURL = url;
	}
	
	/**
	 * 获取翼支付后台返回网�?
	 * @return
	 */
	public static String getBackMerchantURL()
	{
		return OrderParams.BackMerchantURL;
	}
	
	public static void setChargeMoney(String money)
	{
		OrderParams.ChargeMoney = money;
	}
	
	/**
	 * 获取翼支付充值金额
	 * @return
	 */
	public static String getChargeMoney()
	{
		return OrderParams.ChargeMoney;
	}
	
	public static void setOrder_validTime(String time)
	{
		OrderParams.Order_validTime = time;
	}
	
	/**
	 * 设置翼支付订单有效时�?
	 * @return
	 */
	public static String getOrder_validTime()
	{
		return OrderParams.Order_validTime;
	}
	
	public static void setUserIP(String ip)
	{
		OrderParams.UserIP = ip;
	}
	
	/**
	 * 获取翼支付需要提交的 用户ip
	 * @return
	 */
	public static String getUserIP()
	{
		return OrderParams.UserIP;
	}
	
	public static void setAccountID(String phoneNO)
	{
		OrderParams.AccountID = phoneNO;
	}
	
	/**
	 * 获取翼支付账�?
	 * @return
	 */
	public static String getAccountID()
	{
		return OrderParams.AccountID;
	}
	
	/**
	 * 获取翼支付成功状�?
	 * @return true 成功  false 失败
	 */
	public static Boolean getIsChargeSucces()
	{
		return OrderParams.isChargeSucces;
	}

	public static void setIsChargeSucces(Boolean isChargeSucces)
	{
		OrderParams.isChargeSucces = isChargeSucces;
	}
	
	/**
	 * 获取圈存流程完成状�??
	 * @return true 完成  false 失败
	 */
	public static Boolean getIsTransferenceSucces()
	{
		return OrderParams.isTransferenceSucces;
	}
	
	public static void setIsTransferenceSucces(Boolean isTransferenceSucces)
	{
		OrderParams.isTransferenceSucces = isTransferenceSucces;
	}
	
	/**
	 * 请求申请圈存指令成功标志
	 * @return true 成功  false 失败
	 */
	public static Boolean getIsRequestTransferenceCMD()
	{
		return OrderParams.isRequestTransferenceCMD;
	}

	public static void setIsRequestTransferenceCMD(
			Boolean isRequestTransferenceCMD)
	{
		OrderParams.isRequestTransferenceCMD = isRequestTransferenceCMD;
	}
	
	/**
	 * 获取终端号，圈存写卡时使�?
	 * @return
	 */
	public static String getTerminal_Code()
	{
		return OrderParams.Terminal_Code;
	}

	public static void setTerminal_Code(String Terminal_Code)
	{
		OrderParams.Terminal_Code = Terminal_Code;
	}
	
	

	public static void Clear()
	{
		OrderParams.OrderSeq = "";
		OrderParams.Order_ReqTranseq = "";
		OrderParams.OrderTime = "";
		OrderParams.Order_validTime = "";
		OrderParams.ChargeMoney = "";
		OrderParams.BackMerchantURL = "";
		OrderParams.UserIP = "";
		OrderParams.AccountID = "";
		OrderParams.isChargeSucces = false;
		OrderParams.isRequestTransferenceCMD = false;
		OrderParams.isTransferenceSucces = false;
		setTerminal_Code("");
	}
}
