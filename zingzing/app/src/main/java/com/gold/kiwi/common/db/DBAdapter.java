package com.gold.kiwi.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBAdapter
{
	private final String TAG = getClass().getSimpleName();
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context context)
	{
		dbOpenHelper = new DBOpenHelper(context);
	}

	public List<Map<String, String>> selectSQL(String sql)
	{
		db = dbOpenHelper.getReadableDatabase();

		List<Map<String, String>> selectList = new ArrayList<Map<String, String>>();
		Cursor cursor = db.rawQuery(sql, null);

		while(cursor.moveToNext())
		{
			Map<String, String> resultMap = new HashMap<String, String>();

			for(int i = 0 ; i < cursor.getColumnCount() ; i++)
			{
				String columnName = cursor.getColumnName(i);
				resultMap.put(columnName, cursor.getString(i));
			}

			selectList.add(resultMap);
		}

		if(db != null)
			db.close();

		if(cursor != null)
			cursor.close();

		return selectList;
	}

	public List selectSQL(Object obj, String sql)
	{
		List resultList = null;

		String mapKey = "";
		String tempKey = "";
		String methodName = "";

		List<Map<String, String>> tempList = selectSQL(sql);

		try
		{
			resultList = new ArrayList();
			Class<?> tempClass = Class.forName(obj.getClass().getName());

			for(Map<String, String> map : tempList)
			{
				Object tempObj = tempClass.newInstance();
				Iterator<String> iterator = map.keySet().iterator();

				while(iterator.hasNext())
				{
					mapKey = iterator.next();
					tempKey = mapKey.toLowerCase();

					int index = -1;

					while((index = tempKey.indexOf("_")) != -1)
					{
						tempKey = tempKey.substring(0, index) + tempKey.substring(index + 1, index + 2).toUpperCase() + tempKey.substring(index + 2, tempKey.length());
					}

					methodName = "set" + tempKey.substring(0, 1).toUpperCase() + tempKey.substring(1);

					Method[] methods = tempObj.getClass().getDeclaredMethods();
					for(int i = 0 ; i <= methods.length - 1 ; i++)
					{
						if(methodName.equals(methods[i].getName()))
						{
							methods[i].invoke(tempObj, map.get(mapKey));
						}
					}
				}

				resultList.add(tempObj);
			}
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}

		finally
		{
			return resultList;
		}
	}

	public int execSQL(String sql)
	{
		int result = 1;

		db = dbOpenHelper.getWritableDatabase();

		try
		{
			db.execSQL(sql);
		}catch (Exception e)
		{
			result = 0;
			e.printStackTrace();
		}

		return result;
	}
}
