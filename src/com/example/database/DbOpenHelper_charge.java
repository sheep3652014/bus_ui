package com.example.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper_charge extends SQLiteOpenHelper
{
	private static final String TAG = "DbOpenHelper_menu";
	
	private static final String DB = "ChargeRecord.db";
	public static final String TABLE = "chargetable";
	
	public static final String ID = "_id";
	
	
	public static final String ORDER_TIME = "OrderTime";//订单时间
	public static final String ORDER_SEQ = "OrderSEQ";//订单号
	public static final String ORDER_REQTRANSE = "OrderReqTranse";//订单流水号
	public static final String ORDER_AMOUNT = "Amount";//订单金额
	public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//发行卡号，用于标记给那张卡充值
	public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//翼支付支付状态
	public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//圈存写卡状态
	public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//圈存闭环状态
	
	public static final int Version = 1; //数据库版本号，升降级比对用

	public DbOpenHelper_charge(Context context)
	{
		//super(context, name, factory, version);
		super(context, DB, null, Version);
	}

	
	public DbOpenHelper_charge(Context context, String name, CursorFactory factory,
			int version)
	{
		//super(context, name, factory, version);
		super(context, DB, null, Version);
	}

	@SuppressLint("NewApi")
	public DbOpenHelper_charge(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler)
	{
		//super(context, name, factory, version, errorHandler);
		super(context, DB, null, Version, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		
		//sqlite中没有布尔型，此处使用int型存储，0：false，1：true
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(TABLE);
		sb.append("(");
		sb.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");//_id
		sb.append(ORDER_TIME + " VARCHAR(50),");//订单时间
		sb.append(ORDER_SEQ + " VARCHAR(50),");//" STRING,");//订单号 sqlite中String实际上是当作integer存储
		sb.append(ORDER_REQTRANSE + " TEXT,");//" STRING,");//订单流水号
		sb.append(ORDER_AMOUNT + " STRING,");//订单金额
		sb.append(ORDER_PUBLISH_CARDID + " STRING,");//发行卡号，用于区分同一账号对不同卡的充值
		sb.append(ORDER_CHARGE_STATUS + " INTEGER,");//充值状态
		sb.append(ORDER_CHARGE_NFC_STATUS + " INTEGER,");//圈存写卡状态
		sb.append(ORDER_TRANSFERENCE_CLOSE_STATUS + " INTEGER");//圈存闭环状态
		sb.append(");");//
		
		db.execSQL(sb.toString());
		
		Log.d(TAG, "create table:"+ TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		//upgrade: just delete the old table and create a new one
		String sql = "DROP TABLE IF EXISTS" + TABLE + ";" ;
		db.execSQL(sql);
		onCreate(db);
	}

	
}
