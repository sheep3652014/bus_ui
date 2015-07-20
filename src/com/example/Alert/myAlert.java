package com.example.Alert;

import android.content.Context;
import android.widget.Toast;

public class myAlert
{
	
	public static void ShowToast(Context context, String content)
	{
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
	
	
}
