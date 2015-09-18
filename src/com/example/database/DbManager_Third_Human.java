package com.example.database;

import com.example.bus_ui_demo.aty_ThirdPartyPay_Circle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ���ڲ��� ���˹���ֵ ���ݿ����
 * @author tom
 *
 */
public class DbManager_Third_Human
{
	private static final String TAG = "DbManager_Third_Human";
	
	private DbOpenHelper_charge dbOpenHelper;
	private SQLiteDatabase db;

	public DbManager_Third_Human(Context context)
	{
		// TODO Auto-generated constructor stub
		dbOpenHelper = new DbOpenHelper_charge(context);//new DbOpenHelper_Third_Human(context);
		db = dbOpenHelper.getWritableDatabase();
	}
	
	/**
	 * �ر�sqlite���ݿ�
	 */
    public void closeDB() {
        db.close();
    }
    
    /**
     * �����ݿ����һ������
     * @param holder
     * @throws SQLException
     */
    public void InsertItem(Holder_Third_Human holder) throws SQLException
    {
    	 //�Ȳ�ѯ���ݿ����Ƿ����ͬһ�����ţ��������˳������
 	   Cursor c = QueryOrderbyOrderNO(holder.getOrder_NO());
 	   
 	   if(0!=c.getCount())
 	   {
 		   c.moveToNext();
 		   Log.d("insert db", " time "+c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME)));
 		   System.out.println("order"+ " time "+c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME)));
 		 
 		   c.close();
 		   
 		   return;
 	   }
 	   
 	   ContentValues values = new ContentValues();
 	   
// 	   	private   String Username ;//��¼�û���
// 		private   String Order_Time ;//����ʱ��
// 		private   String Order_NO ;//������
// 		private   String Order_Seq ;//������ˮ��
// 		private   String Order_Amount ;//�������
// 		private   String Order_Cash ;//ʵ�ս��
// 		private   String Publish_CardNO ;//���п���
// 		private   String BankCardID ;//���п���
// 		private   int Status_Request ;//�Ƿ����󵽶���
// 		private   int Status_GotCircle ;//�Ƿ�����Ȧ������
// 		private   int Status_Circle ;//Ȧ��״̬
// 		private   int Status_Circle_Report ;//Ȧ������ϱ�״̬
// 		private   String Tac ;//tac
 	   values.put(DbOpenHelper_Third_Human.USERNAME, holder.getUsername());
 	   values.put(DbOpenHelper_Third_Human.ORDER_TIME, holder.getOrder_Time());
 	   values.put(DbOpenHelper_Third_Human.ORDER_NO, holder.getOrder_NO());
 	   values.put(DbOpenHelper_Third_Human.ORDER_SEQ, holder.getOrder_Seq());
 	   values.put(DbOpenHelper_Third_Human.ORDER_AMOUNT, holder.getOrder_Amount());
 	   values.put(DbOpenHelper_Third_Human.ORDER_CASH, holder.getOrder_Cash());
 	   values.put(DbOpenHelper_Third_Human.PUBLISH_CARDNO, holder.getPublish_CardNO());
 	   values.put(DbOpenHelper_Third_Human.BANKCARDID, holder.getBankCardID());
 	   values.put(DbOpenHelper_Third_Human.STATUS_REQUEST, holder.getStatus_Request());
 	   values.put(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE, holder.getStatus_GotCircle());
 	   values.put(DbOpenHelper_Third_Human.STATUS_CIRCLE, holder.getStatus_Circle());
 	   values.put(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT, holder.getStatus_Circle_Report());
 	   values.put(DbOpenHelper_Third_Human.TAC, holder.getTac());//Ȧ��ɹ�TACӦ��
 	   
 	   db.insertOrThrow(DbOpenHelper_Third_Human.TABLE, null,values);//DbOpenHelper_charge.ID, values);
 	   
 	   c = db.query(DbOpenHelper_Third_Human.TABLE, null, null, null, null, null, null);
 	   while(c.moveToNext())
 	   {
 		   System.out.println("insert db liushui "+c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_AMOUNT))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_CASH))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.PUBLISH_CARDNO))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME))+ "��ͬ��������"+c.getCount());
 	   }
 	   c.close();
    }
    
    
    
    /**
     * ���ݶ����Ų�ѯ
     * @param OrderNO
     * @return
     */
    public Cursor QueryOrderbyOrderNO(String OrderNO)
    {
    	String selection = DbOpenHelper_Third_Human.ORDER_NO + "=" + "?";
  	   	String[] selectionArgs = new String[]{""+OrderNO};
  	   	Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, null);
  	   
  	   return c;
    }
    
    /**
     * ���ݵ�¼�û�����ѯ
     * �����û���Ϊ"00000000"
     * @param username
     * @return
     */
    public Cursor QueryOrderbyUserName(String username)
    {
    	String selection = DbOpenHelper_Third_Human.USERNAME + "=" + "?";
  	   	String[] selectionArgs = new String[]{""+username};
  	   	Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, null);
  	   
  	   return c;
    }
    
    
    /**
     * ����״̬���Ҷ���
     * @param StatusRequest int�� ���� 0��1
     * @param StatusGotCircle int�� ���� 0��1
     * @param StatusCircle int�� ���� 0��1
     * @param StatusCircleReport int�� ���� 0��1
     * @return
     * @throws CustomTypeException ״̬��Ϊ0��1 ���쳣
     */
    public Cursor QueryOrderbyStatus(int StatusRequest, int StatusGotCircle, int StatusCircle,
    		int StatusCircleReport) throws CustomTypeException
    {
// 		private   int Status_Request ;//�Ƿ����󵽶���
// 		private   int Status_GotCircle ;//�Ƿ�����Ȧ������
// 		private   int Status_Circle ;//Ȧ��״̬
// 		private   int Status_Circle_Report ;//Ȧ������ϱ�״̬
    	
    	if( (0 != StatusRequest) && (1 != StatusRequest)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusGotCircle) && (1 != StatusGotCircle)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusCircle) && (1 != StatusCircle)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusCircleReport) && (1 != StatusCircleReport)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
    	
    	String selection = DbOpenHelper_Third_Human.STATUS_REQUEST + "=" + "?" + " " + "AND" + " " +
    			 DbOpenHelper_Third_Human.STATUS_GOTCIRCLE + "=" + "?" + " " + "AND" + " " + 
    			 DbOpenHelper_Third_Human.STATUS_CIRCLE + "=" + "?" + " " + "AND" + " " + 
    			 DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT + "=" + "?";
    	String[] selectionArgs = new String[]{""+StatusRequest, ""+StatusGotCircle, ""+StatusCircle, 
    										  ""+StatusCircleReport};
  	   
    	Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, null);
    	
    	return c;
    }
    
    /**
     * ����״̬���Ҷ���
     * @param isStatusRequest boolean
     * @param isStatusGotCircle boolean
     * @param isStatusCircle boolean
     * @param isStatusCircleReport boolean
     * @return
     * @throws CustomTypeException
     */
    public Cursor QueryOrderbyStatus(boolean isStatusRequest, boolean isStatusGotCircle, 
    		boolean isStatusCircle, boolean isStatusCircleReport) throws CustomTypeException
    {
// 		private   int Status_Request ;//�Ƿ����󵽶���
// 		private   int Status_GotCircle ;//�Ƿ�����Ȧ������
// 		private   int Status_Circle ;//Ȧ��״̬
// 		private   int Status_Circle_Report ;//Ȧ������ϱ�״̬
    	
    	int StatusRequest = ((true == isStatusRequest) ? 1 : 0);
    	int StatusGotCircle = ((true == isStatusGotCircle) ? 1 : 0);
    	int StatusCircle = ((true == isStatusCircle) ? 1 : 0);
    	int StatusCircleReport = ((true == isStatusCircleReport) ? 1 : 0);
    	
    	return QueryOrderbyStatus(StatusRequest, StatusGotCircle, StatusCircle, StatusCircleReport);
    }
    
    /**
     * ���ݷ��п��Ų�ѯ
     * ��������
     * @param publishCardNO
     * @return
     */
    public Cursor QueryOrderbyPublishCardNO(String publishCardNO)
    {
    	String orderBy = DbOpenHelper_Third_Human.ORDER_TIME + "";//Ĭ������ASC//" DESC";
    	String selection = DbOpenHelper_Third_Human.PUBLISH_CARDNO + "=" + "?";
  	   	String[] selectionArgs = new String[]{""+publishCardNO};
  	   	Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, orderBy);
  	   
  	   return c;
    }
    
    
    /**
     * ���ݵ����������ݸ��¸ñ����Ӧ�Ĳ�������
     * <p>�������ݣ� 
     * 		   <p>�������ݻ�ȡ״̬
     * 		   <p>��ȡȦ��ָ��״̬
     * 		   <p>Ȧ�����״̬
     * 		   <p>Ȧ�����״̬�ϱ�״̬
     * 		   <p>Ȧ��tacӦ��
     * @param holder
     */
    public void UpdateOrderbyHolderItem(Holder_Third_Human holder)
    {
 	   Cursor c = QueryOrderbyOrderNO(holder.getOrder_NO());
// 	   if(true) return;
 	   if(0 == c.getCount()) 
 	   {
 		   Log.d("-->", "find null"+ holder.getOrder_NO());
 		   c.close();
 		   return;//
 	   }
 	   c.close();
 	   
 	   ContentValues cv = new ContentValues();
 	   
 	   //���������У����ܾ����ı����
 	   cv.put(DbOpenHelper_Third_Human.STATUS_REQUEST, holder.getStatus_Request()); 
 	   cv.put(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE, holder.getStatus_GotCircle()); 
 	   cv.put(DbOpenHelper_Third_Human.STATUS_CIRCLE, holder.getStatus_Circle()); 
 	   cv.put(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT, holder.getStatus_Circle_Report()); 
 	   cv.put(DbOpenHelper_Third_Human.TAC, holder.getTac());
 	   
 	   String whereClause =	DbOpenHelper_Third_Human.ORDER_NO + "=" + "?";
 	   String[] whereArgs = new String[]{holder.getOrder_NO()}; 
 	   
 	   int row = db.update(DbOpenHelper_Third_Human.TABLE, cv, whereClause, whereArgs);
 	   Log.d("-->", "update row = " + row );
    }

    /**
     * ����Ȧ��ɹ�״̬�ϱ�ʧ�ܼ�¼
     * @return
     */
    public Cursor QueryByReportFail()
    {
// 	   DbOpenHelper_menu.GROUPID+"="+"?" +" "+ "AND" +" " +
// 				DbOpenHelper_menu.SUB_NAME + "=" + "?", new String[]{""+gourpID, sub_menu_name}) > 0)
 	   String selection = DbOpenHelper_Third_Human.STATUS_GOTCIRCLE + "=" + "?" + " " + "AND" + " " +
 			   			  DbOpenHelper_Third_Human.STATUS_CIRCLE + "=" + "?" + " " + "AND" + " " + 
 			   			  DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT + "=" + "?";
 	   //��ֵ��ɣ�Ȧ����ɣ�����Ȧ����ɱ����ϱ�ʧ��
 	   String[] selectionArgs = new String[]{""+Holder_Third_Human.TRUE_HERE, ""+Holder_Third_Human.TRUE_HERE, ""+Holder_Third_Human.FALSE_HERE};
 	   
 	   Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, null);
 	   
 	   return c;
    }
    
    /**
	 * ���ݶ����Ų��Ҷ���״̬
	 * @param OrderNO
	 * @return ����
	 * 		   <p>������Ϣ����״̬
	 * 		   <p>��ȡȦ����Ϣ״̬
	 * 		   <p>Ȧ��ɹ�״̬
	 * 		   <p>Ȧ��ɹ�״̬�ϱ�״̬
	 * 		   <P>tac
	 */
	public Holder_Third_Human QueryOrderStatus(String OrderNO)
	{
		Holder_Third_Human holder = new Holder_Third_Human();
		
		Cursor c = QueryOrderbyOrderNO(OrderNO);
		System.out.println("orderno = " + OrderNO + " ,������_" + c.getCount());
		if(0 == c.getCount())
		{	
			return null;
		}
		c.moveToFirst();
		holder.setStatus_Request(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_REQUEST)));
		holder.setStatus_GotCircle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE)));
		holder.setStatus_Circle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE)));
		holder.setStatus_Circle_Report(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT)));
		holder.setTac(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.TAC)));
		c.close();
		return holder;
	}
}
