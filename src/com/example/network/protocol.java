package com.example.network;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.RSA.Key;
import com.example.application.myApplication;
import com.example.bestpay.CryptTool;
import com.example.bus_ui_demo.R;
import com.example.bus_ui_demo.aty_PopRequestPay;
import com.example.config.Global_Config;
import com.example.network.SocketClientCls;
import com.example.nfc.Util;
import com.example.RSA.RSAUtils568;

import android.R.bool;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class protocol
{
	private static final String TAG = "protocl";
	private static final String CITY_CODE = Global_Config.CITY_CODE_YICHANG;
	private static final String BUSINESS_CODE = Global_Config.BUSINESS_CODE;
	private static final String PRIVACY_KEY = Global_Config.PRIVACY_KEY;

	private static final String CARD_NUMBER = Global_Config.CARD_NUMBER;// "cardNO";
	private static final String PHYSICS_CARDNO = Global_Config.PHYSICS_CARDNO;// "physics_cardNO";
	private static final String IMEI_NO = Global_Config.IMEI_NO;// "imei";
	private static final String ORDERNO = Global_Config.ORDERNO;// "orderNO";
	private static final String ORDERSEQ = Global_Config.ORDERSEQ;// "orderSeq";
	private static final String ORDER_AMOUNT = Global_Config.ORDER_AMOUNT;// "orderAmount";
	private static final String OLDAMOUNT = Global_Config.OLDAMOUNT;// "oldAmount";
	private static final String PUBLEN = Global_Config.PUBLEN;// "pubLen";
	private static final String PUBLIC_DATA = Global_Config.PUBLIC_DATA;// "publicdata";
	private static final String PAYTYPE = Global_Config.PAYTYPE;// "payType";
	private static final String SOFTVERSION = Global_Config.SOFTVERSION;// "softversion";
	private static final String PHONENO = Global_Config.PHONENO;// "phoneNO";
	private static final String CIRCLEINIT_DATA = Global_Config.CIRCLEINIT_DATA;// "circleinit";
	private static final String TAC_CARD = Global_Config.TAC_CARD;// "tac";
	private static final String ORDER_DATE = Global_Config.ORDER_DATE;
	private static final String BANKCARDNO = Global_Config.BANKCARDNO;

	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final String CIRCLE_WRITE = Global_Config.CIRCLE_WRITE;
	private static final String PUBLISHCARDNO = Global_Config.PUBLISHCARDNO;

	public static String Bind()
	{
		String result = null;

		return result;
	}

	/**
	 * 申请支付
	 * 
	 * @param choice
	 *            充值方式
	 * @param handler
	 * @return 应答正确返回对应配置信息，错误返回null 如果发送错误，除返回null外，还发送CONNECT_ERROR message
	 */
	public static String RequestPay(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head: 8010
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data:
		// Card_number 16 N 发行卡号
		// PhysicsCardNO 8 N 物理卡号
		// IMEI 15 N 终端唯一标识（全球唯一）
		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// orderMount 8 N 订单金额 单位分，（至银行为浮点数 单位元）
		// OldMount 8 HEX 卡片余额 单位分
		// pubLen 2 H 公共信息长度
		// PublicData N H 公共信息
		// PayType 2 N 支付途径
		// 01 翼支付
		// 02 支付宝
		// 03 建设银行
		// 04 工商银行
		String result = null;

		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return result;

		String Head = "8010" + CITY_CODE + BUSINESS_CODE;
		String data = map.get(CARD_NUMBER) + map.get(PHYSICS_CARDNO)
				+ map.get(IMEI_NO) + StringToLength(30, map.get(ORDERNO))
				+ StringToLength(30, map.get(ORDERSEQ))
				+ map.get(ORDER_AMOUNT) + map.get(OLDAMOUNT) + map.get(PUBLEN)
				+ map.get(PUBLIC_DATA) + map.get(PAYTYPE);
		String len = Integer.toHexString(data.length());
		Head += len;

		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket = new SocketClientCls();

		try
		{
			mac = CryptTool.md5Digest(mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();
			result = null;
			return result;
		}
		System.out.println("request pay frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);
			result = null;
			return result;
		}
		// 应答码 4 AN 1200
		// 或（1201,1202,1203,1204,1205,1206）详情见附录1
		// 终端号 12 N 仅当应答码等于1200时有此域
		// 支付响应地址 N N
		// 仅当应答码等于1200时http://111.4.120.231:9000/NetServer/ResponseUrl
		String Ack = socket.ReceiveText();
		System.out.println("request pay ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			result = null;
			return result;
		}
		String AckCode = Ack.substring(0, 4);// 去前四位错误码

		if (AckCode.equals("1200"))
		{
			if ((Ack.length() < "1200123456789012http://192.168.100.237:8081/NetServer/Response"
					.length()))
			{
				System.out.println("应答错误，长度出错");
				socket.CloseLink();
				result = null;
				return result;
			}

			socket.CloseLink();

			result = Ack;
		} else
		{
			result = null;
			return result;
		}
		System.out.println("request pay result = " + result);
		socket.CloseLink();// 应答完成，关闭资源
		return result;
	}

	/**
	 * 确认订单支付状态 帧拼接
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return true 支付成功 false 支付失败
	 */
	public static boolean CheckPayStatus(Context context, myApplication myApp,
			Handler handler)
	{
		// orderNo 30 AN 订单编号
		// OrderSeq 30 AN 订单流水号
		// OrderAmount 8 AN 订单金额 十进制 单位：分
		// PayMethod 2 N 支付途径
		// 01 翼支付
		// 02 支付宝
		// 03 建设银行
		// 04 工商银行
		// 05

		HashMap<String, String> map = new HashMap<String, String>();

		String m = "" + myApp.getChargeMoney();
		String money = Util.to8String(m);

		map.put(PayParams.ORDERNO, StringToLength(30, myApp.getOrderNO()));
		map.put(PayParams.ORDERSEQ, StringToLength(30, myApp.getOrderSeq()));
		map.put(PayParams.ORDER_AMOUNT, money);
		map.put(PayParams.PAYTYPE, myApp.getPayMethod());

		return (CheckPayStatusCmd(context, map, handler));
	}

	/**
	 * 确认订单支付状态 命令发送
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return
	 */
	private static boolean CheckPayStatusCmd(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head 命 令 4 AN 8020
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// orderNo 30 AN 订单编号
		// OrderSeq 30 AN 订单流水号
		// OrderAmount 8 AN 订单金额 十进制 单位：分
		// PayMethod 2 N 支付途径
		// 01 翼支付
		// 02 支付宝
		// 03 建设银行
		// 04 工商银行
		// 05
		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return false;

		String Head = "8020" + CITY_CODE + BUSINESS_CODE;

		String data = StringToLength(30, map.get(PayParams.ORDERNO)) 
				+ StringToLength(30, map.get(PayParams.ORDERSEQ))
				+ map.get(PayParams.ORDER_AMOUNT) + map.get(PayParams.PAYTYPE);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;

		SocketClientCls socket = new SocketClientCls();

		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		System.out.println("check paystatus cmd frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return false;
		}
		// 应答
		// 812100 成功 812101失败
		String Ack = socket.ReceiveText();
		System.out.println("check paystatus cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 6))
		{
			socket.CloseLink();

			return false;
		}

		if (Ack.equals("812100"))
		{
			socket.CloseLink();// 应答完成，关闭资源
			return true;
		}

		socket.CloseLink();// 应答完成，关闭资源
		return false;

	}

	/**
	 * 请求圈存帧拼接
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return 成功返回指令，否则返回null
	 */
	public static String RequestCircle(Context context, myApplication myApp,
			Handler handler)
	{

		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// PublicData 74 H 公共信息(可补全F)
		// Card_number 16 N 发行卡号
		// orderMount 8 N 订单金额 单位分
		// data8050 32 N 卡片8050返回信息
		HashMap<String, String> map = new HashMap<String, String>();
		String publicData = "";
		for (int i = 0; i < 74; i++)
			publicData += "F";
		// 订单金额
		String orderAmount = "" + myApp.getChargeMoney();
		orderAmount = Util.to8String(orderAmount);

		String publishCardNO = "";
		if (myApp.isBigCard())
		{
			publishCardNO = myApp.getPublishCardNO();
		} else
		{
			publishCardNO = myApp.getSwp_PublishCardNO();
		}

		map.put(PayParams.ORDERNO, StringToLength(30, myApp.getOrderNO()));
		map.put(PayParams.ORDERSEQ, StringToLength(30, myApp.getOrderSeq()));
		map.put(PayParams.PUBLIC_DATA, publicData);
		map.put(PayParams.CARD_NUMBER, publishCardNO);
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.CIRCLEINIT_DATA, myApp.getCircleInitMsg());

		// 调用请求圈存指令
		return (RequestCirleCmd(context, map, handler));
	}

	public static String RequestCirleCmd(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// Head
		// 命 令 4 AN 8002
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// PublicData 74 H 公共信息(可补全F)
		// Card_number 16 N 发行卡号
		// orderMount 8 N 订单金额 单位分
		// data8050 32 N 卡片8050返回信息
		// SIGN 32 H MD5 校验（）

		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return null;

		String Head = "8002" + CITY_CODE + BUSINESS_CODE;

		String data = StringToLength(30, map.get(PayParams.ORDERNO)) 
				+ StringToLength(30, map.get(PayParams.ORDERSEQ))
				+ map.get(PayParams.PUBLIC_DATA)
				+ map.get(PayParams.CARD_NUMBER)
				+ map.get(PayParams.ORDER_AMOUNT)
				+ map.get(PayParams.CIRCLEINIT_DATA);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;

		SocketClientCls socket = new SocketClientCls();

		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return null;
		}
		System.out.println("request circle cmd frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return null;
		}

		// 返回码 4 A 1300，1301，1302，1303,1304,1305,1306,1307
		// 8052写卡数据 42 H RSA加密（需解密保存），仅当返回代码为1300时，有此域，
		// 随机数(10)+datetime(14)+随机数(10)+MAC2(8)
		String Ack = socket.ReceiveText();
		System.out.println("request circle cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return null;
		}

		String AckCode = Ack.substring(0, 4);// 去前四位错误码
		if (AckCode.endsWith("1300"))
		{
			String ackdata = Ack.substring(4);
			String cirleCmd = Decrpyt(ackdata);

			socket.CloseLink();// 应答完成，关闭资源
			System.out.println("circle cmd = " + cirleCmd);
			return cirleCmd;
		} else
		{
			socket.CloseLink();// 应答完成，关闭资源
			return null;
		}
	}

	/**
	 * 数据解密
	 * 
	 * @param str
	 * @return
	 */
	private static String Decrpyt(String str)
	{
		// 数据解密
		RSAPublicKey pubkey = Key.getPublicKey();
		System.out.println("PublicModulus:" + pubkey.getModulus().toString(16));

		String real_data = RSAUtils568.decryptDataStr(str, pubkey);
		String date = real_data.substring(10, 24);
		String mac2 = real_data.substring(34, 42);
		String Tranferance = CIRCLE_WRITE + date + mac2;

		return Tranferance;
	}

	/**
	 * 发送圈存完成报告，发送成功返回true，否则返回false
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean SendCircleCompleteReport(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head 命 令 4 AN 8090
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data Orderseq 30 AN 订单流水号
		// TAC 8 AN 8052应答
		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return false;

		String Head = "8090" + CITY_CODE + BUSINESS_CODE;

		String data = StringToLength(30, map.get(PayParams.ORDERSEQ))
					  + map.get(PayParams.TAC_CARD);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket = new SocketClientCls();
		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		System.out.println("SendCircleCompleteReport frame : " + Head + data
				+ mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return false;
		}
		// 返回码 4 A 1500，1501，1502
		String Ack = socket.ReceiveText();
		System.out.println("SendCircleCompleteReport ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return false;
		}

		if (Ack.equals("1500"))
		{
			socket.CloseLink();

			return true;
		}
		socket.CloseLink();// 应答完成，关闭资源
		return false;
	}

	/**
	 * 退单请求，请求成功返回 true 否则false
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return 请求成功返回 true 否则false
	 */
	public static boolean Refund(Context context, HashMap<String, String> map,
			Handler handler)
	{
		// head 命 令 4 AN 8080
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// orderNO 30 AN 原订单号
		// orderSeq 30 AN 原订单流水号
		// orderAmount 8 N 原订单金额 十进制 分
		// phoneNO 11 N 手机号

		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return false;

		String Head = "8080" + CITY_CODE + BUSINESS_CODE;

		String data = StringToLength(30, map.get(PayParams.ORDERNO))
				+ StringToLength(30, map.get(PayParams.ORDERSEQ))
				+ map.get(PayParams.ORDER_AMOUNT) + map.get(PayParams.PHONENO);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket = new SocketClientCls();
		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		System.out.println("refund frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return false;
		}

		// 返回码 4 A 1600，1601，1602
		String Ack = socket.ReceiveText();
		System.out.println("refund ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			return false;
		}

		if (Ack.equals("1600"))
		{
			socket.CloseLink();
			return true;
		}
		socket.CloseLink();// 应答完成，关闭资源
		return false;
	}

	/**
	 * 补登订单查询： 请求第三方支付订单信息，需要先读卡，获取发行卡号、物理卡号、公共信息、卡片余额
	 * 某些支付方式可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置为相同
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return 第三方成功支付并且是针对本卡的支付订单，没有支付成功的订单则返回空list
	 */
	public static List<Map<String, String>> RequestThridPartyPayOrder(
			Context context, HashMap<String, String> map, Handler handler)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// 命 令 4 AN 8081
		// head
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// Card_number 16 N 发行卡号
		// PhysicsCardNO 8 N 物理卡号
		// IMEI 15 N IMEI
		// OldMount 8 HEX 卡片余额 单位分
		// pubLen 2 H 公共信息长度
		// PublicData N H 公共信息

		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
		{
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);
			return list;
		}

		String Head = "8081" + CITY_CODE + BUSINESS_CODE;

		String data = map.get(PUBLISHCARDNO) + map.get(PHYSICS_CARDNO)
				+ map.get(IMEI_NO) + map.get(OLDAMOUNT) + map.get(PUBLEN)
				+ map.get(PUBLIC_DATA);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket = new SocketClientCls();
		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return list;
		}
		System.out.println("refund frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return list;
		}
		// 返回码 4 A 2200，2201 仅2200时有数据块
		// 数据块 2 H 补登记录数，最大255
		// 8 N orderMount 补登金额，十进制 分
		// 30 AN orderNO
		// 19 N 银行卡号 不足补空格 无卡号 全空格
		// 8 N 订单时间 yyyymmdd
		String Ack = socket.ReceiveText();
		System.out.println("refund ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			return list;
		}
		String AckCode = Ack.substring(0, 4);
		if (!"2200".equals(AckCode))
		{
			socket.CloseLink();
			return list;
		} else
		{
			int cnt = Integer.parseInt(Ack.substring(4, 6), 10);
			for (int i = 0; i < cnt; i++)
			{
				Map<String, String> map1 = new HashMap<String, String>();
				String money = Ack.substring(6 + i * 65, 14 + i * 65);
				String orderNO = Ack.substring(14 + i * 65, 44 + i * 65).trim();
				String BankCardNO = Ack.substring(44 + i * 65, 63 + i * 65)
						.trim();
				String date = Ack.substring(63 + i * 65, 71 + i * 65);
				map1.put(ORDER_AMOUNT, money);
				map1.put(ORDERNO, orderNO); // 可能只有订单号，没有订单流水号，所以此处将订单号、订单流水号人为设置相同
				map1.put(ORDERSEQ, orderNO);
				map1.put(ORDER_DATE, date);
				map1.put(BANKCARDNO, BankCardNO);
				map1.put(PUBLISHCARDNO, map.get(PUBLISHCARDNO));
				list.add(map1);
			}
		}
		return list;
	}

	/**
	 * 补登帧拼接
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return 圈存指令
	 */
	public static String RequestThirdParyPayCircle(Context context,
			myApplication myApp, Handler handler)
	{

		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// PublicData 74 H 公共信息(可补全F)
		// Card_number 16 N 发行卡号
		// orderMount 8 N 订单金额 单位分
		// data8050 32 N 卡片8050返回信息
		HashMap<String, String> map = new HashMap<String, String>();
		String publicData = "";
		for (int i = 0; i < 74; i++)
			publicData += "F";
		// 订单金额
		String orderAmount = "" + myApp.getChargeMoney();
		orderAmount = Util.to8String(orderAmount);

		String publishCardNO = "";
		if (myApp.isBigCard())
		{
			publishCardNO = myApp.getPublishCardNO();
		} else
		{
			publishCardNO = myApp.getSwp_PublishCardNO();
		}

		map.put(PayParams.ORDERNO, StringToLength(30, myApp.getOrderNO()));
		map.put(PayParams.ORDERSEQ, StringToLength(30, myApp.getOrderSeq()));
		map.put(PayParams.PUBLIC_DATA, publicData);
		map.put(PayParams.CARD_NUMBER, publishCardNO);
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.CIRCLEINIT_DATA, myApp.getCircleInitMsg());

		// 调用请求圈存指令
		return (RequestThirdPartyPayCirleCmd(context, map, handler));
	}

	/**
	 * 补登圈存指令申请
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return
	 */
	private static String RequestThirdPartyPayCirleCmd(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// Head
		// 命 令 4 AN 8082
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// PublicData 74 H 公共信息(可补全F)
		// Card_number 16 N 发行卡号
		// orderMount 8 N 订单金额 单位分
		// data8050 32 N 卡片8050返回信息
		// SIGN 32 H MD5 校验（）

		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return null;

		String Head = "8082" + CITY_CODE + BUSINESS_CODE;

		String data = StringToLength(30, map.get(PayParams.ORDERNO))
				+ StringToLength(30, map.get(PayParams.ORDERSEQ))
				+ map.get(PayParams.PUBLIC_DATA)
				+ map.get(PayParams.CARD_NUMBER)
				+ map.get(PayParams.ORDER_AMOUNT)
				+ map.get(PayParams.CIRCLEINIT_DATA);

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;

		SocketClientCls socket = new SocketClientCls();

		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return null;
		}
		System.out.println("request circle cmd frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return null;
		}

		// 返回码 4 A 2300，2301，2302，2303,2304,2305,2306,2307
		// 8052写卡数据 42 H RSA加密（需解密保存），仅当返回代码为1300时，有此域，
		// 随机数(10)+datetime(14)+随机数(10)+MAC2(8)
		String Ack = socket.ReceiveText();
		System.out.println("request circle cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return null;
		}

		String AckCode = Ack.substring(0, 4);// 去前四位错误码
		if (AckCode.endsWith("2300"))
		{
			String ackdata = Ack.substring(4);
			String cirleCmd = Decrpyt(ackdata);

			socket.CloseLink();// 应答完成，关闭资源
			System.out.println("circle cmd = " + cirleCmd);
			return cirleCmd;
		} else
		{
			socket.CloseLink();// 应答完成，关闭资源
			return null;
		}
	}

	/**
	 * 申请登录，用于人工充值，使用长连接
	 * @param context
	 * @param map
	 * @param handler
	 * @param socket
	 * @return 登录正确返回true， 登录失败false
	 */
	public static boolean RequestLogin(Context context,
			HashMap<String, String> map, Handler handler, SocketClientCls socket)
	{
		// head
		// 命 令 4 AN 8072
		// cityCode 4 N 城市代码 4350 黄石
		// TradeCode 4 N 行业代码
		// dataLen 2 H 数据长度(不包括sign)
		// data
		// IMEI 15 N 终端唯一标识（全球唯一）
		// UserNameLen 2 N 用户名长度
		// UserName N N 用户名
		// password 32 H 用户密码 MD5
		// 首先检查网络
		if (!NetCon_Util.isNetConnect(context))
			return false;

		String Head = "8072" + CITY_CODE + BUSINESS_CODE;
		String usernameLen = Integer.toHexString(map.get(PayParams.USERNAME)
				.length());
		String password = map.get(PayParams.PASSWROD) + PRIVACY_KEY;
		try
		{
			password = CryptTool.md5Digest(password);
			System.out.println("pass md5 = " + password);

		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		String data = map.get(PayParams.IMEI_NO) + usernameLen
				+ map.get(PayParams.USERNAME) + password;

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;

		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		System.out.println("request login frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return false;
		}
		// 返回码 4 A 1900，1901
		// 1900 允许登录
		// 1901 登录失败

		String Ack = socket.ReceiveText();
		System.out.println("request login ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return false;
		}
		if (Ack.equals("1900"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 请求补登记录，
	 * 成功返回记录，没有记录或申请失败返回示例数据
	 * @param context
	 * @param map
	 * @param handler
	 */
	public static List<Map<String, String>> RequestThirdPartyLog(Context context, 
			HashMap<String, String> map, Handler handler)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		list = ThirdPartyLog(context, map, handler);
	
		if(list.isEmpty())
		{
			//示例数据
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put(ORDERNO, context.getString(R.string.sample_order));
			map1.put(PUBLISHCARDNO, context.getString(R.string.PulbishCardNO_record));
			map1.put(ORDER_AMOUNT, context.getString(R.string.sample_money));
			list.add(map1);
		}
		
		return list;
	}
	
	/**
	 * 请求补登记录，
	 * 成功返回记录，没有记录或申请失败返回空list
	 * @param context
	 * @param map
	 * @param handler
	 * @return
	 */
	private static List<Map<String, String>> ThirdPartyLog(Context context, 
			HashMap<String, String> map, Handler handler)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if (!NetCon_Util.isNetConnect(context))
			return list;
		
		SocketClientCls socket = new SocketClientCls();
		
		String Head = "6200" + CITY_CODE + BUSINESS_CODE;
		
		String publishCardNO = map.get(PayParams.CARD_NUMBER);
		String data = publishCardNO;

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;

		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return list;
		}
		System.out.println("request login frame : " + Head + data + mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
			msg.what = CONNECT_ERROR;
			handler.sendMessage(msg);

			return list;
		}
		
		String Ack = socket.ReceiveText();
		System.out.println("request thirdparty log ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return list;
		}
		String Ackcode = Ack.substring(0, 4);
//		4	A	6300
		if(Ackcode.equals("6300"))
		{
			list = ParseThirdPartyLog(context, Ack.substring(4));
		}
		socket.CloseLink();
		return list;
	}
	
	/**
	 * 解析补登记录
	 * @param context
	 * @param ack
	 * @return
	 */
	private static List<Map<String, String>> ParseThirdPartyLog(Context context, String ack)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		2	N	组数 十进制 最大5组  00:无数据
//		30	AN	订单号，不足补空格
//		16	N	发行卡号
//		8	N	补登金额 十进制 分
		int number = Integer.parseInt(ack.substring(0, 2), 10);
		
		if(0 != number)
		{	
			for(int i = 0; i < number; i++)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put(ORDERNO, ack.substring(2 + i * 54, 32 + i * 54).trim());
				map.put(PUBLISHCARDNO, ack.substring(32 + i * 54, 48 + i * 54));
				map.put(ORDER_AMOUNT, ack.substring(48 + i * 54, 56 + i * 54));
				list.add(map);
			}
		}
		return list;
	}
	
	// 测试用
	public static boolean sockettest(Context context)
	{
		if (!NetCon_Util.isNetConnect(context))
			return false;

		String Head = "8090" + CITY_CODE + BUSINESS_CODE;

		String data = "201507081503300000123456789012" + "12345678";

		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket = new SocketClientCls();
		System.out.println("mac before md5 = " + Head + data);
		try
		{
			mac = CryptTool.md5Digest(mac);
			System.out.println("mac after md5 = " + mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			System.out.println("md5 error");
			e.printStackTrace();

			return false;
		}
		System.out.println("SendCircleCompleteReport frame : " + Head + data
				+ mac);
		if (!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			System.out.println("send error");

			return false;
		}
		// 返回码 4 A 1500，1501，1502
		String Ack = socket.ReceiveText();
		System.out.println("SendCircleCompleteReport ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return false;
		}

		if (Ack.equals("1500"))
		{
			socket.CloseLink();

			return true;
		}

		return false;
	}

	/**
	 * 将输入字符串str 按要求的长度GoalLen补足，填补字符为" "
	 * @param GoalLen
	 * @param str
	 * @return
	 */
	private static final String StringToLength(int GoalLen, String str)
	{
		String goal = str;
		
		int len = goal.length();
		for(int i = 0; i < (GoalLen -len); i++)
			goal = goal + " ";
		
		return goal;
	}
}
