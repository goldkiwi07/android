package com.gold.kiwi.common.db;

import android.content.Context;
import android.content.res.AssetManager;

import com.gold.kiwi.zingzing.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBInternalCopy
{
    public static final String DB_NAME = "zingzing.db";
    public static boolean dbCopy = false;

    private Context context;
    private String dbPath;

    public DBInternalCopy(Context context)
    {
        this.context = context;
        dbPath = "/data/data/"+ BuildConfig.APPLICATION_ID +"/databases/";
    }

    public boolean isDBCheck()
    {
        boolean result = false;

        File file = new File(dbPath + DB_NAME);

        if(file.exists())
            result = true;

        return result;
    }

    public boolean dbCopy()
    {
        boolean result = false;
        AssetManager manager = context.getAssets();
        File folder = new File(dbPath);
        File dbFile = new File(dbPath + DB_NAME);

        InputStream is = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try
        {
            is = manager.open("db/"+ DB_NAME);
            bis = new BufferedInputStream(is);

            if(!folder.exists())
                folder.mkdirs();

            if(dbFile.exists())
            {
                dbFile.delete();
                dbFile.createNewFile();
            }

            fos = new FileOutputStream(dbFile);
            bos = new BufferedOutputStream(fos);

            int read = -1;
            byte[] buffer = new byte[1024];

            while((read = bis.read(buffer, 0, 1024)) != -1)
                bos.write(buffer, 0, read);

            bos.flush();
            result = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(bos != null) {try {bos.close();} catch (IOException e) {e.printStackTrace();}}
            if(fos != null) {try {fos.close();} catch (IOException e) {e.printStackTrace();}}
            if(bis != null) {try {bis.close();} catch (IOException e) {e.printStackTrace();}}
            if(is != null) {try {is.close();} catch (IOException e) {e.printStackTrace();}}

            if(result)
                dbCopy = true;

            return result;
        }
    }
}