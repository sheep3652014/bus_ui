package android.posapi;

import cn.hzzy.posdemo.util.ByteTools;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class PosBroadcastSender {

	private static boolean DEBUG = true;
	private static String TAG = "PosBroadcastSender";

	public static void setDebug(boolean isDebug){
		DEBUG=isDebug;
	}
	public static void sendPosKeyEvent(Context ctx,String keyValue){
		Intent  i = new Intent(PosSDK.ACTION_POS_KEY_EVENT);
		i.putExtra(PosSDK.KEY_KEY_VALUE, keyValue);
		ctx.sendBroadcast(i);
	}



	
	public static void sendPosStatus(Context ctx,int status ,byte[] buffer ,int bufferLen ){
		if(buffer == null ) return ;
		Intent  i = new Intent(PosSDK.ACTION_POS_GET_STATUS);
		i.putExtra(PosSDK.KEY_CMD_STATUS, status);
		i.putExtra(PosSDK.KEY_POS_STATUS, PosSdkTools.getPosStatusFromData(buffer));
		i.putExtra(PosSDK.KEY_POS_SERIALNO, PosSdkTools.getPosSerialNo(buffer));
		i.putExtra(PosSDK.KEY_POS_FIRMWARE, PosSdkTools.getPosVersionFromData(buffer));
		ctx.sendBroadcast(i);
	}



	
	public static void sendSwipingCard(Context ctx,int status ,byte[] buffer ,int bufferLen ){

		Intent  i = new Intent(PosSDK.ACTION_POS_SWIPING_CARD);
		i.putExtra(PosSDK.KEY_CMD_STATUS, status);
		//刷卡成功
		if(status == PosSDK.COMM_STATUS_SUCCESS){
			try{
				if(buffer == null ) return ;
				byte [] track1 = null;
				byte [] track2 = null;
				byte [] track3 = null;
				byte [] cardNo= null;
				
				int allDataLen = ByteTools.makeWord(buffer[1], buffer[0]);
				LOGD("allDataLen :"+ allDataLen);
				int track1Size =(int) buffer[2]; 
				LOGD("track1Size :"+ track1Size);
				if(track1Size > 0){
					track1 = new byte[track1Size];
					System.arraycopy(buffer, 3, track1, 0, track1.length);
				}

				int track2Size =(int) buffer[2+track1Size+1];
				LOGD("track2Size :"+ track2Size);
				if(track2Size > 0){
					track2 = new byte[track2Size];
					System.arraycopy(buffer, 2+track1Size+1+1, track2, 0, track2.length);
				}


				int track3Size =(int) buffer[2+track1Size+track2Size+1+1];
				LOGD("track3Size :"+ track3Size);
				if(track3Size > 0){
					track3 = new byte[track3Size];
					System.arraycopy(buffer, 2+track1Size+track2Size+1+1+1, track3, 0, track3.length);
				}
				int cardNolen = (int)buffer[2+track1Size+track2Size+track3Size+1+1+1];
				LOGD("cardNolen :"+ cardNolen);
				if(cardNolen > 0 ){
					cardNo = new byte[cardNolen];
					System.arraycopy(buffer, 2+track1Size+track2Size+track3Size+1+1+1+1, cardNo, 0, cardNo.length);
				}

				i.putExtra(PosSDK.KEY_SWIPING_CARD_TRACK1, track1);
				i.putExtra(PosSDK.KEY_SWIPING_CARD_TRACK2, track2);
				i.putExtra(PosSDK.KEY_SWIPING_CARD_TRACK3, track3);
				i.putExtra(PosSDK.KEY_SWIPING_CARD_NO, cardNo);


			}catch(Exception e){
				e.printStackTrace();
				
			}
		}
		ctx.sendBroadcast(i);

	}

	
	public static void sendPosUpdateStatus(Context ctx,int statusCode,int progress){
		Intent  i = new Intent(PosSDK.ACTION_POS_UPDATE_STATUS);
		i.putExtra(PosSDK.KEY_CMD_STATUS, statusCode);
		i.putExtra(PosSDK.KEY_POS_UPDATE_PROGRESS, progress);
		ctx.sendBroadcast(i);
	}
	
	
	public static void sendPosUpdateFontsStatus(Context ctx,int statusCode,int progress){
		Intent  i = new Intent(PosSDK.ACTION_POS_UPDATE_FONTS_STATUS);
		i.putExtra(PosSDK.KEY_CMD_STATUS, statusCode);
		i.putExtra(PosSDK.KEY_POS_UPDATE_PROGRESS, progress);
		ctx.sendBroadcast(i);
	}

	public static void sendPosCommStatus(Context ctx,int cmdFlag, int status ,byte[] buffer ,int bufferLen){
		Intent i = new Intent(PosSDK.ACTION_POS_COMM_STATUS);
		i.putExtra(PosSDK.KEY_CMD_FLAG, cmdFlag);
		i.putExtra(PosSDK.KEY_CMD_STATUS, status);
		i.putExtra(PosSDK.KEY_CMD_DATA_BUFFER, buffer);
		i.putExtra(PosSDK.KEY_CMD_DATA_LENGTH, bufferLen);
		ctx.sendBroadcast(i);
	}

	private static void LOGD(String msg){
		if(DEBUG){
			Log.d(TAG, msg);
		}
	}

}
