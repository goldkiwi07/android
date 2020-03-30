package com.gold.kiwi.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.gold.kiwi.common.network.NetworkRequest;
import com.gold.kiwi.common.network.NetworkRequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;

/**
 * Created by KimGunWoo on 2017-02-15.
 */
public class UncaughtExceptionHandlerApp implements Thread.UncaughtExceptionHandler, NetworkRequestInterface
{
    private final String TAG = getClass().getSimpleName();
    private Thread.UncaughtExceptionHandler handler;
    private Context context;
    private NetworkRequest request;

    public UncaughtExceptionHandlerApp(Thread.UncaughtExceptionHandler handler, Context context)
    {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        Log.e(TAG, "UncaughtException 발생!!");
        Log.e(TAG, "FATAL EXCEPTION: "+ thread.getName() +", PID: "+ android.os.Process.myPid());

        //Log.e(TAG, ex.toString());
        //Log.e(TAG, ex.getStackTrace());

        Throwable cause = ex.getCause();

        if(cause != null)
        {
            Log.e(TAG, "Caused by : " + cause.toString());
            Log.e(TAG, Log.getStackTraceString(cause));
        }

        else
        {
            Log.e(TAG, "Caused by : Not Found!");
            Log.e(TAG, Log.getStackTraceString(ex));
        }

        request = new NetworkRequest(context);
        request.setNetworkRequestInterface(this);

        sendExceptionData(thread, ex);

        //app.setCookie("");
        handler.uncaughtException(thread, cause);
    }

    private void sendExceptionData(Thread thread, Throwable ex)
    {
        JSONObject json = new JSONObject();

        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            /*
            json.put("requestURL", "appException");

            json.put("versionCode", String.valueOf(info.versionCode));
            json.put("versionName", info.versionName);
            json.put("pdtNm", Build.MANUFACTURER);
            json.put("mdlNm", Build.MODEL);
            json.put("osVer", Build.VERSION.RELEASE +"("+ Build.VERSION.SDK_INT +")");

            json.put("thdNm", thread.getName());
            json.put("pid", android.os.Process.myPid());
            json.put("errorTime", String.valueOf(new Timestamp(System.currentTimeMillis())));
            */

            json.put("request_url", "appException");

            json.put("version_code", String.valueOf(info.versionCode));
            json.put("version_name", info.versionName);
            json.put("pdt_nm", Build.MANUFACTURER);
            json.put("mdl_nm", Build.MODEL);
            json.put("os_ver", Build.VERSION.RELEASE +"("+ Build.VERSION.SDK_INT +")");

            json.put("thd_nm", thread.getName());
            json.put("pid", android.os.Process.myPid());
            json.put("error_time", String.valueOf(new Timestamp(System.currentTimeMillis())));

            Throwable cause = ex.getCause();

            if(cause != null)
            {
                json.put("cause", cause.toString());
                json.put("errors", Log.getStackTraceString(cause));
            }

            else
            {
                json.put("cause", "Not Found!");
                json.put("errors", Log.getStackTraceString(ex));
            }
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

    private String getStackTrace(Throwable th)
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        Throwable cause = th;
        while(cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        final String stacktraceAsString = result.toString();
        printWriter.close();

        return stacktraceAsString;
    }

    @Override
    public void onResponseSuccess(int statusCode, JSONObject responseData)
    {
        Log.d(TAG, "onResponseSuccess Status Code : "+ statusCode +", Response Data : "+ responseData.toString());
    }

    @Override
    public void onResponseError(int statusCode, JSONObject errorMsg)
    {
        Log.d(TAG, "onResponseError Status Code : "+ statusCode +", Response Data : "+ errorMsg.toString());
    }
}