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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import android.nfc.Tag;
import android.util.Log;

import com.example.config.Global_Config;
import com.example.nfc.Iso7816;
import com.example.nfc.Util;
import com.example.application.myApplication;


final class YiChangTong extends PbocCard {
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
	
	private YiChangTong(Iso7816.Tag tag, Resources res) {
		super();
		//super(tag);
		//name = res.getString(R.string.name_wht);
	}
	
	final static YiChangTong load(Iso7816.Tag tag, Resources res, myApplication myApp) {
		
		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {

			YiChangTong ret = new YiChangTong(tag, res);
			
			switch (myApp.getCircleStep())
			{
				case PbocCard.INIT_STEP:
				default:	
					//初始化
					ret = getInit(tag, res, myApp);
					break;
				case PbocCard.CIRCLE_INIT_STEP:
					//圈存初始化
					ret = getCircleInit(tag, res, myApp);
					break;
				case PbocCard.CIRCLEING_STEP:
					//圈存
					ret = Circle(tag, res, myApp);
					break;
			}
			
			return ret; 
		}

		return null;
	}
	
	final static YiChangTong getInit(Iso7816.Tag tag, Resources res,myApplication myApp)
	{
		Iso7816.Response SERL, INFO, CASH;
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
		Iso7816.Response CircleInit, CASH;
		
		System.out.println("1--");
		/*--------------------------------------------------------------*/
		// select Main Application
		/*--------------------------------------------------------------*/
		if (tag.TransRawByte(Util.hexStringToBytes(AID)).isOkey()) {
			System.out.println("2--");
			//cmd + 金额 + 终端号(可变)
			//   	金额是8位16进制数字，单位：分。充1分钱，是00000001，
			String circleInitCmd = CIRCLE_INIT + Integer.toHexString(myApp.getChargeMoney()) + myApp.getTerminalCode(); 
			if((CircleInit = tag.TransRawByte(Util.hexStringToBytes(circleInitCmd)) ).isOkey())
			{
				final YiChangTong ret = new YiChangTong(tag, res);
				
				//if( (CASH = tag.getBalance(true)).isOkey() )//获取圈存后金额
				{
					
					String initMsg = Util.toHexString(CircleInit.getBytes(), 0, CircleInit.getBytes().length-4);

					ret.bean.setCircleInitMsg(initMsg);
					
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
	final static YiChangTong Circle(Iso7816.Tag tag, Resources res, myApplication myApp)
	{
		Iso7816.Response Circle, CASH;
		
		System.out.println("--1");
		/*--------------------------------------------------------------*/
		// select Main Application
		/*--------------------------------------------------------------*/
		if (tag.TransRawByte(Util.hexStringToBytes(AID)).isOkey()) {
			System.out.println("--2");
			
			String cmd = myApp.getBigCard_CircleCmd();
			if((Circle = tag.TransRawByte(Util.hexStringToBytes(cmd)) ).isOkey())
			{
				final YiChangTong ret = new YiChangTong(tag, res);
				
				CASH = tag.getBalance(true);
				int after = Util.toInt(CASH.getBytes(), 0, 4);
				if (after > 100000 || after < -100000)
					after -= 0x80000000;
				//比较金额确认是否圈存完成
				ret.bean.setAfterChargeMoney(after);
				
				return ret;
			}
		}
		
		return null;
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
