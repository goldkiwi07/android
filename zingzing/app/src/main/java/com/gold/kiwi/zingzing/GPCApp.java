package com.gold.kiwi.zingzing;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.UncaughtExceptionHandlerApp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GPCApp extends Application
{
	private final String TAG = getClass().getSimpleName();
	private static GPCApp app;

	private FragmentManager fragManager;
	private Toolbar toolbar_main;

	@Override
	public void onCreate()
	{
		super.onCreate();
		app = this;

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerApp(Thread.getDefaultUncaughtExceptionHandler(), this));

		setFontFamily("NotoSans-Regular.ttf");
	}

	public static GPCApp getInstance()
	{
		return app;
	}

	//UI / UX
	private void setFontFamily(String name)
	{
		try
		{
			String path = "fonts/" + name;
			final Typeface regular = Typeface.createFromAsset(getAssets(), path);

			final Field StaticField = Typeface.class.getDeclaredField("sSystemFontMap");
			Map<String, Typeface> newMap = new HashMap<String, Typeface>();
			newMap.put("sans-serif", regular);
			newMap.put("serif", regular);
			newMap.put("monospace", regular);
			StaticField.setAccessible(true);
			StaticField.set(null, newMap);

			LOG.d(TAG, "VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
			LOG.e(TAG, e.toString());
			LOG.e(TAG, e.getStackTrace());
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
			LOG.e(TAG, e.toString());
			LOG.e(TAG, e.getStackTrace());
		}
	}

	public void setToolbarSettingVisible(boolean visible)
	{
		ImageView iv_setting = toolbar_main.findViewById(R.id.iv_setting);
		ImageView iv_setting_cancle = toolbar_main.findViewById(R.id.iv_setting_cancle);
		ImageView iv_folder_add = toolbar_main.findViewById(R.id.iv_folder_add);
		ImageView iv_web_add = toolbar_main.findViewById(R.id.iv_web_add);
		ImageView iv_edit = toolbar_main.findViewById(R.id.iv_edit);
		ImageView iv_scissors = toolbar_main.findViewById(R.id.iv_scissors);
		ImageView iv_lock = toolbar_main.findViewById(R.id.iv_lock);
		ImageView iv_add_lock = toolbar_main.findViewById(R.id.iv_add_lock);
		ImageView iv_cut = toolbar_main.findViewById(R.id.iv_cut);
		ImageView iv_open_all = toolbar_main.findViewById(R.id.iv_open_all);
		//수정, 삭제, 잠금, 잘라내기

		//설정 눌렸을때
		if(visible) {
			iv_cut.setVisibility(View.GONE);
			iv_setting.setVisibility(View.GONE);
			iv_folder_add.setVisibility(View.GONE);
			iv_web_add.setVisibility(View.GONE);
			iv_lock.setVisibility(View.GONE);
			iv_add_lock.setVisibility(View.VISIBLE);
			iv_setting_cancle.setVisibility(View.VISIBLE);
			iv_edit.setVisibility(View.VISIBLE);
			iv_scissors.setVisibility(View.VISIBLE);
			iv_open_all.setVisibility(View.VISIBLE);
		}
		//설정 취소할때
		else {
			iv_setting.setVisibility(View.VISIBLE);
			iv_folder_add.setVisibility(View.VISIBLE);
			iv_web_add.setVisibility(View.VISIBLE);
			iv_lock.setVisibility(View.VISIBLE);
			iv_add_lock.setVisibility(View.GONE);
			iv_cut.setVisibility(View.GONE);
			iv_setting_cancle.setVisibility(View.GONE);
			iv_edit.setVisibility(View.GONE);
			iv_scissors.setVisibility(View.GONE);
			iv_open_all.setVisibility(View.GONE);
		}
	}

	public void setToolbarCutVisible(boolean visible)
	{
		ImageView iv_setting = toolbar_main.findViewById(R.id.iv_setting);
		ImageView iv_setting_cancle = toolbar_main.findViewById(R.id.iv_setting_cancle);
		ImageView iv_folder_add = toolbar_main.findViewById(R.id.iv_folder_add);
		ImageView iv_web_add = toolbar_main.findViewById(R.id.iv_web_add);
		ImageView iv_edit = toolbar_main.findViewById(R.id.iv_edit);
		ImageView iv_scissors = toolbar_main.findViewById(R.id.iv_scissors);
		ImageView iv_lock = toolbar_main.findViewById(R.id.iv_lock);
		ImageView iv_add_lock = toolbar_main.findViewById(R.id.iv_add_lock);
		ImageView iv_cut = toolbar_main.findViewById(R.id.iv_cut);
		ImageView iv_open_all = toolbar_main.findViewById(R.id.iv_open_all);

		//수정, 삭제, 잠금, 잘라내기

		//잘라내기 눌렸을때
		if(visible) {
			iv_setting.setVisibility(View.GONE);
			iv_folder_add.setVisibility(View.GONE);
			iv_web_add.setVisibility(View.GONE);
			iv_lock.setVisibility(View.GONE);
			iv_add_lock.setVisibility(View.GONE);
			iv_setting_cancle.setVisibility(View.GONE);
			iv_edit.setVisibility(View.GONE);
			iv_scissors.setVisibility(View.GONE);
			iv_open_all.setVisibility(View.GONE);
			iv_cut.setVisibility(View.VISIBLE);
		}
	}

	public void setIsLock(boolean isLock)
	{
		ImageView iv_lock = toolbar_main.findViewById(R.id.iv_lock);

		if(isLock){
			iv_lock.setImageResource(R.drawable.lock);
		}else{
			iv_lock.setImageResource(R.drawable.unlock);
		}
	}

	public void setToolbarTitle(String title)
	{
		TextView tv_title = toolbar_main.findViewById(R.id.tv_title);

		tv_title.setText(title);
	}

	public String getToolbarTitle()
	{
		TextView tv_title = toolbar_main.findViewById(R.id.tv_title);

		String title = tv_title.getText().toString();

		return title;
	}

	public void setToolbarMain(Toolbar toolbar_main)
	{
		this.toolbar_main = toolbar_main;
	}

	public void fragAddStack(Fragment fragment)
	{
		LOG.d(TAG, "fragAddStack : " + fragment.getClass().getSimpleName() + ", Stack Count : " + fragManager.getBackStackEntryCount());

		FragmentManager.BackStackEntry finalFragment = null;

		if(fragManager.getBackStackEntryCount() > 0)
		{
			finalFragment = fragManager.getBackStackEntryAt(fragManager.getBackStackEntryCount() - 1);
			LOG.d(TAG, "finalFragment : " + finalFragment.getId() + ", Name : " + finalFragment.getName());
		}

		if(fragment != null)
		{
			if(finalFragment == null || !finalFragment.getName().equals(fragment.getClass().getSimpleName()))
				fragManager.beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).replace(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();
		} else throw new NullPointerException();
	}

	public void fragReplace(Fragment fragment)
	{
		LOG.d(TAG, "fragReplace : " + fragment.getClass().getSimpleName() + ", Stack Count : " + fragManager.getBackStackEntryCount());

		if(fragment != null)
		{
			fragManager.beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).commit();
		} else throw new NullPointerException();
	}

	public void fragBackStack()
	{
		int count = fragManager.getBackStackEntryCount();

		if(count > 0) fragManager.popBackStackImmediate();
	}

	public void fragBackHome()
	{
		int count = fragManager.getBackStackEntryCount();

		for(int i = 0 ; i < count ; i++)
			fragBackStack();
	}

	public void setImmersiveSticky(Activity activity)
	{
		if(activity != null)
		{
			View decorView = activity.getWindow().getDecorView();

			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					//| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					//	| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	public void setFullScreen(Activity activity)
	{
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void setNotOffScreen(Activity activity)
	{
		if(BuildConfig.DEBUG)
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void clearActionBar(AppCompatActivity activity)
	{
		ActionBar actionBar = activity.getSupportActionBar();

		if(actionBar != null) actionBar.hide();
	}

	public void clearStatusBar(Activity activity)
	{
		Window window = activity.getWindow();
		/*
		//window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.TRANSPARENT);
		*/

		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(Color.TRANSPARENT);
	}

	public void clearNavigationBar(Activity activity)
	{
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	public void setStatusBar(Activity activity)
	{
		Window window = activity.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(getResources().getColor(R.color.back_black));
	}

	//Data (getter/setter)

	public FragmentManager getFragManager()
	{
		return fragManager;
	}

	public void setFragManager(FragmentManager fragManager)
	{
		this.fragManager = fragManager;
	}


	@Override
	public void onTerminate()
	{
		super.onTerminate();

		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
