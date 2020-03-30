package com.gold.kiwi.common.network;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

/**
 * <pre>
 * Network 통신을 위한 핸들러 공통된 처리를 위해 사용
 * </pre>
 *
 */

public class NetworkHandler extends Handler
{
	private final String TAG = getClass().getSimpleName();
	private NetworkRequestInterface requestInterface;

	public void setRequestInterface(NetworkRequestInterface requestInterface)
	{
		this.requestInterface = requestInterface;
	}

	@Override
	public void handleMessage(Message msg)
	{
		int selectCode = msg.arg1;

		if(requestInterface != null)
		{
			if(selectCode == NetworkRequest.CALLBACK_SUCCESS)
				requestInterface.onResponseSuccess(msg.arg2, (JSONObject)msg.obj);

			else if(selectCode == NetworkRequest.CALLBACK_ERROR)
				requestInterface.onResponseError(msg.arg2, (JSONObject)msg.obj);

			else
				throw new NullPointerException("Not Matching Callback Code");
		}

		else
			throw new NullPointerException("NetworkRequestInterface Null Reference");
	}
}