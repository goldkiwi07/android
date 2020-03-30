package com.gold.kiwi.common.ad.close;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ad.AdCloseManager;
import com.gold.kiwi.common.ad.AdEndingDialog;
import com.gold.kiwi.common.ad.bean.AdBean;
import com.gold.kiwi.common.ad.bean.AdMANBean;
import com.gold.kiwi.common.ad.listener.AdRetryListener;
import com.mapps.android.view.EndingAdView;
import com.mz.common.listener.AdListener;

public class AdMANClose extends AdCloseManager implements AdCloseInterface, AdListener
{
	private final String TAG = getClass().getSimpleName();
	private AdMANBean manBean;
	private AdRetryListener adRetryListener;

	private AdEndingDialog endingDialog;
	private EndingAdView endingAdView;
	private int adOrderIndex = 0;

	public AdMANClose(Activity activity)
	{
		super(activity);
	}

	@Override
	public void setAdRetryListener(AdRetryListener listener)
	{
		this.adRetryListener = listener;
	}

	@Override
	public void setAdBean(AdBean bean)
	{
		manBean = (AdMANBean) bean;
	}

	@Override
	public void closeRequest(int index)
	{
		this.adOrderIndex = index;

		endingAdView = new EndingAdView(activity);
		endingAdView.setAdViewCode(manBean.getPublisherCode(), manBean.getMediaCode(), manBean.getSectionCode());
		endingAdView.setBannerSize(320, 480);
		//endingAdView.setBannerSize(640, 960);
		endingAdView.setAdListener(this);
	}

	@Override
	public void showAd()
	{
		endingAdView.startEndingAdView();

		endingDialog = new AdEndingDialog(activity);
		endingDialog.setAdView(endingAdView);
		endingDialog.setPositiveButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				activity.finish();
			}
		});
		endingDialog.setNegativeButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(endingAdView != null)
				{
					endingAdView.onDestroy();
				}

				endingDialog.dismiss();
			}
		});
		endingDialog.show();
	}

	@Override
	public void closeStartAd()
	{
	}

	@Override
	public void closeStopAd()
	{
	}

	@Override
	public void onChargeableBannerType(View view, boolean b)
	{
		LOG.d(TAG, "onChargeableBannerType");
	}

	@Override
	public void onFailedToReceive(View view, int errorCode)
	{
		LOG.d(TAG, "onFailedToReceive : "+ errorCode);

		if(adStateListener != null)
			adStateListener.onFailedToReceive(errorCode, "ErrorCode");
	}

	@Override
	public void onInterClose(View view)
	{
		LOG.d(TAG, "onInterClose");
	}

	@Override
	public void onAdClick(View view)
	{
		LOG.d(TAG, "onAdClick");
	}
}