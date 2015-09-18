package com.example.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper_Third_Human //extends SQLiteOpenHelper
{
	private static final String TAG = "DbOpenHelper_Third_Human";
	
	private static final String DB = DbOpenHelper_charge.DB;
	public static final String TABLE = "third_human";
	
	public static final String ID = "_id";
	public static final int Version = 1; //数据库版本号，升降级比对用
	
	public static final String USERNAME = "username";//登录用户名
	public static final String ORDER_TIME = "order_time";//订单时间
	public static final String ORDER_NO = "orderNO";//订单号
	public static final String ORDER_SEQ = "orderseq";//订单流水号
	public static final String ORDER_AMOUNT = "orderamount";//订单金额
	public static final String ORDER_CASH = "cash";//实收金额
	public static final String PUBLISH_CARDNO = "publishcardNO";//发行卡号
	public static final String BANKCARDID = "bankcardid";//银行卡号
	public static final String STATUS_REQUEST = "status_request";//是否请求到订单
	public static final String STATUS_GOTCIRCLE = "status_gotcircle";//是否请求到圈存数据
	public static final String STATUS_CIRCLE = "status_circle";//圈存状态
	public static final String STATUS_CIRCLE_REPORT = "status_circle_report";//圈存完成上报状态
	public static final String TAC = "tac";//tac

	/*public DbOpenHelper_Third_Human(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler)
	{
		super(context, DB, null, Version, null);
	}

	public DbOpenHelper_Third_Human(Context context, String name,
			CursorFactory factory, int version)
	{
		super(context, DB, null, Version);
	}

	public DbOpenHelper_Third_Human(Context context)
	{
		super(context, DB, null, Version);
	}*/
	
	//public void onCreate(SQLiteDatabase db)
	public static final String Create()
	{
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		
		//sqlite中没有布尔型，此处使用int型存储，0：false，1：true
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(TABLE);
		sb.append("(");
		sb.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");//_id
		sb.append(USERNAME + " VARCHAR(50),");//登录用户名
		sb.append(ORDER_TIME + " VARCHAR(50),");//订单时间
		sb.append(ORDER_NO + " VARCHAR(40),");//订单号
		sb.append(ORDER_SEQ + " VARCHAR(40),");//订单流水号
		sb.append(ORDER_AMOUNT + " VARCHAR(10),");//订单金额
		sb.append(ORDER_CASH + " VARCHAR(10),");//实收金额
		sb.append(PUBLISH_CARDNO + " VARCHAR(20),");//发行卡号
		sb.append(BANKCARDID + " VARCHAR(20),");//银行卡号
		sb.append(STATUS_REQUEST + " INTEGER,");//申请补登订单状态
		sb.append(STATUS_GOTCIRCLE + " INTEGER,");//申请圈存指令状态
		sb.append(STATUS_CIRCLE + " INTEGER,");//圈存完成状态
		sb.append(STATUS_CIRCLE_REPORT + " INTEGER,");//圈存完成上报状态
		sb.append(TAC + " VARCHAR(10)");//圈存应答tac
		sb.append(");");//
		
		//db.execSQL(sb.toString());
		
		Log.d(TAG, "create table:"+ TABLE);
		return sb.toString();
	}

	//public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	public static final String Upgrage()
	{
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS" + TABLE + ";" ;
		//db.execSQL(sql);
		//onCreate(db);
		return sql;
	}

}
