package com.example.config;

public class Global_Config
{
	public final static String PRIVACY_KEY = "79636275734A4D4A";
	
	//消息常量定义
	public static final int NETWORK_DISCONNECT = 0;
	public static final int NETWORK_CONNECT = 1;
	public static final int CONNECT_ERROR = 2;
	public static final int INNER_MSG_START = 1000;
	
	public static final long NETWORKCHECK_GAP = 10*1000;//网络通断判断间隔
	public static final String NETWORK_STATUS = "network_status";
	public static final String NETWORK_STATUS_ACTION = "com.example.network.NetworkConnect_action";//自定义广播action
	
	public static final int CONNECT_OUT_TIME = 2000;//网络超时时间
	public final static String BUSINESS_CODE = "0001"; //行业代码
	
//	805000020B01+czje+12345678   //cmd + 金额 + 终端号
//	czje是8位16进制数字，分结尾。充1分钱，是00000001，充1元钱是0000000A
//	圈存写入
//	805200000B+datetimetxt+mac2
	public static final String BIG_AID = "a00000000386980701";//
	public static final String CIRCLE_INIT = "805000020B01";
	public static final String CIRCLE_WRITE = "805200000B";
	public static final String GET_RANDOM =  "0084000004";//获取4byte 随机数
	
	public static final String BIG_AID_YICHANG = "00a40000021002";//宜昌大卡AID
	public static final String CITY_CODE_YICHANG = "4430";//宜昌城市代码
	
	//程序更新地址
	public static final String APKName = "yichang.apk";
	public static String VersionControlUrl = "http://58.19.246.6:19287/NetServer_NFC/app/update.xml";//宜昌
	public static String ApkDownloadUrl = "http://58.19.246.6:19287/NetServer_NFC/app/" + APKName;//宜昌
	//后台服务器ip,port
	public static String SERVER_IP = "58.19.246.6";//宜昌"111.4.120.231"; //测试用地址
	public static int SERVER_PORT = 19288;
	
	//翼支付调用请求码：反编译得来
	public static final int BESTPAY_REQUESTCODE = 1000;
	//翼支付更新标识
	public static final String HAS_BESTPAY_UPDATE = "has update";
	//翼支付订单配置
	public static final String MERCHANTID = "02420201033958000";//宜昌 商户代码
	public static final String MERCHANTPWD = "773429";//宜昌 商户支付密码
	public static final String KEY_BESTPAY = "B9B6D01A51B35D96938EE944D956D5F001B5133F3AC202BE";//宜昌MD5算法KEY，由商户实现
	
	
	
	//用于获取大卡消费记录分析
	public static final String BEFORECHARGE_MONEY = "before_money";
	public static final String CHARGE_MONEY = "charge_money";
	public static final String AFTERCHARGE_MONEY = "after_money";
	public static final String CONSUME = "consume";
	public static final String CONSUME_TYPE = "consume_type";
	public static final String CONSUME_TIME = "consume_time";
	public static final String CONSUME_MONEY = "consume_money";
	public static final String PUBLISHMSG = "publishmsg";
	public static final String PUBLISHCARDNO = "publishCardNO";
	public static final String RANDOM = "random";
	public static final String LOGLIST = "loglist";
	public static final String CICLEINIT_MSG = "cicleinit";
	public static final String TAC_CODE = "tac";
	
	//payparam 常量
	public static final String CARD_NUMBER = "cardNO";
	public static final String PHYSICS_CARDNO = "physics_cardNO";
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
	public static final String CIRCLEINIT_DATA = "circleinit";
	public static final String TAC_CARD = "tac";
	
	//用于大卡充值圈存步骤标识
	public static final int INIT_STEP = 0;
	public static final int CIRCLE_INIT_STEP = 1;
	//public static final int CIRCLEING_STEP = 2;
	//public static final int CIRCLE_COMPELTE_STEP = 3;
	
	//0X02:充值  0x06:消费 0x09复合消费
	public static final String TYPE_CHARGE = "2";//"02"; 
	public static final String TYPE_CONSUME_NORMAL = "6";//"06";
	public static final String TYPE_CONSUME = "9";//"09";
	
//	public static String getVersionControlUrl()
//	{
//		return VersionControlUrl;
//	}
//	public static void setVersionControlUrl(String versionControlUrl)
//	{
//		VersionControlUrl = versionControlUrl + "/app/update.xml";
//	}
//	public static String getApkDownloadUrl()
//	{
//		return ApkDownloadUrl;
//	}
//	public static void setApkDownloadUrl(String apkDownloadUrl)
//	{
//		ApkDownloadUrl = apkDownloadUrl  + "/app/huangshi_demo.apk";
//	}
//	
//	public static String getServerIP()
//	{
//		return SERVER_IP;
//	}
//	
//	public static void setServerIP(String ip)
//	{
//		SERVER_IP = ip;
//	}
//	
//	public static int getServerPort()
//	{
//		return SERVER_PORT;
//	}
//	
//	public static void setServerPort(int port)
//	{
//		SERVER_PORT = port;
//	}
}
