package com.example.network;

import java.util.HashMap;

import com.example.application.myApplication;
import com.example.bestpay.CryptTool;
import com.example.config.Global_Config;
import com.example.network.SocketClientCls;

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
	
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	
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
//		head:
//		cityCode	4	N	城市代码 4350 黄石
//		TradeCode	4	N	行业代码 
//		dataLen	2	H	数据长度(不包括sign)
//		data:
//		Card_number	16	N	发行卡号
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
		
		String Head = CITY_CODE + BUSINESS_CODE;
		String data = map.get(CARD_NUMBER) + map.get(IMEI_NO) + map.get(ORDERNO) 
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
		}
		
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
		return result;
	}
	
	
}
