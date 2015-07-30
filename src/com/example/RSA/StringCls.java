package com.example.RSA;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class StringCls {
	private static final String[] RANDOMDATA = new String[]{"0","1","2","3","4","5","6","7","8","9"};
	private static final String[] RANDOMDATAHex = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	public static String StrToHex(String str,boolean mSpace){
		return StrToHex(str,mSpace,"gb2312");
	}
	
	public static String StrToHex(String str,boolean mSpace,String charsetname) 
	{ 
		if(str == null) return null; 
		try {
			return Bytes2HexString(str.getBytes(charsetname),mSpace);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		String content = str; 
//		String Digital="0123456789ABCDEF"; 
//		StringBuffer sb=new StringBuffer(""); 
//		byte[] bs=content.getBytes(); 		
//		int bit; 
//		for(int i=0;i<bs.length;i++){ 
//			bit=(bs[i]&0x0f0)>>4; 
//			sb.append(Digital.substring(bit,bit+1)); 
//			bit=bs[i]&0x0f; 
//			sb.append(Digital.substring(bit,bit+1)); 
//			if(mSpace)sb.append(" ");
//		} 
//		return sb.toString(); 
		
	} 
	
	public static String HexToStr(String str){
		return HexToStr(str,"gb2312");
	}

	public static String HexToStr(String str,String charsetname) 
	{ 
		if(str == null) return null; 
		try {
			return (new String(HexString2Bytes(str,false),charsetname));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		String b= str; 
//		String Digital="0123456789ABCDEF"; 
//		byte[] bytes=new byte[b.length()/2]; 
//		int temp; 
//		for(int i=0;i<bytes.length;i++){ 
//			temp=Digital.indexOf(b.substring(2*i,2*i+1))*16; 
//			temp+=Digital.indexOf(b.substring(2*i+1,2*i+2)); 
//			bytes[i]=(byte)(temp&0xff); 
//		} 
//		return (new String(bytes)); 
	} 
	
	public static String Bytes2HexString(byte[] b,boolean mSpace) { 
		String ret = ""; 
		for (int i = 0; i < b.length; i++) { 
			String hex = Integer.toHexString(b[i] & 0xFF); 
			if (hex.length() == 1) { 
			hex = '0' + hex; 
			} 
			ret += hex.toUpperCase(); 
			if(mSpace)ret+=" ";
		} 
		return ret; 
	}
	
	/** 
	* 将指定字符串src，以每两个字符分割转换为16进制形式 
	* 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9} 
	* @param src String 
	* @return byte[] 
	*/
	public static byte[] HexString2Bytes(String src,boolean mSpace){ 
		if(mSpace)src=src.replace(" ", "");
		byte[] ret = new byte[src.length()/2]; 
		byte[] tmp = src.getBytes(); 
		for(int i=0; i<ret.length; i++){ 
		ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
		} 
		return ret; 
	} 

	/** 
	* 将两个ASCII字符合成�?个字节； 
	* 如："EF"--> 0xEF 
	* @param src0 byte 
	* @param src1 byte 
	* @return byte 
	*/ 
	public static byte uniteBytes(byte src0, byte src1)
    {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte)(_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte)(_b0 | _b1);
        return ret;
    }
	
	public static int ByteToUint(byte b){
		int i=(int)b;
		if(i<0)i=256+i;
		return i;		
	}
	/*
	 * 取伪随机�?
	 */
   public static String getRandom(int len){
	   StringBuffer sb = new StringBuffer();
	   Random random = new Random();
	   for(int i=0;i<len;i++){
		   sb.append(RANDOMDATA[random.nextInt(10)]);
	   }
	   return sb.toString();
   }
   public static String getRandomHex(int len){
	   StringBuffer sb = new StringBuffer();
	   Random random = new Random();
	   for(int i=0;i<len;i++){
		   sb.append(RANDOMDATAHex[random.nextInt(16)]);
	   }
	   return sb.toString();
   }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s=StringCls.StrToHex("FFEGH", false);
		s=StringCls.StrToHex("FFH", false, "gb2312");
		System.out.println("========"+s);
	}

}
