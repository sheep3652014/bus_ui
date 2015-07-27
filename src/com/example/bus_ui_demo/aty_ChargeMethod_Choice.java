package com.example.bus_ui_demo;

import java.math.BigDecimal;

import com.example.Alert.myAlert;
import com.example.application.myApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class aty_ChargeMethod_Choice extends Activity
{
	private EditText et_PhoneNO;
	private EditText et_Password;
	private Button btn_next;
	private Spinner Spinner_choice;
	private LinearLayout Lin_bestpay;
	private TextView tv_chargeMoney;
	
	private static String[] payMethods;
	
	private myApplication myApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aty_chargemethod_choice_layout);
		
		payMethods = getResources().getStringArray(R.array.payMethod);
		myApp = (myApplication) getApplicationContext();
		
		FindView();
		Init();
	}
	
	private void FindView()
	{
		tv_chargeMoney = (TextView) findViewById(R.id.tv_chargeMoney);
		et_PhoneNO = (EditText) findViewById(R.id.et_PhoneNO);
		et_Password = (EditText) findViewById(R.id.et_password);
		
		btn_next = (Button) findViewById(R.id.btn_next);
		Lin_bestpay = (LinearLayout) findViewById(R.id.Lin_bestpay);
		
		Spinner_choice = (Spinner) findViewById(R.id.spinner_method);
	}
	
	private void Init()
	{
		float charge = new BigDecimal((double)myApp.getChargeMoney()/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_chargeMoney.setText(charge + getString(R.string.YUAN));
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty_ChargeMethod_Choice.this, android.R.layout.simple_spinner_item, payMethods);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty_ChargeMethod_Choice.this, R.layout.simple_spinner_item, payMethods);
		Spinner_choice.setAdapter(adapter);
		Spinner_choice.invalidate();

		Spinner_choice.setSelection(0);//Ĭ��ѡ����֧��
		Lin_bestpay.setVisibility(View.VISIBLE);//ѡ����֧��ʱ��֧����¼��ɼ�
		
		btn_next.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(AdapterView.INVALID_POSITION != Spinner_choice.getSelectedItemPosition())
				{
					Intent intent = new Intent(aty_ChargeMethod_Choice.this, aty_PopRequestPay.class);
					startActivity(intent);
				}
				else
				{
					myAlert.ShowToast(aty_ChargeMethod_Choice.this, getString(R.string.paymethod_need_select));
				}
			}
		});
		
		Spinner_choice.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, getString(R.string.paymethod_toast_notice)+payMethods[position]);
				if(0 != position)//��֧����¼��ֻ��ѡ����֧��ʱ�ɼ�
				{
					Lin_bestpay.setVisibility(View.GONE);
				}
				else if(0 == position)
				{
					Lin_bestpay.setVisibility(View.VISIBLE);
				}
				
				SetPayMethod(myApp, position);
				PayMethodNotice(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				System.out.println("nothing selected");
			}
			
		});
	}
	
	/**
	 * ����ѡ��ĳ�ֵ��ʽ���ύ��Ӧ�ĳ�ֵ����
	 * @param choice
	 */
	private void RequestPaybyChoice(int choice)
	{
		
	}
	
	/**
	 * ��ѡ��֧����ʽ�Ŀ�ͨ����
	 * @param choice
	 */
	private void PayMethodNotice(final int choice)
	{
//		0:��֧��
//      1:֧����
//      2:��������
//      3:��������
//      4:ũҵ����
		switch (choice)
		{
			case 0://��֧��
			default:
				
				break;
			case 1://֧����
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "�ݲ�֧��");
				break;	
			case 2://����
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "�ݲ�֧��");
				break;
			case 3://����
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "�ݲ�֧��");
				break;
			case 4://ũ��
				myAlert.ShowToast(aty_ChargeMethod_Choice.this, "�ݲ�֧��");
				break;
			
		}
	}

	/**
	 * ���ݻ�ȡ��֧����ʽ����ΪЭ��涨��֧����ʽ��ʽ
	 * @param myApp
	 * @param choice
	 */
	private void SetPayMethod(myApplication myApp, final int choice)
	{
		int mychoice = choice;
		mychoice = mychoice+1; //��ȡ��ֵ��0��ʼ��Э���д�1��ʼ
		if((""+mychoice).length()<2)
			myApp.setPayMethod("0"+mychoice);
		else
			myApp.setPayMethod(""+mychoice);
	}
	
}
