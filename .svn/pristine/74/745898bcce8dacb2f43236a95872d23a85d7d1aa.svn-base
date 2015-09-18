package com.example.network;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;



import com.example.application.myApplication;
import com.example.config.Global_Config;


public class SocketClientCls {
	private static String TXIp = myApplication.getServerIP();
	private static int TxPort = myApplication.getServerPort();
	private static int CONNECT_OUT_TIME = Global_Config.CONNECT_OUT_TIME;

	private Socket m_socket = null;
	Calendar lastActive=null;//最后活跃时间

	
	public SocketClientCls() {
		try {
			System.out.println("socket is null = " + (m_socket == null));
			m_socket = new Socket();
			m_socket.setSoTimeout(CONNECT_OUT_TIME); //设置读取超时
			m_socket.connect(new InetSocketAddress(InetAddress.getByName(TXIp), TxPort), Global_Config.CONNECT_OUT_TIME);
			System.out.println("-->  socket create");
		} catch (Exception e) {
			// TODO: handle exception
			CloseLink();//创建出错,关闭资源
			System.out.println("--> error " + e.getMessage());
		} 
		
	}

	public Calendar getLastActive() { 
		return lastActive;
	}

	public void setLastActive(Calendar lastActive) {
		this.lastActive = lastActive;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		CloseLink();
		super.finalize();
	}

	public boolean SendMsg(byte[] buf) {
		try {
			DataOutputStream doc = new DataOutputStream(m_socket.getOutputStream());
			doc.write(buf);
			doc.flush();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("--> error " + e.getMessage());
			return false;
		}
	}

	public byte[] ReceiveMsg() {
		try {
			DataInputStream in = new DataInputStream(m_socket.getInputStream());
			int itime = 0;
			byte[] byteMac = null;
			do {
				Thread.sleep(20);
				itime++;
				byteMac = new byte[in.available()];

			} while (byteMac.length == 0 && itime < 500);
			in.read(byteMac, 0, byteMac.length);
			return byteMac;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("--> error " + e.getMessage());
			return null;
		}
	}

	public boolean SendText(String txt) {
		return SendMsg(txt.getBytes());
	}

	public String ReceiveText() {
		byte[] temp=ReceiveMsg();
		if(temp==null)return "";
		return (new String(temp));
	}

	public boolean CloseLink() {
		if (m_socket != null) {
			try {
				m_socket.close();
				System.out.println("-->  socket destroy");
			} catch (Exception e) {
				System.out.println("--> error " + e.getMessage());
			}
		}
		return true;
	}

	// 判断是否关闭,关闭为T
	public boolean isClose() {
		if (m_socket == null) {
			System.out.println("socket 为空");
			return true;
			// return false;
		} else {
			if (!m_socket.isConnected()) {
				System.out.println("Connect 断开");
			}
			if (m_socket.isInputShutdown()) {
				System.out.println("isInput 被关闭");
				return true;
			}
			if (m_socket.isOutputShutdown()) {
				System.out.println("OutInput 被关闭");
				return true;
			}
			if (m_socket.isClosed()) {
				System.out.println("socket 被关闭");
				return true;
			}
		}
		return false;
	}

	/*public boolean SendHexString(String hexStr) {
		byte[] data = MessagePack.HexString2Bytes(hexStr, false);
		return SendMsg(data);
	}

	public String ReceiveHexString() {
		byte[] data = ReceiveMsg();
		if (data == null)
			return null;
		return MessagePack.Bytes2HexString(data, false);
	}*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*SocketClientCls client = new SocketClientCls();
		String HexParam = "ff010e2caaeb0000000000000000";
		
		if(client.SendText(HexParam)){
			System.out.println(HexParam);
			String result = client.ReceiveText();
			System.out.println("接收" + result);
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}

		client.CloseLink();*/
		
	}

}