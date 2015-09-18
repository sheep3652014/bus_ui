package android.posapi;

import android.content.Context;
import android.os.RemoteException;




public class PosSDK {
	
	/**通用命令执行状态广播action**/
	public static final String ACTION_POS_COMM_STATUS   ="cn.ismart1.pos.commStatus";
	/**获取设备状态广播action**/
	public static final String ACTION_POS_GET_STATUS    ="cn.ismart1.pos.status";
	/**刷卡广播action**/
	public static final String ACTION_POS_SWIPING_CARD  ="cn.ismart1.pos.swipingCard";
	/**实时按键广播action**/
	public static final String ACTION_POS_KEY_EVENT     ="cn.ismart1.pos.keyEvent";
	/**实时固件更新状态播action**/
	public static final String ACTION_POS_UPDATE_STATUS ="cn.ismart1.pos.update";

	public static final String ACTION_POS_UPDATE_FONTS_STATUS ="cn.ismart1.pos.update.fonts";

	/**key按键值**/
	public static final String KEY_KEY_VALUE            ="keyValue";
	/**key命令标识**/
	public static final String KEY_CMD_FLAG             ="cmdFlag";
	/**key命令状态**/
	public static final String KEY_CMD_STATUS           ="status";
	/**key命令回码数据**/
	public static final String KEY_CMD_DATA_BUFFER      ="buffer";
	/**key命令回码数据长度**/
	public static final String KEY_CMD_DATA_LENGTH      ="length";

	/**key轨道1**/
	public static final String KEY_SWIPING_CARD_TRACK1  ="track1";
	/**key轨道2**/
	public static final String KEY_SWIPING_CARD_TRACK2  ="track2";
	/**key轨道3**/
	public static final String KEY_SWIPING_CARD_TRACK3  ="track3";
	/**key卡号**/
	public static final String KEY_SWIPING_CARD_NO      ="cardNo";



	/**key设备状态**/
	public static final String KEY_POS_STATUS           ="posStatus";
	/**key设备序列号**/
	public static final String KEY_POS_SERIALNO         ="serialNo";
	/**key设备固件版本号**/
	public static final String KEY_POS_FIRMWARE         ="firmware";


	public static final String KEY_POS_UPDATE_PROGRESS  ="progress";

	/**命令执行成功**/
	public static final int COMM_STATUS_SUCCESS         =  1;
	/**命令执行失败**/
	public static final int COMM_STATUS_FAILED          =  0;
	public static final int COMM_STATUS_TIMEOUT         =  2;
	/**命令执行取消**/
	public static final int COMM_STATUS_CANCEL          =  3;

	// POS  cmdFlag
	public static final int POS_INIT                    =  0;
	public static final int POS_GET_STATUS              =  1;
	public static final int POS_SWIPING_CARD            =  2;
	public static final int POS_IC_RESET                =  3;
	public static final int POS_IC_CLOSE                =  4;

	public static final int POS_IC_CMD                  =  5;
	public static final int POS_PASM_RESET              =  6;
	public static final int POS_PASM_CLOSE              =  7;
	public static final int POS_PASM_CMD                =  8;
	public static final int POS_RECEIVE_KEY             =  9;
	public static final int POS_INPUT_PASSWORD          =  10;
	public static final int POS_PRINT_TEXT              =  11;
	public static final int POS_PRINT_PICTURE           =  12;
	public static final int POS_PRINT_BARCODE           =  13;
	public static final int POS_EXPAND_SERIAL1          =  14;
	public static final int POS_EXPAND_SERIAL2          =  15;

	/**打印成功**/
	public static final int ERR_POS_PRINT_SUCC          =  0;
	/**打印失败:缺纸**/
	public static final int ERR_POS_PRINT_NO_PAPER      =  1;
	/**打印失败**/
	public static final int ERR_POS_PRINT_FAILED        =  2;
	public static final int ERR_POS_PRINT_VOLTAGE_LOW   =  3;
	public static final int ERR_POS_PRINT_VOLTAGE_HIGH  =  4;
	
	
	public static final String PRODUCT_MODEL_IPM01 	= "ipm01"; 
	public static final String PRODUCT_MODEL_IPM122 	= "ipm122"; 
	
	private  Context mContext;
	private static PosApi api;
	private static final String TAG                     = "PosLog";
	
	private static PosSDK  instance;

	public static PosSDK getInstance(Context ctx){
		if(instance==null){
			instance=new PosSDK(ctx);
		}
		return instance;
	}


	private PosSDK(Context ctx){
		this.mContext=ctx;
		api = PosApi.getInstance(ctx);
	}
	
	
	public void init(String model) {
		// TODO Auto-generated method stub
		api.initPosDev(model);
	}

	public String getVersion() {
		return api.getLibVersion();
	}




	public int resetPos(){
		return api.resetDev(null);
	}


	public int getPosStatus() {
		return api.getDevStatus();
	}


	public int icReset(int timeout){
		return api.icReset(timeout);
	}

	
	public int icClose(int timeout) {
		return api.icClose(timeout);
	}

	
	public int icCmd(byte[] cmdData, int cmdLen)  {
		return api.icCmd(cmdData, cmdLen);
	}

	
	public int psamReset(int timeout, int psamSlot) {
		return api.resetPsam(timeout, psamSlot);
	}

	
	public int psamClose(int psamSlot) {
		return api.closePsam(psamSlot);
	}


	public int psamCmd(int psamSlot, byte[] cmdData, int cmdLen){
		return api.psamCmd(psamSlot, cmdData, cmdLen);
	}


	public int extendSerialCmd(int serialNo, int time, int baudrate,
			byte[] serialCmd, int cmdLen) {
		return api.extendSerialCmd(serialNo, time, baudrate, serialCmd, cmdLen);
	}

	
	public int printText(int concentration, byte[] data, int dataLen) {
		return api.printText(concentration, data, dataLen);
	}

	
	public int printPic(int concentration, int left, int width, int height,
			byte[] data, int dataLen) {
		return api.printImage(concentration, left, width, height, data);
	}

	public int printBarcode1D(int concentration,int codeType ,int height ,int lineWidth,int isPrintText, byte[]data){
		return api.printBarcode1D(concentration, codeType, height, lineWidth, isPrintText, data);
	}
	
	public  int    printBarcode2D(int concentration, byte[]data){
		return api.printBarcode2D(concentration, data);
	}
	
	public int swipCard(int timeout, int isEncrypt, int encryptIndex) {
		return api.swipingCard(timeout, isEncrypt, encryptIndex);
	}

	public int updatePos(byte[] udpateData, int len)  {
		return api.updateDev(udpateData, len);
	}


	public int updateFonts(byte[] data, int len)  {
		return api.updateFonts(data, len);
	}


	public int sendKeyCmd(int keyCmd)  {
		return api.sendKeyCmd(keyCmd);
	}

	
	public int flashErase(int address, int sectorNum) {
		return api.flashErase(address, sectorNum);
	}

	
	public int flashWrite(int address, byte[] buffer, int bufLen) {
		return api.flashWrite(address, buffer, bufLen);
	}

	public int flashCheckSum(int address, int readLen) {
		return api.flashCheckSum(address, readLen);
	}

	
	public int unInitialize() {
		return api.closeDev();
	}

}
