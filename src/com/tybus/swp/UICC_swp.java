
package com.tybus.swp;

import java.io.IOException;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session;
import org.simalliance.openmobileapi.SEService.CallBack;

import com.example.config.Global_Config;
import com.example.nfc.Util;

import android.app.Activity;

/**
 * 用于SWP-NFC卡，支持基于UICC的所有SIM card
 * 具有自建回调，
 * 发送方法，
 * 资源关闭方法
 * 部分ROM需要添加 <uses-library
            android:name="org.simalliance.openmobileapi"
            android:required="false" /> 才能正常使用。
 * @author tom
 *
 */
public final class UICC_swp
{
	private static final String AID = Global_Config.AID;// swp-nfc aid
	private static final String APPLET = Global_Config.APPLET;// swp-nfc mf

	private Activity m_Activity = null;
	//private swp_holder holder = null;
	private CallBack m_Callback = null;
	private SEService _service = null;
	private Session _session = null;
	private Channel _channel = null;
	private Reader _reader = null;
	
	private boolean hasSWP_NFC_applet = false;
	private UICC_Interface mInterface = null;
	
	public UICC_swp(Activity activity, UICC_Interface mInterface)
	{
		m_Activity = activity;
		//holder = new swp_holder();
		this.mInterface = mInterface;
	}

//	private class swp_holder
//	{
//		public SEService _service = null;
//		public Session _session = null;
//		public Channel _channel = null;
//		public Reader _reader = null;
//	}

	/**
	 * 初始化UICC卡回调函数
	 * @return
	 */
	public boolean UICCInit()
	{
		//已绑定，退出
		if((null != _service) || (null != _session) || (null != _channel)) return false;
		
		m_Callback = (CallBack) new SEServiceCallback();
		try
		{
			new SEService(m_Activity, m_Callback);
		} catch (Exception e)
		{
			System.out.println(m_Activity.getClass() + " uicc "
					+ "Exception in creation"+ e.getMessage());
			return false;
		}
		
		return true;
	}


	public class SEServiceCallback implements CallBack
	{
		private UICC_Interface mInterface = null;
		
		@Override
		public void serviceConnected(SEService service)
		{
			//holder._service = service;
			_service = service;
			boolean result = performTest();
			System.out.println("result in callback " + result);
			//setHasSWP_NFC_applet(result);
		}
	}

	/**
	 * service关闭
	 */
	private void UICCServiceClose()
	{
//		if ((null != holder._service) && (holder._service.isConnected()))
//			holder._service.shutdown();
		if ((null != _service) && (_service.isConnected()))
			_service.shutdown();
	}

	/**
	 * seesion关闭
	 */
	private void UICCSeesionClose()
	{
//		if ((null != holder._session) && (!holder._session.isClosed()))
//			holder._session.close();
		if ((null != _session) && (!_session.isClosed()))
			_session.close();
	}

	/**
	 * channel关闭
	 */
	private void UICCChannelClose()
	{
//		if ((null != holder._channel) && (!holder._channel.isClosed()))
//			holder._channel.close();
		if ((null != _channel) && (!_channel.isClosed()))
			_channel.close();
	}

	/**
	 * swp 卡资源关闭
	 */
	public void UICCClose()
	{
		// 按顺序关闭资源
		UICCChannelClose();
		UICCSeesionClose();
		UICCServiceClose();
		System.out.println("close uicc");
	}

	/**
	 * 查询宜昌公交SWP-NFC卡应用，找到对应应用返回true，否则返回false
	 * 
	 * @return 找到对应应用返回true，否则返回false
	 */
	private boolean performTest()
	{

		boolean result = false;

		//if (null == holder._service)
		if (null == _service)
			System.out.println(m_Activity.getClass() + "service is null");

		//Reader[] readers = holder._service.getReaders();
		Reader[] readers = _service.getReaders();
		System.out.println("reader length = " + readers.length);

		if (readers.length == 0)
		{
			UICCClose();
			return result;
		}

		for (Reader reader : readers)
		{
			if (!reader.isSecureElementPresent())
				continue;
			// ZTE G719C 有多个reader 限制只能读取指定含有公交信息的SIM卡应用
			if (!reader.getName().contains("SIM"))
				continue;

			try
			{
//				holder._session = reader.openSession();
				_session = reader.openSession();
			} catch (Exception e)
			{
				System.out.println(m_Activity.getClass()
						+ "Exception on openSession(): " +  e.getMessage());
				UICCClose();
				result = false;
			}
			//if (holder._session == null)
			if (_session == null)
				continue;

			try
			{
//				byte[] atr = holder._session.getATR();
				byte[] atr = _session.getATR();
				System.out.println(m_Activity.getClass() + "--> "
						+ atr.toString());
				System.out.println("--> atr" + atr.toString());
			} catch (Exception e)
			{
				System.out.println(m_Activity.getClass()
						+ "Exception on getATR(): " + e.getMessage());
				System.out.println("--> atr exception " + e.getMessage());
				
				UICCClose();
				
				result = false;
				// 华为 C8817D ZTE G719C 上无应答，但是其它打开操作正常
			}

			result = testLogicalChannel(Util.hexStringToBytes(AID));
			System.out.println("rsult is " + result);
			// _reader = reader;

			// _session.close();
			
			//UICCClose();
		}

		return result;
	}

	/**
	 * 打开逻辑通道
	 * 
	 * @param aid
	 * @return 打开通道正确并且读取正确返回 true， 否则返回 false
	 */
	private boolean testLogicalChannel(byte[] aid)
	{
		boolean result = false;

		try
		{
			System.out.println(m_Activity.getClass() + "--> session"
					//+ holder._session);
					+ _session);
//			holder._channel = holder._session.openLogicalChannel(aid);
			_channel = _session.openLogicalChannel(aid);
			System.out.println("________" + APPLET + " channel = "
					//+ holder._channel);
					+ _channel);
			byte[] rsp;
//			rsp = holder._channel.transmit(Util.hexStringToBytes(APPLET));
			rsp = _channel.transmit(Util.hexStringToBytes(APPLET));
			if (AckCheck(rsp))
			{
				System.out.println("select applect ok " + Util.bytesToString(rsp));
				
				setHasSWP_NFC_applet(true);//找到应用
				
				mInterface.UICCTranSomeCmd();
				
				result = true;// 找到公交应用
			} else
			{
				System.out.println(m_Activity.getClass()
						+ "--> select applet error");
				result = false;// 没有找到公交应用
			}
			
			// channel.close();
			
			return result;
		} catch (Exception e)
		{
			System.out.println(m_Activity.getClass()
					+ " Exception on LogicalChannel: " + e.getMessage());
			
			UICCClose();
			
			return (result = false);
		}
	}
	
	/**
	 * 用于外部调用UICC类时发送apdu命令
	 * @author tom
	 *
	 */
	public interface UICC_Interface
	{
		public void UICCTranSomeCmd();
		
	}
	/**
	 * 应答校验
	 * 
	 * @param bytes
	 *            返回
	 * @return 应答状态带9000 返回true 否则返回false
	 */
	private boolean AckCheck(byte[] bytes)
	{
		if (((byte) 0x90 == bytes[bytes.length - 2])
				&& ((byte) 0x00 == bytes[bytes.length - 1]))
			return true;
		else
		{
			return false;
		}
	}
	
	/**
	 * uicc 指令发送，正确执行返移除状态码9000的字符串，否则返回null
	 * @param cmd 指令字符串
	 * @return 正确执行返移除状态码9000的字符串，否则返回null
	 */
	public String UICC_TRANS(String cmd)
	{
		String ack = null;
		if(!isHasSWP_NFC_applet())	return ack;
		
		try
		{
//			byte[] back = holder._channel.transmit(Util.hexStringToBytes(cmd));
			byte[] back = _channel.transmit(Util.hexStringToBytes(cmd));
			System.out.println(m_Activity.getClass() + "channel transmit ack = " + Util.bytesToString(back));
			if(AckCheck(back))
			{
				ack = Util.bytesToString(back);
				ack = ack.substring(0, ack.length()-4);//去除状态码9000
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(m_Activity.getClass() + " channel transimit error " + e.getMessage());
		}
		
		return ack;
	}

	/**
	 * 获取swp卡是否找到应用，找到返回true，否则false
	 * @return
	 */
	public boolean isHasSWP_NFC_applet()
	{
		return hasSWP_NFC_applet;
	}

	/**
	 * 设置SWP卡是否找到应用，找到设置true 分否false
	 * @param hasSWP_NFC_applet
	 */
	public void setHasSWP_NFC_applet(boolean hasSWP_NFC_applet)
	{
		this.hasSWP_NFC_applet = hasSWP_NFC_applet;
	}
}
