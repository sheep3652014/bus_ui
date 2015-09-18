package com.example.bus_ui_demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.Alert.myAlert;
import com.example.application.myApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class aty_SWP_ChargeMoneySelect extends Activity
{
//	ע�⣺��Ƭ������Ϊ800.0Ԫ������ǰ���Ϊ300.0Ԫ����໹���ٳ�500.0Ԫ��
	private static final String NOTICE_HEAD = "ע�⣺��Ƭ������Ϊ800.00Ԫ������ǰ���Ϊ";
	private static final String NOTICE_MIDDLE = "Ԫ����໹���ٳ�";
	private static final String NOTICE_TAIL = "Ԫ��";
	
	private ListView listview;
	private TextView tv_notice;
	private Button btn_payMethod;
	
	private myApplication myApp = null;
	private int chargeMoney = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//������Ļ����ֻ����ֱ
		setContentView(R.layout.aty_swp_chargemoneyselect_layout);
		
		myApp = (myApplication) getApplicationContext();
		
		listview=(ListView)findViewById(R.id.lv_ChargeSelect);
		//listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,getData()));
		listview.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_single_choice_text16sp,getData()));
		listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
					long arg3) {
				// TODO Auto-generated method stub
				listview.setItemChecked(postion, true);
				if(postion==0){
					chargeMoney = 10*100;
				}else if(postion==1){
					chargeMoney = 30*100;
				}else if(postion==2){
					chargeMoney = 50*100;
				}else if(postion==3){
					chargeMoney = 100*100;
				}else if(postion==4){
					chargeMoney = 200*100;
				}else if(postion==5){
					chargeMoney = 1;
				}
				
				
			}
		});
		
		listview.setItemChecked(0, false);//Ĭ�ϳ�ֵ10Ԫ
		
		init();
	}
	
	private void init(){
		btn_payMethod =(Button )findViewById(R.id.btn_payMethod);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		
		int m = myApp.getSwp_balance();
		float money = new BigDecimal((double)m/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		float m1 = new BigDecimal((double)(80000-m)/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
		tv_notice.setText(NOTICE_HEAD + money + NOTICE_MIDDLE + m1 + NOTICE_TAIL);
		
		btn_payMethod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listview.getCheckedItemCount() < 1)//û��ѡ���ֵ�����ܽ����ֵ����
				{
					//Toast.makeText(aty_SWP_ChargeMoneySelect.this, getString(R.string.bigCard_youneedChoice), Toast.LENGTH_SHORT).show();
					myAlert.ShowToast(aty_SWP_ChargeMoneySelect.this, getString(R.string.bigCard_youneedChoice));
					return;
				}
				else
				{
					//�����ֵ���	
					myApp.setChargeMoney(chargeMoney);
					Intent intentE =new Intent();
					intentE.setClass(aty_SWP_ChargeMoneySelect.this,aty_ChargeMethod_Choice.class);
					startActivity(intentE);	
					//aty_SWP_ChargeMoneySelect.this.finish();
				}
				
			}
		});
		
	}

	/**
	 * ���������棬�����Ե��õ�ǰActivity ��onDestory
	 * @param context ��ǰActivity.this
	 */
	private void BackHomeActivity(Context context)
	{
		Intent intent = new Intent(context, aty_SWP_main.class);
		startActivity(intent);
		((Activity) context).finish();
	}
	
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		BackHomeActivity(aty_SWP_ChargeMoneySelect.this);
		super.onBackPressed();
	}

	private List<String >getData(){
		List<String >data =new ArrayList<String>();
		data.add("10.00Ԫ");
		data.add("30.00Ԫ");
		data.add("50.00Ԫ");
		data.add("100.00Ԫ");
		data.add("200.00Ԫ");
		data.add("0.01Ԫ");
		return data;
		
	}
}
