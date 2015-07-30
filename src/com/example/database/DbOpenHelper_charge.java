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
	
	
	public static final String ORDER_TIME = "OrderTime";//����ʱ��
	public static final String ORDER_SEQ = "OrderSEQ";//������
	public static final String ORDER_REQTRANSE = "OrderReqTranse";//������ˮ��
	public static final String ORDER_AMOUNT = "Amount";//�������
	public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//���п��ţ����ڱ�Ǹ����ſ���ֵ
	public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//��֧��֧��״̬
	public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//Ȧ��д��״̬
	public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//Ȧ��ջ�״̬
	
	public static final int Version = 1; //���ݿ�汾�ţ��������ȶ���

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
		
		//sqlite��û�в����ͣ��˴�ʹ��int�ʹ洢��0��false��1��true
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(TABLE);
		sb.append("(");
		sb.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");//_id
		sb.append(ORDER_TIME + " VARCHAR(50),");//����ʱ��
		sb.append(ORDER_SEQ + " VARCHAR(50),");//" STRING,");//������ sqlite��Stringʵ�����ǵ���integer�洢
		sb.append(ORDER_REQTRANSE + " TEXT,");//" STRING,");//������ˮ��
		sb.append(ORDER_AMOUNT + " STRING,");//�������
		sb.append(ORDER_PUBLISH_CARDID + " STRING,");//���п��ţ���������ͬһ�˺ŶԲ�ͬ���ĳ�ֵ
		sb.append(ORDER_CHARGE_STATUS + " INTEGER,");//��ֵ״̬
		sb.append(ORDER_CHARGE_NFC_STATUS + " INTEGER,");//Ȧ��д��״̬
		sb.append(ORDER_TRANSFERENCE_CLOSE_STATUS + " INTEGER");//Ȧ��ջ�״̬
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
