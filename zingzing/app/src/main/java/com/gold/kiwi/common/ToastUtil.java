package com.gold.kiwi.common;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
	private Toast toast = null;
	private Context context;

	public ToastUtil(Context context)
	{
		this.context = context;

		if(toast == null)
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}

	public void showToast(String value)
	{
		showToast(value, Toast.LENGTH_SHORT);
	}

	public void showToast(String value, int duration)
	{
		showToast(value, duration, false);
	}

	public void showToast(String value, int duration, boolean duplication)
	{
		if(duplication)
		{
			toast = Toast.makeText(context, value, duration);
			toast.show();
		}

		else
		{
			if(toast != null)
			{
				toast.setText(value);
				toast.setDuration(duration);
				toast.show();
			}
		}
	}

	public void cancelToast()
	{
		if(toast != null)
			toast.cancel();
	}
}
