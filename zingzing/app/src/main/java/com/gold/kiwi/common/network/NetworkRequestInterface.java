package com.gold.kiwi.common.network;

import org.json.JSONObject;

public interface NetworkRequestInterface
{
	void onResponseSuccess(int statusCode, JSONObject responseData);

	void onResponseError(int statusCode, JSONObject errorMsg);
}