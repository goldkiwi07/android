package com.gold.kiwi.common.ad;

import android.app.Activity;

import com.gold.kiwi.common.ad.bean.AdCaulyBean;
import com.gold.kiwi.common.ad.bean.AdMANBean;
import com.gold.kiwi.common.ad.listener.AdStateListener;

public abstract class AdManager
{
	protected String TAG = getClass().getSimpleName();
	abstract public void setAdOrder(AdCompany[] companies);

	protected Activity activity;
	protected AdStateListener adStateListener;

	private AdCaulyBean caulyBean;
	private AdMANBean manBean;

	public AdManager(Activity activity)
	{
		this.activity = activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public void setAdStateListener(AdStateListener adStateListener)
	{
		this.adStateListener = adStateListener;
	}


	//AD Bean
	public AdCaulyBean getCaulyBean()
	{
		return caulyBean;
	}

	public void setCaulyBean(AdCaulyBean caulyBean)
	{
		this.caulyBean = caulyBean;
	}

	public AdMANBean getManBean()
	{
		return manBean;
	}

	public void setManBean(AdMANBean manBean)
	{
		this.manBean = manBean;
	}
}
