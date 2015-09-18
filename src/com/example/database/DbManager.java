package com.example.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ���嶩���������ݿ���ʽӿ�
 * @author Administrator
 *
 */
public class DbManager
{
	private static final String TAG = "DbManager";
	
	private DbOpenHelper_charge dbOpenHelper;
	private SQLiteDatabase db;

	public DbManager(Context context)
	{
		// TODO Auto-generated constructor stub
		dbOpenHelper = new DbOpenHelper_charge(context);
		db = dbOpenHelper.getWritableDatabase();
	}
	
	/**
	 * �ر�sqlite���ݿ�
	 */
    public void closeDB() {
        db.close();
    }

   /**
    * ��sqlite�в���ChargeHolder��װ������
    * @param ch
    * @throws SQLException
    */
   public void InsertItem(ChargeHolder ch) throws SQLException
   {
	   
	   //�Ȳ�ѯ���ݿ����Ƿ����ͬһ�����ţ��������˳������
	   Cursor c = QueryOrderbyReqTranse(ch.getOrder_Reqtranse());
	   
	   if(0!=c.getCount())
	   {
		   c.moveToNext();
		   Log.d("insert db", " time "+c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_TIME)));
		   System.out.println("liushui"+ " time "+c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_TIME)));
		 
		   c.close();
		   
		   return;
	   }
	   
	   
	   ContentValues values = new ContentValues();
	   
//	   public static final String ORDER_TIME = "OrderTime";//����ʱ��
//		public static final String ORDER_SEQ = "OrderSEQ";//������
//		public static final String ORDER_REQTRANSE = "OrderReqTranse";//������ˮ��
//		public static final String ORDER_AMOUNT = "Amount";//�������
//		public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//���п��ţ����ڱ�Ǹ����ſ���ֵ
//		public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//��֧��֧��״̬
//		public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//Ȧ��д��״̬
//		public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//Ȧ��ջ�״̬
	   values.put(DbOpenHelper_charge.ORDER_TIME, ch.getOrder_Time());
	   values.put(DbOpenHelper_charge.ORDER_SEQ, ch.getOrder_Seq());
	   values.put(DbOpenHelper_charge.ORDER_REQTRANSE, ch.getOrder_Reqtranse());
	   values.put(DbOpenHelper_charge.ORDER_AMOUNT, ch.getOrder_Amount());
	   values.put(DbOpenHelper_charge.ORDER_PUBLISH_CARDID, ch.getOrder_Publish_CardID());
	   values.put(DbOpenHelper_charge.PAYMETHOD, ch.getPayMethod());//֧����ʽ  
	   values.put(DbOpenHelper_charge.ORDER_CHARGE_STATUS, ch.getOrder_Charge_Status_Int());
	   values.put(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS, ch.getOrder_Charge_NFC_Status_Int());
	   values.put(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS, ch.getOrder_Transference_Close_Status_Int());
	   values.put(DbOpenHelper_charge.TAC_NFCCARD, ch.getTAC());//Ȧ��ɹ�TACӦ��
	   
	   db.insertOrThrow(DbOpenHelper_charge.TABLE, null,values);//DbOpenHelper_charge.ID, values);
	   
	   c = db.query(DbOpenHelper_charge.TABLE, null, null, null, null, null, null);
	   while(c.moveToNext())
	   {
		   System.out.println("insert db liushui "+c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_TIME))+ "��ͬ��������"+c.getCount());
	   }
	   c.close();
			   
   }
    
   /**
    * query by ORDER_SEQ �������Ų�ѯ
    * @return Cursorָ��
    */
   public Cursor QueryOrderbySeqNO(String OrderSeq)
   {
//	   Cursor c = db.query(DbOpenHelper_charge.MENU_TABLE, null, null, null, DbOpenHelper_charge.ORDER_SEQ, null, null);
	   String selection = DbOpenHelper_charge.ORDER_SEQ + "=" + "?";
	   String[] selectionArgs = new String[]{""+OrderSeq};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   
	   
	   return c;
   }
   
   /**
    * query by ORDER_REQTRANSE ��������ˮ�Ų�ѯ  
    * @return Cursorָ��
    */
   public Cursor QueryOrderbyReqTranse(String OrderReqTranse)
   {
	   String selection = DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?";
	   String[] selectionArgs = new String[]{""+OrderReqTranse};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   
   /**
    * ������Ч������ѯ��֧��ʧ�ܣ�Ȧ��ʧ�ܣ�
    * @param OrderReqTranse ������ˮ��
    * @return Cursorָ��
    */
   public Cursor QueryByInvalidOrder(String OrderReqTranse)
   {
	   String selection = DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?" + " " + "AND" + " " +
			      DbOpenHelper_charge.ORDER_CHARGE_STATUS + "=" + "?" + " " + "AND" + " " +
	   			  DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS + "=" + "?";
	   //��ֵʧ�ܣ�Ȧ��ʧ�ܣ�ָ��������ˮ��
	   String[] selectionArgs = new String[]{""+OrderReqTranse, ""+ChargeHolder.FALSE_HERE, ""+ChargeHolder.FALSE_HERE};
   
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   /**
    * query by ORDER_SEQ �����Ų�ѯ
    * @return Cursorָ��
    */
   public Cursor QueryOrderbyPublisCardNO(String PublishCardID)
   {
//	   Cursor c = db.query(DbOpenHelper_charge.MENU_TABLE, null, null, null, DbOpenHelper_charge.ORDER_CARDID, null, null);
	   String selection = DbOpenHelper_charge.ORDER_PUBLISH_CARDID + "=" + "?";
	   String[] selectionArgs = new String[]{""+PublishCardID};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   
	   return c;
   }
   
   /**
    * query by id ����Ų�ѯ
    * @param id
    * @return
    */
   public Cursor QueryOrderbyID(String id)
   {
	   String selection = DbOpenHelper_charge.ID + "=" + "?";
	   String[] selectionArgs = new String[]{""+id};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   /**
    * query by publishCardNO �����п��Ų�ѯ
    * @param PublishCardNO
    * @return
    */
   public Cursor QueryOrderbyPublishCardNO(String PublishCardNO)
   {
	   String selection = DbOpenHelper_charge.ORDER_PUBLISH_CARDID + "=" + "?";
	   String[] selectionArgs = new String[]{PublishCardNO};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   /**
    * query by Transference fail status ���ϱ�Ȧ������ʧ��״̬��ѯ
    * 
    */
   public Cursor QueryByTransferenceFail()
   {
//	   DbOpenHelper_menu.GROUPID+"="+"?" +" "+ "AND" +" " +
//				DbOpenHelper_menu.SUB_NAME + "=" + "?", new String[]{""+gourpID, sub_menu_name}) > 0)
	   String selection = DbOpenHelper_charge.ORDER_CHARGE_STATUS + "=" + "?" + " " + "AND" + " " +
			   			  DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS + "=" + "?" + " " + "AND" + " " + 
			   			  DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS + "=" + "?";
	   //��ֵ��ɣ�Ȧ����ɣ�����Ȧ����ɱ����ϱ�ʧ��
	   String[] selectionArgs = new String[]{""+ChargeHolder.TRUE_HERE, ""+ChargeHolder.TRUE_HERE, ""+ChargeHolder.FALSE_HERE};
	   
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   
   /**
    * query all data ��ѯsqlite����������
    */
   public Cursor QueryOrderAll()
   {
	   String orderBy = DbOpenHelper_charge.ORDER_TIME + " DESC";
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, null, null, null, null, orderBy);
	   
	   return c;
   }
   
   /**
    * ���ݶ�����ˮ�Ÿ��¶���״̬
    * @param ORDER_REQTRANSE
    */
   public void UpdateOrderbyReqTranse(ChargeHolder ch)
   {
	   Cursor c = QueryOrderbyReqTranse(ch.getOrder_Reqtranse());
//	   if(true) return;
	   if(0 == c.getCount()) 
	   {
		   Log.d("-->", "find null"+ ch.getOrder_Reqtranse());
		   c.close();
		   return;//
	   }
	   c.close();
	   
	   ContentValues cv = new ContentValues();
	   
	   //���������У����ܾ����ı����
	   cv.put(DbOpenHelper_charge.ORDER_CHARGE_STATUS, ch.getOrder_Charge_Status_Int()); 
	   cv.put(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS, ch.getOrder_Charge_NFC_Status_Int()); 
	   cv.put(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS, ch.getOrder_Transference_Close_Status_Int()); 
	   cv.put(DbOpenHelper_charge.TAC_NFCCARD, ch.getTAC());
	   
	   String whereClause =	DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?";
	   String[] whereArgs = new String[]{ch.getOrder_Reqtranse()}; 
	   
	   int row = db.update(DbOpenHelper_charge.TABLE, cv, whereClause, whereArgs);
	   Log.d("-->", "update row = " + row );
   }

   /**
    * delete by ORDER_REQTRANSE,���ݶ�����ˮ�ţ�ɾ������
    * @return ɾ��������
    */
   public int DeletebyReqTranse(String Order_ReqTranse)
   {
	   String whereClause = DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?";
	   String[] whereArgs = new String[]{Order_ReqTranse};
	   
	   return(db.delete(DbOpenHelper_charge.TABLE, whereClause, whereArgs));
   }
   
   //return(db.delete(DbOpenHelper_menu.MENU_TABLE, DbOpenHelper_menu.GROUPID+"="+"?" +" "+ "AND" +" " +
//		DbOpenHelper_menu.SUB_NAME + "=" + "?", new String[]{""+gourpID, sub_menu_name}) > 0);

}


