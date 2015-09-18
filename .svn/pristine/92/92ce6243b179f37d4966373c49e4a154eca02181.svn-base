package com.example.Alert;

import com.example.bus_ui_demo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class myAlert
{
	/**
	 * Toast提示
	 * @param context
	 * @param content
	 */
	public static void ShowToast(Context context, String content)
	{
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示单按钮提示框
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
}
