package com.gold.kiwi.common.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.common.SettingValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;

public class NetworkRequest
{
	private final String TAG = getClass().getSimpleName();
	private static final int CONNECTION_TIMEOUT = 3000;
	public static final int NETWORK_OK = 200;
	public static final int NETWORK_ERROR = 400;
	public static final int CALLBACK_SUCCESS = 1;
	public static final int CALLBACK_ERROR = 2;

	private Context context;
	private DataUtil dataUtil;
	private NetworkRequestInterface networkRequestInterface;
	private NetworkHandler handler = null;
	private ProgressDialog dialog;

	private HttpURLConnection conn;
	private OutputStream os;
	private StringBuffer sBuffer;
	private JSONObject jsonData;
	private String urlAddr;

	public NetworkRequest(Context context)
	{
		this.context = context;
		dataUtil = new DataUtil(context);
	}

	public NetworkRequest(Context context, NetworkHandler handler)
	{
		this(context);

		this.handler = handler;
	}

	public void setNetworkRequestInterface(NetworkRequestInterface networkRequestInterface)
	{
		this.networkRequestInterface = networkRequestInterface;
	}

	public void setData(JSONObject data)
	{
		if(data != null)
		{
			try
			{
				this.jsonData = data;

				this.jsonData.put("category", "request");
				this.jsonData.put("package_name", context.getPackageName());
				this.jsonData.put("device_id", dataUtil.getStringData("device_id", "no_device_id"));
				this.jsonData.put("user_code", dataUtil.getStringData("user_code", "no_user_code"));

				String url = jsonData.optString("request_url");

				if(url != null && !url.equals(""))
					urlAddr = SettingValues.URL + url;
			}
			catch(JSONException e)
			{
				e.printStackTrace();
				Log.d(TAG, "setData JSON Exception");
				Log.e(TAG, e.toString());
				//Log.e(TAG, e.getStackTrace());
			}
		}

		else throw new NullPointerException("SendData is Null!!");
	}

	public void send()
	{
		if(handler != null)
			showDialog();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				JSONObject json = new JSONObject();

				try
				{
					jsonData.put("ip", getLocalIpAddress());
					json.put("request_url", jsonData.getString("request_url"));

					URL url = new URL(urlAddr);

					if(urlAddr.toLowerCase().contains("http://"))
						conn = (HttpURLConnection)url.openConnection();

					else if(urlAddr.toLowerCase().contains("https://"))
						conn = (HttpsURLConnection)url.openConnection();

					int statusCode = -1;

					if(conn != null)
					{
						conn.setDoOutput(true);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
						conn.setRequestProperty("Content-Length", "length");
						conn.setConnectTimeout(CONNECTION_TIMEOUT);
						//conn.setRequestProperty("cookie", app.getCookie());

						Log.d(TAG, "urlAddr : " + urlAddr);
						//Log.d(TAG, "Wifi 통신세기(요청) : " + app.getRssi() +", "+ app.getWifiValue());
						Log.d(TAG, "요청 : " + jsonData.toString());

						os = conn.getOutputStream();
						//os.write(("appData=" + jsonData.toString()).getBytes());
						os.write((jsonData.toString()).getBytes());
						os.flush();

						if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK)
						{
							Log.d(TAG, "HTTP_Connection_OK!!");
							InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
							BufferedReader br = new BufferedReader(isr);
							sBuffer = new StringBuffer();
							String line;

							while((line = br.readLine()) != null)
							{
								sBuffer.append(line);
								Log.v(TAG, "readLine : " + line);
							}

							//Log.d(TAG, "Wifi 통신세기(응답) : " + app.getRssi() +", "+ app.getWifiValue());
							Log.d(TAG, "응답 : " + sBuffer.toString());

							statusCode = NETWORK_OK;

							br.close();
							conn.disconnect();
						}

						else
						{
							statusCode = NETWORK_ERROR;
							Log.d(TAG, "Connection Error : " + conn.getResponseCode() + ", " + conn.getResponseMessage());
						}
					}

					//Log.d(TAG, "sBuffer : "+ sBuffer.toString());

					if(sBuffer == null || sBuffer.toString().trim().equals("") || sBuffer.toString().equals("null"))
					{
						json.put("res_code", conn.getResponseCode());
						json.put("res_msg", conn.getResponseMessage());
						json.put("return_code", -1);
						json.put("msg", "데이터 리딩 오류발생!");

						//networkRequestInterface.onResponseError(statusCode, json);
						sendError(statusCode, json);
					}

					else
					{
						//networkRequestInterface.onResponseSuccess(statusCode, new JSONObject(sBuffer.toString()));

						//Thread.sleep(3000);

						sendSuccess(statusCode, new JSONObject(sBuffer.toString()));
					}
				}
				catch(SocketTimeoutException se)
				{
					Log.e(context.getClass().getSimpleName(), "SocketTimeout 에러 발생, 처리시간 지연 오류 발생");
					Log.e(TAG, se.toString());
					//Log.e(TAG, se.getStackTrace());
					se.printStackTrace();

					try
					{
						json.put("return_code", -1);
						json.put("msg", "SocketTimeoutException 네트워크 오류발생!");
					}
					catch(JSONException e)
					{
						e.printStackTrace();
					}

					//networkRequestInterface.onResponseError(NETWORK_ERROR, json);
					sendError(NETWORK_ERROR, json);
				}
				catch(UnknownHostException e)
				{
					e.printStackTrace();
					Log.d(TAG, "UnknownHostException");
					Log.e(TAG, e.toString());

					try
					{
						json.put("return_code", -1);
						json.put("msg", "UnknownHostException");
					}
					catch(JSONException je)
					{
						je.printStackTrace();
					}

					//networkRequestInterface.onResponseError(NETWORK_ERROR, json);
					sendError(NETWORK_ERROR, json);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.e(context.getClass().getSimpleName(), "SocketTimeout 에러 발생, 처리시간 지연 오류 발생");
					Log.e(TAG, e.toString());
					//Log.e(TAG, e.getStackTrace());

					try
					{
						json.put("return_code", -1);
						json.put("msg", "알수 없는 네트워크 오류발생!");
					}
					catch(JSONException je)
					{
						je.printStackTrace();
					}

					//networkRequestInterface.onResponseError(NETWORK_ERROR, json);
					sendError(NETWORK_ERROR, json);
				}
				finally
				{
					sBuffer = null;
					try
					{
						if(os != null) os.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					if(conn != null) conn.disconnect();
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
		}

		else
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
				Log.d(TAG, "dialog 사용자 취소");

				if(conn != null)
				{
					conn.disconnect();
					Log.d(TAG, "dialog disconnect()");
				}
			}
		});
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				if(event.getAction() == KeyEvent.ACTION_DOWN)
				{
					Log.d(TAG, "DialogInterface setOnKeyListener ACTION_DOWN");
					if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
					{
						Log.d(TAG, "DialogInterface setOnKeyListener KEYCODE_VOLUME_UP");
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

	public String getLocalIpAddress()
	{
		try
		{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces() ; en.hasMoreElements() ; )
			{
				NetworkInterface intf = en.nextElement();
				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses() ; enumIpAddr.hasMoreElements() ; )
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch(SocketException ex)
		{
			ex.printStackTrace();
		}

		return "0.0.0.0";
	}
}