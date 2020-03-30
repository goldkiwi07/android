package com.gold.kiwi.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class DataUtil
{
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final String SP_NAME = "dataUtil";

	public DataUtil(Context context)
	{
		this.context = context;
		sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	public boolean setStringData(String key, String value)
	{
		editor = sp.edit();
		editor.putString(key, value);

		return editor.commit();
	}

	public boolean setIntData(String key, int value)
	{
		editor = sp.edit();
		editor.putInt(key, value);

		return editor.commit();
	}

	public boolean setBooleanData(String key, boolean value)
	{
		editor = sp.edit();
		editor.putBoolean(key, value);

		return editor.commit();
	}

	public String getStringData(String key)
	{
		return getStringData(key, "");
	}

	public String getStringData(String key, String defaultValue)
	{
		return sp.getString(key, defaultValue);
	}

	public int getIntData(String key)
	{
		return getIntData(key, 0);
	}

	public int getIntData(String key, int defaultValue)
	{
		return sp.getInt(key, defaultValue);
	}

	public boolean getBooleanData(String key)
	{
		return getBooleanData(key, false);
	}

	public boolean getBooleanData(String key, boolean defaultValue)
	{
		return sp.getBoolean(key, defaultValue);
	}

	public boolean isData(String key)
	{
		return sp.contains(key);
	}

	public void printAllData()
	{
		Map<String, ?> keys = sp.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet())
			Log.d(TAG, entry.getKey() + " : " + entry.getValue().toString());
	}

	public boolean removeData(String key)
	{
		editor = sp.edit();

		editor.remove(key);
		return editor.commit();
	}

	public boolean removeAll()
	{
		editor = sp.edit();

		editor.clear();
		return editor.commit();
	}
}