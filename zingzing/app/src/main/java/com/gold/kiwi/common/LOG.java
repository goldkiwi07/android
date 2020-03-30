package com.gold.kiwi.common;

import android.util.Log;

import com.gold.kiwi.zingzing.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LOG
{
	private static final String TAG = "LOG";
	private static SimpleDateFormat sdfAllTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfSysLogTime = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");

	//e, w, i, d, v 1~5
	public static void v(String TAG, String log)
	{
		if(BuildConfig.DEBUG)
		{
			Log.v(TAG, log);

			if(SettingValues.LOG_LEVEL >= 5)
				logPrint("V/", TAG, log);
		}
	}

	public static void d(String TAG, String log)
	{
		if(BuildConfig.DEBUG)
		{
			Log.d(TAG, log);

			if(SettingValues.LOG_LEVEL >= 4)
				logPrint("D/", TAG, log);
		}
	}

	public static void i(String TAG, String log)
	{
		if(BuildConfig.DEBUG)
		{
			Log.i(TAG, log);

			if(SettingValues.LOG_LEVEL >= 3)
				logPrint("I/", TAG, log);
		}
	}

	public static void w(String TAG, String log)
	{
		if(BuildConfig.DEBUG)
		{
			Log.w(TAG, log);

			if(SettingValues.LOG_LEVEL >= 2)
				logPrint("W/", TAG, log);
		}
	}

	public static void e(String TAG, String log)
	{
		if(BuildConfig.DEBUG)
		{
			Log.e(TAG, log);

			if(SettingValues.LOG_LEVEL >= 1)
				logPrint("E/", TAG, log);
		}
	}

	public static void e(String TAG, StackTraceElement[] errors)
	{
		if(BuildConfig.DEBUG)
		{
			for(int i = 0 ; i < errors.length ; i++)
			{
				e(TAG, "\t\t" + errors[i].toString());
			}
		}
	}

	public static void logPrint(String logLevel, String TAG, String log)
	{

	}

	public static void f(String logLevel, String request, String TAG, String log)
	{
		File file;
		FileOutputStream fos = null;
		String inputTemp = logLevel;
		String logTime = sdfAllTime.format(new Date());
		String fileName = logTime.substring(0, 10);

		if(!request.equals(""))
			inputTemp += request;

		inputTemp += "[" + TAG + ":" + logTime + "] " + log + "\n";

		//String path = SeowonApp.getInternalPath();
		String path = "";
		file = new File(path);

		if(!file.exists())
			file.mkdirs();

		try
		{
			file = new File(path + fileName + ".tk");
			fos = new FileOutputStream(file, true);

			fos.write(inputTemp.getBytes());
			//fos.write("\n".getBytes());
		}
		catch(IOException e)
		{
			Log.d("LOG", "파일 input 에러");
		}
		finally
		{
			try
			{
				fos.close();
			}
			catch(IOException e)
			{
				Log.d("LOG", "파일 close 에러");
			}
		}
	}
}
