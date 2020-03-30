package com.gold.kiwi.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gold.kiwi.zingzing.R;

public class CommonAlertDialog extends AlertDialog.Builder implements View.OnClickListener
{
	private Context context;
	private AlertDialog dialog;

	private LinearLayout ll_title;
	private LinearLayout ll_button;

	private TextView tv_title;
	private TextView tv_message;

	private Button btn_positive;
	private Button btn_negative;

	private DialogInterface.OnClickListener positiveListener;
	private DialogInterface.OnClickListener negativeListener;

	public CommonAlertDialog(Context context)
	{
		super(context);
		this.context = context;
		setCancelable(false);

		View view = View.inflate(context, R.layout.basic_dialog, null);

		ll_title = view.findViewById(R.id.ll_title);
		ll_button = view.findViewById(R.id.ll_button);

		tv_title = view.findViewById(R.id.tv_title);
		tv_message = view.findViewById(R.id.tv_message);

		btn_positive = view.findViewById(R.id.btn_positive);
		btn_negative = view.findViewById(R.id.btn_negative);

		btn_positive.setOnClickListener(this);
		btn_negative.setOnClickListener(this);

		setView(view);
	}

	public CommonAlertDialog(Context context, boolean cancelable)
	{
		this(context);
		setCancelable(cancelable);
	}

	@Override
	public CommonAlertDialog setTitle(int titleId)
	{
		tv_title.setText(titleId);
		ll_title.setVisibility(View.VISIBLE);

		return this;
	}

	@Override
	public CommonAlertDialog setTitle(CharSequence title)
	{
		tv_title.setText(title);
		ll_title.setVisibility(View.VISIBLE);

		return this;
	}

	@Override
	public CommonAlertDialog setMessage(int messageId)
	{
		tv_message.setText(messageId);

		return this;
	}

	@Override
	public CommonAlertDialog setMessage(CharSequence message)
	{
		tv_message.setText(message);

		return this;
	}

	@Override
	public CommonAlertDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_positive.setVisibility(View.VISIBLE);

		btn_positive.setText(text);
		positiveListener = listener;

		return this;
	}

	@Override
	public CommonAlertDialog setPositiveButton(int textId, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_positive.setVisibility(View.VISIBLE);

		btn_positive.setText(textId);
		positiveListener = listener;

		return this;
	}

	public void setPositiveButton(DialogInterface.OnClickListener listener)
	{
		this.setPositiveButton(context.getString(android.R.string.ok), listener);
	}

	@Override
	public CommonAlertDialog setNegativeButton(int textId, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_negative.setVisibility(View.VISIBLE);

		btn_negative.setText(textId);
		negativeListener = listener;

		return this;
	}

	@Override
	public CommonAlertDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_negative.setVisibility(View.VISIBLE);

		btn_negative.setText(text);
		negativeListener = listener;

		return this;
	}

	public void setNegativeButton(DialogInterface.OnClickListener listener)
	{
		this.setNegativeButton(context.getString(android.R.string.cancel), listener);
	}

	public boolean isShowing()
	{
		if(dialog == null)
			return false;

		else
			return dialog.isShowing();
	}

	public AlertDialog show()
	{
		dialog = create();
		dialog.show();

		return dialog;
	}

	public void dismiss()
	{
		dialog.dismiss();
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();

		if(id == R.id.btn_positive)
		{
			positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		} else if(id == R.id.btn_negative)
		{
			negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}
}