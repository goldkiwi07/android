package com.gold.kiwi.common;

import android.Manifest;

public class SettingValues
{
	//로그레벨
	public static final int LOG_LEVEL = 5;		//ASSERT : 0, ERROR : 1, WARN : 2, INFO : 3, DEBUG : 4, VERBOSE : 5

	//DB
	public static final int DB_VERSION = 1;

	//서버
	public static final String URL = "http://192.168.0.11:8888/app/";

	//광고
	public static final String AD_MAIN_BOTTOM_BANNER = "";

	//권한
	public static final String[] PERMISSIONS = {
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE
	};

	//팝업 비율
	public static final double POP_SIZE = 0.8;

}
