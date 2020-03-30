package com.gold.kiwi.common.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gold.kiwi.zingzing.GPCApp;
import com.gold.kiwi.common.CommonAlertDialog;
import com.gold.kiwi.common.CommonDialog;
import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.common.db.DBAdapter;

public class BaseFragment extends Fragment
{
	protected String TAG = getClass().getSimpleName();
	protected String title;
	protected GPCApp app;
	protected DataUtil dataUtil;
	protected ToastUtil toast;

	protected CommonDialog dialog;
	protected CommonAlertDialog alertDialog;
	protected DBAdapter dbAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		app = GPCApp.getInstance();

		dataUtil = new DataUtil(getContext());
		toast = new ToastUtil(getContext());
		dialog = new CommonDialog(getContext());
		alertDialog = new CommonAlertDialog(getContext());
		dbAdapter = new DBAdapter(getContext());

		LOG.d(TAG, "onCreate");
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LOG.d(TAG, "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		//app.setImmersiveSticky(getActivity());
	}
}
