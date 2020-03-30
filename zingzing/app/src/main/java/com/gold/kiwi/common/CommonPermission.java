package com.gold.kiwi.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gold.kiwi.zingzing.BuildConfig;

import java.util.List;


public class CommonPermission {

    public static final int REQUEST_CODE = 1;

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private List<String> permissionList;

    public CommonPermission(Activity activity){
        this.activity = activity;
    }

    public void setPermissionList(List<String> permissionList)
    {
        this.permissionList = permissionList;
    }

    public boolean checkPermissions()
    {
        boolean resultFlag = true;

        if(permissionList != null && !permissionList.isEmpty())
        {
            for(String permission : permissionList)
            {
                int result = ContextCompat.checkSelfPermission(activity, permission);

                if(result == PackageManager.PERMISSION_GRANTED)
                {
                    LOG.d(TAG, "checkPermissions("+ permission +") - PERMISSION_GRANTED");
                }

                else if(result == PackageManager.PERMISSION_DENIED)
                {
                    LOG.d(TAG, "checkPermissions("+ permission +") - PERMISSION_DENIED");
                    resultFlag = resultFlag && false;
                }

                else
                {
                    LOG.d(TAG, "checkPermissions("+ permission +") - "+ result);
                    resultFlag = resultFlag && false;
                }
            }

            return resultFlag;
        }

        else
            throw new NullPointerException("Check permission list");
    }

    public void requestPermissions()
    {
        if(permissionList != null && !permissionList.isEmpty())
        {
            ActivityCompat.requestPermissions(activity, (String[])permissionList.toArray(), REQUEST_CODE);
        }

        else
            throw new NullPointerException("Check permission list");
    }

    public void requestPopupPermission()
    {
        //최상단 팝업 띄우는 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(activity)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                activity.startActivityForResult(intent, 1);
            }
        }
    }
}
