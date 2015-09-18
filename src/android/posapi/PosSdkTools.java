package android.posapi;

import android.posapi.Conversion;



public class PosSdkTools {
	
	
	
	public static  byte[] getPosStatusFromData(byte[] bufferData){
		if(bufferData == null) return null;
		byte [] status = new byte[8];
		System.arraycopy(bufferData, 0, status, 0, status.length);
		return status;
	}
	
	
	public static  String getPosVersionFromData(byte[] bufferData){
		if(bufferData == null) return null;
		byte [] version = new byte[24];
		System.arraycopy(bufferData, 20, version, 0, version.length);
		return Conversion.decode(Conversion.Bytes2HexString(version));
	}
	
	
	public static String getPosSerialNo(byte[] bufferData){
		if(bufferData == null ) return null;
		byte [] serialNo = new byte[8];
		System.arraycopy(bufferData, 20, serialNo, 0, serialNo.length);
		return Conversion.bcd2Str(serialNo);
	}
	
	
}
