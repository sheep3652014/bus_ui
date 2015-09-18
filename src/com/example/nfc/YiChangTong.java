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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.array;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.example.config.Global_Config;
import com.example.network.PayParams;
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
	//Ȧ���ʼ������
	private final static String CIRCLE_INIT = Global_Config.CIRCLE_INIT;
	public final static byte VERIFY_Lc = (byte)0x03;
	public final static byte[] VERIFY_KEY = {(byte)0x12,(byte)0x34,(byte)0x56};
	private final static String GET_RANDOM = Global_Config.GET_RANDOM;
	
	private static final String REQUEST_CIRCLE_ACK = Global_Config.REQUEST_CIRCLE_ACK;//����Ȧ��ָ���Ӧ��
	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	
	private static final int CIRCLE_START_MSG = Global_Config.INNER_MSG_START + 1;
	private static final int GOT_THIRDPARTYPAY_MSG = Global_Config.INNER_MSG_START + 2;
	private static final int THIRDPARTYPAY_REQUEST_CMD_FIAL_MSG = Global_Config.INNER_MSG_START + 3;
	private static final int THIRDPARTYPAY_CIRCLE_FAIL_MSG = Global_Config.INNER_MSG_START + 4;
	private static final int THIRDPARTYPAY_CIRCLE_SUCCESS_MSG = Global_Config.INNER_MSG_START + 5;
	private static final int THIRDPARTYPAY_REQUEST_CMD_SUCCESS_MSG = Global_Config.INNER_MSG_START + 6;
	private static final int GOT_THIRDPARTYPAY_FAIL_MSG = Global_Config.INNER_MSG_START + 7;
	private static final int GOT_TAG_SHOW_NOTICE = Global_Config.INNER_MSG_START + 8;
	private static final int GOT_THIRDPARTYPAY_NETLOG_BALANCE = Global_Config.INNER_MSG_START + 9;
	private static final int GOT_THIRDPARTYPAY_NETLOG = Global_Config.INNER_MSG_START + 10;
	
	private static final String NFC_TAG = Global_Config.NFC_TAG;
	private static final String THIRDPARTYPAY_LIST = Global_Config.THIRDPARTYPAY_LIST;
	private static final String THIRDPARTYPAY_NETLOG = Global_Config.THIRDPARTYPAY_NETLOG;
	
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
			System.out.println("circle step = " + myApp.getCircleStep());
			switch (myApp.getCircleStep())
			{
				case PbocCard.INIT_STEP:
				default:
					System.out.println("here in init_step");
					// ��ʼ��
					ret = getInit(tag, res, myApp);
					tag.close();
					break;

			
				case PbocCard.CIRCLE_INIT_STEP:
					System.out.println("-" + myApp.getCircleStep() + "_"
							+ PbocCard.CIRCLE_INIT_STEP);

					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							YiChangTong ret = new YiChangTong(tag, res);
							 //Ȧ���ʼ�� 
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
					
				case PbocCard.THIRDPARTY_PAY_INIT:
					ThirdPartyPay(context, tag, res, myApp, handler);
					break;
				case PbocCard.THIRDPARTY_PAY_NETRECORD://���߲��Ǽ�¼
					ThirdPartyPay_NetLog(tag, res, myApp, handler, context);
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
			//��ʼ����ȡ ���  + ���Ѽ�¼ + ������Ϣ
			if (!CASH.isOkey())
				CASH = tag.getBalance(true);
					
			//ȡ�����
			if( (INFO = tag.TransRawByte(Util.hexStringToBytes(GET_RANDOM))).isOkey() ) 
			{
				random = Util.toHexString(INFO.getBytes(), 0, INFO.getBytes().length-2);
			}
			else
				random = null;
			/*--------------------------------------------------------------*/
			// read log file, record (24)
			/*--------------------------------------------------------------*/
			//��ȡ��¼ǰ��������У��
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
			
			/*//��ȡ���п�ѡ��ֵ����Ӧ��Ȧ���ʼ����Ϣ
			 * �η��� ����8050 8052 �м���ڿ�Ƭ�ƶ���д�����ܳɹ�������
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
					initMsgs[i] = Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length);//���ص������Ѿ�ȥ����9000
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
	 * ��ȡȦ���ʼ����Ϣ
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
			//cmd + ��� + �ն˺�(�ɱ�)
			//   	�����8λ16�������֣���λ���֡���1��Ǯ����00000001��
			String hex = Integer.toHexString(myApp.getChargeMoney());
			hex = Util.to8String(hex);
			String circleInitCmd = CIRCLE_INIT + hex + myApp.getTerminalCode(); 
			//String circleInitCmd = "805000020B01" + hex + myApp.getTerminalCode(); 
			if((CircleInit = tag.TransRawByte(Util.hexStringToBytes(circleInitCmd)) ).isOkey())
			{
				final YiChangTong ret = new YiChangTong(tag, res);
				
				//if( (CASH = tag.getBalance(true)).isOkey() )//��ȡȦ�����
				{
					String initMsg = Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length);
					System.out.println("initmsg = " + initMsg);
					ret.bean.setCircleInitMsg(initMsg);
					
					/* mi3 ��Ȧ���ʼ�����ȡ���ɵ��¶��Ȧ����ܳɹ�Ȧ��
					 * if( (CASH = tag.getBalance(true)).isOkey() )
					{
						System.out.println("after init = " + Util.bytesToString(CASH.getBytes()));
					}*/
					//myApp.setCircleInitMsg(initMsg);
					return ret;
				}
				
			}
			
		}
		return null;
	}

	
	/**
	 * Ȧ��д
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
				//�ȽϽ��ȷ���Ƿ�Ȧ�����
				ret.bean.setAfterChargeMoney(after);
				ret.bean.setTAC_CARD(Util.bytesToString(Circle.getBytes()));//TAC code
				
				return ret;
			}
		}
		
		return null;
	}
	
	/**
	 * activity ���ã�����msg������Ȧ��д������
	 * @param tag
	 * @param res
	 * @param msg
	 * @param myApp
	 * @return Ȧ��ɹ�����true ���򷵻�false
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
	
	/**
	 * ��������
	 * ���� GOT_THIRDPARTYPAY_MSG ��Ϣ �ں�Iso7816.Tag, �͵�ǰ��Ƭ�µĲ�������list
	 * @param context
	 * @param tag
	 * @param res
	 * @param myApp 
	 * @param handler
	 * @return 
	 */
	final static void ThirdPartyPay(final Context context,final Iso7816.Tag tag, final Resources res, 
			final myApplication myApp, final Handler handler)
	{
		YiChangTong ret = new YiChangTong(tag, res);
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				
				YiChangTong ret1 = new YiChangTong(tag, res);
				System.out.println("here in init_step");
				// ��ʼ��
				ret1 = getInit(tag, res, myApp);
				Message msg = null;//handler.obtainMessage();
				
				GotTagNotice(msg, handler);//���ͷ��ֿ�Ƭ��Ϣ����ʾ�ȴ�����
				
				myApp.setBeforeChargeMoney(ret1.bean.getBeforeChargeMoney());
				myApp.setPublicMsg(ret1.bean.getPublishMsg());
				myApp.setPublishCardNO(ret1.bean.getPublishCardNO());
				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
				list = (ArrayList<Map<String, String>>) getThirdPartyOrder(ret1, myApp, context, handler);
				
//				if(list.isEmpty())
//				{
//					//��ȡ��������ʧ�ܣ�����û�в�������,����ʾ������
//					HashMap<String, String> map = new HashMap<String, String>();
//					
//					map.put(ORDER_AMOUNT, ""+100);
//					map.put(ORDERNO, "12345667890123456"); //����ֻ�ж����ţ�û�ж�����ˮ�ţ����Դ˴��������š�������ˮ����Ϊ������ͬ
//					map.put(ORDERSEQ, "12345667890123456");
//					map.put(ORDER_DATE, "20150816");
//					map.put(BANKCARDNO, "0000000000000000000");
//					map.put(PUBLISHCARDNO, "1234567890123456");
//					list.add(map);
//				}
			    
				if(list.isEmpty())
				{
					msg = handler.obtainMessage();
					msg.what = GOT_THIRDPARTYPAY_FAIL_MSG;
					handler.sendMessage(msg);
				}
				else {
					msg = handler.obtainMessage();
					msg.what = GOT_THIRDPARTYPAY_MSG;
					Bundle bundle = new Bundle();
					bundle.putParcelable(NFC_TAG, tag.getIsoDep_Tag().getTag());
					bundle.putSerializable(THIRDPARTYPAY_LIST, list);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
				Looper.loop();
			}
		}).start();
		
		//return ret;
	}
	
	/**
	 * ���ֿ���UI�߳���ʾ�ȴ���ʾ����
	 * @param msg
	 * @param handler
	 */
	private final static void GotTagNotice(Message msg, Handler handler)
	{
		synchronized (handler)
		{
			msg = handler.obtainMessage();
			msg.what = GOT_TAG_SHOW_NOTICE;
			handler.sendMessage(msg);
			
			try
			{
				handler.wait();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				System.out.println("wait error = " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	/**
	 * ������Ϣ����֡ƴ��
	 * @param ret
	 * @param myApp
	 * @param context
	 * @param handler
	 * @return ����ɹ�����ָ�����п����µĲ�������list��ʧ�ܷ���list.isEmpty
	 */
	final static List<Map<String, String>> getThirdPartyOrder(YiChangTong ret, myApplication myApp,
			Context context, Handler handler)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		int m = ret.bean.getBeforeChargeMoney();
		String m1 = Integer.toHexString(m);
		String money = Util.to8String(m1);
		String publicMsg = ret.bean.getPublishMsg();
		String publicNO = ret.bean.getPublishCardNO();
		String physicCardNO = myApp.getPhysicsCardNO();
		String IMEI = myApp.getIMEI();
		String len = Integer.toHexString(publicMsg.length());
		if(len.length() > 2) //���ȴ���255��ֱ�����0���Ƿ��������ش���
			len = "00";
		//		Card_number	16	N	���п���
		//		PhysicsCardNO	8	N	������
		//		IMEI	15	N	IMEI
		//		OldMount	8	HEX	��Ƭ��� ��λ��
		//		pubLen	2	H	������Ϣ����
		//		PublicData	N	H	������Ϣ
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(PUBLISHCARDNO, publicNO); 
		map.put(PHYSICS_CARDNO,physicCardNO); 
		map.put(IMEI_NO,IMEI);
	    map.put(OLDAMOUNT,money); 
	    map.put(PUBLEN,len); 
	    map.put(PUBLIC_DATA,publicMsg);
		
		return (protocol.RequestThridPartyPayOrder(context, map, handler));
	}
	
	/**
	 * ����Ȧ��
	 * ����Ȧ���ʼ��������Ȧ��ָ�Ȧ��
	 * @param tag
	 * @param res
	 * @param myApp
	 * @param handler
	 * @param context
	 */
	final static public void ThirdPartyPay_Circle(final Iso7816.Tag tag, final Resources res, 
			final myApplication myApp, final Handler handler, final Context context)
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				YiChangTong ret = new YiChangTong(tag, res);
				 //Ȧ���ʼ�� 
				ret = getCircleInit(tag, res, myApp);
				
				myApp.setCircleInitMsg(ret.bean.getCircleInitMsg());
				
				// TODO Auto-generated method stub
				//String Ack = protocol.RequestCircle(context, myApp, handler);
				
				String Ack = protocol.RequestThirdParyPayCircle(context, myApp, handler);
				
				Message msg = null;//handler.obtainMessage();
				Bundle bundle = new Bundle();
				if(null != Ack)
				{	
					synchronized (handler)
					{
						msg = handler.obtainMessage();
						msg.what = THIRDPARTYPAY_REQUEST_CMD_SUCCESS_MSG;
						handler.sendMessage(msg);
						try
						{
							handler.wait();//�ȴ�aty_ThirdParyPay_Circle����
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("wait error = " + e.getMessage());
							//tag.close();
						}
					}
					
					myApp.setBigCard_CircleCmd(Ack);
					//��ʼȦ��
					ret = Circle(tag, res, myApp);
					if(null != ret)
					{
						//Ȧ��ɹ�
						System.out.println("before money = " + ret.bean.getBeforeChargeMoney());
						System.out.println("tac = "+ret.bean.getTAC_CARD());
						System.out.println("after money = " + ret.bean.getAfterChargeMoney());
						myApp.setBeforeChargeMoney(ret.bean.getBeforeChargeMoney());
						myApp.setAfterChargeMoney(ret.bean.getAfterChargeMoney());
						myApp.setTAC_CARD(ret.bean.getTAC_CARD());
						
						//myApp.setCircleStep(INIT_STEP);
						//�ر���Դ 
						tag.close();
						msg = handler.obtainMessage();
						msg.what = THIRDPARTYPAY_CIRCLE_SUCCESS_MSG;
						handler.sendMessage(msg);
					}
					else
					{
						//Ȧ��ʧ��
						System.out.println("thirdpartypay_circle fail");
						myApp.setCircleStep(INIT_STEP);
						//�ر���Դ 
						tag.close();
						msg = handler.obtainMessage();
						msg.what = THIRDPARTYPAY_CIRCLE_FAIL_MSG;
						handler.sendMessage(msg);
					}
				}
				else {
					//����Ȧ��ʧ��
					System.out.println("request circle cmd fail");
					//myApp.setCircleStep(INIT_STEP);
					//�ر���Դ 
					//tag.close();
					msg = handler.obtainMessage();
					msg.what = THIRDPARTYPAY_REQUEST_CMD_FIAL_MSG;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	/**
	 * ��ѯ���߲��Ǽ�¼
	 * @param tag
	 * @param res
	 * @param myApp
	 * @param handler
	 * @param context
	 */
	private static void ThirdPartyPay_NetLog(final Iso7816.Tag tag, final Resources res, 
			final myApplication myApp, final Handler handler, final Context context)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Looper.prepare();
				
				YiChangTong ret = new YiChangTong(tag, res);
				// ��ʼ��
				ret = getInit(tag, res, myApp);
				
				ArrayList<Map<String, String>> listLog = (ArrayList<Map<String, String>>) ret.bean.getListLog();
				String publishCardNO = ret.bean.getPublishCardNO();
				int balance = ret.bean.getBeforeChargeMoney();
				Message msg = null;//handler.obtainMessage();
				
				synchronized (handler)
				{
					msg = handler.obtainMessage();
					msg.what = GOT_THIRDPARTYPAY_NETLOG_BALANCE;
					msg.arg1 = balance;
					Bundle data = new Bundle();
					data.putSerializable(PbocCard.LOGLIST, listLog);
					msg.setData(data);
					handler.sendMessage(msg);
					try
					{
						handler.wait();
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						System.out.println("handler wait error " + e.getMessage());
						e.printStackTrace();
					}
				}
				ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(PayParams.CARD_NUMBER, publishCardNO);
				list = (ArrayList<Map<String,String>>)protocol.RequestThirdPartyLog(context, map, handler);
				
				tag.close();
				msg = handler.obtainMessage();
				msg.what = GOT_THIRDPARTYPAY_NETLOG;
				Bundle data = new Bundle();
				data.putSerializable(THIRDPARTYPAY_NETLOG, list);
				msg.setData(data);
				handler.sendMessage(msg);
				
				Looper.loop();
			}
		}).start();
		
	}
	
	
}
