package com.example.database;

import android.database.Cursor;

public class Holder_Third_Human
{
	//sqlite中没有布尔型，干脆直接用int存，此处常量用于转换为外部正常使用的布尔型
	public static final int FALSE_HERE = 0;//失败
	public static final int TRUE_HERE = 1;//成功
	public static final String THIRDPAY_USERNAME = "00000000";//补登用户名
	
	private   String Username ;//登录用户名
	private   String Order_Time ;//订单时间
	private   String Order_NO ;//订单号
	private   String Order_Seq ;//订单流水号
	private   String Order_Amount ;//订单金额
	private   String Order_Cash ;//实收金额
	private   String Publish_CardNO ;//发行卡号
	private   String BankCardID ;//银行卡号
	private   int Status_Request ;//是否请求到订单
	private   int Status_GotCircle ;//是否请求到圈存数据
	private   int Status_Circle ;//圈存状态
	private   int Status_Circle_Report ;//圈存完成上报状态
	private   String Tac ;//tac
	
	/**
	 * 根据整形创建 数据库单条记录
	 * @param username 登录用户名
	 * @param OrderTime 订单时间
	 * @param OrderNO 订单号
	 * @param OrderSeq 订单流水号
	 * @param OrderAmount 订单金额
	 * @param OrderCash 实收金额
	 * @param PublishCardNO 发行卡号
	 * @param BankCardID 银行卡号
	 * @param StatusRequest 申请补登订单状态
	 * @param StatusGotCircle 获取圈存指令状态
	 * @param StatusCircle 圈存成功状态
	 * @param StatusCircleReport 圈存成功上报状态
	 * @param Tac 圈存成功应答tac
	 * @throws CustomTypeException 状态不为 0，1 时抛出异常
	 */
	public Holder_Third_Human(String username, String OrderTime, String OrderNO, String OrderSeq,
			String OrderAmount, String OrderCash, String PublishCardNO, String BankCardID,
			int StatusRequest, int StatusGotCircle, int StatusCircle, int StatusCircleReport,
			String Tac) throws CustomTypeException
	{
		if( (0 != StatusRequest) && (1 != StatusRequest)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusGotCircle) && (1 != StatusGotCircle)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusCircle) && (1 != StatusCircle)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 
		if( (0 != StatusCircleReport) && (1 != StatusCircleReport)) throw new CustomTypeException("Holder_Third_Human 创建时int型只能为0或1"); 

		setUsername(username);
		setOrder_Time(OrderTime);
		setOrder_NO(OrderNO);
		setOrder_Seq(OrderSeq);
		setOrder_Amount(OrderAmount);
		setOrder_Cash(OrderCash);
		setPublish_CardNO(PublishCardNO);
		setBankCardID(BankCardID);
		setStatus_Request(StatusRequest);
		setStatus_GotCircle(StatusGotCircle);
		setStatus_Circle(StatusCircle);
		setStatus_Circle_Report(StatusCircleReport);
		setTac(Tac);
	}
	
	/**
	 * 根据布尔型创建 数据库单条记录
	 * @param username 登录用户名
	 * @param OrderTime 订单时间
	 * @param OrderNO 订单号
	 * @param OrderSeq 订单流水号
	 * @param OrderAmount 订单金额
	 * @param OrderCash 实收金额
	 * @param PublishCardNO 发行卡号
	 * @param BankCardID 银行卡号
	 * @param StatusRequest 申请补登订单状态
	 * @param StatusGotCircle 获取圈存指令状态
	 * @param StatusCircle 圈存成功状态
	 * @param StatusCircleReport 圈存成功上报状态
	 * @param Tac 圈存成功应答tac
	 * @throws CustomTypeException 状态不为 0，1 时抛出异常
	 */
	public Holder_Third_Human(String username, String OrderTime, String OrderNO, String OrderSeq,
			String OrderAmount, String OrderCash, String PublishCardNO, String BankCardID,
			boolean isStatusRequest, boolean isStatusGotCircle, boolean isStatusCircle, boolean isStatusCircleReport,
			String Tac) 
	{

		setUsername(username);
		setOrder_Time(OrderTime);
		setOrder_NO(OrderNO);
		setOrder_Seq(OrderSeq);
		setOrder_Amount(OrderAmount);
		setOrder_Cash(OrderCash);
		setPublish_CardNO(PublishCardNO);
		setBankCardID(BankCardID);
		set_isStatus_Request(isStatusRequest);
		set_isStatus_GotCircle(isStatusGotCircle);
		set_isStatus_Circle(isStatusCircle);
		set_isStatus_Circle_Report(isStatusCircleReport);
		setTac(Tac);
	}
	
	
	/**
	 * 根据数据库查询返回的cursor 生成记录 
	 * cursor 为空直接返回
	 * @param c
	 */
	public Holder_Third_Human(Cursor c)
	{
		if(0 == c.getCount())	return;//内容为空
		
		setUsername(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.USERNAME)));
		setOrder_Time(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_TIME)));
		setOrder_NO(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_NO)));
		setOrder_Seq(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_SEQ)));
		setOrder_Amount(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_AMOUNT)));
		setOrder_Cash(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.ORDER_CASH)));
		setPublish_CardNO(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.PUBLISH_CARDNO)));
		setBankCardID(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.BANKCARDID)));
		setStatus_Request(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_REQUEST)));
		setStatus_GotCircle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_GOTCIRCLE)));
		setStatus_Circle(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE)));
		setStatus_Circle_Report(c.getInt(c.getColumnIndex(DbOpenHelper_Third_Human.STATUS_CIRCLE_REPORT)));
		setTac(c.getString(c.getColumnIndex(DbOpenHelper_Third_Human.TAC)));
	}
	
	/**
	 * 创建空表单项
	 */
	public Holder_Third_Human()
	{
		
	}
	
	public String getUsername()
	{
		return Username;
	}

	/**
	 * 当为补登时，不需要登录，设置用户名为"00000000"
	 * @param username
	 */
	private void setUsername(String username)
	{
		if(null == username)//补登
		{
			Username = THIRDPAY_USERNAME;//"00000000";
		}
		else {
			Username = username;
		}
	}


	public String getOrder_Time()
	{
		return Order_Time;
	}


	private void setOrder_Time(String order_Time)
	{
		Order_Time = order_Time;
	}


	public String getOrder_NO()
	{
		return Order_NO;
	}


	private void setOrder_NO(String order_NO)
	{
		Order_NO = order_NO;
	}


	public String getOrder_Seq()
	{
		return Order_Seq;
	}


	private void setOrder_Seq(String order_Seq)
	{
		Order_Seq = order_Seq;
	}


	public String getOrder_Amount()
	{
		return Order_Amount;
	}


	private void setOrder_Amount(String order_Amount)
	{
		Order_Amount = order_Amount;
	}


	public String getOrder_Cash()
	{
		return Order_Cash;
	}


	private void setOrder_Cash(String order_Cash)
	{
		if(null == order_Cash)
		{
			Order_Cash = "0";
		}
		else
		{	
			Order_Cash = order_Cash;
	
		}
	}

	public String getPublish_CardNO()
	{
		return Publish_CardNO;
	}


	private void setPublish_CardNO(String publish_CardNO)
	{
		Publish_CardNO = publish_CardNO;
	}


	public String getBankCardID()
	{
		return BankCardID;
	}


	private void setBankCardID(String bankCardID)
	{
		BankCardID = bankCardID;
	}


	public int getStatus_Request()
	{
		return Status_Request;
	}
		
	public boolean isStatus_Request()
	{
		return ((TRUE_HERE == Status_Request) ? true : false);
	}
	
	public void setStatus_Request(int status_Request)
	{
		Status_Request = status_Request;
	}
	
	public void set_isStatus_Request(boolean isStatusRequest)
	{
		if(true == isStatusRequest)
			setStatus_Request(TRUE_HERE);
		else
			setStatus_Request(FALSE_HERE);
	}
	

	public int getStatus_GotCircle()
	{
		return Status_GotCircle;
	}
	
	public boolean isStatus_GotCircle()
	{
		return ((TRUE_HERE == Status_GotCircle) ? true : false);
	}

	public void setStatus_GotCircle(int status_GotCircle)
	{
		Status_GotCircle = status_GotCircle;
	}
	
	public void set_isStatus_GotCircle(boolean isStatus_GotCircle)
	{
		if(true == isStatus_GotCircle)
			setStatus_GotCircle(TRUE_HERE);
		else
			setStatus_GotCircle(FALSE_HERE);
	}

	public int getStatus_Circle()
	{
		return Status_Circle;
	}
	
	public boolean isStatus_Circle()
	{
		return ((TRUE_HERE == Status_Circle) ? true : false);
	}

	public void setStatus_Circle(int status_Circle)
	{
		Status_Circle = status_Circle;
	}
	
	public void set_isStatus_Circle(boolean isStatus_Circle)
	{
		if(true == isStatus_Circle)
			setStatus_Circle(TRUE_HERE);
		else
			setStatus_Circle(FALSE_HERE);
	}

	public int getStatus_Circle_Report()
	{
		return Status_Circle_Report;
	}
	
	public boolean isStatus_Circle_Report()
	{
		return ((TRUE_HERE == Status_Circle_Report) ? true : false);
	}

	public void setStatus_Circle_Report(int status_Circle_Report)
	{
		Status_Circle_Report = status_Circle_Report;
	}
	
	public void set_isStatus_Circle_Report(boolean isStatus_Circle_Report)
	{
		if(true == isStatus_Circle_Report)
			setStatus_Circle_Report(TRUE_HERE);
		else
			setStatus_Circle_Report(FALSE_HERE);
	}

	public String getTac()
	{
		return Tac;
	}


	public void setTac(String tac)
	{
		String temp = tac;
		if(null == temp)
		{
			temp = "00000000";
		}
		if(temp.isEmpty())
		{
			temp = "12345678";
		}
		if(temp.length() > 8)
		{
			temp.substring(temp.length()-8);
		}
		Tac = tac;
	}
	
}
