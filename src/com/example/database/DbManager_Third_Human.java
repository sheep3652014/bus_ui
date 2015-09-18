package com.example.database;

import com.example.bus_ui_demo.aty_ThirdPartyPay_Circle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 用于补登 和人工充值 数据库管理
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
	 * 关闭sqlite数据库
	 */
    public void closeDB() {
        db.close();
    }
    
    /**
     * 向数据库插入一条数据
     * @param holder
     * @throws SQLException
     */
    public void InsertItem(Holder_Third_Human holder) throws SQLException
    {
    	 //先查询数据库中是否存在同一订单号，存在则退出不填加
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
 	   
// 	   	private   String Username ;//登录用户名
// 		private   String Order_Time ;//订单时间
// 		private   String Order_NO ;//订单号
// 		private   String Order_Seq ;//订单流水号
// 		private   String Order_Amount ;//订单金额
// 		private   String Order_Cash ;//实收金额
// 		private   String Publish_CardNO ;//发行卡号
// 		private   String BankCardID ;//银行卡号
// 		private   int Status_Request ;//是否请求到订单
// 		private   int Status_GotCircle ;//是否请求到圈存数据
// 		private   int Status_Circle ;//圈存状态
// 		private   int Status_Circle_Report ;//圈存完成上报状态
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
 	   values.put(DbOpenHelper_Third_Human.TAC, holder.getTac());//圈存成功TAC应答
 	   
 	   db.insertOrThrow(DbOpenHelper_Third_Human.TABLE, null,values);//DbOpenHelper_charge.ID, values);
 	   
 	   c = db.query(DbOpenHelper_Third_Human.TABLE, null, null, null, null, null, null);
 	   while(c.moveToNext())
 	   {
 		   System.out.println("insert db liushui "+c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_AMOUNT))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_CASH))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.PUBLISH_CARDNO))+"_"+
 				   c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME))+ "相同的数量："+c.getCount());
 	   }
 	   c.close();
    }
    
    
    
    /**
     * 根据订单号查询
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
     * 根据登录用户名查询
     * 补登用户名为"00000000"
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
     * 根据状态查找订单
     * @param StatusRequest int型 等于 0，1
     * @param StatusGotCircle int型 等于 0，1
     * @param StatusCircle int型 等于 0，1
     * @param StatusCircleReport int型 等于 0，1
     * @return
     * @throws CustomTypeException 状态不为0，1 抛异常
     */
    public Cursor QueryOrderbyStatus(int StatusRequest, int StatusGotCircle, int StatusCircle,
    		int StatusCircleReport) throws CustomTypeException
    {
// 		private   int Status_Request ;//是否请求到订单
// 		private   int Status_GotCircle ;//是否请求到圈存数据
// 		private   int Status_Circle ;//圈存状态
// 		private   int Status_Circle_Report ;//圈存完成上报状态
    	
    	if( (0 != StatusRequest) && (1 != StatusRequest)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusGotCircle) && (1 != StatusGotCircle)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusCircle) && (1 != StatusCircle)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusCircleReport) && (1 != StatusCircleReport)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
    	
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
     * 根据状态查找订单
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
// 		private   int Status_Request ;//是否请求到订单
// 		private   int Status_GotCircle ;//是否请求到圈存数据
// 		private   int Status_Circle ;//圈存状态
// 		private   int Status_Circle_Report ;//圈存完成上报状态
    	
    	int StatusRequest = ((true == isStatusRequest) ? 1 : 0);
    	int StatusGotCircle = ((true == isStatusGotCircle) ? 1 : 0);
    	int StatusCircle = ((true == isStatusCircle) ? 1 : 0);
    	int StatusCircleReport = ((true == isStatusCircleReport) ? 1 : 0);
    	
    	return QueryOrderbyStatus(StatusRequest, StatusGotCircle, StatusCircle, StatusCircleReport);
    }
    
    /**
     * 根据发行卡号查询
     * 升序排列
     * @param publishCardNO
     * @return
     */
    public Cursor QueryOrderbyPublishCardNO(String publishCardNO)
    {
    	String orderBy = DbOpenHelper_Third_Human.ORDER_TIME + "";//默认升序ASC//" DESC";
    	String selection = DbOpenHelper_Third_Human.PUBLISH_CARDNO + "=" + "?";
  	   	String[] selectionArgs = new String[]{""+publishCardNO};
  	   	Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, orderBy);
  	   
  	   return c;
    }
    
    
    /**
     * 根据单条表项内容更新该表项对应的部分内容
     * <p>更新内容： 
     * 		   <p>补登数据获取状态
     * 		   <p>获取圈存指令状态
     * 		   <p>圈存完成状态
     * 		   <p>圈存完成状态上报状态
     * 		   <p>圈存tac应答
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
 	   
 	   //程序运行中，可能经常改变的量
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
     * 查找圈存成功状态上报失败记录
     * @return
     */
    public Cursor QueryByReportFail()
    {
// 	   DbOpenHelper_menu.GROUPID+"="+"?" +" "+ "AND" +" " +
// 				DbOpenHelper_menu.SUB_NAME + "=" + "?", new String[]{""+gourpID, sub_menu_name}) > 0)
 	   String selection = DbOpenHelper_Third_Human.STATUS_GOTCIRCLE + "=" + "?" + " " + "AND" + " " +
 			   			  DbOpenHelper_Third_Human.STATUS_CIRCLE + "=" + "?" + " " + "AND" + " " + 
 			   			  DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT + "=" + "?";
 	   //充值完成，圈存完成，但是圈存完成报告上报失败
 	   String[] selectionArgs = new String[]{""+Holder_Third_Human.TRUE_HERE, ""+Holder_Third_Human.TRUE_HERE, ""+Holder_Third_Human.FALSE_HERE};
 	   
 	   Cursor c = db.query(DbOpenHelper_Third_Human.TABLE, null, selection, selectionArgs, null, null, null);
 	   
 	   return c;
    }
    
    /**
	 * 根据订单号查找订单状态
	 * @param OrderNO
	 * @return 返回
	 * 		   <p>补登信息申请状态
	 * 		   <p>获取圈存信息状态
	 * 		   <p>圈存成功状态
	 * 		   <p>圈存成功状态上报状态
	 * 		   <P>tac
	 */
	public Holder_Third_Human QueryOrderStatus(String OrderNO)
	{
		Holder_Third_Human holder = new Holder_Third_Human();
		
		Cursor c = QueryOrderbyOrderNO(OrderNO);
		System.out.println("orderno = " + OrderNO + " ,订单号_" + c.getCount());
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
