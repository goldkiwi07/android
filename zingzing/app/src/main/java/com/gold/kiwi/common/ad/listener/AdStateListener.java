package com.gold.kiwi.common.ad.listener;

public interface AdStateListener
{
	void onSuccessToReceive();
	void onFailedToReceive(int errorCode, String errorMessage);
}