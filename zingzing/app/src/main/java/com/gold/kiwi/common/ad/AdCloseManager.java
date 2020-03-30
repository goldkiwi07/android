package com.gold.kiwi.common.ad;

import android.app.Activity;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ad.close.AdCaulyClose;
import com.gold.kiwi.common.ad.close.AdCloseInterface;
import com.gold.kiwi.common.ad.close.AdMANClose;
import com.gold.kiwi.common.ad.listener.AdInterface;
import com.gold.kiwi.common.ad.listener.AdRetryListener;

public class AdCloseManager extends AdManager implements AdInterface, AdRetryListener
{
	private AdCloseInterface closeManager;
	private AdCompany[] companies = {AdCompany.CAULY, AdCompany.MAN};
	private int index = 0;

	public AdCloseManager(Activity activity)
	{
		super(activity);
	}

	@Override
	public void setAdOrder(AdCompany[] companies)
	{
		this.companies = companies;
	}

	@Override
	public void requestAd()
	{
		//index = 1;

		recursiveRequestAd(index, false);
	}

	private void recursiveRequestAd(int index, boolean show)
	{
		LOG.d(TAG, "recursiveRequestAd : "+ index +", company : "+ companies[index]);

		if(companies[index] == AdCompany.CAULY && getCaulyBean() != null)
		{
			if(getCaulyBean().getAppCode().isEmpty())
				throw new RuntimeException("Invalid App Code");

			closeManager = new AdCaulyClose(activity);
			closeManager.setAdBean(getCaulyBean());
			closeManager.setAdRetryListener(this);
			closeManager.closeRequest(index);
		}

		else if(companies[index] == AdCompany.MAN && getManBean() != null)
		{
			closeManager = new AdMANClose(activity);
			closeManager.setAdBean(getManBean());
			closeManager.setAdRetryListener(this);
			closeManager.closeRequest(index);
		}

		if(closeManager != null && show)
			closeManager.showAd();
	}

	@Override
	public void failShowAd(int adOrderIndex, String className, String errorCode, String errorMessage)
	{
		closeManager.closeStopAd();
		recursiveRequestAd(adOrderIndex + 1, true);
	}

	@Override
	public void showAd()
	{
		if(closeManager != null)
		{
			closeManager.showAd();
		}

		else
			throw new NullPointerException("AdCloseManager is Null");
	}

	public void startAd()
	{
		if(closeManager != null)
		{
			closeManager.closeStartAd();
		}

		else
			throw new NullPointerException("AdCloseManager is Null");
	}

	public void stopAd()
	{
		if(closeManager != null)
		{
			closeManager.closeStopAd();
		}

		else
			throw new NullPointerException("AdCloseManager is Null");
	}
}
