package com.gold.kiwi.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class CommonDialog
{
    private AlertDialog.Builder builder;
    private Context context;
    private AlertDialog dialog;

    public CommonDialog(Context context)
    {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
    }

    public CommonDialog(Context context, boolean cancelable)
    {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
    }

    public void setTitle(String title)
    {
        builder.setTitle(title);
    }

    public void setMessage(String message)
    {
        builder.setMessage(message);
    }

    public void setView(int layoutResId)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        builder.setView(v);
    }

    public void setView(View view)
    {
        builder.setView(view);
    }

    public void setPositiveButton(DialogInterface.OnClickListener listener)
    {
        this.setPositiveButton("확인", listener);
    }

    public void setPositiveButton(String value, DialogInterface.OnClickListener listener)
    {
        builder.setPositiveButton(value, listener);
    }

    public void setNegativeButton(DialogInterface.OnClickListener listener)
    {
        this.setNegativeButton("취소", listener);
    }

    public void setNegativeButton(String value, DialogInterface.OnClickListener listener)
    {
        builder.setNegativeButton(value, listener);
    }

    public boolean isShowing()
    {
        if(dialog == null)
            return false;

        else
            return dialog.isShowing();
    }

    public void show()
    {
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss()
    {
        dialog.dismiss();
    }
}