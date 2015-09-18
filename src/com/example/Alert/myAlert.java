package com.example.Alert;

import java.net.InterfaceAddress;

import com.example.bus_ui_demo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.StaticLayout;
import android.widget.Toast;

public class myAlert
{
	/**
	 * Toast��ʾ
	 * @param context
	 * @param content
	 */
	public static void ShowToast(Context context, String content)
	{
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * ��ʾ����ť��ʾ��
	 * @param context
	 * @param content
	 */
	public static void ShowOneClickDialog(Context context, String content, final ShowOneClickDialog_Interface myInterface)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.Dialog_title));
		builder.setMessage(content);
		builder.setNegativeButton(context.getString(R.string.Dialog_onButton_Content), new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				myInterface.DoSomething();
			}
		});
		builder.create();
		builder.show();
	}
	
	public static interface ShowOneClickDialog_Interface
	{
		public void DoSomething();
	}
	
	/**
	 * ��ʾ����ť��ʾ��
	 * @param context
	 * @param content ��ʾ����
	 * @param negative ��ť����
	 * @param positive ��ť����
	 * @param myInterface ����ť��ʾ�����¼��ӿ�
	 */
	public static void ShowTwoClickDialog(Context context, String content, String negative, String positive,
			final ShowTwoClickDialog_Interface myInterface)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.Dialog_title));
		builder.setMessage(content);
		builder.setNegativeButton(negative, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				myInterface.DoNegative();
			}
		});
		
		builder.setPositiveButton(positive, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				myInterface.DoPositive();
			}
		});
		builder.create();
		builder.show();
	}
	
	/**
	 * ����ť��ʾ�����¼��ӿ�
	 * @author tom
	 *
	 */
	public static interface ShowTwoClickDialog_Interface
	{
		public void DoPositive();
		public void DoNegative();
	}
}
