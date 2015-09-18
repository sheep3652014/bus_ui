package com.example.config;

import android.R.integer;

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
	
	public static final String SIMCARD_STATUS = "simcard_status";//simcard启用状态事件名
	public static final String SIMCARD_APPLET_STATUS = "swp_nfc_status";//simcard是否有公交应用
	
	public static final int CONNECT_OUT_TIME = 2000;//网络超时时间
	public static final String BUSINESS_CODE = "0001"; //行业代码
	
//	805000020B01+czje+12345678   //cmd + 金额 + 终端号
//	czje是8位16进制数字，分结尾。充1分钱，是00000001，充1元钱是0000000A
//	圈存写入
//	805200000B+datetimetxt+mac2
	public static final String BIG_AID = "a00000000386980701";//
	public static final String CIRCLE_INIT = "805000020B01";
	public static final String CIRCLE_WRITE = "805200000B";
	public static final String GET_RANDOM =  "0084000004";//获取4byte 随机数
	public static final String MONEY_CMD = "805c000204";  //读余额
	public static final String READ_COMMON = "00b0950000"; //读公共信息
	public static final int MAX_RECORD = 10; //最多读取记录数,可变,此处取10
	public static final String READ_RECORD_HEAD = "00b2";  //读记录 READ_RECORD_HEAD + 记录号 + READ_RECORD_TAIL
	public static final String READ_RECORD_TAIL = "c400"; //04|(24<<3) = c4
	public static final String VERIFY_PIN = "123456";  //暂时不用校验，
	public static final String VERIFY = "0020000003";
	public static final String BIG_AID_YICHANG = "00a40000021002";//宜昌大卡AID
	public static final String CITY_CODE_YICHANG = "4430";//宜昌城市代码

	//swp-nfc
	public static final String AID = "315041592E5359532E44444630311702";//"D1560000401000400000000100000000";//宜昌
	public static final String APPLET = "00a40000021002";//宜昌
	
	//程序更新地址
	public static final String APKName = "yichang.apk";
	public static String VersionControlUrl = "http://58.19.246.6:19287/NetServer_NFC/app/update.xml";//宜昌
	public static String ApkDownloadUrl = "http://58.19.246.6:19287/NetServer_NFC/app/" + APKName;//宜昌
	//后台服务器ip,port
	public static String SERVER_IP = "58.19.246.6";//"192.168.102.106";//宜昌"111.4.120.231"; //测试用地址
	public static int SERVER_PORT = 19288;
	
	//翼支付订单配置
	public static final String MERCHANTID = "02420201033958000";//宜昌 商户代码
	public static final String MERCHANTPWD = "773429";//宜昌 商户支付密码
	public static final String KEY_BESTPAY = "B9B6D01A51B35D96938EE944D956D5F001B5133F3AC202BE";//宜昌MD5算法KEY，由商户实现
	
	//翼支付调用请求码：反编译得来
	public static final int BESTPAY_REQUESTCODE = 1000;
	
	
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
	public static final String CONSUME_TERMINAL = "consume_terminal";
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
	public static final String ORDER_DATE = "order_date";
	public static final String BANKCARDNO = "bank_cardNO";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	//public static final String USERNME_LEN = "username_len";
	
	//用于大卡充值圈存步骤标识
	public static final int INIT_STEP = 0;
	public static final int CIRCLE_INIT_STEP = 1;
	//public static final int CIRCLEING_STEP = 2;
	//public static final int CIRCLE_COMPELTE_STEP = 3;
	
	//补登标识
	public static final int THIRDPARTY_PAY_INIT = 10;
	public static final int THIRDPARTY_PAY_NETRECORD = 11;//补登在线记录
	
	//0X02:充值  0x06:消费 0x09复合消费
	public static final String TYPE_CHARGE = "2";//"02"; 
	public static final String TYPE_CONSUME_NORMAL = "6";//"06";
	public static final String TYPE_CONSUME = "9";//"09";
	
	public static final String NFC_TAG = "tag";//NFC tag
	public static final String REQUEST_CIRCLE_ACK = "ack";//申请圈存指令的应答
	public static final String THIRDPARTYPAY_LIST = "thirdpartypay_list";//补登信息列表
	public static final String THIRDPARTYPAY_NETLOG = "thirdpartypay_netlog";//在线补登信息列表

	public static final String FAKE_PUBLISHCARDNO = "1234567890123456";//虚假发行卡号
	
	public static final String FRG_WAIT_CALL_FROM = "callfrom";
	public static final String FRG_WAIT_CALL_FROM_SWP = "callfrom_swp";
	public static final String FRG_WAIT_CALL_FROM_THIRDPARTY = "callfrom_THIRDPARTY";
	public static final String FRG_WAIT_CALL_FROM_THIRDPARTY_NETLOG = "callfrom_thirdparty_netlog";
}
