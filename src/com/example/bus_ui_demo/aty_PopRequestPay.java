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
import com.example.network.PayParams;
import com.example.network.protocol;
import com.example.bestpay.CryptTool;
import com.example.bestpay.OrderParams;
import com.example.bestpay.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
	private static final int ORDERTO_PAY_PLATFROMS = Global_Config.INNER_MSG_START + 0;//开始向充值平台提交订单
	private static final int ORDER_SUCCESS = Global_Config.INNER_MSG_START + 1;//翼支付下单成功
	private static final int ORDER_FAIL = Global_Config.INNER_MSG_START + 2;//翼支付下单失败
	private static final int CIRCLE_CMD_FAIL = Global_Config.INNER_MSG_START + 3;//获取圈存指令失败
	private static final int CIRCLE_CMD_SUCCESS = Global_Config.INNER_MSG_START + 4;//获取圈存指令失败
	
	private static final String CIRCLE_CMD_STR = "circleCmd";
	
	private static final String TAG = "aty_PopRequestPay";
	private TextView tv_notice;
	
	private final boolean mNeedOrder = true;//用于翼支付
	private static final int BestPay_RequestCode = Global_Config.BESTPAY_REQUESTCODE;//翼支付请求码，反编译得来
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_poprequetpay_layout);
		
		myApp = (myApplication) getApplicationContext();
		
		FindView();
		
		mThread.start();
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
		if(backpress_cnt++ < 1)
		{
			//第一次按返回键 给出提示
			myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.ClickBackAgain));
		}
		else {
			//第二次按返回键 退出当前页面，返回上一页面
			backpress_cnt = 0;
			aty_PopRequestPay.this.finish();
			super.onBackPressed();
		}
	}
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch (msg.what)
			{
				case CONNECT_ERROR:
					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.ConnectServer_error));
					break;
				case ORDERTO_PAY_PLATFROMS:
					tv_notice.setText(getString(R.string.PayRequest2platfrom));//设置提交订单提示
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
					break;
//				case CIRCLE_CMD_FAIL:
//					//获取指令失败，给出提示，返回首页
//					tv_notice.setText(getString(R.string.RequestCirlce_fail));
//					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.RequestCirlce_fail));
//					BackHomeActivity(aty_PopRequestPay.this);
//					break;
//				case CIRCLE_CMD_SUCCESS:
//					tv_notice.setText(getString(R.string.RequestCirlce_success));
//					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.RequestCirlce_success));
//					//获取指令，并跳转至圈存写卡页面
//					String cmd = msg.getData().getString(CIRCLE_CMD_STR);
//					myApp.setBigCard_CircleCmd(cmd);
//					Intent intent = new Intent(aty_PopRequestPay.this, aty_NFC_bigCard_Circle.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					break;
				default:
					break;
			}
		}
		
	};
	
	Thread mThread = new Thread(new Runnable()
	{
		
		@Override
		public void run()
		{
			Looper.prepare();
			
			String Ack = RequestPay();

			if(null != Ack)
			{
				//发送充值申请
				String terminalCode = Ack.substring(4, 16);//4-15终端号 圈存使用
				String Url = Ack.substring(16, Ack.length());
				String Ip = Url.replace("http://", "").split(":")[0];
				myApplication.setServerIP(Ip);//设置后台服务IP
				myApp.setTerminalCode(terminalCode);//设置终端号
				myApplication.setVersionControlUrl(Url);//设置版本更新地址
				myApplication.setApkDownloadUrl(Url);//设置版本更新下载地址
				OrderParams.setBackMerchantURL(Url);// 设置后台返回地址 翼支付独有
					
				//调用对应支付平台，提交订单
				Message msg = new Message();
				msg.what = ORDERTO_PAY_PLATFROMS; //设置提交订单提示
				mHandler.sendMessage(msg);
					
				//调用支付平台
				CallPay_Platfroms(myApp.getPayMethod());
			}
			else //
			{
				myAlert.ShowOneClickDialog(aty_PopRequestPay.this, getString(R.string.PayRequest_error), new myAlert.ShowOneClickDialog_Interface()
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
	});
	
	/**
	 * 拼接申请支付帧，调用申请支付函数
	 * @return 充值请求返回值，错误返回null，成功返回协议定义的配置信息
	 */
	private String RequestPay()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
//		Card_number	16	N	发行卡号
//		PhysicsCardNO	8	N	物理卡号
//		IMEI	15	N	终端唯一标识（全球唯一）
//		orderNo	30	AN	订单编号
//		Orderseq	30	AN	订单流水号
//		orderMount	8	N	订单金额 10进制 单位分，（至银行为浮点数 单位元） 
//		OldMount	8	HEX	卡片余额 单位分
//		pubLen	2	H	公共信息长度
//		PublicData	N	H	公共信息
//		PayType	2	N	支付途径
//		01 翼支付
//		02 支付宝
//		03 建设银行
//		04 工商银行
//		05 
		long time_ms = System.currentTimeMillis();
		Date date = new Date(time_ms);
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
		//订单号唯一  30位
		String OrderNO = time + myApp.getRandom() + myApp.getRandom() +myApp.getRandom();//4位随机数
		//订单流水号唯一 30位
		String OrderSeq = myApp.getRandom() + myApp.getRandom() +myApp.getRandom() + time;
		//卡片余额
		String hexMoney = Integer.toHexString(myApp.getBeforeChargeMoney()); 
		hexMoney = to8String(hexMoney);
		//订单金额
		String orderAmount = ""+myApp.getChargeMoney();
		orderAmount = to8String(orderAmount);
		
		String publishMsg_len;
		if(myApp.getPublishMsg().length()>0xff)
		{//长度超出0xff则直接认为长度为0，让服务器不能通过申请
			publishMsg_len = "00";
		}
		else {
			publishMsg_len = Integer.toHexString(myApp.getPublishMsg().length());
			System.out.println("len = "+publishMsg_len + "_"+myApp.getPublishMsg().length()+ "__"+myApp.getPublishMsg());
		}
		//保存订单信息,
		myApp.setOrderNO(OrderNO);
		myApp.setOrderSeq(OrderSeq);
		myApp.setOrderTime(time_ms);//调用时，根据不同的支付平台要求转换格式
		myApp.setOrderAmount(orderAmount);
		
		//拼接帧
		map.put(PayParams.CARD_NUMBER, myApp.getPublishCardNO());
		map.put(PayParams.PHYSICS_CARDNO, myApp.getPhysicsCardNO());
		map.put(PayParams.IMEI_NO, myApp.getIMEI());
		map.put(PayParams.ORDERNO, OrderNO);
		map.put(PayParams.ORDERSEQ, OrderSeq);
		map.put(PayParams.ORDER_AMOUNT, orderAmount);
		map.put(PayParams.OLDAMOUNT, hexMoney);
		map.put(PayParams.PUBLEN, publishMsg_len);
		map.put(PayParams.PUBLIC_DATA, myApp.getPublishMsg());
		map.put(PayParams.PAYTYPE, myApp.getPayMethod());
		
		return(protocol.RequestPay(aty_PopRequestPay.this,map, mHandler));
		
	}
	
	/**
	 * 根据之前选择的支付方式，调用对应的支付平台
	 * @param payMethod //        01:翼支付
	 *		 			//        02:支付宝
	 *		 			//        03:建设银行
	 *		 			//        04:工商银行
	 *		 			//        05:农业银行
	 */
	private void CallPay_Platfroms(String payMethod)
	{
		int method;;
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
		paramsHashtable.put(Plugin.SUBMERCHANTID, OrderParams.SUBMERCHANTID);// 子商户ID 必填
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
			System.out.println("parseint error "+e.getMessage());
		}	
		
		float money = new BigDecimal((double)m/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		OrderParams.setChargeMoney(""+money);
		paramsHashtable.put(Plugin.ORDERAMOUNT, OrderParams.getChargeMoney());
		Log.d("money in pay", "" + paramsHashtable.get(Plugin.ORDERAMOUNT)
				+ "_" + OrderParams.getChargeMoney());
		
		// 产品金额，单位：元，小数点后2位，选填
		// paramsHashtable.put(Plugin.PRODUCTAMOUNT, "1.00");

		// 附加金额，单位：元，小数点后2位，选填
		// paramsHashtable.put(Plugin.ATTACHAMOUNT, "0.00");

		// 订单时间，格式yyyyMMddhhmmss
		String time = DateFormat.format("yyyyMMddHHmmss", myApp.getOrderTime()).toString();
		OrderParams.setOrderTime(time);
		paramsHashtable.put(Plugin.ORDERTIME, OrderParams.getOrderTime());
		
		// 有效订单时间，格式yyyyMMddhhmmss 此处设置有效时间5min
		String validtime = DateFormat.format("yyyyMMddHHmmss", myApp.getOrderTime() + 5*60*1000).toString();
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
		
		//订单流水号  30位，唯一		OrderParams.setOrder_Transeq(myApp.getOrderSeq());
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
		
		Request_BestPayPlatfroms(aty_PopRequestPay.this, mHandler, paramsHashtable);
		
	}
	
	/**
	 * 调用翼支付收银台
	 * @param context 
	 * @param handler 
	 * @param paramsHashtable
	 */
	private void Request_BestPayPlatfroms(Context context,final Handler handler, Hashtable<String, String> paramsHashtable)
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
					Looper.prepare();

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
						//在mHandler中关闭资源，删除下单失败订单，并退回主界面
						handler.sendEmptyMessage(ORDER_FAIL);
					}
					Looper.loop();
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		System.out.println(requestCode+"__"+requestCode);
		if (data != null)
		{
			System.out.println("___back" + data.toString() + "_"
					+ data.getExtras().toString() + "_"
					+ data.getStringExtra("result"));//data maybe null
			
			String resMsg = data.getStringExtra("result");
			
			System.out.println("in data!=null");
			if(BestPay_RequestCode == requestCode)//判断是否是翼支付收银台返回
			{
				System.out.println("back from bestpay platfrom! the back msg = " + resMsg);
				if(ProcessBestPayAck(resMsg))
				{
					//跳转至圈存写卡界面
					
//					//向后台发送支付成功消息，请求圈存指令，成功获取指令后，跳转至圈存写卡界面
//					new Thread(new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							// TODO Auto-generated method stub
//							Looper.prepare();
//							String Ack = RequestCircleCmd(myApp);
//							if(null != Ack)
//							{//成功获取圈存指令
//								Message msg = new Message();
//								msg.what = CIRCLE_CMD_SUCCESS;
//								Bundle data = new Bundle();
//								data.putString(CIRCLE_CMD_STR, Ack);
//								msg.setData(data);
//								mHandler.sendMessage(msg);
//							}
//							else
//							{//获取圈存指令失败
//								Message msg = new Message();
//								msg.what = CIRCLE_CMD_FAIL;
//								mHandler.sendMessage(msg);
//							}
//							Looper.loop();
//						}
//					}).start();
				}
				else //支付失败
				{
					myAlert.ShowToast(aty_PopRequestPay.this, getString(R.string.BestPay_PayFail));//支付失败，给出提示，返回主界面
					//清理订单产生的信息 ？
					BackHomeActivity(aty_PopRequestPay.this);
				}
			}
			
			System.out.println("--> " + requestCode + "_" + resultCode + "_" + data.getStringExtra("result"));
			//CancelCharge(data);
		}
	}
	
	/**
	 * 处理翼支付返回结果
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
		            if(resMsg.equals("取消支付")) return false;
					
		            if (resMsg.equals("支付成功"))
					{
		            	// 标记翼支付完成，进入请求圈存指令
		            	OrderParams.setIsChargeSucces(true);
		            	
						// 翼支付充值完成，提醒用户将卡片再次贴近手机，读取写卡初始化信息
		            	Intent intent = new Intent(aty_PopRequestPay.this, aty_NFC_bigCard_Circle.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						aty_PopRequestPay.this.finish();
						
						//UpdateBestPayStatus();//更新翼支付完成状态
						
						// 弹出充值完成提示界面,并传递参数
						//Intent intent = new Intent(Aty_ChargeWait.this,
						//		Aty_ChargeOK.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("Money", OrderParams.getChargeMoney());//Order_Amount);
//						bundle.putString("OrderSeq", OrderParams.getOrder_Seq());//Order_Seq);
//						bundle.putString("TranSeq", OrderParams.getOrder_Transeq());//Order_Reqtranse);
						//intent.putExtras(bundle);
						//startActivity(intent);
						
						return true;
					} else
					{
						System.out.println("pay fail");

						// 标记翼支付未完成
						OrderParams.setIsChargeSucces(false);

						//UpdateBestPayStatus();//更新翼支付完成状态
						
						//关闭资源，返回主界面
						//CloseRes();
						//BackHomeActivity(Aty_ChargeWait.this);
						
						return false;
					}
	}

	/**
	 * 返回主界面，并尝试调用当前Activity 的onDestory
	 * @param context 当前Activity.this
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		((Activity) context).finish();
	}
	
//	/**
//	 * 请求圈存帧拼接
//	 * @return 成功返回指令，否则返回null
//	 */
//	private String RequestCircleCmd(myApplication myApp)
//	{
//
////		orderNo	30	AN	订单编号
////		Orderseq	30	AN	订单流水号
////		PublicData	74	H	公共信息(可补全F)
////		Card_number	16	N	发行卡号
////		orderMount	8	N	订单金额 单位分
////		data8050	32	N	卡片8050返回信息
//		HashMap<String, String> map = new HashMap<String, String>();
//		String publishData = "";
//		for(int i = 0; i < 74; i++)
//			publishData += "F";
//		//订单金额
//		String orderAmount = ""+myApp.getChargeMoney();
//		orderAmount = to8String(orderAmount);
//		
//		map.put(PayParams.ORDERNO, myApp.getOrderNO());
//		map.put(PayParams.ORDERSEQ, myApp.getOrderSeq());
//		map.put(PayParams.PUBLIC_DATA, publishData);
//		map.put(PayParams.CARD_NUMBER, myApp.getPublishCardNO());
//		map.put(PayParams.ORDER_AMOUNT, orderAmount);
//		map.put(PayParams.CIRCLEINIT_DATA, myApp.getCircleInitMsg());
//		
//		
//		//调用请求圈存指令
//		return (protocol.RequestCirleCmd(aty_PopRequestPay.this, map, mHandler));
//	}
	
	/**
	 * 将字符串小于8位的补齐为8位，补齐方法：左补零
	 * @param str
	 * @return
	 */
	private String to8String(String str)
	{	
		final int strlen = str.length();
		System.out.println("--before "+str +"_"+str.length());
		if(str.length() < 8)
		{
			for(int i = 0; i < (8-strlen); i++)
			{	
				str = "0"+str;
			}
		}
		
		System.out.println("--after "+str+"_"+str.length());
		return str;
	}
}
