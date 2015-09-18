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
	 * 解析小卡消费记录
	 * @param context Activity
	 * @param record 单条消费记录
	 * @return Map<String, String> 消费记录
	 *  Map key :  CONSUME_TYPE, CONSUME_TIME, CONSUME_MONEY, CONSUME_TERMINAL
	 */
	public static Map<String, String> PraseRecord(Context context, String record)
	{
		Map<String, String> map = new HashMap<String, String>();
		
			//HEX 0-1 联机或脱机交易号
			//    2-4透支金额
			//	  5-8交易金额
			//    9-9交易类型 0X02:充值  0x06:消费 0x09复合消费
			//    10-15交易终端编码（可为认证SAM卡号）
			//BCD 16-27终端交易时间(YYYYMMDDHHMMSS)
			//交易金额
			String m = record.substring(10, 18);
			int m1 = Integer.decode("0x"+m);
			System.out.println("-->"+m1);
			//设置小数点后2位精度，四舍五入
			float money = new BigDecimal((float)m1/100.0).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			//交易类型
			String type = record.substring(18, 20);
			//交易终端
			String terminal = record.substring(20, 32);
			//交易时间
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
			//类型和日期不为0 这认为不是空数据
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
	 * 公共信息解析， 保存发行卡号，新旧卡标识
	 * @param myApp
	 * @param Msg
	 */
	public static void PrasePublicMsg(myApplication myApp, String Msg)
	{
		//发行卡号 24, 40
		String publishCardNO = Msg.substring(24,40);
		myApp.setSwp_PublishCardNO(publishCardNO);
		//新旧卡标识(16, 18);//启用标志,是否为新卡 00:新卡 01:旧卡
		String NewCard = Msg.substring(16, 18);
		try
		{
			myApp.setSwp_isNewCard(NewCard);
		} catch (CustomTypeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("新旧卡解析出错！" + e.getMessage());
		}
	}
}
