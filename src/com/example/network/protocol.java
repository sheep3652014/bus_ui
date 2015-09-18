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
	 * ����֧��
	 * 
	 * @param choice
	 *            ��ֵ��ʽ
	 * @param handler
	 * @return Ӧ����ȷ���ض�Ӧ������Ϣ�����󷵻�null ������ʹ��󣬳�����null�⣬������CONNECT_ERROR message
	 */
	public static String RequestPay(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head: 8010
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data:
		// Card_number 16 N ���п���
		// PhysicsCardNO 8 N ������
		// IMEI 15 N �ն�Ψһ��ʶ��ȫ��Ψһ��
		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// orderMount 8 N ������� ��λ�֣���������Ϊ������ ��λԪ��
		// OldMount 8 HEX ��Ƭ��� ��λ��
		// pubLen 2 H ������Ϣ����
		// PublicData N H ������Ϣ
		// PayType 2 N ֧��;��
		// 01 ��֧��
		// 02 ֧����
		// 03 ��������
		// 04 ��������
		String result = null;

		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// Ӧ���� 4 AN 1200
		// ��1201,1202,1203,1204,1205,1206���������¼1
		// �ն˺� 12 N ����Ӧ�������1200ʱ�д���
		// ֧����Ӧ��ַ N N
		// ����Ӧ�������1200ʱhttp://111.4.120.231:9000/NetServer/ResponseUrl
		String Ack = socket.ReceiveText();
		System.out.println("request pay ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();
			result = null;
			return result;
		}
		String AckCode = Ack.substring(0, 4);// ȥǰ��λ������

		if (AckCode.equals("1200"))
		{
			if ((Ack.length() < "1200123456789012http://192.168.100.237:8081/NetServer/Response"
					.length()))
			{
				System.out.println("Ӧ����󣬳��ȳ���");
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
		socket.CloseLink();// Ӧ����ɣ��ر���Դ
		return result;
	}

	/**
	 * ȷ�϶���֧��״̬ ֡ƴ��
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return true ֧���ɹ� false ֧��ʧ��
	 */
	public static boolean CheckPayStatus(Context context, myApplication myApp,
			Handler handler)
	{
		// orderNo 30 AN �������
		// OrderSeq 30 AN ������ˮ��
		// OrderAmount 8 AN ������� ʮ���� ��λ����
		// PayMethod 2 N ֧��;��
		// 01 ��֧��
		// 02 ֧����
		// 03 ��������
		// 04 ��������
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
	 * ȷ�϶���֧��״̬ �����
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return
	 */
	private static boolean CheckPayStatusCmd(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head �� �� 4 AN 8020
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// orderNo 30 AN �������
		// OrderSeq 30 AN ������ˮ��
		// OrderAmount 8 AN ������� ʮ���� ��λ����
		// PayMethod 2 N ֧��;��
		// 01 ��֧��
		// 02 ֧����
		// 03 ��������
		// 04 ��������
		// 05
		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// Ӧ��
		// 812100 �ɹ� 812101ʧ��
		String Ack = socket.ReceiveText();
		System.out.println("check paystatus cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 6))
		{
			socket.CloseLink();

			return false;
		}

		if (Ack.equals("812100"))
		{
			socket.CloseLink();// Ӧ����ɣ��ر���Դ
			return true;
		}

		socket.CloseLink();// Ӧ����ɣ��ر���Դ
		return false;

	}

	/**
	 * ����Ȧ��֡ƴ��
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return �ɹ�����ָ����򷵻�null
	 */
	public static String RequestCircle(Context context, myApplication myApp,
			Handler handler)
	{

		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// PublicData 74 H ������Ϣ(�ɲ�ȫF)
		// Card_number 16 N ���п���
		// orderMount 8 N ������� ��λ��
		// data8050 32 N ��Ƭ8050������Ϣ
		HashMap<String, String> map = new HashMap<String, String>();
		String publicData = "";
		for (int i = 0; i < 74; i++)
			publicData += "F";
		// �������
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

		// ��������Ȧ��ָ��
		return (RequestCirleCmd(context, map, handler));
	}

	public static String RequestCirleCmd(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// Head
		// �� �� 4 AN 8002
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// PublicData 74 H ������Ϣ(�ɲ�ȫF)
		// Card_number 16 N ���п���
		// orderMount 8 N ������� ��λ��
		// data8050 32 N ��Ƭ8050������Ϣ
		// SIGN 32 H MD5 У�飨��

		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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

		// ������ 4 A 1300��1301��1302��1303,1304,1305,1306,1307
		// 8052д������ 42 H RSA���ܣ�����ܱ��棩���������ش���Ϊ1300ʱ���д���
		// �����(10)+datetime(14)+�����(10)+MAC2(8)
		String Ack = socket.ReceiveText();
		System.out.println("request circle cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return null;
		}

		String AckCode = Ack.substring(0, 4);// ȥǰ��λ������
		if (AckCode.endsWith("1300"))
		{
			String ackdata = Ack.substring(4);
			String cirleCmd = Decrpyt(ackdata);

			socket.CloseLink();// Ӧ����ɣ��ر���Դ
			System.out.println("circle cmd = " + cirleCmd);
			return cirleCmd;
		} else
		{
			socket.CloseLink();// Ӧ����ɣ��ر���Դ
			return null;
		}
	}

	/**
	 * ���ݽ���
	 * 
	 * @param str
	 * @return
	 */
	private static String Decrpyt(String str)
	{
		// ���ݽ���
		RSAPublicKey pubkey = Key.getPublicKey();
		System.out.println("PublicModulus:" + pubkey.getModulus().toString(16));

		String real_data = RSAUtils568.decryptDataStr(str, pubkey);
		String date = real_data.substring(10, 24);
		String mac2 = real_data.substring(34, 42);
		String Tranferance = CIRCLE_WRITE + date + mac2;

		return Tranferance;
	}

	/**
	 * ����Ȧ����ɱ��棬���ͳɹ�����true�����򷵻�false
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return ���ͳɹ�����true�����򷵻�false
	 */
	public static boolean SendCircleCompleteReport(Context context,
			HashMap<String, String> map, Handler handler)
	{
		// head �� �� 4 AN 8090
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data Orderseq 30 AN ������ˮ��
		// TAC 8 AN 8052Ӧ��
		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// ������ 4 A 1500��1501��1502
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
		socket.CloseLink();// Ӧ����ɣ��ر���Դ
		return false;
	}

	/**
	 * �˵���������ɹ����� true ����false
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return ����ɹ����� true ����false
	 */
	public static boolean Refund(Context context, HashMap<String, String> map,
			Handler handler)
	{
		// head �� �� 4 AN 8080
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// orderNO 30 AN ԭ������
		// orderSeq 30 AN ԭ������ˮ��
		// orderAmount 8 N ԭ������� ʮ���� ��
		// phoneNO 11 N �ֻ���

		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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

		// ������ 4 A 1600��1601��1602
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
		socket.CloseLink();// Ӧ����ɣ��ر���Դ
		return false;
	}

	/**
	 * ���Ƕ�����ѯ�� ���������֧��������Ϣ����Ҫ�ȶ�������ȡ���п��š������š�������Ϣ����Ƭ���
	 * ĳЩ֧����ʽ����ֻ�ж����ţ�û�ж�����ˮ�ţ����Դ˴��������š�������ˮ����Ϊ����Ϊ��ͬ
	 * 
	 * @param context
	 * @param map
	 * @param handler
	 * @return �������ɹ�֧����������Ա�����֧��������û��֧���ɹ��Ķ����򷵻ؿ�list
	 */
	public static List<Map<String, String>> RequestThridPartyPayOrder(
			Context context, HashMap<String, String> map, Handler handler)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// �� �� 4 AN 8081
		// head
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// Card_number 16 N ���п���
		// PhysicsCardNO 8 N ������
		// IMEI 15 N IMEI
		// OldMount 8 HEX ��Ƭ��� ��λ��
		// pubLen 2 H ������Ϣ����
		// PublicData N H ������Ϣ

		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// ������ 4 A 2200��2201 ��2200ʱ�����ݿ�
		// ���ݿ� 2 H ���Ǽ�¼�������255
		// 8 N orderMount ���ǽ�ʮ���� ��
		// 30 AN orderNO
		// 19 N ���п��� ���㲹�ո� �޿��� ȫ�ո�
		// 8 N ����ʱ�� yyyymmdd
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
				map1.put(ORDERNO, orderNO); // ����ֻ�ж����ţ�û�ж�����ˮ�ţ����Դ˴��������š�������ˮ����Ϊ������ͬ
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
	 * ����֡ƴ��
	 * 
	 * @param context
	 * @param myApp
	 * @param handler
	 * @return Ȧ��ָ��
	 */
	public static String RequestThirdParyPayCircle(Context context,
			myApplication myApp, Handler handler)
	{

		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// PublicData 74 H ������Ϣ(�ɲ�ȫF)
		// Card_number 16 N ���п���
		// orderMount 8 N ������� ��λ��
		// data8050 32 N ��Ƭ8050������Ϣ
		HashMap<String, String> map = new HashMap<String, String>();
		String publicData = "";
		for (int i = 0; i < 74; i++)
			publicData += "F";
		// �������
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

		// ��������Ȧ��ָ��
		return (RequestThirdPartyPayCirleCmd(context, map, handler));
	}

	/**
	 * ����Ȧ��ָ������
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
		// �� �� 4 AN 8082
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// PublicData 74 H ������Ϣ(�ɲ�ȫF)
		// Card_number 16 N ���п���
		// orderMount 8 N ������� ��λ��
		// data8050 32 N ��Ƭ8050������Ϣ
		// SIGN 32 H MD5 У�飨��

		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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

		// ������ 4 A 2300��2301��2302��2303,2304,2305,2306,2307
		// 8052д������ 42 H RSA���ܣ�����ܱ��棩���������ش���Ϊ1300ʱ���д���
		// �����(10)+datetime(14)+�����(10)+MAC2(8)
		String Ack = socket.ReceiveText();
		System.out.println("request circle cmd ack = " + Ack);
		if ((Ack.isEmpty()) || (Ack.length() < 4))
		{
			socket.CloseLink();

			return null;
		}

		String AckCode = Ack.substring(0, 4);// ȥǰ��λ������
		if (AckCode.endsWith("2300"))
		{
			String ackdata = Ack.substring(4);
			String cirleCmd = Decrpyt(ackdata);

			socket.CloseLink();// Ӧ����ɣ��ر���Դ
			System.out.println("circle cmd = " + cirleCmd);
			return cirleCmd;
		} else
		{
			socket.CloseLink();// Ӧ����ɣ��ر���Դ
			return null;
		}
	}

	/**
	 * �����¼�������˹���ֵ��ʹ�ó�����
	 * @param context
	 * @param map
	 * @param handler
	 * @param socket
	 * @return ��¼��ȷ����true�� ��¼ʧ��false
	 */
	public static boolean RequestLogin(Context context,
			HashMap<String, String> map, Handler handler, SocketClientCls socket)
	{
		// head
		// �� �� 4 AN 8072
		// cityCode 4 N ���д��� 4350 ��ʯ
		// TradeCode 4 N ��ҵ����
		// dataLen 2 H ���ݳ���(������sign)
		// data
		// IMEI 15 N �ն�Ψһ��ʶ��ȫ��Ψһ��
		// UserNameLen 2 N �û�������
		// UserName N N �û���
		// password 32 H �û����� MD5
		// ���ȼ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// ������ 4 A 1900��1901
		// 1900 �����¼
		// 1901 ��¼ʧ��

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
	 * ���󲹵Ǽ�¼��
	 * �ɹ����ؼ�¼��û�м�¼������ʧ�ܷ���ʾ������
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
			//ʾ������
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put(ORDERNO, context.getString(R.string.sample_order));
			map1.put(PUBLISHCARDNO, context.getString(R.string.PulbishCardNO_record));
			map1.put(ORDER_AMOUNT, context.getString(R.string.sample_money));
			list.add(map1);
		}
		
		return list;
	}
	
	/**
	 * ���󲹵Ǽ�¼��
	 * �ɹ����ؼ�¼��û�м�¼������ʧ�ܷ��ؿ�list
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
	 * �������Ǽ�¼
	 * @param context
	 * @param ack
	 * @return
	 */
	private static List<Map<String, String>> ParseThirdPartyLog(Context context, String ack)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		2	N	���� ʮ���� ���5��  00:������
//		30	AN	�����ţ����㲹�ո�
//		16	N	���п���
//		8	N	���ǽ�� ʮ���� ��
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
	
	// ������
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
			// ����ر�socket��Դ
			socket.CloseLink();
			Log.d(TAG, "md5 �������");
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
		// ������ 4 A 1500��1501��1502
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
	 * �������ַ���str ��Ҫ��ĳ���GoalLen���㣬��ַ�Ϊ" "
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
