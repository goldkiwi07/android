package com.gold.kiwi.common.ad.listener;

public interface AdRetryListener
{
	void failShowAd(int adOrderIndex, String className, String errorCode, String errorMessage);
}
