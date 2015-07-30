package com.example.network;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import com.example.RSA.Key;
import com.example.application.myApplication;
import com.example.bestpay.CryptTool;
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
	
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final String CIRCLE_WRITE = Global_Config.CIRCLE_WRITE;
	
	public static String Bind()
	{
		String result=null;
		
		return result;
	}
	
	/**
	 * 申请支付
	 * @param choice 充值方式
	 * @param handler
	 * @return 应答正确返回对应配置信息，错误返回null
	 *         如果发送错误，除返回null外，还发送CONNECT_ERROR message
	 */
	public static String RequestPay(Context context,HashMap<String, String> map, Handler handler)
	{
//		head:  8010
//		cityCode	4	N	城市代码 4350 黄石
//		TradeCode	4	N	行业代码 
//		dataLen	2	H	数据长度(不包括sign)
//		data:
//		Card_number	16	N	发行卡号
//		PhysicsCardNO	8	N	物理卡号
//		IMEI	15	N	终端唯一标识（全球唯一）
//		orderNo	30	AN	订单编号
//		Orderseq	30	AN	订单流水号
//		orderMount	8	N	订单金额 单位分，（至银行为浮点数 单位元）
//		OldMount	8	HEX	卡片余额 单位分
//		pubLen	2	H	公共信息长度
//		PublicData	N	H	公共信息
//		PayType	2	N	支付途径
//		01 翼支付
//		02 支付宝
//		03 建设银行
//		04 工商银行
		String result=null;
		
		//首先检查网络
		if(!NetCon_Util.isNetConnect(context))
			return result;
		
		String Head = "8010"+CITY_CODE + BUSINESS_CODE;
		String data = map.get(CARD_NUMBER) + map.get(PHYSICS_CARDNO)
					 + map.get(IMEI_NO) + map.get(ORDERNO) 
					 + map.get(ORDERSEQ) + map.get(ORDER_AMOUNT) + map.get(OLDAMOUNT)
					 + map.get(PUBLEN) + map.get(PUBLIC_DATA) + map.get(PAYTYPE);
		String len = Integer.toHexString(data.length());
		Head += len;
		
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket =new SocketClientCls();
		
		try
		{	
			mac = CryptTool.md5Digest(mac);
		} catch (Exception e)
		{
			// 出错关闭socket资源
			socket.CloseLink();
			Log.d(TAG, "md5 计算出错");
			e.printStackTrace();
			result = null;
    		return result;
		}
		System.out.println("request pay frame : "+Head+data+mac);
		if(!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
    		msg.what = CONNECT_ERROR;
    		handler.sendMessage(msg);
			result = null;
    		return result;
		}
//		应答码	4	AN	1200
//		或（1201,1202,1203,1204,1205,1206）详情见附录1
//		终端号	12	N	仅当应答码等于1200时有此域
//		支付响应地址	N	N	仅当应答码等于1200时http://111.4.120.231:9000/NetServer/ResponseUrl
		String Ack = socket.ReceiveText();
		System.out.println("request pay ack = "+Ack);
		if( (Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			result = null;
    		return result;
		}
		String AckCode = Ack.substring(0, 4);//去前四位错误码
		
		if(AckCode.equals("1200"))
		{	
			if((Ack.length() < "1200123456789012http://192.168.100.237:8081/NetServer/Response".length()))
			{
				System.out.println("应答错误，长度出错");
				socket.CloseLink();
				result = null;
				return result;
			}
			
			socket.CloseLink();
			
			result = Ack;
		}
		else {
			result = null;
			return result;
		}
		System.out.println("request pay result = " + result);
		return result;
	}
	
	
	/**
	 * 请求圈存帧拼接
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return 成功返回指令，否则返回null
	 */
	public static String RequestCircle(Context context, myApplication myApp, Handler handler)
	{

//		orderNo	30	AN	订单编号
//		Orderseq	30	AN	订单流水号
//		PublicData	74	H	公共信息(可补全F)
//		Card_number	16	N	发行卡号
//		orderMount	8	N	订单金额 单位分
//		data8050	32	N	卡片8050返回信息
		HashMap<String, String> map = new HashMap<String, String>();
		String publishData = "";
		for(int i = 0; i < 74; i++)
			publishData += "F";
		//订单金额
		String orderAmount = ""+myApp.getChargeMoney();
		orderAmount = Util.to8String(orderAmount);
		
		map.put(PayParams.ORDERNO, myApp.getOrderNO());
		map.put(PayParams.ORDERSEQ, myApp.getOrderSeq());
		map.put(PayParams.PUBLIC_DATA, publishData);
		map.put(PayParams.CARD_NUMBER, myApp.getPublishCardNO());
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.CIRCLEINIT_DATA, myApp.getCircleInitMsg());
		
		
		//调用请求圈存指令
		return (RequestCirleCmd(context, map, handler));
	}
	
	public static String RequestCirleCmd(Context context,HashMap<String, String> map, Handler handler)
	{
//		Head
//		命   令	4	AN	8002
//		cityCode	4	N	城市代码 4350 黄石
//		TradeCode	4	N	行业代码 
//		dataLen	2	H	数据长度(不包括sign)
//		data
//		orderNo	30	AN	订单编号
//		Orderseq	30	AN	订单流水号
//		PublicData	74	H	公共信息(可补全F)
//		Card_number	16	N	发行卡号
//		orderMount	8	N	订单金额 单位分
//		data8050	32	N	卡片8050返回信息
//		SIGN	32	H	MD5 校验（）
		
		//首先检查网络
		if(!NetCon_Util.isNetConnect(context)) 	return null;
		
		String Head = "8002" + CITY_CODE + BUSINESS_CODE;
		
		String data = map.get(PayParams.ORDERNO) + map.get(PayParams.ORDERSEQ) + 
					  map.get(PayParams.PUBLIC_DATA) + map.get(PayParams.CARD_NUMBER) + 
					  map.get(PayParams.ORDER_AMOUNT) + map.get(PayParams.CIRCLEINIT_DATA);
		
		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		
		SocketClientCls socket =new SocketClientCls();
		
		System.out.println("mac before md5 = " + Head+data);
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
		System.out.println("request circle cmd frame : "+Head+data+mac);
		if(!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
    		msg.what = CONNECT_ERROR;
    		handler.sendMessage(msg);

    		return null;
		}
		
//		返回码	4	A	1300，1301，1302，1303,1304,1305,1306,1307
//		8052写卡数据	42	H	RSA加密（需解密保存），仅当返回代码为1300时，有此域，
//		随机数(10)+datetime(14)+随机数(10)+MAC2(8)
		String Ack = socket.ReceiveText();
		System.out.println("request circle cmd ack = "+Ack);
		if( (Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			
    		return null;
		}

		String AckCode = Ack.substring(0, 4);//去前四位错误码
		if(AckCode.endsWith("1300"))
		{
			String ackdata = Ack.substring(4);
			String cirleCmd = Decrpyt(ackdata);
			
			System.out.println("circle cmd = " + cirleCmd);
			return cirleCmd;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 数据解密
	 * @param str
	 * @return
	 */
	private static String Decrpyt(String str)
	{
		//数据解密
		RSAPublicKey pubkey = Key.getPublicKey();
		System.out.println("PublicModulus:"+pubkey.getModulus().toString(16));
		
		String real_data = RSAUtils568.decryptDataStr(str, pubkey);
		String date = real_data.substring(10, 24);
		String mac2 = real_data.substring(34, 42);
		String Tranferance = CIRCLE_WRITE + date + mac2;
		
		return Tranferance;
	}
	
	
	/**
	 * 发送圈存完成报告，发送成功返回true，否则返回false
	 * @param context
	 * @param map
	 * @param handler
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean SendCircleCompleteReport(Context context,HashMap<String, String> map, Handler handler)
	{
//		head	命   令	4	AN	8090
//		cityCode	4	N	城市代码 4350 黄石
//		TradeCode	4	N	行业代码 
//		dataLen	2	H	数据长度(不包括sign)
//		data	Orderseq	30	AN	订单流水号
//		TAC	8	AN	8052应答
		//首先检查网络
		if(!NetCon_Util.isNetConnect(context)) 	return false;
				
		String Head = "8090" + CITY_CODE + BUSINESS_CODE;
		
		String data = map.get(PayParams.ORDERSEQ) + map.get(PayParams.TAC_CARD);
		
		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket =new SocketClientCls();
		System.out.println("mac before md5 = " + Head+data);
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
		System.out.println("SendCircleCompleteReport frame : "+Head+data+mac);
		if(!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			Message msg = new Message();
    		msg.what = CONNECT_ERROR;
    		handler.sendMessage(msg);

    		return false;
		}
		//返回码	4	A	1500，1501，1502
		String Ack = socket.ReceiveText();
		System.out.println("SendCircleCompleteReport ack = " + Ack);
		if( (Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			
    		return false;
		}
		
		if(Ack.equals("1500"))
		{
			socket.CloseLink();
			
    		return true;
		}

		return false;
	}
	
	//测试用
	public static boolean sockettest(Context context)
	{
		if(!NetCon_Util.isNetConnect(context)) 	return false;
		
		String Head = "8090" + CITY_CODE + BUSINESS_CODE;
		
		String data = "201507081503300000123456789012" + "12345678";
		
		String len = Integer.toHexString(data.length());
		Head += len;
		String mac = Head + data + PRIVACY_KEY;
		SocketClientCls socket =new SocketClientCls();
		System.out.println("mac before md5 = " + Head+data);
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
		System.out.println("SendCircleCompleteReport frame : "+Head+data+mac);
		if(!socket.SendText(Head + data + mac))
		{
			socket.CloseLink();
			System.out.println("send error");

    		return false;
		}
		//返回码	4	A	1500，1501，1502
		String Ack = socket.ReceiveText();
		System.out.println("SendCircleCompleteReport ack = " + Ack);
		if( (Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			
    		return false;
		}
		
		if(Ack.equals("1500"))
		{
			socket.CloseLink();
			
    		return true;
		}

		return false;
	}
}
