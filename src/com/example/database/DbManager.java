package com.example.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 定义订单本地数据库访问接口
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
	 * 关闭sqlite数据库
	 */
    public void closeDB() {
        db.close();
    }

   /**
    * 向sqlite中插入ChargeHolder封装的数据
    * @param ch
    * @throws SQLException
    */
   public void InsertItem(ChargeHolder ch) throws SQLException
   {
	   
	   //先查询数据库中是否存在同一订单号，存在则退出不填加
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
	   
//	   public static final String ORDER_TIME = "OrderTime";//订单时间
//		public static final String ORDER_SEQ = "OrderSEQ";//订单号
//		public static final String ORDER_REQTRANSE = "OrderReqTranse";//订单流水号
//		public static final String ORDER_AMOUNT = "Amount";//订单金额
//		public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//发行卡号，用于标记给那张卡充值
//		public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//翼支付支付状态
//		public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//圈存写卡状态
//		public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//圈存闭环状态
	   values.put(DbOpenHelper_charge.ORDER_TIME, ch.getOrder_Time());
	   values.put(DbOpenHelper_charge.ORDER_SEQ, ch.getOrder_Seq());
	   values.put(DbOpenHelper_charge.ORDER_REQTRANSE, ch.getOrder_Reqtranse());
	   values.put(DbOpenHelper_charge.ORDER_AMOUNT, ch.getOrder_Amount());
	   values.put(DbOpenHelper_charge.ORDER_PUBLISH_CARDID, ch.getOrder_Publish_CardID());
	   values.put(DbOpenHelper_charge.PAYMETHOD, ch.getPayMethod());//支付方式  
	   values.put(DbOpenHelper_charge.ORDER_CHARGE_STATUS, ch.getOrder_Charge_Status_Int());
	   values.put(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS, ch.getOrder_Charge_NFC_Status_Int());
	   values.put(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS, ch.getOrder_Transference_Close_Status_Int());
	   values.put(DbOpenHelper_charge.TAC_NFCCARD, ch.getTAC());//圈存成功TAC应答
	   
	   db.insertOrThrow(DbOpenHelper_charge.TABLE, null,values);//DbOpenHelper_charge.ID, values);
	   
	   c = db.query(DbOpenHelper_charge.TABLE, null, null, null, null, null, null);
	   while(c.moveToNext())
	   {
		   System.out.println("insert db liushui "+c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID))+"_"+
				   c.getString(c.getColumnIndex(DbOpenHelper_charge.ORDER_TIME))+ "相同的数量："+c.getCount());
	   }
	   c.close();
			   
   }
    
   /**
    * query by ORDER_SEQ 按订单号查询
    * @return Cursor指针
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
    * query by ORDER_REQTRANSE 按订单流水号查询  
    * @return Cursor指针
    */
   public Cursor QueryOrderbyReqTranse(String OrderReqTranse)
   {
	   String selection = DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?";
	   String[] selectionArgs = new String[]{""+OrderReqTranse};
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   
   /**
    * 按照无效订单查询（支付失败，圈存失败）
    * @param OrderReqTranse 订单流水号
    * @return Cursor指针
    */
   public Cursor QueryByInvalidOrder(String OrderReqTranse)
   {
	   String selection = DbOpenHelper_charge.ORDER_REQTRANSE + "=" + "?" + " " + "AND" + " " +
			      DbOpenHelper_charge.ORDER_CHARGE_STATUS + "=" + "?" + " " + "AND" + " " +
	   			  DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS + "=" + "?";
	   //充值失败，圈存失败，指定订单流水号
	   String[] selectionArgs = new String[]{""+OrderReqTranse, ""+ChargeHolder.FALSE_HERE, ""+ChargeHolder.FALSE_HERE};
   
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   /**
    * query by ORDER_SEQ 按卡号查询
    * @return Cursor指针
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
    * query by id 按序号查询
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
    * query by publishCardNO 按发行卡号查询
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
    * query by Transference fail status 按上报圈存流程失败状态查询
    * 
    */
   public Cursor QueryByTransferenceFail()
   {
//	   DbOpenHelper_menu.GROUPID+"="+"?" +" "+ "AND" +" " +
//				DbOpenHelper_menu.SUB_NAME + "=" + "?", new String[]{""+gourpID, sub_menu_name}) > 0)
	   String selection = DbOpenHelper_charge.ORDER_CHARGE_STATUS + "=" + "?" + " " + "AND" + " " +
			   			  DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS + "=" + "?" + " " + "AND" + " " + 
			   			  DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS + "=" + "?";
	   //充值完成，圈存完成，但是圈存完成报告上报失败
	   String[] selectionArgs = new String[]{""+ChargeHolder.TRUE_HERE, ""+ChargeHolder.TRUE_HERE, ""+ChargeHolder.FALSE_HERE};
	   
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, selection, selectionArgs, null, null, null);
	   
	   return c;
   }
   
   
   /**
    * query all data 查询sqlite中所有数据
    */
   public Cursor QueryOrderAll()
   {
	   String orderBy = DbOpenHelper_charge.ORDER_TIME + " DESC";
	   Cursor c = db.query(DbOpenHelper_charge.TABLE, null, null, null, null, null, orderBy);
	   
	   return c;
   }
   
   /**
    * 根据订单流水号更新订单状态
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
	   
	   //程序运行中，可能经常改变的量
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
    * delete by ORDER_REQTRANSE,根据订单流水号，删除表项
    * @return 删除的行数
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


