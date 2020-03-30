package com.gold.kiwi.common.ad.close;

import android.app.Activity;
import android.content.DialogInterface;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.fsn.cauly.Logger;
import com.gold.kiwi.zingzing.BuildConfig;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.common.CommonAlertDialog;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ad.AdCloseManager;
import com.gold.kiwi.common.ad.bean.AdBean;
import com.gold.kiwi.common.ad.bean.AdCaulyBean;
import com.gold.kiwi.common.ad.listener.AdRetryListener;

public class AdCaulyClose extends AdCloseManager implements AdCloseInterface, CaulyCloseAdListener
{
	private final String TAG = getClass().getSimpleName();
	private AdRetryListener adRetryListener;
	private AdCaulyBean caulyBean;

	private CommonAlertDialog alertDialog;
	private CaulyAdInfo caulyAdInfo;
	private CaulyCloseAd closeAd;

	private int adOrderIndex = 0;

	public AdCaulyClose(Activity activity)
	{
		super(activity);

		if(BuildConfig.DEBUG)
			Logger.setLogLevel(Logger.LogLevel.Debug);

		else
			Logger.setLogLevel(Logger.LogLevel.Info);
	}

	@Override
	public void setAdRetryListener(AdRetryListener listener)
	{
		this.adRetryListener = listener;
	}

	@Override
	public void setAdBean(AdBean bean)
	{
		caulyBean = (AdCaulyBean) bean;
	}

	@Override
	public void showAd()
	{
		if(closeAd != null && closeAd.isModuleLoaded())
		{
			closeAd.show(activity);
		}

		else
		{
			alertDialog = new CommonAlertDialog(activity);
			alertDialog.setTitle(activity.getString(R.string.ad_ending_title));
			alertDialog.setMessage(activity.getString(R.string.ad_ending_message));
			alertDialog.setPositiveButton(activity.getString(R.string.ad_ending_positive), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface diaLOG, int which)
				{
					activity.finish();
				}
			});
			alertDialog.setNegativeButton(activity.getString(R.string.ad_ending_negative), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
		}
	}

	@Override
	public void closeRequest(int index)
	{
		this.adOrderIndex = index;

		caulyAdInfo = new CaulyAdInfoBuilder(caulyBean.getAppCode())
							.age("all")
							.build();

		closeAd = new CaulyCloseAd();
		closeAd.setAdInfo(caulyAdInfo);
		closeAd.setDescriptionText(activity.getString(R.string.ad_ending_message));
		closeAd.setButtonText(activity.getString(R.string.ad_ending_positive), activity.getString(R.string.ad_ending_negative));
		closeAd.disableBackKey();
		closeAd.setCloseAdListener(this);
	}

	@Override
	public void closeStartAd()
	{
		if(closeAd != null)
			closeAd.resume(activity);
	}

	@Override
	public void closeStopAd()
	{
		if(closeAd != null)
			closeAd.cancel();
	}

	@Override
	public void onFailedToReceiveCloseAd(CaulyCloseAd caulyCloseAd, int errorCode, String errorMessage)
	{
		LOG.d(TAG, "onFailedToReceiveCloseAd");

		if(adStateListener != null)
			adStateListener.onFailedToReceive(errorCode, errorMessage);

		if(adRetryListener != null)
			adRetryListener.failShowAd(adOrderIndex, getClass().getSimpleName(), String.valueOf(errorCode), errorMessage);
	}


	@Override
	public void onReceiveCloseAd(CaulyCloseAd caulyCloseAd, boolean b)
	{
		LOG.d(TAG, "onReceiveCloseAd");
	}

	@Override
	public void onShowedCloseAd(CaulyCloseAd caulyCloseAd, boolean b)
	{
		LOG.d(TAG, "onShowedCloseAd");

	}

	@Override
	public void onLeftClicked(CaulyCloseAd caulyCloseAd)
	{
		LOG.d(TAG, "onLeftClicked");
		activity.finish();
	}

	@Override
	public void onRightClicked(CaulyCloseAd caulyCloseAd)
	{
		LOG.d(TAG, "onRightClicked");

		if(alertDialog != null)
			alertDialog.dismiss();
	}

	@Override
	public void onLeaveCloseAd(CaulyCloseAd caulyCloseAd)
	{
		LOG.d(TAG, "onLeaveCloseAd");
	}
}