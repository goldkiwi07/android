package com.gold.kiwi.common.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.gold.kiwi.common.LOG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

public class NetworkJsoup
{
	private final String TAG = getClass().getSimpleName();
	public static final int NETWORK_OK = 200;
	public static final int NETWORK_ERROR = 400;
	public static final int CALLBACK_SUCCESS = 1;
	public static final int CALLBACK_ERROR = 2;

	private Context context;
	private NetworkRequestInterface networkRequestInterface;
	private NetworkHandler handler = null;
	private ProgressDialog dialog;

	private String urlAddr;

	public NetworkJsoup(Context context)
	{
		this.context = context;
	}

	public NetworkJsoup(Context context, NetworkHandler handler)
	{
		this(context);

		this.handler = handler;
	}

	public void setNetworkRequestInterface(NetworkRequestInterface networkRequestInterface)
	{
		this.networkRequestInterface = networkRequestInterface;
	}

	public void setGooglePlay()
	{
		urlAddr = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
	}

	public void send()
	{
		if(handler != null) showDialog();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				JSONObject json = new JSONObject();

				try
				{
					JSONArray array = new JSONArray();
					json.put("request_url", "getGooglePlayVersion");
					json.put("msg", "정상처리");
					json.put("return_code", 0);

					Document doc = Jsoup.connect(urlAddr).get();

					Elements version = doc.select(".htlgb");

					/*
					LOG.d(TAG, "Version : "+ version.text());

					for(int i=0 ; i < version.size() ; i++)
					{
						LOG.d(TAG, "Version("+ i +") : "+ version.get(i).text());
					}
					*/

					for(Element element : version)
					{
						String temp = element.text();
						//LOG.d(TAG, "Version Element "+ temp);

						if(Pattern.matches("^[0-9]+.[0-9]+.[0-9]+$", temp))
						{
							JSONObject jsonTemp = new JSONObject();
							jsonTemp.put("store_version", temp);

							array.put(jsonTemp);

							break;
						}
					}

					json.put("items", array);

					sendSuccess(NETWORK_OK, json);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					Log.d(TAG, "IOException");
					Log.e(TAG, e.toString());

					try
					{
						json.put("return_code", -1);
						json.put("msg", "IOException");
					}
					catch(JSONException je)
					{
						je.printStackTrace();
					}

					//networkRequestInterface.onResponseError(NETWORK_ERROR, json);
					sendError(NETWORK_ERROR, json);
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Log.d(TAG, "JSONException");
					Log.e(TAG, e.toString());

					try
					{
						json.put("return_code", -1);
						json.put("msg", "JSONException");
					}
					catch(JSONException je)
					{
						je.printStackTrace();
					}

					//networkRequestInterface.onResponseError(NETWORK_ERROR, json);
					sendError(NETWORK_ERROR, json);
				}
			}
		}).start();
	}

	private void sendSuccess(int statusCode, JSONObject responseData)
	{
		dismissDialog();

		if(handler != null)
		{
			Message msg = new Message();
			msg.arg1 = CALLBACK_SUCCESS;
			msg.arg2 = statusCode;
			msg.obj = responseData;

			handler.setRequestInterface(networkRequestInterface);
			handler.sendMessage(msg);
		}

		else
		{
			networkRequestInterface.onResponseSuccess(statusCode, responseData);
		}
	}

	private void sendError(int statusCode, JSONObject errorMsg)
	{
		dismissDialog();
		try
		{
			errorMsg.put("category", "error");
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(handler != null)
		{
			Message msg = new Message();
			msg.arg1 = CALLBACK_ERROR;
			msg.arg2 = statusCode;
			msg.obj = errorMsg;

			handler.setRequestInterface(networkRequestInterface);
			handler.sendMessage(msg);
		} else
		{
			networkRequestInterface.onResponseError(statusCode, errorMsg);
		}
	}

	private void showDialog()
	{
		dialog = new ProgressDialog(context);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				LOG.d(TAG, "dialog 사용자 취소");

			}
		});
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				if(event.getAction() == KeyEvent.ACTION_DOWN)
				{
					LOG.d(TAG, "DialogInterface setOnKeyListener ACTION_DOWN");
					if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
					{
						LOG.d(TAG, "DialogInterface setOnKeyListener KEYCODE_VOLUME_UP");
						return true;
					}
				}
				return false;
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("처리중..");
		dialog.show();
	}

	private void dismissDialog()
	{
		if(dialog != null) dialog.dismiss();

		dialog = null;
	}
}