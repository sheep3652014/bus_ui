package com.example.database;

import android.database.Cursor;

public class Holder_Third_Human
{
	//sqlite��û�в����ͣ��ɴ�ֱ����int�棬�˴���������ת��Ϊ�ⲿ����ʹ�õĲ�����
	public static final int FALSE_HERE = 0;//ʧ��
	public static final int TRUE_HERE = 1;//�ɹ�
	public static final String THIRDPAY_USERNAME = "00000000";//�����û���
	
	private   String Username ;//��¼�û���
	private   String Order_Time ;//����ʱ��
	private   String Order_NO ;//������
	private   String Order_Seq ;//������ˮ��
	private   String Order_Amount ;//�������
	private   String Order_Cash ;//ʵ�ս��
	private   String Publish_CardNO ;//���п���
	private   String BankCardID ;//���п���
	private   int Status_Request ;//�Ƿ����󵽶���
	private   int Status_GotCircle ;//�Ƿ�����Ȧ������
	private   int Status_Circle ;//Ȧ��״̬
	private   int Status_Circle_Report ;//Ȧ������ϱ�״̬
	private   String Tac ;//tac
	
	/**
	 * �������δ��� ���ݿⵥ����¼
	 * @param username ��¼�û���
	 * @param OrderTime ����ʱ��
	 * @param OrderNO ������
	 * @param OrderSeq ������ˮ��
	 * @param OrderAmount �������
	 * @param OrderCash ʵ�ս��
	 * @param PublishCardNO ���п���
	 * @param BankCardID ���п���
	 * @param StatusRequest ���벹�Ƕ���״̬
	 * @param StatusGotCircle ��ȡȦ��ָ��״̬
	 * @param StatusCircle Ȧ��ɹ�״̬
	 * @param StatusCircleReport Ȧ��ɹ��ϱ�״̬
	 * @param Tac Ȧ��ɹ�Ӧ��tac
	 * @throws CustomTypeException ״̬��Ϊ 0��1 ʱ�׳��쳣
	 */
	public Holder_Third_Human(String username, String OrderTime, String OrderNO, String OrderSeq,
			String OrderAmount, String OrderCash, String PublishCardNO, String BankCardID,
			int StatusRequest, int StatusGotCircle, int StatusCircle, int StatusCircleReport,
			String Tac) throws CustomTypeException
	{
		if( (0 != StatusRequest) && (1 != StatusRequest)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusGotCircle) && (1 != StatusGotCircle)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusCircle) && (1 != StatusCircle)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != StatusCircleReport) && (1 != StatusCircleReport)) throw new CustomTypeException("Holder_Third_Human ����ʱint��ֻ��Ϊ0��1"); 

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
	 * ���ݲ����ʹ��� ���ݿⵥ����¼
	 * @param username ��¼�û���
	 * @param OrderTime ����ʱ��
	 * @param OrderNO ������
	 * @param OrderSeq ������ˮ��
	 * @param OrderAmount �������
	 * @param OrderCash ʵ�ս��
	 * @param PublishCardNO ���п���
	 * @param BankCardID ���п���
	 * @param StatusRequest ���벹�Ƕ���״̬
	 * @param StatusGotCircle ��ȡȦ��ָ��״̬
	 * @param StatusCircle Ȧ��ɹ�״̬
	 * @param StatusCircleReport Ȧ��ɹ��ϱ�״̬
	 * @param Tac Ȧ��ɹ�Ӧ��tac
	 * @throws CustomTypeException ״̬��Ϊ 0��1 ʱ�׳��쳣
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
	 * �������ݿ��ѯ���ص�cursor ���ɼ�¼ 
	 * cursor Ϊ��ֱ�ӷ���
	 * @param c
	 */
	public Holder_Third_Human(Cursor c)
	{
		if(0 == c.getCount())	return;//����Ϊ��
		
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
	 * �����ձ���
	 */
	public Holder_Third_Human()
	{
		
	}
	
	public String getUsername()
	{
		return Username;
	}

	/**
	 * ��Ϊ����ʱ������Ҫ��¼�������û���Ϊ"00000000"
	 * @param username
	 */
	private void setUsername(String username)
	{
		if(null == username)//����
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
