package com.gold.kiwi.zingzing.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.zingzing.GPCApp;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.common.ad.AdCloseManager;
import com.gold.kiwi.common.ad.AdCompany;
import com.gold.kiwi.common.ad.bean.AdCaulyBean;
import com.gold.kiwi.common.ad.bean.AdMANBean;
import com.gold.kiwi.common.db.DBAdapter;
import com.gold.kiwi.common.network.NetworkHandler;
import com.gold.kiwi.common.network.NetworkRequest;
import com.gold.kiwi.common.network.NetworkRequestInterface;
import com.gold.kiwi.zingzing.bean.ResultBean;
import com.gold.kiwi.zingzing.bean.UserBean;
import com.gold.kiwi.zingzing.dialog.DialLogin;
import com.gold.kiwi.zingzing.fragment.FragRoom;
import com.gold.kiwi.zingzing.fragment.FragRoomList;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.adfit.ads.ba.BannerAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActMain extends AppCompatActivity implements NetworkRequestInterface, View.OnClickListener
{
	private DataUtil dataUtil;
	private BannerAdView adView;
	protected NetworkRequest request;
	private final String TAG = getClass().getSimpleName();
	private DBAdapter dbAdapter = new DBAdapter(this);
	private GPCApp app;

	private ToastUtil toast;

	private Toolbar toolbar_main;

	private AdCloseManager closeAdManager;

	private ImageView iv_folder_add;

	RetrofitConnection retrofitConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		app = GPCApp.getInstance();
		app.setNotOffScreen(this);

		toast = new ToastUtil(this);
		dataUtil = new DataUtil(this);
		request = new NetworkRequest(this, new NetworkHandler());
		request.setNetworkRequestInterface(this);

		app.setFragManager(getSupportFragmentManager());

		closeAdManager = new AdCloseManager(this);
		closeAdManager.setCaulyBean(new AdCaulyBean("ttt"));
		closeAdManager.setManBean(new AdMANBean("ttt", "ttt", "ttt"));
		closeAdManager.setAdOrder(new AdCompany[]{AdCompany.CAULY, AdCompany.MAN});
		closeAdManager.requestAd();

		// AdFit sdk 초기화 시작
		adView = (BannerAdView) findViewById(R.id.adView);
		// 할당 받은 clientId 설정
		adView.setClientId("ttt");
		// 광고 불러오기
		adView.loadAd();

		toolbar_main = findViewById(R.id.toolbar_main);

		iv_folder_add = toolbar_main.findViewById(R.id.iv_folder_add);

		iv_folder_add.setOnClickListener(this);

		app.setToolbarMain(toolbar_main);
		app.setToolbarSettingVisible(false);

		String user_name = dataUtil.getStringData("set_user_name","null");
		String user_pw = dataUtil.getStringData("set_user_pw","null");

		retrofitConnection = new RetrofitConnection();

		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
					@Override
					public void onComplete(@NonNull Task<InstanceIdResult> task) {
						if (!task.isSuccessful()) {
							LOG.d("getInstanceId failed", task.getException().toString());
							return;
						}

						// Get new Instance ID token
						String token = task.getResult().getToken();
						String oriKey = dataUtil.getStringData("set_fcm_key","null");
						if("null".equals(oriKey)) {
							dataUtil.setStringData("set_fcm_key", token);
						}else{
							if(!token.equals(oriKey)){
								if(!"null".equals(user_name)) {
									//토큰이 초기값과 다름
									//update 처리
									Call<ResultBean> login = retrofitConnection.server.login(user_name,token,user_pw);
									login.enqueue(new Callback<ResultBean>() {
										@Override
										public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
											if (response.isSuccessful()) {
												LOG.d("fcm update", "successful retrofit");
												dataUtil.setStringData("set_fcm_key", token);
												// 성공적으로 서버에서 데이터 불러옴.
											} else {
												dailOpen();
												LOG.d("fcm update", "successful but err");
												// 서버와 연결은 되었으나, 오류 발생
											}
										}

										@Override
										public void onFailure(Call<ResultBean> call, Throwable t) {
											dailOpen();
											LOG.d("getrest", "fail retrofit");
										}
									});
								}
							}
						}
						Log.d("FirebaseInstanceId", token);
						// Log and toast
						//String msg = getString(R.string.msg_token_fmt, token);
						//Log.d(TAG, msg);
						//toast.showToast(msg);
						//Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});

		//처음 진입시 유져 닉네임 없으면 회원가입 창 오픈
		if("null".equals(user_name))
			dailOpen();

		app.fragReplace(new FragRoomList());

	}

	public void dailOpen(){
		DialLogin builder = new DialLogin(this);

		builder.show();
	}


	@Override
	protected void onResume()
	{
		//mRewardedVideoAd.resume(this);
		closeAdManager.startAd();
		adView.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		adView.pause();
		//mRewardedVideoAd.pause(this);
		super.onPause();
	}

	@Override
	public void onResponseSuccess(int statusCode, JSONObject responseData)
	{
		LOG.d(TAG, "onResponseSuccess Status Code : " + statusCode + ", Response Data : " + responseData.toString());

		try
		{
			String requestUrl = responseData.getString("request_url");
			String msg = responseData.optString("msg");

			if(statusCode == NetworkRequest.NETWORK_OK)
			{
				if(responseData.getInt("return_code") == 0)
				{
					JSONArray items = responseData.getJSONArray("items");

					if(requestUrl.equals("insertUserReaderCertInfo"))
					{
					}
				} else
				{
					toast.showToast(msg);
				}
			}

			app.setImmersiveSticky(this);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onResponseError(int statusCode, JSONObject errorMsg)
	{
		LOG.d(TAG, "onResponseError Status Code : " + statusCode + ", Response Data : " + errorMsg.toString());
	}

	@Override
	public void onBackPressed()
	{

		if (app.getFragManager().getBackStackEntryCount() == 0) {
			closeAdManager.showAd();

			return;

		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.iv_folder_add) {
			Fragment fragment = new FragRoom();
			app.fragReplace(fragment);
		}
	}

	@Override
	protected void onDestroy()
	{
		adView.destroy();
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}