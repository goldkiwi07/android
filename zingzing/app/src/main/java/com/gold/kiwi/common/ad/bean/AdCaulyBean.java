package com.gold.kiwi.common.ad.bean;

public class AdCaulyBean extends AdBean
{
	private String appCode;

	public AdCaulyBean()
	{
	}

	public AdCaulyBean(String appCode)
	{
		this.appCode = appCode;
	}

	public String getAppCode()
	{
		return appCode;
	}

	public void setAppCode(String appCode)
	{
		this.appCode = appCode;
	}
}