package android.posapi;

import android.content.Context;
import android.graphics.Bitmap;


import android.posapi.PosBroadcastSender;
import android.util.Log;

public class PosApi {
	private static final String TAG = "Pos";
	private Context context;
	private static PosApi instace=null;


	private PosApi(Context ctx){
		this.context=ctx;
	}
	public static synchronized PosApi getInstance(Context ctx){
		if(instace==null){
			instace=new PosApi(ctx);
			Log.d(TAG, "PosApi---> instance==null");
		}
		Log.d(TAG, "PosApi---> instance!=null");
		return instace;
	}


	public native int    initPosDev(String model);

	public native String getLibVersion();
	
	public native int    resetDev(String serialNum);
	
	public native int    getDevStatus();

	
	public native int    swipingCard(int delayTime, int isEncrypt, int encryptIndex);
	
	public native int    icReset(int delayTime);
	
	public native int    icClose(int delayTime);
	
	public native int    icCmd(byte[] cmdBuffer , int cmdLen);

	
	public native int    resetPsam(int delayTime, int psamSlot);
	
	public native int    closePsam(int psamSlot);
	
	public native int    psamCmd(int psamSlot,byte[] cmdBuffer , int cmdLen);
	
	public native int 	 extendSerialCmd(int serialNo,int time, int baudrate, byte[] serialCmd, int cmdLen);
	
	public native int    printText(int concentration, byte data[], int dataLen);


	public native int    printImage(int concentration,int left ,int width, int height, byte data[]);
	
	

	public native int    printBarcode1D(int concentration,int codeType ,int height ,int lineWidth,int isPrintText, byte[]data);
	public native int    printBarcode2D(int concentration, byte[]data);
	
	public native int    updateDev(byte[] udpateData, int len);
	
	
	public native int    updateFonts(byte[] udpateData, int len);
	
	
	public native int sendKeyCmd(int keyValue);
	
	
	public native int flashErase(int address, int sectorNum);
	
	public native int flashWrite(int address, byte[]buffer, int bufLen);
	
	public native int flashCheckSum(int address, int readLen);
	
	
	public native int closeDev();


	
	public void  commStatusCallback(int cmdFlag, int status ,byte[] buffer ,int bufferLen){
		Log.v("hello", "commStatus:"+cmdFlag+" status :"+ status);
		if(cmdFlag == PosSDK.POS_SWIPING_CARD){
			PosBroadcastSender.sendSwipingCard(context, status, buffer, bufferLen);
		}
		else if(cmdFlag == PosSDK.POS_GET_STATUS){
			PosBroadcastSender.sendPosStatus(context, status, buffer, bufferLen);
		}
		else{
			PosBroadcastSender.sendPosCommStatus(context, cmdFlag, status, buffer, bufferLen);	
		}
	}
	

	
	public void keyEventCallback(String keyValue){
		PosBroadcastSender.sendPosKeyEvent(context, keyValue);
	}

	
	
	public void PosUpdateStatusCallback(int status, int progress){
		Log.v("hello", "update status:"+status+" progress :"+ progress);
		PosBroadcastSender.sendPosUpdateStatus(context, status,progress);
	}
	
	public void PosUpdateFontsCallBack(int status,int porgress){
		Log.v("hello", "PosUpdateFontsCallBack:"+status+" progress :"+ porgress);
		PosBroadcastSender.sendPosUpdateFontsStatus(context, status,porgress);
	}


	static{
		System.loadLibrary("PosApi");
	}
}
