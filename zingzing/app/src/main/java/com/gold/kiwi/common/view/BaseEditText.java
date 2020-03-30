package com.gold.kiwi.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

public class BaseEditText extends androidx.appcompat.widget.AppCompatEditText
{
	private TextView.OnEditorActionListener onEditorActionListener;

	public BaseEditText(Context context)
	{
		super(context);
	}

	public BaseEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setOnEditorActionListener(TextView.OnEditorActionListener listener)
	{
		onEditorActionListener = listener;
		super.setOnEditorActionListener(listener);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
		{
			if(onEditorActionListener != null)
			{
				//you can define and use custom listener,
				//OR define custom R.id.<imeId>
				//OR check event.keyCode in listener impl
				//* I used editor action because of ButterKnife @
				onEditorActionListener.onEditorAction(this, android.R.id.closeButton, event);
			}
		}

		return super.onKeyPreIme(keyCode, event);
	}
}
