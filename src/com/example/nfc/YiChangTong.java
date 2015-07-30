/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.example.nfc;

import java.lang.annotation.Retention;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.example.bus_ui_demo.R;
import com.example.config.Global_Config;
import com.example.network.protocol;
import com.example.nfc.Iso7816;
import com.example.nfc.Util;
import com.example.Alert.myAlert;
import com.example.application.myApplication;


public final class YiChangTong extends PbocCard {
	private final static int SFI_INFO = 5;
	private final static int SFI_SERL = 10;
	
	private final static byte[] DFN_SRV = { (byte) 0x41, (byte) 0x50,
			(byte) 0x31, (byte) 0x2E, (byte) 0x57, (byte) 0x48, (byte) 0x43,
			(byte) 0x54, (byte) 0x43, };
	
	
	private final static int SFI_COM = 0x15;
	private final static String CITYCODE = Global_Config.CITY_CODE_YICHANG;
	private final static String AID = Global_Config.BIG_AID_YICHANG;
	//圈存初始化命令
	private final static String CIRCLE_INIT = Global_Config.CIRCLE_INIT;
	public final static byte VERIFY_Lc = (byte)0x03;
	public final static byte[] VERIFY_KEY = {(byte)0x12,(byte)0x34,(byte)0x56};
	private final static String GET_RANDOM = Global_Config.GET_RANDOM;
	
	private static final String REQUEST_CIRCLE_ACK = "ack";//申请圈存指令的应答
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	
	private static final int CIRCLE_START_MSG = Global_Config.INNER_MSG_START + 1;
	private static final String NFC_TAG = "tag";
	
	//private YiChangTong(Iso7816.Tag tag, Resources res) {
	public YiChangTong(Iso7816.Tag tag, Resources res) {
		super();
		//super(tag);
		//name = res.getString(R.string.name_wht);
	}
	
	final static YiChangTong load(final Iso7816.Tag tag, final Resources res,
			final myApplication myApp, final Context context,final Handler handler)
	{

		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey())
		{

			YiChangTong ret = new YiChangTong(tag, res);

			switch (myApp.getCircleStep())
			{
				case PbocCard.INIT_STEP:
				default:
					System.out.println("here in init_step");
					// 初始化
					ret = getInit(tag, res, myApp);
					tag.close();
					break;
/*				case PbocCard.INIT_STEP:
				default:
					System.out.println("here in init_step");
					// 初始化
					ret = getInit(tag, res, myApp);
					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							Looper.prepare();
							Message msg = handler.obtainMessage();
							msg.what = 99;
							Bundle data = new Bundle();
							Parcelable p = (Parcelable)tag.getIsoDep_Tag().getTag();
							Tag tag1 = (Tag)p;
							IsoDep iso = IsoDep.get(tag1);
							Iso7816.Tag tag2 = new Iso7816.Tag(iso);
							System.out.println("tag is " + tag.getIsoDep_Tag().getTag() +"_"+ tag2.toString());
							data.putParcelable(NFC_TAG, tag.getIsoDep_Tag().getTag());
							msg.setData(data);
							handler.sendMessage(msg);
							Looper.loop();
						}
					}).start();
					
					break;*/
			/*case PbocCard.INIT_STEP:
				default:
					System.out.println("here in init_step");
					
					final Handler handler = new Handler()
					{

						@Override
						public void handleMessage(Message msg)
						{
							// TODO Auto-generated method stub
							//super.handleMessage(msg);
							if(msg.what == 2)
							{
								YiChangTong ret = new YiChangTong(tag, res);
								System.out.println("---111");
								ret = getInit(tag, res, myApp);
								System.out.println("---111---");
								tag.close();
							}
						}
						
					};
					Thread thread = new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							YiChangTong ret1 = new YiChangTong(tag, res);
							ret1 = getInit(tag, res, myApp);
							
							boolean send = protocol.sockettest(context);
							System.out.println("send is = " +send);
							
							handler.sendEmptyMessage(2);	

						}	
					});
					//thread.setPriority(Thread.MAX_PRIORITY);
					thread.start();
					
					break;*/
				case PbocCard.CIRCLE_INIT_STEP:
					System.out.println("-" + myApp.getCircleStep() + "_"
							+ PbocCard.CIRCLE_INIT_STEP);

					/*final Handler mHandler = new Handler()
					{

						@Override
						public void handleMessage(Message msg)
						{
							// TODO Auto-generated method stub
							//super.handleMessage(msg);
							YiChangTong ret1 = new YiChangTong(tag, res);
							if(CONNECT_ERROR == msg.what)
							{
								myAlert.ShowToast(context, context.getString(R.string.network_error));
							}
							if(CIRCLE_START_MSG == msg.what)
							{
								myApp.setBigCard_CircleCmd(msg.getData().getString(REQUEST_CIRCLE_ACK));
								ret1 = Circle(tag, res, myApp);
								if(null != ret1)
								{
									System.out.println("before money = " + ret1.bean.getBeforeChargeMoney());
									System.out.println("tac = "+ret1.bean.getTAC_CARD());
									System.out.println("after money = " + ret1.bean.getAfterChargeMoney());
									myApp.setBeforeChargeMoney(ret1.bean.getBeforeChargeMoney());
									myApp.setAfterChargeMoney(ret1.bean.getAfterChargeMoney());
									myApp.setTAC_CARD(ret1.bean.getTAC_CARD());
								}
							}
							tag.close();
							super.handleMessage(msg);
						}
						
					};*/
					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							YiChangTong ret = new YiChangTong(tag, res);
							 //圈存初始化 
							ret = getCircleInit(tag, res, myApp);
							
							myApp.setCircleInitMsg(ret.bean.getCircleInitMsg());
							
							// TODO Auto-generated method stub
							String Ack = protocol.RequestCircle(context, myApp, handler);
							if(null != Ack)
							{
								Message msg = handler.obtainMessage();
								msg.what = CIRCLE_START_MSG;
								Bundle bundle = new Bundle();
								bundle.putParcelable(NFC_TAG, tag.getIsoDep_Tag().getTag());
								bundle.putString(REQUEST_CIRCLE_ACK, Ack);
								msg.setData(bundle);
								handler.sendMessage(msg);
							}
						}
					}).start();
					
					break;

			}
			
			return ret;
		}

		return null;
	}
	
	
	
	
	final static YiChangTong getInit(Iso7816.Tag tag, Resources res,myApplication myApp)
	{
		Iso7816.Response CircleInit,SERL, INFO, CASH;
		String random;
		
		CASH = tag.getBalance(true);
		
		System.out.println("1");
		/*--------------------------------------------------------------*/
		// select Main Application
		/*--------------------------------------------------------------*/
		if (tag.TransRawByte(Util.hexStringToBytes(AID)).isOkey()) {
			System.out.println("2");
			/**
			 * read 0x15 get city code , if the citycode == 4430 then is yichang card
			 */
			String comstr;
			SERL = tag.readBinary(SFI_COM);
			System.out.println("--"+Util.bytesToString(SERL.getBytes()));
			if(!SERL.isOkey())
				return null;
			else {
				comstr = Util.bytesToString(SERL.getBytes());
				String citycode = comstr.substring(4, 8);
				if(!citycode.equals(CITYCODE))
					return null;
			}
			//初始化读取 余额  + 消费记录 + 公共信息
			if (!CASH.isOkey())
				CASH = tag.getBalance(true);
					
			//取随机数
			if( (INFO = tag.TransRawByte(Util.hexStringToBytes(GET_RANDOM))).isOkey() ) 
			{
				random = Util.toHexString(INFO.getBytes(), 0, INFO.getBytes().length-2);
			}
			else
				random = null;
			/*--------------------------------------------------------------*/
			// read log file, record (24)
			/*--------------------------------------------------------------*/
			//读取记录前个人密码校验
			final Iso7816.Response rsp1 = tag.Verify(VERIFY_Lc, VERIFY_KEY);
			
			if(!rsp1.isOkey())
			{
				Log.d("tag from read record", "readRecord is not Prepare");
			}
			ArrayList<Map<String, String>> listLog = readLog(tag, SFI_LOG);
			
			/*--------------------------------------------------------------*/
			// build result string
			/*--------------------------------------------------------------*/
			final YiChangTong ret = new YiChangTong(tag, res);
			//ret.parseBalance(CASH,myApp);
			//ret.parseCommon(SERL,myApp);
			//myApp.setListLog(listLog);
			int before = Util.toInt(CASH.getBytes(), 0, 4);
			if (before > 100000 || before < -100000)
				before -= 0x80000000;
			System.out.println("--money before " + before);
			ret.bean.setBeforeChargeMoney(before);
			String publishMsg = Util.toHexString(SERL.getBytes(), 0, SERL.getBytes().length-2);
			System.out.println("-->"+publishMsg);
			ret.bean.setPublishMsg(publishMsg);
			ret.bean.setPublishCardNO(publishMsg.substring(24, 40));
			System.out.println("-->"+publishMsg.substring(24, 40));
			ret.bean.setRandom(random);
			System.out.println("-->  "+random);
			
			/*//读取所有可选充值金额对应的圈存初始化信息
			 * 次方法 由于8050 8052 中间存在卡片移动，写卡不能成功，弃用
			int[] chargemoneys = {10*100, 20*100, 50*100, 100*100, 200*100, 1};
			String[] initMsgs = new String[6];
			
			for(int i = 0; i < 6; i++)
			{
				String hex = Integer.toHexString(chargemoneys[i]);
				hex = Util.to8String(hex);
				System.out.println("hex money = "+hex);
				String circleInitCmd = CIRCLE_INIT + hex + myApp.getTerminalCode();
				if((CircleInit = tag.TransRawByte(Util.hexStringToBytes(circleInitCmd)) ).isOkey())
				{
					initMsgs[i] = Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length);//返回的数据已经去掉了9000
					System.out.println("circle init = " + i + "_" + initMsgs[i] +"_"+Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length) + "_"+CircleInit.getBytes().length);
				}
				else {
					initMsgs[i] = null;
				}
			}
			ret.bean.setCircleInitMsg(initMsgs);*/
			
			ret.bean.setListLog(listLog);
			System.out.println("-- "+listLog.toString());
			
			return ret;
		}
		return null;
	}
	
	/**
	 * 获取圈存初始化信息
	 * @param tag
	 * @param res
	 * @param myApp
	 * @return
	 */
	final static YiChangTong getCircleInit(Iso7816.Tag tag, Resources res, myApplication myApp)
	{
		Iso7816.Response CircleInit, CASH,SERL;
		
		System.out.println("1--");
		/*--------------------------------------------------------------*/
		// select Main Application
		/*--------------------------------------------------------------*/
		if (tag.TransRawByte(Util.hexStringToBytes(AID)).isOkey()) 
		{
			CASH = tag.Verify(VERIFY_Lc, VERIFY_KEY);
			SERL = tag.readBinary(SFI_COM);
			System.out.println("--"+Util.bytesToString(SERL.getBytes()) +"_"+Util.bytesToString(CASH.getBytes()));
			System.out.println("2--");
			//cmd + 金额 + 终端号(可变)
			//   	金额是8位16进制数字，单位：分。充1分钱，是00000001，
			String hex = Integer.toHexString(myApp.getChargeMoney());
			hex = Util.to8String(hex);
			String circleInitCmd = CIRCLE_INIT + hex + myApp.getTerminalCode(); 
			//String circleInitCmd = "805000020B01" + hex + myApp.getTerminalCode(); 
			if((CircleInit = tag.TransRawByte(Util.hexStringToBytes(circleInitCmd)) ).isOkey())
			{
				final YiChangTong ret = new YiChangTong(tag, res);
				
				//if( (CASH = tag.getBalance(true)).isOkey() )//获取圈存后金额
				{
					String initMsg = Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length);
					System.out.println("initmsg = " + initMsg);
					ret.bean.setCircleInitMsg(initMsg);
					
					if( (CASH = tag.getBalance(true)).isOkey() )
					{
						System.out.println("after init = " + Util.bytesToString(CASH.getBytes()));
					}
					//myApp.setCircleInitMsg(initMsg);
					return ret;
				}
				
			}
			
		}
		return null;
	}

	
	/**
	 * 圈存写
	 * @param tag
	 * @param res
	 * @param de
	 * @return
	 */
	private final static YiChangTong Circle(Iso7816.Tag tag, Resources res, myApplication myApp)
	{
		Iso7816.Response Circle, CASH;
		
		System.out.println("--1");
		/*--------------------------------------------------------------*/
		// select Main Application
		/*--------------------------------------------------------------*/
		//if (tag.TransRawByte(Util.hexStringToBytes(AID)).isOkey())
		{
			System.out.println("--2");
			
			final YiChangTong ret = new YiChangTong(tag, res);
			
			CASH = tag.getBalance(true);
			int before = Util.toInt(CASH.getBytes(), 0, 4);
			if (before > 100000 || before < -100000)
				before -= 0x80000000;
			System.out.println("before charge = " + before);
			ret.bean.setBeforeChargeMoney(before);
//			
			String cmd = myApp.getBigCard_CircleCmd();
			System.out.println("cmd = " +cmd);
			if((Circle = tag.TransRawByte(Util.hexStringToBytes(cmd)) ).isOkey())
			{
				
				CASH = tag.getBalance(true);
				int after = Util.toInt(CASH.getBytes(), 0, 4);
				if (after > 100000 || after < -100000)
					after -= 0x80000000;
				System.out.println("after charge = " + after);
				//比较金额确认是否圈存完成
				ret.bean.setAfterChargeMoney(after);
				ret.bean.setTAC_CARD(Util.bytesToString(Circle.getBytes()));//TAC code
				
				return ret;
			}
		}
		
		return null;
	}
	
	/**
	 * activity 调用，用于msg出发的圈存写卡操作
	 * @param tag
	 * @param res
	 * @param msg
	 * @param myApp
	 * @return 圈存成功返回true 否则返回false
	 */
	public static boolean CircleByActivity(Iso7816.Tag tag, Resources res, Message msg, myApplication myApp)
	{
		YiChangTong ret = new YiChangTong(tag, res);
		
		myApp.setBigCard_CircleCmd(msg.getData().getString(REQUEST_CIRCLE_ACK));
		ret = YiChangTong.Circle(tag, res, myApp);
		if(null != ret)
		{
			System.out.println("before money = " + ret.bean.getBeforeChargeMoney());
			System.out.println("tac = "+ret.bean.getTAC_CARD());
			System.out.println("after money = " + ret.bean.getAfterChargeMoney());
			myApp.setBeforeChargeMoney(ret.bean.getBeforeChargeMoney());
			myApp.setAfterChargeMoney(ret.bean.getAfterChargeMoney());
			myApp.setTAC_CARD(ret.bean.getTAC_CARD());
			return true;
		}
		return false;
	}
	/*private void parseInfo(Iso7816.Response sn, Iso7816.Response info) {
		if (sn.size() < 27 || info.size() < 27) {
			serl = version = date = count = null;
			return;
		}

		final byte[] d = info.getBytes();
		serl = Util.toHexString(sn.getBytes(), 0, 5);
		version = String.format("%02d", d[24]);
		date = String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20],
				d[21], d[22], d[23], d[16], d[17], d[18], d[19]);
		count = null;
	}*/
	
	
	
}
