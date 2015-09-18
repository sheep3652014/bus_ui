package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import com.bestpay.plugin.Plugin;
import com.example.Alert.myAlert;
import com.example.application.myApplication;
import com.example.config.Global_Config;
import com.example.database.ChargeHolder;
import com.example.database.CustomTypeException;
import com.example.database.DbManager;
import com.example.database.DbOpenHelper_charge;
import com.example.network.PayParams;
import com.example.network.protocol;
import com.example.bestpay.CryptTool;
import com.example.bestpay.OrderParams;
import com.example.bestpay.Util;
import com.tybus.swp.UICC_swp;
import com.tybus.swp.UICC_swp.UICC_Interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class aty_PopRequestPay extends Activity
{
	private static int backpress_cnt = 0;
	private myApplication myApp;

	private static final int CONNECT_ERROR = Global_Config.CONNECT_ERROR;
	private static final int ORDERTO_PAY_PLATFROMS = Global_Config.INNER_MSG_START + 0;// ��ʼ���ֵƽ̨�ύ����
	private static final int ORDER_SUCCESS = Global_Config.INNER_MSG_START + 1;// ��֧���µ��ɹ�
	private static final int ORDER_FAIL = Global_Config.INNER_MSG_START + 2;// ��֧���µ�ʧ��
	//private static final int CIRCLE_CMD_FAIL = Global_Config.INNER_MSG_START + 3;// ��ȡȦ��ָ��ʧ��
	//private static final int CIRCLE_CMD_SUCCESS = Global_Config.INNER_MSG_START + 4;// ��ȡȦ��ָ��ʧ��
	private static final int PAY_SUCCESS = Global_Config.INNER_MSG_START + 3;
	private static final int PAY_FAIL = Global_Config.INNER_MSG_START + 4;
	
	private static final String CIRCLE_CMD_STR = "circleCmd";

	private static final String TAG = "aty_PopRequestPay";
	private TextView tv_notice;

	private final boolean mNeedOrder = true;// ������֧��
	private static final int BestPay_RequestCode = Global_Config.BESTPAY_REQUESTCODE;// ��֧�������룬���������
	private static final String GET_RANDOM = Global_Config.GET_RANDOM;

	private UICC_swp m_swp = null;
	// private Handler delayHandler = null;

	Handler mHandler = new Handler()
	{

		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_PopRequestPay.this,
							getString(R.string.ConnectServer_error));
					break;
				case ORDERTO_PAY_PLATFROMS:
					tv_notice.setText(getString(R.string.PayRequest2platfrom));// �����ύ������ʾ
					break;
				case ORDER_SUCCESS:
					Hashtable<String, String> table = (Hashtable<String, String>) msg.obj;
					for (String key : table.keySet())
					{
						// System.out.println(key+":"+table.get(key));
					}
					Plugin.pay(aty_PopRequestPay.this,
							(Hashtable<String, String>) msg.obj);
					break;
				case ORDER_FAIL:
					// ɾ���µ�ʧ�ܶ�����������������
					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.PayRequest_error));
					// ɾ���µ�ʧ�ܵĶ���
					DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
					BackHomeActivity(aty_PopRequestPay.this);
					break;
				case PAY_FAIL://֧��ʧ��
					myAlert.ShowToast(aty_PopRequestPay.this,
							getString(R.string.BestPay_PayFail));// ֧��ʧ�ܣ�������ʾ������������
					// ɾ��ȡ��֧���Ķ���
					DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
					BackHomeActivity(aty_PopRequestPay.this);
					break;
				case PAY_SUCCESS:
					// �����֧�����
					UpdateBestPayStatus(myApp.getOrderSeq(), true);// ������֧�����״̬

					Intent intent = null;
					if (myApp.isBigCard())
					{// ��
						intent = new Intent(aty_PopRequestPay.this,
								aty_NFC_bigCard_Circle.class);
					} else
					{// С��
						intent = new Intent(aty_PopRequestPay.this,
								aty_SWP_Circle.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					aty_PopRequestPay.this.finish();
					break;
				default:
					break;
			}
			// super.handleMessage(msg);
		}

	};

	/*
	 * Thread mThread = new Thread(new Runnable() {
	 * 
	 * @Override public void run() { Looper.prepare();
	 * 
	 * String Ack = RequestPay();
	 * 
	 * if (null != Ack) { // ���ͳ�ֵ���� String terminalCode = Ack.substring(4,
	 * 16);// 4-15�ն˺� Ȧ��ʹ�� String Url = Ack.substring(16, Ack.length()); String
	 * Ip = Url.replace("http://", "").split(":")[0];
	 * myApplication.setServerIP(Ip);// ���ú�̨����IP
	 * myApp.setTerminalCode(terminalCode);// �����ն˺�
	 * myApplication.setVersionControlUrl(Url);// ���ð汾���µ�ַ
	 * myApplication.setApkDownloadUrl(Url);// ���ð汾�������ص�ַ
	 * OrderParams.setBackMerchantURL(Url);// ���ú�̨���ص�ַ ��֧������
	 * 
	 * // ���ö�Ӧ֧��ƽ̨���ύ���� Message msg = new Message(); msg.what =
	 * ORDERTO_PAY_PLATFROMS; // �����ύ������ʾ mHandler.sendMessage(msg);
	 * 
	 * // ����֧��ƽ̨ CallPay_Platfroms(myApp.getPayMethod()); } else // {
	 * myAlert.ShowOneClickDialog(aty_PopRequestPay.this,
	 * getString(R.string.PayRequest_error), new
	 * myAlert.ShowOneClickDialog_Interface() {
	 * 
	 * @Override public void DoSomething() { aty_PopRequestPay.this.finish(); }
	 * }); }
	 * 
	 * Looper.loop(); } });
	 */
	Runnable mRunnable = new Runnable()
	{
		public void run()
		{
			Looper.prepare();

			String Ack = RequestPay();

			if (null != Ack)
			{
				// ���ͳ�ֵ����
				String terminalCode = Ack.substring(4, 16);// 4-15�ն˺� Ȧ��ʹ��
				String Url = Ack.substring(16, Ack.length());
				String Ip = Url.replace("http://", "").split(":")[0];
				myApplication.setServerIP(Ip);// ���ú�̨����IP
				myApp.setTerminalCode(terminalCode);// �����ն˺�
				myApplication.setVersionControlUrl(Url);// ���ð汾���µ�ַ
				myApplication.setApkDownloadUrl(Url);// ���ð汾�������ص�ַ
				OrderParams.setBackMerchantURL(Url);// ���ú�̨���ص�ַ ��֧������

				// ���ö�Ӧ֧��ƽ̨���ύ����
				Message msg = new Message();
				msg.what = ORDERTO_PAY_PLATFROMS; // �����ύ������ʾ
				mHandler.sendMessage(msg);
				System.out.println("in ack ---");
				// ����֧��ƽ̨
				CallPay_Platfroms(myApp.getPayMethod());
			} else
			{
				DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
				myAlert.ShowOneClickDialog(aty_PopRequestPay.this,
						getString(R.string.PayRequest_error),
						new myAlert.ShowOneClickDialog_Interface()
						{

							@Override
							public void DoSomething()
							{
								aty_PopRequestPay.this.finish();
							}
						});
			}

			Looper.loop();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_poprequetpay_layout);

		myApp = (myApplication) getApplicationContext();

		FindView();

		// mThread.start();
		//Handler delayHandler = new Handler();
		System.out.println("aty_pop in 1");
		final Thread thread = new Thread(mRunnable);
		if (myApp.isBigCard())
		{
			//delayHandler.postDelayed(mRunnable, 0);//
			thread.start();
		} else
		{
			//delayHandler.postDelayed(mRunnable, 1 * 1000);// �ȴ�1s��ִ�У���Ҫ��Ϊ�˵ȴ�С���������
			
			m_swp = new UICC_swp(aty_PopRequestPay.this, new UICC_Interface()
			{

				@Override
				public void UICCTranSomeCmd()
				{
					// TODO Auto-generated method stub
					System.out.println("here");
					// ��ȡ�����
					myApp.setRandom(GetRandom());
					m_swp.UICCClose();//�˴�֮��Ҫ��ȡ������������������Ϲر���Դ
					thread.start();
					
				}
			});

			if (m_swp.UICCInit())
			{
				System.out.println("uicc init ok");
			}

		}
	}

	/**
	 * ��ȡ���������ȡʧ���߽���ʱʱ���msģʽ�ĺ�4λ�ַ���Ϊ�����
	 * ���������4λ����ֻȡ4λ
	 * @return
	 */
	private String GetRandom()
	{
		String random = m_swp.UICC_TRANS(GET_RANDOM);
		System.out.println("get random = " +random );
		if (null == random)
		{
			String longtime = "" + System.currentTimeMillis();
			random = longtime.substring(longtime.length() - 4);
		}
		if(random.length() > 4)
			random = random.substring(0, 4);
		System.out.println("random = "+random);
		return random;
	}

	private void FindView()
	{
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_notice.setText(getString(R.string.PayRequest));
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		if (backpress_cnt++ < 1)
		{
			// ��һ�ΰ����ؼ� ������ʾ
			myAlert.ShowToast(aty_PopRequestPay.this,
					getString(R.string.ClickBackAgain));
		} else
		{
			// �ڶ��ΰ����ؼ� �˳���ǰҳ�棬������һҳ��
			backpress_cnt = 0;
			aty_PopRequestPay.this.finish();
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		// �ر������Դ
		if (null != m_swp)
			m_swp.UICCClose();
		else
		{
			System.out.println("is null");
		}
		super.onDestroy();
	}

	/**
	 * ƴ������֧��֡����������֧������
	 * 
	 * @return ��ֵ���󷵻�ֵ�����󷵻�null���ɹ�����Э�鶨���������Ϣ
	 */
	private String RequestPay()
	{
		HashMap<String, String> map = new HashMap<String, String>();

		// Card_number 16 N ���п���
		// PhysicsCardNO 8 N ������
		// IMEI 15 N �ն�Ψһ��ʶ��ȫ��Ψһ��
		// orderNo 30 AN �������
		// Orderseq 30 AN ������ˮ��
		// orderMount 8 N ������� 10���� ��λ�֣���������Ϊ������ ��λԪ��
		// OldMount 8 HEX ��Ƭ��� ��λ��
		// pubLen 2 H ������Ϣ����
		// PublicData N H ������Ϣ
		// PayType 2 N ֧��;��
		// 01 ��֧��
		// 02 ֧����
		// 03 ��������
		// 04 ��������
		// 05
		System.out.println("aty_pop requestpay --");
		long time_ms = System.currentTimeMillis();
		Date date = new Date(time_ms);
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
		// ������Ψһ 30λ
		String OrderNO = time + myApp.getRandom() + myApp.getRandom()
				+ myApp.getRandom();// 4λ�����
		// ������ˮ��Ψһ 30λ
		String OrderSeq = myApp.getRandom() + myApp.getRandom()
				+ myApp.getRandom() + time;

		String hexMoney;// ��Ƭ���
		String publicMsg_len;// ������Ϣ����
		String publicMsg;// ������Ϣ
		String publishCardNO;// ���п���
		String physicCardNO;// ������
		if (myApp.isBigCard())
		{
			hexMoney = Integer.toHexString(myApp.getSwp_balance());
			publicMsg = myApp.getPublicMsg();
			publishCardNO = myApp.getPublishCardNO();
			physicCardNO = myApp.getPhysicsCardNO();
		} else
		{// С��
			hexMoney = Integer.toHexString(myApp.getSwp_balance());
			publicMsg = myApp.getSwp_PublicMsg();
			publishCardNO = myApp.getSwp_PublishCardNO();// myApp.getPublishCardNO();
			physicCardNO = myApp.getSwp_physicsCardNO();// myApp.getPhysicsCardNO();
		}
		hexMoney = to8String(hexMoney);
		// �������
		String orderAmount = "" + myApp.getChargeMoney();
		orderAmount = to8String(orderAmount);

		// if(myApp.getPublicMsg().length()>0xff)
		if (publicMsg.length() > 0xff)
		{// ���ȳ���0xff��ֱ����Ϊ����Ϊ0���÷���������ͨ������
			publicMsg_len = "00";
		} else
		{
			// publishMsg_len =
			// Integer.toHexString(myApp.getPublicMsg().length());
			publicMsg_len = Integer.toHexString(publicMsg.length());
			System.out.println("len = " + publicMsg_len + "_"
					+ publicMsg.length() + "__"
					+ publicMsg);
		}
		// ���涩����Ϣ,
		myApp.setOrderNO(OrderNO);
		myApp.setOrderSeq(OrderSeq);
		myApp.setOrderTime(time_ms);// ����ʱ�����ݲ�ͬ��֧��ƽ̨Ҫ��ת����ʽ
		myApp.setOrderAmount(orderAmount);

		// ƴ��֡
		map.put(PayParams.CARD_NUMBER, publishCardNO);// myApp.getPublishCardNO());
		map.put(PayParams.PHYSICS_CARDNO, physicCardNO);// myApp.getPhysicsCardNO());
		map.put(PayParams.IMEI_NO, myApp.getIMEI());
		map.put(PayParams.ORDERNO, OrderNO);
		map.put(PayParams.ORDERSEQ, OrderSeq);
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.OLDAMOUNT, hexMoney);
		map.put(PayParams.PUBLEN, publicMsg_len);
		// map.put(PayParams.PUBLIC_DATA, myApp.getPublicMsg());
		map.put(PayParams.PUBLIC_DATA, publicMsg);
		map.put(PayParams.PAYTYPE, myApp.getPayMethod());

		SaveOrder(map, myApp);// ���涩��

		return (protocol.RequestPay(aty_PopRequestPay.this, map, mHandler));

	}

	/**
	 * ���涩��,�Լ�����״̬
	 * 
	 * @param map
	 * @param myApp
	 */
	private void SaveOrder(HashMap<String, String> map, myApplication myApp)
	{
		String orderTime = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date(myApp.getOrderTime()));
		
		String tac = null;
		// �½�sqlite����
		ChargeHolder ch = new ChargeHolder(orderTime,
				map.get(PayParams.ORDERNO), map.get(PayParams.ORDERSEQ),
				map.get(PayParams.ORDER_AMOUNT),
				map.get(PayParams.CARD_NUMBER), myApp.getPayMethod(), false,
				false, false, tac);

		DbManager dm = new DbManager(this);
		dm.InsertItem(ch);
		dm.closeDB();
	}

	/**
	 * ����֮ǰѡ���֧����ʽ�����ö�Ӧ��֧��ƽ̨
	 * 
	 * @param payMethod
	 *            // 01:��֧�� // 02:֧���� // 03:�������� // 04:�������� // 05:ũҵ����
	 */
	private void CallPay_Platfroms(String payMethod)
	{
		int method;
		
		try
		{
			method = Integer.parseInt(payMethod);
		} catch (Exception e)
		{
			Log.d(TAG, "paymethod ֧����ʽ��ʽ����,���֧����ʽ��ȡ��" + e.getMessage());
			method = 1;
		}
		switch (method)
		{
			case myApplication.PAYMETHOD_BESTPAY:
			default:
				System.out.println("pay method = " + method);
				BestPay();
				break;
			case myApplication.PAYMETHOD_ALIPAY:
				break;
			case myApplication.PAYMETHOD_CCBPAY:
				break;
			case myApplication.PAYMETHOD_ICBCPAY:
				break;
			case myApplication.PAYMETHOD_ABCPAY:
				break;
		}
	}

	/**
	 * ��֧��֡ƴ�ӣ�֮�������֧��֧��ƽ̨
	 */
	private void BestPay()
	{
		Hashtable<String, String> paramsHashtable = new Hashtable<String, String>();
		paramsHashtable.put(Plugin.MERCHANTID, OrderParams.MERCHANTID);// �̻�ID
		paramsHashtable.put(Plugin.SUBMERCHANTID, OrderParams.SUBMERCHANTID);// ���̻�ID
																				// ����
		paramsHashtable.put(Plugin.MERCHANTPWD, OrderParams.MERCHANTPWD); // ����KEY

		paramsHashtable.put(Plugin.ORDERSEQ, // �������
				myApp.getOrderNO());

		// �������,��λ��Ԫ С�����2λ �������=��Ʒ���+���ӽ��
		int m = 0;
		try
		{
			m = Integer.parseInt(myApp.getOrderAmount());
		} catch (Exception e)
		{
			// TODO: handle exception
			System.out.println("parseint error " + e.getMessage());
		}

		float money = new BigDecimal((double) m / 100).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		OrderParams.setChargeMoney("" + money);
		paramsHashtable.put(Plugin.ORDERAMOUNT, OrderParams.getChargeMoney());
		Log.d("money in pay", "" + paramsHashtable.get(Plugin.ORDERAMOUNT)
				+ "_" + OrderParams.getChargeMoney());

		// ��Ʒ����λ��Ԫ��С�����2λ��ѡ��
		// paramsHashtable.put(Plugin.PRODUCTAMOUNT, "1.00");

		// ���ӽ���λ��Ԫ��С�����2λ��ѡ��
		// paramsHashtable.put(Plugin.ATTACHAMOUNT, "0.00");

		// ����ʱ�䣬��ʽyyyyMMddhhmmss
		String time = DateFormat.format("yyyyMMddHHmmss", myApp.getOrderTime())
				.toString();
		OrderParams.setOrderTime(time);
		paramsHashtable.put(Plugin.ORDERTIME, OrderParams.getOrderTime());

		// ��Ч����ʱ�䣬��ʽyyyyMMddhhmmss �˴�������Чʱ��5min
		String validtime = DateFormat.format("yyyyMMddHHmmss",
				myApp.getOrderTime() + 5 * 60 * 1000).toString();
		OrderParams.setOrder_validTime(validtime);
		paramsHashtable.put(Plugin.ORDERVALIDITYTIME,
				OrderParams.getOrder_validTime());
		// ��Ʒ����
		paramsHashtable.put(Plugin.PRODUCTDESC, OrderParams.PRODUCTDESC);
		// �û�ID������
		paramsHashtable.put(Plugin.CUSTOMERID, "01");

		/*
		 * paramsHashtable .put(Plugin.PRODUCTAMOUNT,
		 * mAmmount.getText().toString());
		 * paramsHashtable.put(Plugin.ATTACHAMOUNT, "0.00");
		 */

		// ���֣��̶��RMB��
		paramsHashtable.put(Plugin.CURTYPE, OrderParams.CURTYPE);
		// ��̨֪ͨ��ַ
		paramsHashtable.put(Plugin.BACKMERCHANTURL,
				OrderParams.getBackMerchantURL());// "http://127.0.0.1:8040/wapBgNotice.action");
		// ������Ϣ������
		paramsHashtable.put(Plugin.ATTACH, OrderParams.ATTACH);
		// ��ƷID������
		paramsHashtable.put(Plugin.PRODUCTID, OrderParams.PRODUCTID);
		// �û�IP
		OrderParams.setUserIP(myApp.getServerIP());
		paramsHashtable.put(Plugin.USERIP, OrderParams.getUserIP());// paramsHashtable.put(Plugin.USERIP,
																	// "IP��ַ");
		// ������ϸ��ѡ��
		// paramsHashtable.put(Plugin.DIVDETAILS, "");
		/**
		 * �������̻�ʵ�֣�
		 */
		paramsHashtable.put(Plugin.KEY, OrderParams.KEY);//
		paramsHashtable.put(Plugin.ACCOUNTID, OrderParams.getAccountID());// mAccount.getText().toString());
		/**
		 * �ʽ�Դ���ͣ���Ĭ��ʹ��04
		 */
		paramsHashtable.put(Plugin.BUSITYPE, OrderParams.BUSITYPE);// merchantbank.getText().toString());

		// ������ˮ�� 30λ��Ψһ
		OrderParams.setOrder_Transeq(myApp.getOrderSeq());
		paramsHashtable.put(Plugin.ORDERREQTRANSEQ,
				OrderParams.getOrder_Transeq());

		// save ORDERSEQ , ORDERREQTRANSEQ
		// SaveCharge(paramsHashtable.get(Plugin.ORDERSEQ));

		String mac = "MERCHANTID=" + paramsHashtable.get(Plugin.MERCHANTID)
				+ "&ORDERSEQ=" + paramsHashtable.get(Plugin.ORDERSEQ)
				+ "&ORDERREQTRNSEQ="
				+ paramsHashtable.get(Plugin.ORDERREQTRANSEQ) + "&ORDERTIME="
				+ paramsHashtable.get(Plugin.ORDERTIME) + "&KEY="
				// + "�������̻���key"; //02420105024032000
				+ paramsHashtable.get(Plugin.KEY);

		System.out.println("in pay");

		try
		{
			mac = CryptTool.md5Digest(mac);
		} catch (Exception e)
		{

			Log.d(TAG, "md5 �������pay");
			// �ر�socket��Դ
			e.printStackTrace();
		}
		// MAC ����
		paramsHashtable.put(Plugin.MAC, mac);

		Request_BestPayPlatfroms(aty_PopRequestPay.this, mHandler,
				paramsHashtable);

	}

	/**
	 * ������֧������̨
	 * 
	 * @param context
	 * @param handler
	 * @param paramsHashtable
	 */
	private void Request_BestPayPlatfroms(Context context,
			final Handler handler, Hashtable<String, String> paramsHashtable)
	{
		if (mNeedOrder)
		{
			final Hashtable<String, String> paramsHashtable2 = paramsHashtable;
			Log.i("aaa", "order params" + paramsHashtable2.toString());
			// �����µ�����
			new Thread()
			{// (new Runnable() {
				@Override
				public void run()
				{
					//Looper.prepare();

					String orderResult = Util.order(paramsHashtable2);
					Log.d("from xia dan", orderResult);
					if (orderResult != null
							&& "00".equals((orderResult.split("&"))[0]))
					{
						Message msg = new Message();
						msg.what = ORDER_SUCCESS;
						msg.obj = paramsHashtable2;
						handler.sendMessage(msg);
					} else
					{
						// ��mHandler�йر���Դ��ɾ���µ�ʧ�ܶ��������˻�������
						handler.sendEmptyMessage(ORDER_FAIL);
					}
					//Looper.loop();
				}
			}.start();
		} else
		{

			Log.d("������", "���̺ţ�" + paramsHashtable.get(Plugin.MERCHANTID)
					+ "�����ţ�" + paramsHashtable.get(Plugin.ORDERSEQ) + " ������ˮ�ţ�"
					+ paramsHashtable.get(Plugin.ORDERREQTRANSEQ));

			// ����PAY������֧�����
			Plugin.pay(context, paramsHashtable);

		}
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		System.out.println(requestCode + "__" + requestCode);
		if (data != null)
		{
			System.out.println("___back" + data.toString() + "_"
					+ data.getExtras().toString() + "_"
					+ data.getStringExtra("result"));// data maybe null

			String resMsg = data.getStringExtra("result");

			System.out.println("in data!=null");
			if (BestPay_RequestCode == requestCode)// �ж��Ƿ�����֧������̨����
			{
				System.out
						.println("back from bestpay platfrom! the back msg = "
								+ resMsg);
				//��֧������֮���ٴ����̨��������ѯ����״̬��ȷ��֮���ٽ��к�������
				if (!ProcessBestPayAck(resMsg))
				{
					myAlert.ShowToast(aty_PopRequestPay.this,
							getString(R.string.BestPay_PayFail));// ֧��ʧ�ܣ�������ʾ������������
					// ��������������Ϣ ��
					BackHomeActivity(aty_PopRequestPay.this);
				}
			}

			System.out.println("--> " + requestCode + "_" + resultCode + "_"
					+ data.getStringExtra("result"));
		}
	}*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		System.out.println(requestCode + "__" + requestCode);
		if (data != null)
		{
			System.out.println("___back" + data.toString() + "_"
					+ data.getExtras().toString() + "_"
					+ data.getStringExtra("result"));// data maybe null

			final String resMsg = data.getStringExtra("result");

			System.out.println("in data!=null");
			if (BestPay_RequestCode == requestCode)// �ж��Ƿ�����֧������̨����
			{
				System.out
						.println("back from bestpay platfrom! the back msg = "
								+ resMsg);
				//��֧������֮���ٴ����̨��������ѯ����״̬��ȷ��֮���ٽ��к�������
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Looper.prepare();
						
						boolean ack1 = protocol.CheckPayStatus(aty_PopRequestPay.this, myApp, mHandler);
						boolean ack2 = ProcessBestPayAck(resMsg);
						
						Message msg = mHandler.obtainMessage();
						
						if( (ack1) || (ack2) ) //��̨��ѯ����֧������̨����һ�����سɹ�������Ϊ֧���ɹ�
						{
							msg.what = PAY_SUCCESS;
							mHandler.sendMessage(msg);
						}
						else if((!ack1) && (!ack2))//��̨��ѯ����֧������̨������֧��ʧ�ܣ���֧��ʧ��
						{
							msg.what = PAY_FAIL;
							mHandler.sendMessage(msg);
						}
						
						Looper.loop();
					}
				}).start();
				
			}

			System.out.println("--> " + requestCode + "_" + resultCode + "_"
					+ data.getStringExtra("result"));
		}
	}
	
	
	/**
	 * ������֧�����ؽ��
	 * 
	 * @param resMsg
	 * @return ֧���ɹ�����true ����false
	 *//*
	private boolean ProcessBestPayAck(String resMsg)
	{
		// reequestCode:
		// Plugin.REQUEST_SUBMIT_ORDER �ύ����ȥ֧������
		// Plugin.REQUEST_BESTPAY_EXCHARGE ��֧����ֵ����
		// Plugin.REQUEST_THIRDPART_EXCHARGE ��������ֵ����
		// resultCode��
		// Context.RESULT_OK (-1) ��ʾ����ɹ�
		// data���������ݳɹ�����success
		if (resMsg.equals("ȡ��֧��"))
		{
			// ɾ��ȡ��֧���Ķ���
			DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
			return false;
		}
		if (resMsg.equals("֧���ɹ�"))
		{
			// �����֧�����
			UpdateBestPayStatus(myApp.getOrderSeq(), true);// ������֧�����״̬

			Intent intent = null;
			if (myApp.isBigCard())
			{// ��
				intent = new Intent(aty_PopRequestPay.this,
						aty_NFC_bigCard_Circle.class);
			} else
			{// С��
				intent = new Intent(aty_PopRequestPay.this,
						aty_SWP_Circle.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			aty_PopRequestPay.this.finish();

			return true;
		} else
		{
			System.out.println("pay fail");

			// �����֧��δ���
			UpdateBestPayStatus(myApp.getOrderSeq(), false);// ������֧�����״̬

			// �ر���Դ������������
			// CloseRes();
			// BackHomeActivity(Aty_ChargeWait.this);

			return false;
		}
	}*/
	/**
	 * ������֧�����ؽ��
	 * 
	 * @param resMsg
	 * @return ֧���ɹ�����true ����false
	 */
	private boolean ProcessBestPayAck(String resMsg)
	{
		// reequestCode:
		// Plugin.REQUEST_SUBMIT_ORDER �ύ����ȥ֧������
		// Plugin.REQUEST_BESTPAY_EXCHARGE ��֧����ֵ����
		// Plugin.REQUEST_THIRDPART_EXCHARGE ��������ֵ����
		// resultCode��
		// Context.RESULT_OK (-1) ��ʾ����ɹ�
		// data���������ݳɹ�����success
		if (resMsg.equals("ȡ��֧��"))
		{
			return false;
		}
		if (resMsg.equals("֧���ɹ�"))
		{
			return true;
		} 
			
		return false;
		
	}
	
	/**
	 * ɾ����Ч������¼ ---�����������֧��ʧ�ܣ���֧���µ�ʧ��
	 * 
	 * @param Transeq
	 *            ������ˮ��
	 * @return ɾ���ļ�¼��
	 */
	private int DeleteInvalidOrderByTranseq(String Transeq)
	{
		int num = 0;
		DbManager dm = new DbManager(aty_PopRequestPay.this);
		// �����ж��Ƿ�Ϊ��Ч�����жϣ�������ɾ
		// ���Ҷ�Ӧ��Ч������Ϣ
		Cursor c = dm.QueryByInvalidOrder(Transeq);
		for (int i = 0; i < c.getCount(); i++)
		{
			num = dm.DeletebyReqTranse(Transeq);
		}
		c.close();
		dm.closeDB();
		System.out.println("--> delete " + num + "��"
				+ OrderParams.getOrder_Transeq());

		return num;
	}

	/**
	 * ������֧�����״̬
	 * 
	 * @param OrderSeq
	 *            ������ˮ��
	 * @param PayStatus
	 *            true:֧���ɹ� false��֧��ʧ��
	 */
	private void UpdateBestPayStatus(String OrderSeq, boolean PayStatus)
	{
		String OrderTime = "";
		String Order_Seq = "";
		String Order_Reqtranse = "";
		String Order_Amount = "";
		String Order_Publish_CardID = "";
		String payMethod = "";
		String tac = "";
		
		DbManager dm = new DbManager(this);
		Cursor c = dm.QueryOrderbyReqTranse(OrderSeq);
		c.moveToNext();
		if (0 != c.getCount())// ���ݿ��д��ڸö�����ˮ�ŵĶ�����Ϣ
		{
			OrderTime = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_TIME));
			Order_Seq = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_SEQ));
			Order_Reqtranse = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_REQTRANSE));
			Order_Amount = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_AMOUNT));
			Order_Publish_CardID = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.ORDER_PUBLISH_CARDID));
			payMethod = c.getString(c
					.getColumnIndex(DbOpenHelper_charge.PAYMETHOD));
			tac = c.getString(c.getColumnIndex(DbOpenHelper_charge.TAC_NFCCARD));
			
			int ChargeNFC_Status = c
					.getInt(c
							.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS));
			// int ChargeNFC_Status =
			// c.getInt(c.getColumnIndex(DbOpenHelper_charge.ORDER_CHARGE_NFC_STATUS));
			int TransferenceClose_Status = c
					.getInt(c
							.getColumnIndex(DbOpenHelper_charge.ORDER_TRANSFERENCE_CLOSE_STATUS));
			int Charge_Status = 0;
			if (PayStatus)
			{
				Charge_Status = 1;
			}

			try
			{
				ChargeHolder ch = new ChargeHolder(OrderTime, Order_Seq,
						Order_Reqtranse, Order_Amount, Order_Publish_CardID,
						payMethod, Charge_Status, ChargeNFC_Status,
						TransferenceClose_Status, tac);

				dm.UpdateOrderbyReqTranse(ch);
			} catch (CustomTypeException e)
			{

				Log.d(TAG, "����״̬����onActivityResult");
				// �����ȹر����ݿ���Դ
				c.close();
				dm.closeDB();
				e.printStackTrace();
			}
		}
		c.close();
		dm.closeDB();
	}

	/**
	 * ���������棬�����Ե��õ�ǰActivity ��onDestory
	 * 
	 * @param context
	 *            ��ǰActivity.this
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity) context).finish();
	}

	// /**
	// * ����Ȧ��֡ƴ��
	// * @return �ɹ�����ָ����򷵻�null
	// */
	// private String RequestCircleCmd(myApplication myApp)
	// {
	//
	// // orderNo 30 AN �������
	// // Orderseq 30 AN ������ˮ��
	// // PublicData 74 H ������Ϣ(�ɲ�ȫF)
	// // Card_number 16 N ���п���
	// // orderMount 8 N ������� ��λ��
	// // data8050 32 N ��Ƭ8050������Ϣ
	// HashMap<String, String> map = new HashMap<String, String>();
	// String publishData = "";
	// for(int i = 0; i < 74; i++)
	// publishData += "F";
	// //�������
	// String orderAmount = ""+myApp.getChargeMoney();
	// orderAmount = to8String(orderAmount);
	//
	// map.put(PayParams.ORDERNO, myApp.getOrderNO());
	// map.put(PayParams.ORDERSEQ, myApp.getOrderSeq());
	// map.put(PayParams.PUBLIC_DATA, publishData);
	// map.put(PayParams.CARD_NUMBER, myApp.getPublishCardNO());
	// map.put(PayParams.ORDER_AMOUNT, orderAmount);
	// map.put(PayParams.CIRCLEINIT_DATA, myApp.getCircleInitMsg());
	//
	//
	// //��������Ȧ��ָ��
	// return (protocol.RequestCirleCmd(aty_PopRequestPay.this, map, mHandler));
	// }

	/**
	 * ���ַ���С��8λ�Ĳ���Ϊ8λ�����뷽��������
	 * 
	 * @param str
	 * @return
	 */
	private String to8String(String str)
	{
		final int strlen = str.length();
		System.out.println("--before " + str + "_" + str.length());
		if (str.length() < 8)
		{
			for (int i = 0; i < (8 - strlen); i++)
			{
				str = "0" + str;
			}
		}

		System.out.println("--after " + str + "_" + str.length());
		return str;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		//����ϵͳ�л���Ļ�����ǣ���ε���������֧���߳�
		System.out.println("orientation1 = " + newConfig.orientation);
		if((Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation)
			|| (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) )
		{
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.aty_poprequetpay_layout);
			FindView();
		}
		super.onConfigurationChanged(newConfig);
	}
	
	
}
