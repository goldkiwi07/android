package com.gold.kiwi.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.SettingValues;

public class DBOpenHelper extends SQLiteOpenHelper
{
    public DBOpenHelper(Context context)
    {
        super(context, DBInternalCopy.DB_NAME, null, SettingValues.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        LOG.d("DBOpenHelper", "onCreate");

        if(!DBInternalCopy.dbCopy)
        {
            String sql = "CREATE TABLE TB_BOOKMARKER (`SEQ` INTEGER, `TITLE` TEXT,`URL` TEXT,`FLAG` TEXT,`LOCK` TEXT,`PARENT` INTEGER,`FAVICON` TEXT, PRIMARY KEY(`SEQ`))";
            db.execSQL(sql);
            String sql2 = "CREATE TABLE TB_SETTING (`SEQ` INTEGER, `PWD` TEXT,`COL_CNT` TEXT, PRIMARY KEY(`SEQ`))";
            db.execSQL(sql2);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        LOG.d("DBOpenHelper", "onUpgrade");
    }
}
