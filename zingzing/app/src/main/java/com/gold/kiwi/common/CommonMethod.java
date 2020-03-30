package com.gold.kiwi.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.gold.kiwi.zingzing.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonMethod
{
	public static Uri getTempImageUri(Context context, Bitmap bitmap)
	{
		File storage = context.getCacheDir();
		String fileName = System.currentTimeMillis() +".jpg";

		File tempFile = new File(storage, fileName);

		try
		{
			tempFile.createNewFile();

			FileOutputStream out = new FileOutputStream(tempFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".fileProvider", tempFile);
	}

	public static int getDrawableResourceId(Context context, String resourceFileName)
	{
		return context.getResources().getIdentifier("@drawable/"+ resourceFileName, "drawable", BuildConfig.APPLICATION_ID);
	}
}
