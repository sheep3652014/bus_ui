package cn.hzzy.posdemo.util;

import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapTools {

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	public void PrintImageNew(Bitmap bitmapCode) {
		// TODO Auto-generated method stub
		int w = bitmapCode.getWidth();
		int h = bitmapCode.getHeight();
		//byte[] sendbuf = StartBmpToPrintCode(bitmapCode);

		//write(sendbuf);

	}

	
	public static Bitmap convertToBlackWhite(Bitmap bmp) {
		int width = bmp.getWidth(); // 获取位图的宽
		int height = bmp.getHeight(); // 获取位图的高
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}



	public static void encodeYUV420SP(byte[] yuv420sp, int[] rgba, int width, int height){
		int frameSize = width * height;

		int[] U = new int[frameSize];
		int[] V = new int[frameSize];
		int uvwidth = width / 2;

		int bits = 8;
		int index = 0;
		int f = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int r = (rgba[index] & 0xFF000000) >> 24;
			int g = (rgba[index] & 0xFF0000) >> 16;
			int b = (rgba[index] & 0xFF00) >> 8;

				int y = (66 * r + 129 * g + 25 * b + 128 >> 8) + 16;
				int u = (-38 * r - 74 * g + 112 * b + 128 >> 8) + 128;
				int v = (112 * r - 94 * g - 18 * b + 128 >> 8) + 128;

				byte temp = (byte)(y > 255 ? 255 : y < 0 ? 0 : y);
				yuv420sp[(index++)] = (byte) (temp > 0 ? 1 : 0);


				//				{
				//					if (f == 0) {
				//						yuv420sp[index++] = 0;
				//						f = 1;
				//					} else {
				//						yuv420sp[index++] = 1;
				//						f = 0;
				//					}
				//				}

			}

		}

		f = 0;
	}





	
	public  static  byte[] bitmap2PrinterBytes (Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		//Log.v("hello", "height?:"+height);
		int startX = 0;
		int startY = 0;
		int offset = 0;
		int scansize = width;
		int writeNo = 0;
		int rgb=0;
		int colorValue = 0;
		int[] rgbArray = new int[offset + (height - startY) * scansize
		                         + (width - startX)];
		bitmap.getPixels(rgbArray, offset, scansize, startX, startY,
				width, height);

		int iCount = (height % 8);
		if (iCount > 0) {
			iCount = (height / 8) + 1;
		} else {
			iCount = (height / 8);
		}
		
		byte [] mData = new byte[iCount*width];
		
		//Log.v("hello", "myiCount?:"+iCoun t);
		for (int l = 0; l <= iCount - 1; l++) {
			//Log.v("hello", "iCount?:"+l);
			//Log.d("hello", "l?:"+l);
			for (int i = 0; i < width; i++) {
				int rowBegin = l * 8;
				//Log.v("hello", "width?:"+i);
				int tmpValue = 0;
				int leftPos = 7;
				int newheight = ((l + 1) * 8 - 1);
				//Log.v("hello", "newheight?:"+newheight);
				for (int j = rowBegin; j <=newheight; j++) {
					//Log.v("hello", "width?:"+i+"  rowBegin?:"+j);
					if (j >= height) {
						colorValue = 0;
					} else {
						rgb = rgbArray[offset + (j - startY)* scansize + (i - startX)];
						if (rgb == -1) {
							colorValue = 0;
						} else {
							colorValue = 1;
						}
					}
					//Log.d("hello", "rgbArray?:"+(offset + (j - startY)
					//		* scansize + (i - startX)));
					//Log.d("hello", "colorValue?:"+colorValue);
					tmpValue = (tmpValue + (colorValue << leftPos));
					leftPos = leftPos - 1;					

				}
				mData[writeNo]=(byte) tmpValue;
				writeNo++;
			}
		}

		return mData;
	}



	
}
