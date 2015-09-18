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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.util.Log;

import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.network.PayParams;
import com.example.nfc.Iso7816.Tag;


public class PbocCard {
	protected final static byte[] DFI_MF = { (byte) 0x3F, (byte) 0x00 };
	protected final static byte[] DFI_EP = { (byte) 0x10, (byte) 0x01 };

	protected final static byte[] DFN_PSE = { (byte) '1', (byte) 'P',
			(byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
			(byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
			(byte) '0', (byte) '1', };

	protected final static byte[] DFN_PXX = { (byte) 'P' };

	protected final static int MAX_LOG = 10;
	protected final static int SFI_EXTRA = 21;
	protected final static int SFI_LOG = 24; 

	protected final static byte TRANS_CSU = 6;
	protected final static byte TRANS_CSU_CPX = 9;

	/*protected String name;
	protected String id;
	protected String serl;
	protected String version;
	protected String date;
	protected String count;
	protected String cash;
	protected String log;
	protected String common;
	protected ArrayList<Map<String,String>> logList = new ArrayList<Map<String, String>>();
	protected String circleInit;//圈存初始化信息
	protected String circleStatus;//false 失败 true成功
*/	
	public BigCardBean bean = new BigCardBean();
	
	private static final String CONSUME = Global_Config.CONSUME;
	private static final String CONSUME_TYPE = Global_Config.CONSUME_TYPE;
	private static final String CONSUME_TIME = Global_Config.CONSUME_TIME;
	private static final String CONSUME_MONEY = Global_Config.CONSUME_MONEY;
	
	protected static final int INIT_STEP = Global_Config.INIT_STEP;
	protected static final int CIRCLE_INIT_STEP = Global_Config.CIRCLE_INIT_STEP;
	protected static final int THIRDPARTY_PAY_INIT = Global_Config.THIRDPARTY_PAY_INIT;
	protected static final int THIRDPARTY_PAY_NETRECORD = Global_Config.THIRDPARTY_PAY_NETRECORD;
	//protected static final int THIRDPARTY_PAY_CIRCLE = Global_Config.THIRDPARTY_PAY_CIRCLE;
	
	protected static final String PUBLISHCARDNO = Global_Config.PUBLISHCARDNO;
	protected static final String PHYSICS_CARDNO = Global_Config.PHYSICS_CARDNO;
	protected static final String IMEI_NO = Global_Config.IMEI_NO;
	protected static final String OLDAMOUNT = Global_Config.OLDAMOUNT;// "oldAmount";
	protected static final String PUBLEN = Global_Config.PUBLEN;// "pubLen";
	protected static final String PUBLIC_DATA = Global_Config.PUBLIC_DATA;
	
	protected static final String ORDERNO = Global_Config.ORDERNO;// "orderNO";
	protected static final String ORDERSEQ = Global_Config.ORDERSEQ;// "orderSeq";
	protected static final String ORDER_AMOUNT = Global_Config.ORDER_AMOUNT;
	protected static final String ORDER_DATE = Global_Config.ORDER_DATE;
	protected static final String BANKCARDNO = Global_Config.BANKCARDNO;
	protected static final String LOGLIST = Global_Config.LOGLIST;
	/*public static String load(IsoDep tech, Resources res) {
		final Iso7816.Tag tag = new Iso7816.Tag(tech);

		tag.connect();

		PbocCard card = null;

		do {
			if ((card = ShenzhenTong.load(tag, res)) != null)
				break;

			if ((card = BeijingMunicipal.load(tag, res)) != null)
				break;

			if ((card = ChanganTong.load(tag, res)) != null)
				break;

			
			if ((card = WuhanTong.load(tag, res)) != null)
				break;

			if ((card = YangchengTong.load(tag, res)) != null)
				break;

			if ((card = HardReader.load(tag, res)) != null)
				break;

		} while (false);
		
		
		tag.close();
		
		return (card != null) ? card.toString(res) : null;
	}
*/
	public static BigCardBean load(IsoDep tech, Resources res, myApplication myApp, Context context,Handler mHandler) {
		final Iso7816.Tag tag = new Iso7816.Tag(tech);

		tag.connect();

		PbocCard card = null;

		do{
			//if ((card = WuhanTong.loadCommon(tag, res)) != null)
			if((card = YiChangTong.load(tag, res, myApp, context, mHandler)) != null)
				break;
		}while(false);
		
		//tag.close();//此处必须关闭，避免多线程切换时，后续NFC操作还没完成，从而导致错误
		
		return (null != card) ? card.bean : null;
	}
	
/*	protected PbocCard(Iso7816.Tag tag) {
		id = tag.getID().toString();
	}

	protected void parseInfo(Iso7816.Response data, int dec, boolean bigEndian) {
		if (!data.isOkey() || data.size() < 30) {
			serl = version = date = count = null;
			return;
		}

		final byte[] d = data.getBytes();
		if (dec < 1 || dec > 10) {
			serl = Util.toHexString(d, 10, 10);
		} else {
			final int sn = bigEndian ? Util.toIntR(d, 19, dec) : Util.toInt(d,
					20 - dec, dec);

			serl = String.format("%d", 0xFFFFFFFFL & sn);
		}

		version = (d[9] != 0) ? String.valueOf(d[9]) : null;
		date = String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20],
				d[21], d[22], d[23], d[24], d[25], d[26], d[27]);
		count = null;
	}*/

	/*protected static boolean addLog(final Iso7816.Response r,
			ArrayList<byte[]> l) {
		if (!r.isOkey())
			return false;

		final byte[] raw = r.getBytes();
		final int N = raw.length - 23;
		if (N < 0)
			return false;

		for (int s = 0, e = 0; s <= N; s = e) {
			l.add(Arrays.copyOfRange(raw, s, (e = s + 23)));
		}

		return true;
	}*/
	
	protected static Map<String,String> addLog(final Iso7816.Response r) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (!r.isOkey())
			return null;

		final byte[] raw = r.getBytes();
		final int N = raw.length - 23;
		if (N < 0)
			return null;

//		0000――0001	2	HEX	联机或脱机交易序号
//		0002――0004	3	HEX	透支限额
//		0005――0008	4	HEX	交易金额
//		0009――0009	1	HEX	交易类型标识
//		0010――0015	6	HEX	交易终端编码（可为认证SAM卡号）
//		0016――0027	7	BCD	终端交易时间（YYYYMMDDHHMMSS）
		final int cash = Util.toInt(raw, 5, 4);
		map.put(CONSUME_MONEY, ""+cash);
		final int type = Util.toInt(raw, 9, 1);
		map.put(CONSUME_TYPE, ""+type);
		final String time = String.format("%02X%02X.%02X.%02X %02X:%02X:%02X ",
				raw[16], raw[17], raw[18], raw[19], raw[20], raw[21],
				raw[22]);
		map.put(CONSUME_TIME, time);

		return map;
	}
	
	/*
	protected static ArrayList<byte[]> readLog(Iso7816.Tag tag, int sfi) {
		final ArrayList<byte[]> ret = new ArrayList<byte[]>(MAX_LOG);
		final Iso7816.Response rsp = tag.readRecord(sfi);
		
		Log.d("tag from read record", "record sizes = "+rsp.size());
		Log.d("tag from read record", "rsp = " + rsp.toString());
		
		if (rsp.isOkey()) {
			addLog(rsp, ret);
		} else {
			for (int i = 1; i <= MAX_LOG; ++i) {
				if (!addLog(tag.readRecord(sfi, i), ret))
					break;
			}
		}

		return ret;
	}
	*/
	/*protected static ArrayList<byte[]> readLog(Iso7816.Tag tag, int sfi) {
		final ArrayList<byte[]> ret = new ArrayList<byte[]>(MAX_LOG);
		
		//added by yh
		tag.getData();
		
		//个人密码校验
		final Iso7816.Response rsp1 = tag.Verify(WuhanTong.VERIFY_Lc, WuhanTong.VERIFY_KEY);
		
		if(!rsp1.isOkey())
		{
			Log.d("tag from read record", "readRecord is not Prepare");
		}
		
		final Iso7816.Response rsp = tag.readRecord(sfi, 0);
		
		Log.d("tag from read record", "record sizes = "+rsp.size());
		Log.d("tag from read record", "rsp = " + rsp.toString());
		
		
		if (rsp.isOkey()) {
			addLog(rsp, ret);
		} else {
			for (int i = 1; i <= MAX_LOG; ++i) {
				if (!addLog(tag.readRecord(sfi, i), ret))
					break;
			}
		}

		return ret;
	}*/
	
	protected static ArrayList<Map<String, String>> readLog(Iso7816.Tag tag, int sfi) {
		final ArrayList<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		
		//added by yh
		tag.getData();
		
		final Iso7816.Response rsp = tag.readRecord(sfi, 0);
		
		Log.d("tag from read record", "record sizes = "+rsp.size());
		Log.d("tag from read record", "rsp = " + rsp.toString());
		
		
		if (rsp.isOkey()) {
			Map<String, String> map = new HashMap<String, String>();
			map = addLog(rsp);
			if(null != map)
			{
				ret.add(map);
			}
		} else {
			for (int i = 1; i <= MAX_LOG; ++i) {
				Map<String, String> map = new HashMap<String, String>();
				map = addLog(tag.readRecord(sfi, i));
				if (null == map)
					break;
				
				ret.add(map);
			}
		}
		
		return ret;
	}
	
	/*protected void parseLog(ArrayList<byte[]>... logs) {
		final StringBuilder r = new StringBuilder();

		for (final ArrayList<byte[]> log : logs) {
			if (log == null)
				continue;

			if (r.length() > 0)
				r.append("<br />--------------");

			for (final byte[] v : log) {
				final int cash = Util.toInt(v, 5, 4);
				if (cash > 0) {
					r.append("<br />").append(
							String.format("%02X%02X.%02X.%02X %02X:%02X ",
									v[16], v[17], v[18], v[19], v[20], v[21],
									v[22]));

					final char t = (v[9] == TRANS_CSU || v[9] == TRANS_CSU_CPX) ? '-'
							: '+';

					r.append(t).append(Util.toAmountString(cash / 100.0f));

					final int over = Util.toInt(v, 2, 3);
					if (over > 0)
						r.append(" [o:")
								.append(Util.toAmountString(over / 100.0f))
								.append(']');

					r.append(" [").append(Util.toHexString(v, 10, 6))
							.append(']');
				}
			}
		}

		this.log = r.toString();
	}*/

	
	
	/*protected void parseBalance(Iso7816.Response data) {
		if (!data.isOkey() || data.size() < 4) {
			cash = null;
			return;
		}

		int n = Util.toInt(data.getBytes(), 0, 4);
		if (n > 100000 || n < -100000)
			n -= 0x80000000;

		cash = Util.toAmountString(n / 100.0f);
	}*/

	
	
	
	
	
/*	protected String formatInfo(Resources res) {
		if (serl == null)
			return null;

		final StringBuilder r = new StringBuilder();

		r.append(res.getString(R.string.lab_serl)).append(' ').append(serl);

		if (version != null) {
			final String sv = res.getString(R.string.lab_ver);
			r.append("<br />").append(sv).append(' ').append(version);
		}

		if (date != null) {
			final String sd = res.getString(R.string.lab_date);
			r.append("<br />").append(sd).append(' ').append(date);
		}

		if (count != null) {
			final String so = res.getString(R.string.lab_op);
			final String st = res.getString(R.string.lab_op_time);
			r.append("<br />").append(so).append(' ').append(count).append(st);
		}

		return r.toString();
	}*/

/*	protected String formatLog(Resources res) {
		if (log == null || log.length() < 1)
			return null;

		final StringBuilder ret = new StringBuilder();
		final String sl = res.getString(R.string.lab_log);
		ret.append("<b>").append(sl).append("</b><small>");
		ret.append(log).append("</small>");

		return ret.toString();
	}

	protected String formatBalance(Resources res) {
		if (cash == null || cash.length() < 1)
			return null;

		final String s = res.getString(R.string.lab_balance);
		final String c = res.getString(R.string.lab_cur_cny);
		return new StringBuilder("<b>").append(s)
				.append("<font color=\"teal\"> ").append(cash).append(' ')
				.append(c).append("</font></b>").toString();
	}

	protected String toString(Resources res) {
		final String info = formatInfo(res);
		final String hist = formatLog(res);
		final String cash = formatBalance(res);

		return CardManager.buildResult(name, info, cash, hist);
	}*/
}
