package com.example.database;

public class ChargeHolder
{
//	public static final String ORDER_TIME = "OrderTime";//����ʱ��
//	public static final String ORDER_SEQ = "OrderSEQ";//������
//	public static final String ORDER_REQTRANSE = "OrderReqTranse";//������ˮ��
//	public static final String ORDER_AMOUNT = "Amount";//�������
//	public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//���п��ţ����ڱ�Ǹ����ſ���ֵ
//	public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//��֧��֧��״̬
//	public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//Ȧ��д��״̬
//	public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//Ȧ��ջ�״̬
	
	//sqlite��û�в����ͣ��ɴ�ֱ����int�棬�˴���������ת��Ϊ�ⲿ����ʹ�õĲ�����
	public static final int FALSE_HERE = 0;//ʧ��
	public static final int TRUE_HERE = 1;//�ɹ�
	
	
	private  String Order_Time;// = "OrderTime";//����ʱ��
	private  String Order_Seq;//  = "OrderSEQ";//������
	private  String Order_Reqtranse;//  = "OrderReqTranse";//������ˮ��
	private  String Order_Amount;//  = "Amount";//�������
	private  String Order_Publish_CardID;
	private  String payMethod;//֧����ʽ
	private  int Order_Charge_Status;//  = "ChargeStatus";//֧��״̬
	private  int Order_Charge_NFC_Status;//  = "ChargeResult";//Ȧ��״̬
	private  int Order_Transference_Close_Status;//Ȧ��ջ�״̬
	private  String TAC;//Ȧ��ɹ�Ӧ��TAC
	/**
	 * ���ݲ����ʹ���
	 * @param OrderTime ����ʱ��
	 * @param Order_Seq  ������
	 * @param Order_Reqtranse ������ˮ��
	 * @param Order_Amount �������
	 * @param Order_Publish_CardID ���п���
	 * @param payMethod ֧����ʽ  01:��֧��  02:֧���� 03:�������� 04:�������� 05:ũҵ����
	 * @param isCharge ֧��״̬
	 * @param isChargeNFC Ȧ��״̬
	 * @param isTransferenceClose Ȧ��ջ�״̬
	 * @param tAC Ȧ��ɹ�TACӦ��
	 */
	public ChargeHolder(String OrderTime, String Order_Seq, String Order_Reqtranse,
			String Order_Amount, String Order_Publish_CardID, String payMethod,
			Boolean isCharge, Boolean isChargeNFC, Boolean isTransferenceClose,
			String tAC)
	{
		setOrder_Time(OrderTime);
		setOrder_Seq(Order_Seq);
		setOrder_Reqtranse(Order_Reqtranse);
		setOrder_Amount(Order_Amount);
		setOrder_Publish_CardID(Order_Publish_CardID);
		setPayMethod(payMethod);
		setOrder_Charge_Status(isCharge);
		setOrder_CHARGE_NFC_STATUS(isChargeNFC);
		setOrder_Transference_Close_Status(isTransferenceClose);
		setTAC(tAC);
	}

	/**
	 * �������δ���
	 * @param OrderTime ����ʱ��
	 * @param Order_Seq ������
	 * @param Order_Reqtranse ������ˮ��
	 * @param Order_Amount �������
	 * @param Order_Publish_CardID ���п���
	 * @param payMethod ֧����ʽ     01:��֧��  02:֧���� 03:�������� 04:�������� 05:ũҵ����
	 * @param Charge_Status ֧��״̬ true��֧��
	 * @param ChargeNFC_Status Ȧ��״̬ true��Ȧ��ɹ�
	 * @param TransferenceClose_Status ���׹ر�״̬ true��Ȧ��ĳɹ��ϱ���ɣ����׹ر�
	 * @param tAC Ȧ��ɹ�TACӦ��
	 * @throws CustomTypeException ״̬�������������ȷʱ�׳����Զ����쳣
	 */
	public ChargeHolder(String OrderTime, String Order_Seq, String Order_Reqtranse,
			String Order_Amount, String Order_Publish_CardID, String payMethod,
			int Charge_Status, int ChargeNFC_Status, 
			int TransferenceClose_Status, String tAC) throws CustomTypeException
	{
		if( (0 != Charge_Status) && (1 != Charge_Status)) throw new CustomTypeException("ChargeHolder ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != ChargeNFC_Status) && (1 != ChargeNFC_Status)) throw new CustomTypeException("ChargeHolder ����ʱint��ֻ��Ϊ0��1"); 
		if( (0 != TransferenceClose_Status) && (1 != TransferenceClose_Status)) throw new CustomTypeException("ChargeHolder ����ʱint��ֻ��Ϊ0��1"); 
		
		setOrder_Time(OrderTime);
		setOrder_Seq(Order_Seq);
		setOrder_Reqtranse(Order_Reqtranse);
		setOrder_Amount(Order_Amount);
		setOrder_Publish_CardID(Order_Publish_CardID);
		setPayMethod(payMethod);
		setOrder_Charge_Status((TRUE_HERE == Charge_Status)?true:false);
		setOrder_CHARGE_NFC_STATUS((TRUE_HERE == ChargeNFC_Status)?true:false);
		setOrder_Transference_Close_Status((TRUE_HERE == TransferenceClose_Status)?true:false);
		setTAC(tAC);
	}
	
	public String getOrder_Time()
	{
		return Order_Time;
	}

	public void setOrder_Time(String order_Time)
	{
		Order_Time = order_Time;
	}

	public String getOrder_Seq()
	{
		return Order_Seq;
	}

	public void setOrder_Seq(String order_Seq)
	{
		Order_Seq = order_Seq;
	}

	public String getOrder_Reqtranse()
	{
		return Order_Reqtranse;
	}

	public void setOrder_Reqtranse(String order_Reqtranse)
	{
		Order_Reqtranse = order_Reqtranse;
	}

	public String getOrder_Amount()
	{
		return Order_Amount;
	}

	public void setOrder_Amount(String order_Amount)
	{
		Order_Amount = order_Amount;
	}

	public String getOrder_Publish_CardID()
	{
		return Order_Publish_CardID;
	}

	public void setOrder_Publish_CardID(String order_Publish_CardID)
	{
		Order_Publish_CardID = order_Publish_CardID;
	}

	/**
	 * ��ȡ֧����ʽ  
	 * //		 01:��֧��
	 * //        02:֧����
	 * //        03:��������
	 * //        04:��������
	 * //        05:ũҵ����
	 * @return
	 */
	public String getPayMethod()
	{
		return payMethod;
	}
	
	/**
	 * ����֧����ʽ
	 * //		 01:��֧��
	 * //        02:֧����
	 * //        03:��������
	 * //        04:��������
	 * //        05:ũҵ����
	 * @param payMethod
	 */
	public void setPayMethod(String payMethod)
	{
		this.payMethod = payMethod;
	}

	public Boolean getOrder_Charge_Status()
	{
		if(FALSE_HERE == Order_Charge_Status)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public int getOrder_Charge_Status_Int()
	{
		return Order_Charge_Status;
	}
	
	
	public void setOrder_Charge_Status(Boolean order_Charge_Status)
	{
		if(false == order_Charge_Status)
		{
			Order_Charge_Status = FALSE_HERE;
		}
		else {
			Order_Charge_Status = TRUE_HERE;
		}
	}

	public Boolean getOrder_Charge_NFC_Status()
	{
		if(FALSE_HERE == Order_Charge_NFC_Status)
		{
			return false;
		}
		else {
			return true;
		}
	}
	
	public int getOrder_Charge_NFC_Status_Int()
	{
		return Order_Charge_NFC_Status;
	}

	public void setOrder_CHARGE_NFC_STATUS(Boolean isorder_CHARGE_NFC_STATUS)
	{
		if(true == isorder_CHARGE_NFC_STATUS)
		{
			Order_Charge_NFC_Status = TRUE_HERE;
		}
		else {
			Order_Charge_NFC_Status = FALSE_HERE;
		}
	}

	public Boolean getOrder_Transference_Close_Status()
	{
		if(FALSE_HERE == Order_Transference_Close_Status)
		{
			return false;
		}
		else {
			
			return true;
		}
	}

	public int getOrder_Transference_Close_Status_Int()
	{
		return Order_Transference_Close_Status;
	}
	
	public void setOrder_Transference_Close_Status(
			Boolean isorder_Transference_Close_Status)
	{
		if(true == isorder_Transference_Close_Status)
		{
			Order_Transference_Close_Status = TRUE_HERE;
		}
		else{
			Order_Transference_Close_Status = FALSE_HERE;
		}
			
	}

	/**
	 * ��ȡȦ��ɹ�TACӦ��
	 * @return
	 */
	public String getTAC()
	{
		return TAC;
	}
	/**
	 * ����Ȧ��ɹ�TACӦ�𣬳��Ȳ�λ8���ַ������ȣ��Զ��ض�
	 * null == tAC �滻Ϊ 00000000
	 * tAC.isEmpty �滻Ϊ 12345678
	 * tAC.length>8 ȡ��8λ 
	 * @param tAC
	 */
	public void setTAC(final String tAC)
	{
		String temp = tAC;
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
		
		TAC = tAC;
	}
	
	
	
}
