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

import android.content.res.Resources;
import android.util.Log;

import com.example.config.Global_Config;
import com.example.nfc.Iso7816;
import com.example.nfc.Util;
import com.example.bus_ui_demo.R;


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
	
	private YiChangTong(Iso7816.Tag tag, Resources res) {
		super(tag);
		name = res.getString(R.string.name_wht);
	}
	
	@SuppressWarnings("unchecked")
	final static YiChangTong load(Iso7816.Tag tag, Resources res) {
		
		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {

			Iso7816.Response SERL, INFO, CASH;

			
			/*--------------------------------------------------------------*/
			// read card info file, binary (5, 10)
			/*--------------------------------------------------------------*/
//			if (!(SERL = tag.readBinary(SFI_SERL)).isOkey())
//				return null;
//			
//			if (!(INFO = tag.readBinary(SFI_INFO)).isOkey())
//				return null;

			CASH = tag.getBalance(true);
			
			//added by yh
			//tag.getMoney();
			System.out.println("1");
			/*--------------------------------------------------------------*/
			// select Main Application
			/*--------------------------------------------------------------*/
			//if (tag.selectByName(DFN_SRV).isOkey()) {
			byte[] rsp1 = tag.transceive(Util.hexStringToBytes(AID));
			String rspstr = Util.bytesToString(rsp1);
			System.out.println("--rsp " + rspstr);
			if(rspstr.contains("9000")){
			//if (tag.selectByID(Util.hexStringToBytes(AID)).isOkey()) {
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
				
				System.out.println(comstr);
				String comHead = comstr.substring(0, 72);
				String solddate = comstr.substring(72, 80);
				String comTail = comstr.substring(80);
				String mycom = comHead + "55550505" + comTail;
				byte[] rsp = tag.transceive(Util.hexStringToBytes("00d69500" + Integer.toHexString(mycom.length()) + mycom));
				System.out.println("write 15 file ack " + Util.bytesToString(rsp));
				SERL = tag.readBinary(SFI_COM);
				System.out.println("after write " + Util.bytesToString(SERL.getBytes()));
				
				/*--------------------------------------------------------------*/
				// read balance
				/*--------------------------------------------------------------*/
				if (!CASH.isOkey())
					CASH = tag.getBalance(true);
								
				/*--------------------------------------------------------------*/
				// read log file, record (24)
				/*--------------------------------------------------------------*/
				ArrayList<byte[]> LOG = readLog(tag, SFI_LOG);
				
				
				/*--------------------------------------------------------------*/
				// build result string
				/*--------------------------------------------------------------*/
				final YiChangTong ret = new YiChangTong(tag, res);
				ret.parseBalance(CASH);
				//ret.parseInfo(SERL, INFO);
				ret.parseLog(LOG);
				
				return ret;
			}
		}

		return null;
	}
	
	
	

	/**
	 * 读取余额和公共信息
	 * @param tag
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unchecked")
	final static YiChangTong loadCommon(Iso7816.Tag tag, Resources res) {
		
		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {
//		if (tag.selectByID(com.hmbst.bestpaytest.Util.hexStringToBytes(AID_HUANGSHI)).isOkey()) {
			
			Iso7816.Response SERL, INFO, CASH;

			System.out.println("--> loadCommon");
			
			//if (tag.selectByName(Util.hexStringToBytes(AID_HUANGSHI)).isOkey()) 
			if (tag.selectByName(DFN_PSE).isOkey())
			{
				
				//个人密码校验
//				final Iso7816.Response rsp1 = tag.Verify(WuhanTong.VERIFY_Lc, WuhanTong.VERIFY_KEY);
				CASH = tag.getMoney();//tag.getBalance(true);
				//float money = Util.Bytes2FloatMoney(CASH.getBytes());
				//de.setBig_Money("" + money);
				
				INFO = tag.readBinary(SFI_COM);//读公共信息
				String publicMsg = Util.bytesToString(INFO.getBytes());
				//de.setBig_PublicMsg(publicMsg);
				//de.setBig_PublishCardNO(publicMsg.substring(24, 40));//发行卡号截取
				System.out.println("pulbic msg " + publicMsg);
				//读记录
				
				Log.d("from loadtimes", "cash = "+Util.bytesToString(CASH.getBytes())+"__++++"+CASH.toString() + "___" + Util.bytesToString(INFO.getBytes()));
				ArrayList<byte[]> LOG = readLog(tag, SFI_LOG);
				
			}
	   }	
		return null;
    }
	
	/**
	 * 读取圈存初始化信息
	 * @param tag
	 * @param res
	 * @param de
	 * @return
	 */
	final static YiChangTong loadCircleInit(Iso7816.Tag tag, Resources res) {
		
		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {
//		if (tag.selectByID(com.hmbst.bestpaytest.Util.hexStringToBytes(AID_HUANGSHI)).isOkey()) {
			
			Iso7816.Response SERL, INFO, CASH , Init;

			System.out.println("-->loadCircleInit");
			
			if (tag.selectByName(Util.hexStringToBytes(AID)).isOkey()) {
				
//				805000020B01+czje+12345678   //cmd + 金额 + 终端号(可变)
//				金额是8位16进制数字，分结尾。充1分钱，是00000001，
//				String CircleInit = CIRCLE_INIT + Tools.string2HexString8(de.getCzje()) + OrderParams.getTerminal_Code();
//				
//				Init = tag.read(Util.hexStringToBytes(CircleInit));
//				if(Init.isOkey())
//				{
//					byte[] bytes = Init.getBytes();
//					de.setBigCard_CircleInit(Arrays.copyOfRange(bytes, 0, bytes.length-2));
//				}
				
				
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
	final static YiChangTong loadCircleWrite(Iso7816.Tag tag, Resources res) {
		
		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		if (tag.selectByName(DFN_PSE).isOkey()) {
//		if (tag.selectByID(com.hmbst.bestpaytest.Util.hexStringToBytes(AID_HUANGSHI)).isOkey()) {
			
			Iso7816.Response SERL, INFO, CASH , Ack;

			System.out.println("-->loadCircleInit");
			
			if (tag.selectByName(Util.hexStringToBytes(AID)).isOkey()) {
				
//				Ack = tag.read(de.getBigCard_CircleCMD());
//				if(Ack.isOkey())
//				{
//					//写卡成功 读取金额 比对 写入是否正确
//					CASH = tag.getBalance(true);
//					float after = Util.Bytes2FloatMoney(CASH.getBytes()) *100;
//					float before = Float.parseFloat(de.getBig_Money()) * 100;
//					float charge = Float.parseFloat(OrderParams.getChargeMoney()) * 100;
//					
//					if(0 == (after - before - charge))
//					{
//						//圈存成功
//					}
//					
//					
//				}
				
				
			}
	   }	
		return null;
    }
	
	
	
	private void parseInfo(Iso7816.Response sn, Iso7816.Response info) {
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
	}
}
