package com.gold.kiwi.zingzing.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.gold.kiwi.zingzing.BuildConfig;
import com.gold.kiwi.zingzing.GPCApp;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.common.CommonDialog;
import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.common.db.DBInternalCopy;
import com.gold.kiwi.common.network.NetworkJsoup;
import com.gold.kiwi.common.network.NetworkRequest;
import com.gold.kiwi.common.network.NetworkRequestInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActIntro extends Activity implements NetworkRequestInterface
{
	private final String TAG = getClass().getSimpleName();
	private GPCApp app;
	private ToastUtil toast;

	private NetworkRequest request;
	private NetworkJsoup jsoup;
	private CommonDialog dialog;
	private DataUtil dataUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		app = GPCApp.getInstance();

		dataUtil = new DataUtil(this);
		toast = new ToastUtil(this);

		request = new NetworkRequest(this);
		jsoup = new NetworkJsoup(this);

		request.setNetworkRequestInterface(this);
		jsoup.setNetworkRequestInterface(this);

		dataUtil.printAllData();

		DBInternalCopy dbCopy = new DBInternalCopy(this);
		dbCopy.dbCopy();

		//appVersion();
		getGooglePlayVersion();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
		{
			app.clearNavigationBar(this);
			app.clearStatusBar(this);
			app.setImmersiveSticky(this);
		}
	}

	private void getGooglePlayVersion()
	{
		jsoup.setGooglePlay();
		jsoup.send();
	}

	private void appVersion()
	{
		JSONObject json = new JSONObject();

		try
		{
			PackageManager packageManager = getPackageManager();
			PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);

			json.put("request_url", "appVersion");
			json.put("version_code", String.valueOf(info.versionCode));
			json.put("version_name", info.versionName);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		catch(PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}

		request.setData(json);
		request.send();
	}

	private void moveMainActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, ActMain.class);

		startActivity(intent);
		finish();
	}

	@Override
	public void onResponseSuccess(int statusCode, JSONObject responseData)
	{
		LOG.d(TAG, "onResponseSuccess Status Code : " + statusCode + ", Response Data : " + responseData.toString());

		try
		{
			String requestUrl = responseData.getString("request_url");
			String msg = responseData.optString("msg");

			if(statusCode == NetworkRequest.NETWORK_OK)
			{
				if(responseData.getInt("return_code") == 0)
				{
					JSONArray items = responseData.getJSONArray("items");

					if(requestUrl.equals("getGooglePlayVersion"))
					{
						JSONObject json = items.getJSONObject(0);
						String storeVersion = json.getString("store_version");

						if(storeVersion == null || storeVersion.equals(""))
							storeVersion = "0.0.0";

						LOG.d(TAG, "GooglePlay Version : "+ storeVersion);

						if(!storeVersion.equals("0.0.0") && !storeVersion.equals(BuildConfig.VERSION_NAME) && !dataUtil.isData("v"+ storeVersion))
						{
							final String storeVersionName = storeVersion;

							runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									CommonDialog dialog = new CommonDialog(ActIntro.this);
									dialog.setTitle(getString(R.string.update));
									dialog.setMessage(getString(R.string.update_exist));
									dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
											Intent intent = new Intent(Intent.ACTION_VIEW, uri);

											startActivity(intent);
											finish();
										}
									});
									dialog.setNegativeButton(getString(R.string.never_see_again), new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											dataUtil.setStringData("v" + storeVersionName, "Y");

											moveMainActivity();
										}
									});
									dialog.show();
								}
							});
						}

						else
						{
							moveMainActivity();
						}
					}
				}
			}
		}

		catch(JSONException e)
		{
			e.printStackTrace();
		}

		/*
		Intent intent = new Intent();
		intent.setClass(this, ActMain.class);

		startActivity(intent);
		finish();
		*/
	}

	@Override
	public void onResponseError(int statusCode, JSONObject errorMsg)
	{
		LOG.d(TAG, "onResponseError Status Code : " + statusCode + ", Response Data : " + errorMsg.toString());

		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				CommonDialog dialog = new CommonDialog(ActIntro.this);
				dialog.setTitle("NETWORK ERROR");
				dialog.setMessage("TRY AGAIN LATER");
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				});
				//dialog.show();
			}
		});

		moveMainActivity();

		/*
		Intent intent = new Intent();
		intent.setClass(this, ActMain.class);

		startActivity(intent);
		finish();
		*/
	}
}