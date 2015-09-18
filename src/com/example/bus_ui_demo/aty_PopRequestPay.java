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
	private static final int ORDERTO_PAY_PLATFROMS = Global_Config.INNER_MSG_START + 0;// 开始向充值平台提交订单
	private static final int ORDER_SUCCESS = Global_Config.INNER_MSG_START + 1;// 翼支付下单成功
	private static final int ORDER_FAIL = Global_Config.INNER_MSG_START + 2;// 翼支付下单失败
	//private static final int CIRCLE_CMD_FAIL = Global_Config.INNER_MSG_START + 3;// 获取圈存指令失败
	//private static final int CIRCLE_CMD_SUCCESS = Global_Config.INNER_MSG_START + 4;// 获取圈存指令失败
	private static final int PAY_SUCCESS = Global_Config.INNER_MSG_START + 3;
	private static final int PAY_FAIL = Global_Config.INNER_MSG_START + 4;
	
	private static final String CIRCLE_CMD_STR = "circleCmd";

	private static final String TAG = "aty_PopRequestPay";
	private TextView tv_notice;

	private final boolean mNeedOrder = true;// 用于翼支付
	private static final int BestPay_RequestCode = Global_Config.BESTPAY_REQUESTCODE;// 翼支付请求码，反编译得来
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
					tv_notice.setText(getString(R.string.PayRequest2platfrom));// 设置提交订单提示
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
					// 删除下单失败订单，并返回主界面
					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.PayRequest_error));
					// 删除下单失败的订单
					DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
					BackHomeActivity(aty_PopRequestPay.this);
					break;
				case PAY_FAIL://支付失败
					myAlert.ShowToast(aty_PopRequestPay.this,
							getString(R.string.BestPay_PayFail));// 支付失败，给出提示，返回主界面
					// 删除取消支付的订单
					DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
					BackHomeActivity(aty_PopRequestPay.this);
					break;
				case PAY_SUCCESS:
					// 标记翼支付完成
					UpdateBestPayStatus(myApp.getOrderSeq(), true);// 更新翼支付完成状态

					Intent intent = null;
					if (myApp.isBigCard())
					{// 大卡
						intent = new Intent(aty_PopRequestPay.this,
								aty_NFC_bigCard_Circle.class);
					} else
					{// 小卡
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
	 * if (null != Ack) { // 发送充值申请 String terminalCode = Ack.substring(4,
	 * 16);// 4-15终端号 圈存使用 String Url = Ack.substring(16, Ack.length()); String
	 * Ip = Url.replace("http://", "").split(":")[0];
	 * myApplication.setServerIP(Ip);// 设置后台服务IP
	 * myApp.setTerminalCode(terminalCode);// 设置终端号
	 * myApplication.setVersionControlUrl(Url);// 设置版本更新地址
	 * myApplication.setApkDownloadUrl(Url);// 设置版本更新下载地址
	 * OrderParams.setBackMerchantURL(Url);// 设置后台返回地址 翼支付独有
	 * 
	 * // 调用对应支付平台，提交订单 Message msg = new Message(); msg.what =
	 * ORDERTO_PAY_PLATFROMS; // 设置提交订单提示 mHandler.sendMessage(msg);
	 * 
	 * // 调用支付平台 CallPay_Platfroms(myApp.getPayMethod()); } else // {
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
				// 发送充值申请
				String terminalCode = Ack.substring(4, 16);// 4-15终端号 圈存使用
				String Url = Ack.substring(16, Ack.length());
				String Ip = Url.replace("http://", "").split(":")[0];
				myApplication.setServerIP(Ip);// 设置后台服务IP
				myApp.setTerminalCode(terminalCode);// 设置终端号
				myApplication.setVersionControlUrl(Url);// 设置版本更新地址
				myApplication.setApkDownloadUrl(Url);// 设置版本更新下载地址
				OrderParams.setBackMerchantURL(Url);// 设置后台返回地址 翼支付独有

				// 调用对应支付平台，提交订单
				Message msg = new Message();
				msg.what = ORDERTO_PAY_PLATFROMS; // 设置提交订单提示
				mHandler.sendMessage(msg);
				System.out.println("in ack ---");
				// 调用支付平台
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
			//delayHandler.postDelayed(mRunnable, 1 * 1000);// 等待1s再执行，主要是为了等待小卡读卡完成
			
			m_swp = new UICC_swp(aty_PopRequestPay.this, new UICC_Interface()
			{

				@Override
				public void UICCTranSomeCmd()
				{
					// TODO Auto-generated method stub
					System.out.println("here");
					// 获取随机数
					myApp.setRandom(GetRandom());
					m_swp.UICCClose();//此处之需要获取随机数，所以用完马上关闭资源
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
	 * 获取随机数，获取失败者将当时时间的ms模式的后4位字符作为随机数
	 * 随机数大于4位，则只取4位
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
			// 第一次按返回键 给出提示
			myAlert.ShowToast(aty_PopRequestPay.this,
					getString(R.string.ClickBackAgain));
		} else
		{
			// 第二次按返回键 退出当前页面，返回上一页面
			backpress_cnt = 0;
			aty_PopRequestPay.this.finish();
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		// 关闭相关资源
		if (null != m_swp)
			m_swp.UICCClose();
		else
		{
			System.out.println("is null");
		}
		super.onDestroy();
	}

	/**
	 * 拼接申请支付帧，调用申请支付函数
	 * 
	 * @return 充值请求返回值，错误返回null，成功返回协议定义的配置信息
	 */
	private String RequestPay()
	{
		HashMap<String, String> map = new HashMap<String, String>();

		// Card_number 16 N 发行卡号
		// PhysicsCardNO 8 N 物理卡号
		// IMEI 15 N 终端唯一标识（全球唯一）
		// orderNo 30 AN 订单编号
		// Orderseq 30 AN 订单流水号
		// orderMount 8 N 订单金额 10进制 单位分，（至银行为浮点数 单位元）
		// OldMount 8 HEX 卡片余额 单位分
		// pubLen 2 H 公共信息长度
		// PublicData N H 公共信息
		// PayType 2 N 支付途径
		// 01 翼支付
		// 02 支付宝
		// 03 建设银行
		// 04 工商银行
		// 05
		System.out.println("aty_pop requestpay --");
		long time_ms = System.currentTimeMillis();
		Date date = new Date(time_ms);
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
		// 订单号唯一 30位
		String OrderNO = time + myApp.getRandom() + myApp.getRandom()
				+ myApp.getRandom();// 4位随机数
		// 订单流水号唯一 30位
		String OrderSeq = myApp.getRandom() + myApp.getRandom()
				+ myApp.getRandom() + time;

		String hexMoney;// 卡片余额
		String publicMsg_len;// 公共信息长度
		String publicMsg;// 公共信息
		String publishCardNO;// 发行卡号
		String physicCardNO;// 物理卡号
		if (myApp.isBigCard())
		{
			hexMoney = Integer.toHexString(myApp.getSwp_balance());
			publicMsg = myApp.getPublicMsg();
			publishCardNO = myApp.getPublishCardNO();
			physicCardNO = myApp.getPhysicsCardNO();
		} else
		{// 小卡
			hexMoney = Integer.toHexString(myApp.getSwp_balance());
			publicMsg = myApp.getSwp_PublicMsg();
			publishCardNO = myApp.getSwp_PublishCardNO();// myApp.getPublishCardNO();
			physicCardNO = myApp.getSwp_physicsCardNO();// myApp.getPhysicsCardNO();
		}
		hexMoney = to8String(hexMoney);
		// 订单金额
		String orderAmount = "" + myApp.getChargeMoney();
		orderAmount = to8String(orderAmount);

		// if(myApp.getPublicMsg().length()>0xff)
		if (publicMsg.length() > 0xff)
		{// 长度超出0xff则直接认为长度为0，让服务器不能通过申请
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
		// 保存订单信息,
		myApp.setOrderNO(OrderNO);
		myApp.setOrderSeq(OrderSeq);
		myApp.setOrderTime(time_ms);// 调用时，根据不同的支付平台要求转换格式
		myApp.setOrderAmount(orderAmount);

		// 拼接帧
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

		SaveOrder(map, myApp);// 保存订单

		return (protocol.RequestPay(aty_PopRequestPay.this, map, mHandler));

	}

	/**
	 * 保存订单,以及订单状态
	 * 
	 * @param map
	 * @param myApp
	 */
	private void SaveOrder(HashMap<String, String> map, myApplication myApp)
	{
		String orderTime = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date(myApp.getOrderTime()));
		
		String tac = null;
		// 新建sqlite表行
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
	 * 根据之前选择的支付方式，调用对应的支付平台
	 * 
	 * @param payMethod
	 *            // 01:翼支付 // 02:支付宝 // 03:建设银行 // 04:工商银行 // 05:农业银行
	 */
	private void CallPay_Platfroms(String payMethod)
	{
		int method;
		
		try
		{
			method = Integer.parseInt(payMethod);
		} catch (Exception e)
		{
			Log.d(TAG, "paymethod 支付方式格式错误,检查支付方式获取！" + e.getMessage());
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
	 * 翼支付帧拼接，之后调用翼支付支付平台
	 */
	private void BestPay()
	{
		Hashtable<String, String> paramsHashtable = new Hashtable<String, String>();
		paramsHashtable.put(Plugin.MERCHANTID, OrderParams.MERCHANTID);// 商户ID
		paramsHashtable.put(Plugin.SUBMERCHANTID, OrderParams.SUBMERCHANTID);// 子商户ID
																				// 必填
		paramsHashtable.put(Plugin.MERCHANTPWD, OrderParams.MERCHANTPWD); // 交易KEY

		paramsHashtable.put(Plugin.ORDERSEQ, // 订单编号
				myApp.getOrderNO());

		// 订单金额,单位：元 小数点后2位 订单金额=产品金额+附加金额
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

		// 产品金额，单位：元，小数点后2位，选填
		// paramsHashtable.put(Plugin.PRODUCTAMOUNT, "1.00");

		// 附加金额，单位：元，小数点后2位，选填
		// paramsHashtable.put(Plugin.ATTACHAMOUNT, "0.00");

		// 订单时间，格式yyyyMMddhhmmss
		String time = DateFormat.format("yyyyMMddHHmmss", myApp.getOrderTime())
				.toString();
		OrderParams.setOrderTime(time);
		paramsHashtable.put(Plugin.ORDERTIME, OrderParams.getOrderTime());

		// 有效订单时间，格式yyyyMMddhhmmss 此处设置有效时间5min
		String validtime = DateFormat.format("yyyyMMddHHmmss",
				myApp.getOrderTime() + 5 * 60 * 1000).toString();
		OrderParams.setOrder_validTime(validtime);
		paramsHashtable.put(Plugin.ORDERVALIDITYTIME,
				OrderParams.getOrder_validTime());
		// 产品名称
		paramsHashtable.put(Plugin.PRODUCTDESC, OrderParams.PRODUCTDESC);
		// 用户ID，必填
		paramsHashtable.put(Plugin.CUSTOMERID, "01");

		/*
		 * paramsHashtable .put(Plugin.PRODUCTAMOUNT,
		 * mAmmount.getText().toString());
		 * paramsHashtable.put(Plugin.ATTACHAMOUNT, "0.00");
		 */

		// 币种，固定填“RMB”
		paramsHashtable.put(Plugin.CURTYPE, OrderParams.CURTYPE);
		// 后台通知地址
		paramsHashtable.put(Plugin.BACKMERCHANTURL,
				OrderParams.getBackMerchantURL());// "http://127.0.0.1:8040/wapBgNotice.action");
		// 附加信息，必填
		paramsHashtable.put(Plugin.ATTACH, OrderParams.ATTACH);
		// 产品ID，必填
		paramsHashtable.put(Plugin.PRODUCTID, OrderParams.PRODUCTID);
		// 用户IP
		OrderParams.setUserIP(myApp.getServerIP());
		paramsHashtable.put(Plugin.USERIP, OrderParams.getUserIP());// paramsHashtable.put(Plugin.USERIP,
																	// "IP地址");
		// 分账明细，选填
		// paramsHashtable.put(Plugin.DIVDETAILS, "");
		/**
		 * 这里由商户实现；
		 */
		paramsHashtable.put(Plugin.KEY, OrderParams.KEY);//
		paramsHashtable.put(Plugin.ACCOUNTID, OrderParams.getAccountID());// mAccount.getText().toString());
		/**
		 * 资金源类型；《默认使用04
		 */
		paramsHashtable.put(Plugin.BUSITYPE, OrderParams.BUSITYPE);// merchantbank.getText().toString());

		// 订单流水号 30位，唯一
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
				// + "这里是商户的key"; //02420105024032000
				+ paramsHashtable.get(Plugin.KEY);

		System.out.println("in pay");

		try
		{
			mac = CryptTool.md5Digest(mac);
		} catch (Exception e)
		{

			Log.d(TAG, "md5 计算出错，pay");
			// 关闭socket资源
			e.printStackTrace();
		}
		// MAC 加密
		paramsHashtable.put(Plugin.MAC, mac);

		Request_BestPayPlatfroms(aty_PopRequestPay.this, mHandler,
				paramsHashtable);

	}

	/**
	 * 调用翼支付收银台
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
			// 增加下单流程
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
						// 在mHandler中关闭资源，删除下单失败订单，并退回主界面
						handler.sendEmptyMessage(ORDER_FAIL);
					}
					//Looper.loop();
				}
			}.start();
		} else
		{

			Log.d("订单：", "商铺号：" + paramsHashtable.get(Plugin.MERCHANTID)
					+ "订单号：" + paramsHashtable.get(Plugin.ORDERSEQ) + " 订单流水号："
					+ paramsHashtable.get(Plugin.ORDERREQTRANSEQ));

			// 调用PAY，启动支付插件
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
			if (BestPay_RequestCode == requestCode)// 判断是否是翼支付收银台返回
			{
				System.out
						.println("back from bestpay platfrom! the back msg = "
								+ resMsg);
				//翼支付返回之后，再次向后台服务器查询订单状态，确认之后再进行后续处理
				if (!ProcessBestPayAck(resMsg))
				{
					myAlert.ShowToast(aty_PopRequestPay.this,
							getString(R.string.BestPay_PayFail));// 支付失败，给出提示，返回主界面
					// 清理订单产生的信息 ？
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
			if (BestPay_RequestCode == requestCode)// 判断是否是翼支付收银台返回
			{
				System.out
						.println("back from bestpay platfrom! the back msg = "
								+ resMsg);
				//翼支付返回之后，再次向后台服务器查询订单状态，确认之后再进行后续处理
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Looper.prepare();
						
						boolean ack1 = protocol.CheckPayStatus(aty_PopRequestPay.this, myApp, mHandler);
						boolean ack2 = ProcessBestPayAck(resMsg);
						
						Message msg = mHandler.obtainMessage();
						
						if( (ack1) || (ack2) ) //后台查询和翼支付收银台其中一个返回成功，即认为支付成功
						{
							msg.what = PAY_SUCCESS;
							mHandler.sendMessage(msg);
						}
						else if((!ack1) && (!ack2))//后台查询和翼支付收银台均返回支付失败，则支付失败
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
	 * 处理翼支付返回结果
	 * 
	 * @param resMsg
	 * @return 支付成功返回true 否则false
	 *//*
	private boolean ProcessBestPayAck(String resMsg)
	{
		// reequestCode:
		// Plugin.REQUEST_SUBMIT_ORDER 提交订单去支付请求
		// Plugin.REQUEST_BESTPAY_EXCHARGE 翼支付充值请求
		// Plugin.REQUEST_THIRDPART_EXCHARGE 第三方充值请求
		// resultCode：
		// Context.RESULT_OK (-1) 标示受理成功
		// data：请求数据成功返回success
		if (resMsg.equals("取消支付"))
		{
			// 删除取消支付的订单
			DeleteInvalidOrderByTranseq(myApp.getOrderSeq());
			return false;
		}
		if (resMsg.equals("支付成功"))
		{
			// 标记翼支付完成
			UpdateBestPayStatus(myApp.getOrderSeq(), true);// 更新翼支付完成状态

			Intent intent = null;
			if (myApp.isBigCard())
			{// 大卡
				intent = new Intent(aty_PopRequestPay.this,
						aty_NFC_bigCard_Circle.class);
			} else
			{// 小卡
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

			// 标记翼支付未完成
			UpdateBestPayStatus(myApp.getOrderSeq(), false);// 更新翼支付完成状态

			// 关闭资源，返回主界面
			// CloseRes();
			// BackHomeActivity(Aty_ChargeWait.this);

			return false;
		}
	}*/
	/**
	 * 处理翼支付返回结果
	 * 
	 * @param resMsg
	 * @return 支付成功返回true 否则false
	 */
	private boolean ProcessBestPayAck(String resMsg)
	{
		// reequestCode:
		// Plugin.REQUEST_SUBMIT_ORDER 提交订单去支付请求
		// Plugin.REQUEST_BESTPAY_EXCHARGE 翼支付充值请求
		// Plugin.REQUEST_THIRDPART_EXCHARGE 第三方充值请求
		// resultCode：
		// Context.RESULT_OK (-1) 标示受理成功
		// data：请求数据成功返回success
		if (resMsg.equals("取消支付"))
		{
			return false;
		}
		if (resMsg.equals("支付成功"))
		{
			return true;
		} 
			
		return false;
		
	}
	
	/**
	 * 删除无效订单记录 ---向服务器申请支付失败，翼支付下单失败
	 * 
	 * @param Transeq
	 *            订单流水号
	 * @return 删除的记录数
	 */
	private int DeleteInvalidOrderByTranseq(String Transeq)
	{
		int num = 0;
		DbManager dm = new DbManager(aty_PopRequestPay.this);
		// 首先判断是否为无效订单判断，避免误删
		// 查找对应无效订单信息
		Cursor c = dm.QueryByInvalidOrder(Transeq);
		for (int i = 0; i < c.getCount(); i++)
		{
			num = dm.DeletebyReqTranse(Transeq);
		}
		c.close();
		dm.closeDB();
		System.out.println("--> delete " + num + "个"
				+ OrderParams.getOrder_Transeq());

		return num;
	}

	/**
	 * 更新翼支付完成状态
	 * 
	 * @param OrderSeq
	 *            订单流水号
	 * @param PayStatus
	 *            true:支付成功 false：支付失败
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
		if (0 != c.getCount())// 数据库中存在该订单流水号的订单信息
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

				Log.d(TAG, "更新状态出错，onActivityResult");
				// 出错先关闭数据库资源
				c.close();
				dm.closeDB();
				e.printStackTrace();
			}
		}
		c.close();
		dm.closeDB();
	}

	/**
	 * 返回主界面，并尝试调用当前Activity 的onDestory
	 * 
	 * @param context
	 *            当前Activity.this
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity) context).finish();
	}

	// /**
	// * 请求圈存帧拼接
	// * @return 成功返回指令，否则返回null
	// */
	// private String RequestCircleCmd(myApplication myApp)
	// {
	//
	// // orderNo 30 AN 订单编号
	// // Orderseq 30 AN 订单流水号
	// // PublicData 74 H 公共信息(可补全F)
	// // Card_number 16 N 发行卡号
	// // orderMount 8 N 订单金额 单位分
	// // data8050 32 N 卡片8050返回信息
	// HashMap<String, String> map = new HashMap<String, String>();
	// String publishData = "";
	// for(int i = 0; i < 74; i++)
	// publishData += "F";
	// //订单金额
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
	// //调用请求圈存指令
	// return (protocol.RequestCirleCmd(aty_PopRequestPay.this, map, mHandler));
	// }

	/**
	 * 将字符串小于8位的补齐为8位，补齐方法：左补零
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
		//避免系统切换屏幕方向是，多次调用申请翼支付线程
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
