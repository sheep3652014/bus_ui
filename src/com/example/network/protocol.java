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
	 * ����֧��
	 * @param choice ��ֵ��ʽ
	 * @param handler
	 * @return Ӧ����ȷ���ض�Ӧ������Ϣ�����󷵻�null
	 *         ������ʹ��󣬳�����null�⣬������CONNECT_ERROR message
	 */
	public static String RequestPay(Context context,HashMap<String, String> map, Handler handler)
	{
//		head:
//		cityCode	4	N	���д��� 4350 ��ʯ
//		TradeCode	4	N	��ҵ���� 
//		dataLen	2	H	���ݳ���(������sign)
//		data:
//		Card_number	16	N	���п���
//		IMEI	15	N	�ն�Ψһ��ʶ��ȫ��Ψһ��
//		orderNo	30	AN	�������
//		Orderseq	30	AN	������ˮ��
//		orderMount	8	N	������� ��λ�֣���������Ϊ������ ��λԪ��
//		OldMount	8	HEX	��Ƭ��� ��λ��
//		pubLen	2	H	������Ϣ����
//		PublicData	N	H	������Ϣ
//		PayType	2	N	֧��;��
//		01 ��֧��
//		02 ֧����
//		03 ��������
//		04 ��������
		String result=null;
		
		//���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
//		Ӧ����	4	AN	1200
//		��1201,1202,1203,1204,1205,1206���������¼1
//		�ն˺�	12	N	����Ӧ�������1200ʱ�д���
//		֧����Ӧ��ַ	N	N	����Ӧ�������1200ʱhttp://111.4.120.231:9000/NetServer/ResponseUrl
		String Ack = socket.ReceiveText();
		if( (Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			result = null;
    		return result;
		}
		String AckCode = Ack.substring(0, 4);//ȥǰ��λ������
		
		if(AckCode.equals("1200"))
		{	
			if((Ack.length() < "1200123456789012http://192.168.100.237:8081/NetServer/Response".length()))
			{
				System.out.println("Ӧ����󣬳��ȳ���");
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
