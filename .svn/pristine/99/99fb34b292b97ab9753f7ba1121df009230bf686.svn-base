package com.example.bestpay;

import android.util.Log;

import com.bestpay.plugin.Plugin;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Util {

	// 模拟下单url
	public static final String ORDER_URL = "https://webpaywg.bestpay.com.cn/order.do";//"下单地址";
	// Timeout
	public static final int CONNECT_TIMEOUT = 30 * 1000;
	public static final int READ_TIMEOUT = 30 * 1000;

	public static String order(Hashtable<String, String> hash) {
		HttpPost httpRequest = new HttpPost(ORDER_URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("MERCHANTID", hash
				.get(Plugin.MERCHANTID)));
		params.add(new BasicNameValuePair("ORDERSEQ", hash.get(Plugin.ORDERSEQ)));
		params.add(new BasicNameValuePair("ORDERREQTRANSEQ", hash
				.get(Plugin.ORDERREQTRANSEQ)));
		params.add(new BasicNameValuePair("ORDERREQTIME", hash
				.get(Plugin.ORDERTIME)));
		params.add(new BasicNameValuePair("KEY", hash.get(Plugin.KEY)));
		
		//added by yh
		Log.d("from order", "__"+params.toString());
		
		String encodedString = "";
		try {
			encodedString = CryptTool.md5Digest(Util.getMacString(params));
			
			//added by yh
			Log.d("from order", "MD5 = "+encodedString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add(new BasicNameValuePair("ORDERAMT", hash
				.get(Plugin.ORDERAMOUNT)));
		params.add(new BasicNameValuePair("SUBMERCHANTID", hash
				.get(Plugin.SUBMERCHANTID)));
		params.add(new BasicNameValuePair("MAC", encodedString));
		params.add(new BasicNameValuePair("TRANSCODE", "01"));
		
		//added by yh  分账明细，必填和支付请求参数中一�??
		params.add(new BasicNameValuePair("DIVDETAILS", hash.get(Plugin.DIVDETAILS)));
		
		// Log.d("test", "param:" + params);
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = Util.getNewHttpClient()
					.execute(httpRequest);
			return getReturnCode(response.getEntity().getContent(), response
					.getEntity().getContentLength());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;

	}

	public static String getMacString(List<NameValuePair> list) {
		String str = "";
		
		
		for (int i = 0; i < list.size(); i++) {
			NameValuePair nvp = list.get(i);
			if (str.equals("")) {
				str += nvp.getName() + "=" + nvp.getValue();
			}

			else {
				str += "&" + nvp.getName() + "=" + nvp.getValue();
			}
		}
		 Log.e("test", "mac:" + str);
		return str;

	}

	public static String getReturnCode(InputStream stream, long length) {
		byte bb[] = new byte[(int) length];

		try {
			stream.read(bb);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Log.e("test", "result:" + new String(bb));
		return new String(bb);
	}

	public static HttpClient getNewHttpClient() {
		HttpClient httpCilent;
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			httpCilent = new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			httpCilent = new DefaultHttpClient(params);
		}
		return httpCilent;
	}

	public static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {

				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	/**
	 * 获取准确的金�??
	 * 
	 * @param orderAmt
	 * @return
	 */
	public static int getOrderAmt(String orderAmt) {
		BigDecimal b1 = new BigDecimal(orderAmt);
		BigDecimal b2 = new BigDecimal("100");
		return b1.multiply(b2).intValue();
	}

	/**
	 * hex字符串转bytes
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}
	
	private static byte charToByte(char c) {  
	    return (byte) ("0123456789ABCDEF".indexOf(c)&0xff);  
	} 
		
    /**
     * bytes转字符串
     * @param bytes
     * @return
     */
	public static String bytesToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes)
			sb.append(String.format("%02x", b & 0xFF));
		
		return sb.toString();
	}
	
	/**
	 * byte[] 型money �? float型money  用于swp�? 和大卡读余额返回的金额的处理
	 * @param byteMoney  �?9000
	 * @return
	 */
	public static float Bytes2FloatMoney(byte[] byteMoney)
	{
		String Ack = bytesToString(byteMoney);
		System.out.println("-->"+Ack);
		Ack = Ack.substring(0, Ack.length()-4);
		System.out.println("-->"+Ack);
		int m =	Integer.decode("0x"+Ack);
		System.out.println("-->"+m);
		float m1 = ((float)m)/100;
		System.out.println("-->"+m1);
		//设置小数点后2位精度，四舍五入
		float money = new BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		
		return money;
	}
}
