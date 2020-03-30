package com.gold.kiwi.common.ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gold.kiwi.zingzing.R;

public class AdEndingDialog extends AlertDialog.Builder implements View.OnClickListener
{
	private Context context;
	private AlertDialog dialog;

	private LinearLayout ll_title;
	private LinearLayout ll_button;

	private TextView tv_title;
	private FrameLayout fl_ad_container;

	private Button btn_positive;
	private Button btn_negative;

	private DialogInterface.OnClickListener positiveListener;
	private DialogInterface.OnClickListener negativeListener;

	public AdEndingDialog(Context context)
	{
		super(context);
		this.context = context;
		setCancelable(false);

		View view = View.inflate(context, R.layout.ad_ending_dialog, null);

		ll_title = view.findViewById(R.id.ll_title);
		ll_button = view.findViewById(R.id.ll_button);

		tv_title = view.findViewById(R.id.tv_title);
		fl_ad_container = view.findViewById(R.id.fl_ad_container);

		btn_positive = view.findViewById(R.id.btn_positive);
		btn_negative = view.findViewById(R.id.btn_negative);

		btn_positive.setOnClickListener(this);
		btn_negative.setOnClickListener(this);

		setView(view);
	}

	public AdEndingDialog(Context context, boolean cancelable)
	{
		this(context);
		setCancelable(cancelable);
	}

	@Override
	public AdEndingDialog setTitle(int titleId)
	{
		tv_title.setText(titleId);
		ll_title.setVisibility(View.VISIBLE);

		return this;
	}

	@Override
	public AdEndingDialog setTitle(CharSequence title)
	{
		tv_title.setText(title);
		ll_title.setVisibility(View.VISIBLE);

		return this;
	}

	@Override
	public AdEndingDialog setMessage(int messageId)
	{
		return this;
	}

	@Override
	public AdEndingDialog setMessage(CharSequence message)
	{
		return this;
	}

	@Override
	public AdEndingDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_positive.setVisibility(View.VISIBLE);

		btn_positive.setText(text);
		positiveListener = listener;

		return this;
	}

	@Override
	public AdEndingDialog setPositiveButton(int textId, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_positive.setVisibility(View.VISIBLE);

		btn_positive.setText(textId);
		positiveListener = listener;

		return this;
	}

	public void setPositiveButton(DialogInterface.OnClickListener listener)
	{
		this.setPositiveButton(context.getString(R.string.ad_ending_positive), listener);
	}

	@Override
	public AdEndingDialog setNegativeButton(int textId, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_negative.setVisibility(View.VISIBLE);

		btn_negative.setText(textId);
		negativeListener = listener;

		return this;
	}

	@Override
	public AdEndingDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		ll_button.setVisibility(View.VISIBLE);
		btn_negative.setVisibility(View.VISIBLE);

		btn_negative.setText(text);
		negativeListener = listener;

		return this;
	}

	public void setNegativeButton(DialogInterface.OnClickListener listener)
	{
		this.setNegativeButton(context.getString(R.string.ad_ending_negative), listener);
	}

	public void setAdView(View view)
	{
		fl_ad_container.addView(view);
	}

	public boolean isShowing()
	{
		if(dialog == null) return false;

		else return dialog.isShowing();
	}

	public AlertDialog show()
	{
		dialog = create();
		dialog.show();

		return dialog;
	}

	public void dismiss()
	{
		fl_ad_container.removeAllViews();
		dialog.dismiss();
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();

		if(id == R.id.btn_positive)
		{
			positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}

		else if(id == R.id.btn_negative)
		{
			negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}
}