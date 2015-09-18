package com.tybus.swp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.example.application.myApplication;
import com.example.bus_ui_demo.R;
import com.example.config.Global_Config;
import com.example.database.CustomTypeException;

public class swp_dataPrase
{
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	private static final String CONSUME_TERMINAL = Global_Config.CONSUME_TERMINAL;
	
	/**
	 * ����С�����Ѽ�¼
	 * @param context Activity
	 * @param record �������Ѽ�¼
	 * @return Map<String, String> ���Ѽ�¼
	 *  Map key :  CONSUME_TYPE, CONSUME_TIME, CONSUME_MONEY, CONSUME_TERMINAL
	 */
	public static Map<String, String> PraseRecord(Context context, String record)
	{
		Map<String, String> map = new HashMap<String, String>();
		
			//HEX 0-1 �������ѻ����׺�
			//    2-4͸֧���
			//	  5-8���׽��
			//    9-9�������� 0X02:��ֵ  0x06:���� 0x09��������
			//    10-15�����ն˱��루��Ϊ��֤SAM���ţ�
			//BCD 16-27�ն˽���ʱ��(YYYYMMDDHHMMSS)
			//���׽��
			String m = record.substring(10, 18);
			int m1 = Integer.decode("0x"+m);
			System.out.println("-->"+m1);
			//����С�����2λ���ȣ���������
			float money = new BigDecimal((float)m1/100.0).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			//��������
			String type = record.substring(18, 20);
			//�����ն�
			String terminal = record.substring(20, 32);
			//����ʱ��
			String time = record.substring(32,46);
			String year = record.substring(32,36);
			String month = record.substring(36,38);
			String day = record.substring(38,40);
			String hour = record.substring(40,42);
			String min = record.substring(42,44);
			String second = record.substring(44,46);
			String date = year + context.getString(R.string.YEAR) + month + context.getString(R.string.MONTH) 
						 + day + context.getString(R.string.DAY) 
						 + hour + ":" + min + ":" + second;
			System.out.println("--> record " + money + "_" + type + "_" + time + "_" +terminal );
			//���ͺ����ڲ�Ϊ0 ����Ϊ���ǿ�����
			if( (!time.equals("00000000000000")) && (!type.equals("00")) )
			{
				map.put(CONSUME_TYPE, type);
				map.put(CONSUME_MONEY, ""+money + context.getString(R.string.YUAN));
				map.put(CONSUME_TERMINAL, context.getString(R.string.terminalName)+terminal);
				map.put(CONSUME_TIME, date);
			}
			else {
				map = null;
			}
			return map;
		}

	/**
	 * ������Ϣ������ ���淢�п��ţ��¾ɿ���ʶ
	 * @param myApp
	 * @param Msg
	 */
	public static void PrasePublicMsg(myApplication myApp, String Msg)
	{
		//���п��� 24, 40
		String publishCardNO = Msg.substring(24,40);
		myApp.setSwp_PublishCardNO(publishCardNO);
		//�¾ɿ���ʶ(16, 18);//���ñ�־,�Ƿ�Ϊ�¿� 00:�¿� 01:�ɿ�
		String NewCard = Msg.substring(16, 18);
		try
		{
			myApp.setSwp_isNewCard(NewCard);
		} catch (CustomTypeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�¾ɿ���������" + e.getMessage());
		}
	}
}
