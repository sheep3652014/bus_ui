package com.example.network;

import com.example.bus_ui_demo.R.string;

public class PayParams
{
//	Card_number	16	N	���п���
//	IMEI	15	N	�ն�Ψһ��ʶ��ȫ��Ψһ��
//	orderNo	30	AN	�������
//	Orderseq	30	AN	������ˮ��
//	orderMount	8	N	������� ��λ�֣���������Ϊ������ ��λԪ��
//	OldMount	8	HEX	��Ƭ��� ��λ��
//	pubLen	2	H	������Ϣ����
//	PublicData	N	H	������Ϣ
//	PayType	2	N	֧��;��
//	01 ��֧��
//	02 ֧����
//	03 ��������
//	04 ��������
//	version	1	N	����汾��
//	Phone	11	N	�ֻ��� //swp����ʱʹ��

	public static final String CARD_NUMBER = "cardNO";
	public static final String IMEI_NO = "imei";
	public static final String ORDERNO = "orderNO";
	public static final String ORDERSEQ = "orderSeq";
	public static final String ORDER_AMOUNT = "orderAmount";
	public static final String OLDAMOUNT = "oldAmount";
	public static final String PUBLEN = "pubLen";
	public static final String PUBLIC_DATA = "publicdata";
	public static final String PAYTYPE = "payType";
	public static final String SOFTVERSION = "softversion";
	public static final String PHONENO = "phoneNO";
	
	private String Card_number;
	private String IMEI;
	private String OrderNO;
	private String OrderAmount;
	private String OldAmount;
	private String PubLen;
	private String PublicData;
	private String PayType;
	private String SoftVersion;
	private String phoneNO;
	
	/**
	 * ��ȡ���п���
	 * @return
	 */
	public String getCard_number()
	{
		return Card_number;
	}
	/**
	 * ���÷��п���
	 * @param card_number
	 */
	public void setCard_number(String card_number)
	{
		Card_number = card_number;
	}
	/**
	 * ����IMEI��
	 * @return
	 */
	public String getIMEI()
	{
		return IMEI;
	}
	/**
	 * ��ȡIMEI��
	 * @param iMEI
	 */
	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
	}
	/**
	 * ��ȡ������ 30λ
	 * @return
	 */
	public String getOrderNO()
	{
		return OrderNO;
	}
	/**
	 * ���ö�����
	 * @param orderNO
	 */
	public void setOrderNO(String orderNO)
	{
		OrderNO = orderNO;
	}
	/**
	 * ��ȡ�������
	 * @return
	 */
	public String getOrderAmount()
	{
		return OrderAmount;
	}
	/**
	 * ���ö������
	 * @param orderAmount
	 */
	public void setOrderAmount(String orderAmount)
	{
		OrderAmount = orderAmount;
	}
	/**
	 * ��ȡ��ֵǰ���
	 * @return
	 */
	public String getOldAmount()
	{
		return OldAmount;
	}
	/**
	 * ���ó�ֵǰ���
	 * @param oldAmount
	 */
	public void setOldAmount(String oldAmount)
	{
		OldAmount = oldAmount;
	}
	/**
	 * ��ȡ������Ϣ����
	 * @return
	 */
	public String getPubLen()
	{
		return PubLen;
	}
	/**
	 * ���ù�����Ϣ����
	 * @param pubLen
	 */
	private void setPubLen(String pubLen)
	{
		PubLen = pubLen;
	}
	/**
	 * ��ȡ������Ϣ
	 * @return
	 */
	public String getPublicData()
	{
		return PublicData;
	}
	/**
	 * ���ù�����Ϣ,���Զ����湫����Ϣ����
	 * ��������Ϣ���ȴ���0XFFʱ����
	 * @param publicData
	 */
	public void setPublicData(String publicData)
	{
		PublicData = publicData;
		setPubLen(Integer.toHexString(publicData.length()));
	}
	/**
	 * ��ȡ֧��;��
	 * 01 ��֧��
	 * 02 ֧����
	 * 03 ��������
	 * 04 ��������
	 * @return
	 */
	public String getPayType()
	{
		return PayType;
	}
	/**
	 * ����֧��;��
	 * 01 ��֧��
	 * 02 ֧����
	 * 03 ��������
	 * 04 ��������
	 * @param payType
	 */
	public void setPayType(String payType)
	{
		PayType = payType;
	}
	/**
	 * ��ȡ����汾��
	 * @return
	 */
	public String getSoftVersion()
	{
		return SoftVersion;
	}
	/**
	 * ��������汾��
	 * @param softVersion
	 */
	public void setSoftVersion(String softVersion)
	{
		SoftVersion = softVersion;
	}
	/**
	 * ��ȡ�ֻ�����
	 * @return
	 */
	public String getPhoneNO()
	{
		return phoneNO;
	}
	/**
	 * �����ֻ�����
	 * @param phoneNO
	 */
	public void setPhoneNO(String phoneNO)
	{
		this.phoneNO = phoneNO;
	}
	
	
}
