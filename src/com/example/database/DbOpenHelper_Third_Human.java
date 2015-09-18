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
	public static final int Version = 1; //���ݿ�汾�ţ��������ȶ���
	
	public static final String USERNAME = "username";//��¼�û���
	public static final String ORDER_TIME = "order_time";//����ʱ��
	public static final String ORDER_NO = "orderNO";//������
	public static final String ORDER_SEQ = "orderseq";//������ˮ��
	public static final String ORDER_AMOUNT = "orderamount";//�������
	public static final String ORDER_CASH = "cash";//ʵ�ս��
	public static final String PUBLISH_CARDNO = "publishcardNO";//���п���
	public static final String BANKCARDID = "bankcardid";//���п���
	public static final String STATUS_REQUEST = "status_request";//�Ƿ����󵽶���
	public static final String STATUS_GOTCIRCLE = "status_gotcircle";//�Ƿ�����Ȧ������
	public static final String STATUS_CIRCLE = "status_circle";//Ȧ��״̬
	public static final String STATUS_CIRCLE_REPORT = "status_circle_report";//Ȧ������ϱ�״̬
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
		
		//sqlite��û�в����ͣ��˴�ʹ��int�ʹ洢��0��false��1��true
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(TABLE);
		sb.append("(");
		sb.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");//_id
		sb.append(USERNAME + " VARCHAR(50),");//��¼�û���
		sb.append(ORDER_TIME + " VARCHAR(50),");//����ʱ��
		sb.append(ORDER_NO + " VARCHAR(40),");//������
		sb.append(ORDER_SEQ + " VARCHAR(40),");//������ˮ��
		sb.append(ORDER_AMOUNT + " VARCHAR(10),");//�������
		sb.append(ORDER_CASH + " VARCHAR(10),");//ʵ�ս��
		sb.append(PUBLISH_CARDNO + " VARCHAR(20),");//���п���
		sb.append(BANKCARDID + " VARCHAR(20),");//���п���
		sb.append(STATUS_REQUEST + " INTEGER,");//���벹�Ƕ���״̬
		sb.append(STATUS_GOTCIRCLE + " INTEGER,");//����Ȧ��ָ��״̬
		sb.append(STATUS_CIRCLE + " INTEGER,");//Ȧ�����״̬
		sb.append(STATUS_CIRCLE_REPORT + " INTEGER,");//Ȧ������ϱ�״̬
		sb.append(TAC + " VARCHAR(10)");//Ȧ��Ӧ��tac
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
