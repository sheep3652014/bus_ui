package com.example.database;

public class ChargeHolder
{
//	public static final String ORDER_TIME = "OrderTime";//订单时间
//	public static final String ORDER_SEQ = "OrderSEQ";//订单号
//	public static final String ORDER_REQTRANSE = "OrderReqTranse";//订单流水号
//	public static final String ORDER_AMOUNT = "Amount";//订单金额
//	public static final String ORDER_PUBLISH_CARDID = "NFCPublishCardID";//发行卡号，用于标记给那张卡充值
//	public static final String ORDER_CHARGE_STATUS = "ChargeStatus";//翼支付支付状态
//	public static final String ORDER_CHARGE_NFC_STATUS = "ChargeResult";//圈存写卡状态
//	public static final String ORDER_TRANSFERENCE_CLOSE_STATUS = "TransferenceStatus";//圈存闭环状态
	
	//sqlite中没有布尔型，干脆直接用int存，此处常量用于转换为外部正常使用的布尔型
	public static final int FALSE_HERE = 0;
	public static final int TRUE_HERE = 1;
	
	
	private  String Order_Time;// = "OrderTime";//订单时间
	private  String Order_Seq;//  = "OrderSEQ";//订单号
	private  String Order_Reqtranse;//  = "OrderReqTranse";//订单流水号
	private  String Order_Amount;//  = "Amount";//订单金额
	private  String Order_Publish_CardID;
	private  int Order_Charge_Status;//  = "ChargeStatus";//支付状态
	private  int Order_Charge_NFC_Status;//  = "ChargeResult";//圈存状态
	private  int Order_Transference_Close_Status;//圈存闭环状态
	
	/**
	 * 根据布尔型创建
	 * @param OrderTime
	 * @param Order_Seq
	 * @param Order_Reqtranse
	 * @param Order_Amount
	 * @param Order_Publish_CardID
	 * @param isCharge
	 * @param isChargeNFC
	 * @param isTransferenceClose
	 */
	public ChargeHolder(String OrderTime, String Order_Seq, String Order_Reqtranse,
			String Order_Amount, String Order_Publish_CardID, Boolean isCharge, Boolean isChargeNFC, Boolean isTransferenceClose)
	{
		setOrder_Time(OrderTime);
		setOrder_Seq(Order_Seq);
		setOrder_Reqtranse(Order_Reqtranse);
		setOrder_Amount(Order_Amount);
		setOrder_Publish_CardID(Order_Publish_CardID);
		setOrder_Charge_Status(isCharge);
		setOrder_CHARGE_NFC_STATUS(isChargeNFC);
		setOrder_Transference_Close_Status(isTransferenceClose);
		
	}

	/**
	 * 根据整形创建
	 * @param OrderTime
	 * @param Order_Seq
	 * @param Order_Reqtranse
	 * @param Order_Amount
	 * @param Order_Publish_CardID
	 * @param Charge_Status
	 * @param ChargeNFC_Status
	 * @param TransferenceClose_Status
	 * @throws CustomTypeException 
	 */
	public ChargeHolder(String OrderTime, String Order_Seq, String Order_Reqtranse,
			String Order_Amount, String Order_Publish_CardID, int Charge_Status, int ChargeNFC_Status, 
			int TransferenceClose_Status) throws CustomTypeException
	{
		if( (0 != Charge_Status) && (1 != Charge_Status)) throw new CustomTypeException("ChargeHolder 创建时int型只能为0或1"); 
		if( (0 != ChargeNFC_Status) && (1 != ChargeNFC_Status)) throw new CustomTypeException("ChargeHolder 创建时int型只能为0或1"); 
		if( (0 != TransferenceClose_Status) && (1 != TransferenceClose_Status)) throw new CustomTypeException("ChargeHolder 创建时int型只能为0或1"); 
		
		setOrder_Time(OrderTime);
		setOrder_Seq(Order_Seq);
		setOrder_Reqtranse(Order_Reqtranse);
		setOrder_Amount(Order_Amount);
		setOrder_Publish_CardID(Order_Publish_CardID);
		setOrder_Charge_Status((TRUE_HERE == Charge_Status)?true:false);
		setOrder_CHARGE_NFC_STATUS((TRUE_HERE == ChargeNFC_Status)?true:false);
		setOrder_Transference_Close_Status((TRUE_HERE == TransferenceClose_Status)?true:false);
	
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
	
	
	
}
