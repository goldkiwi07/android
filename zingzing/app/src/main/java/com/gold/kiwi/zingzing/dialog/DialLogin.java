package com.gold.kiwi.zingzing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.zingzing.adapter.ClearableEditText;
import com.gold.kiwi.zingzing.bean.ResultBean;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;
import com.google.android.material.tabs.TabLayout;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialLogin extends AlertDialog.Builder implements View.OnClickListener
{
    private Context context;
    private DataUtil dataUtil;
    private AlertDialog dialog;
    private Button btn_save;
    private ClearableEditText edt_nm, edt_pw;
    private ToastUtil toast;
    private TextView tv_title,tv_msg;
    private TabLayout tabLayout;
    private String type;
    RetrofitConnection retrofitConnection;

    public DialLogin(Context context)
    {
        super(context);
        this.context = context;
        setCancelable(false);

        retrofitConnection = new RetrofitConnection();

        View view = View.inflate(context, R.layout.dial_login, null);

        type="s";

        toast = new ToastUtil(getContext());
        dataUtil = new DataUtil(getContext());

        edt_nm = view.findViewById(R.id.edt_nm);
        edt_pw = view.findViewById(R.id.edt_pw);
        tv_title = view.findViewById(R.id.tv_title);
        tv_msg = view.findViewById(R.id.tv_msg);
        btn_save = view.findViewById(R.id.btn_save);

        tv_title.setText(getContext().getString(R.string.title_join));

        btn_save.setOnClickListener(this);

        TabLayout tabLayout = view.findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_1));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_2));

		//tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition() ;
                if (pos == 0) { // 회원가입
                    type="s";
                }else{
                    type="l";
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });

        //edt_tag.setText(dataUtil.getStringData("search_first_data", context.getString(R.string.default_first_search_tag)));
        edt_nm.setHint(getContext().getString(R.string.hint_id));
        edt_pw.setHint(getContext().getString(R.string.hint_pw));

        setView(view);
    }

    public DialLogin(Context context, boolean cancelable)
    {
        this(context);
        setCancelable(cancelable);
    }

    public boolean isShowing()
    {
        if(dialog == null)
            return false;

        else
            return dialog.isShowing();
    }

    public AlertDialog show()
    {
        dialog = create();
        dialog.show();

        return dialog;
    }

    public void dismiss()
    {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if (id == R.id.btn_save)
        {
            if(tagValidation(edt_nm.getText().toString(),true)){
                if(tagValidation(edt_pw.getText().toString(),false)){
                    LOG.d("typetypetype","typetype"+type);
                    if("s".equals(type)){
                        //회원가입
                        Call<ResultBean> join =  retrofitConnection.server.join(edt_nm.getText().toString(),dataUtil.getStringData("set_fcm_key"),edt_pw.getText().toString());
                        join.enqueue(new Callback<ResultBean>() {
                            @Override
                            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                                if (response.isSuccessful()) {

                                    LOG.d("signup","successful retrofit : "+ response.body().toString());

                                    if(response.body().getResult()){
                                        dataUtil.setStringData("set_user_name",edt_nm.getText().toString());
                                        dataUtil.setStringData("set_user_pw",edt_pw.getText().toString());
                                        dismiss();
                                    }else{
                                        if("DUP".equals(response.body().getMsg())){
                                            tv_msg.setText("닉네임 중복");
                                        }else{
                                            tv_msg.setText("다시 시도 해주세요.");
                                        }
                                    }
                                    /**/
                                    // 성공적으로 서버에서 데이터 불러옴.
                                } else {
                                    LOG.d("signup","successful but err : " + response.body());
                                    // 서버와 연결은 되었으나, 오류 발생
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultBean> call, Throwable t) {
                                LOG.d("getrest","fail retrofit");
                            }
                        });
                    }else{
                        //로그인
                        Call<ResultBean> login =  retrofitConnection.server.login(edt_nm.getText().toString(),dataUtil.getStringData("set_fcm_key"),edt_pw.getText().toString());
                        login.enqueue(new Callback<ResultBean>() {
                            @Override
                            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                                if (response.isSuccessful()) {
                                    LOG.d("login","successful retrofit");

                                    if(response.body().getResult()){
                                        dataUtil.setStringData("set_user_name",edt_nm.getText().toString());
                                        dataUtil.setStringData("set_user_pw",edt_pw.getText().toString());
                                        dismiss();
                                    }else{
                                        if("WRONG INFO".equals(response.body().getMsg())){
                                            tv_msg.setText("아이디 또는 패스워드 불일치");
                                        }else{
                                            tv_msg.setText("다시 시도 해주세요.");
                                        }
                                    }

                                    // 성공적으로 서버에서 데이터 불러옴.
                                } else {
                                    LOG.d("login","successful but err");
                                    // 서버와 연결은 되었으나, 오류 발생
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultBean> call, Throwable t) {
                                LOG.d("getrest","fail retrofit");
                            }
                        });
                    }
                }

//                toast.showToast(context.getString(R.string.save_success));
//                dataUtil.setStringData("search_first_data", edt_tag.getText().toString().trim());

            }
        }
    }

    public boolean tagValidation(String tag,Boolean id){
        boolean rtn = false;
        if(id){
            if(!"".equals(tag)){
                try {
                    //아이디는 특문 안됨
                    Pattern pattern = Pattern.compile("[ !@#$%^&*(),.?\":{}|<>]");
                    if(Pattern.matches(pattern.pattern(),tag)){
                        toast.showToast(context.getString(R.string.chk_special));
                        rtn = false;
                    } else {
                        rtn = true;
                    }
                } catch (PatternSyntaxException e) {
                    rtn = false;

                    toast.showToast(context.getString(R.string.chk_err));
                    //System.err.println("An Exception Occured");
                    e.printStackTrace();
                }
            }else{
                toast.showToast(context.getString(R.string.chk_null));
            }
        }else{
            if(!"".equals(tag)){
                try {
                    //비밀번호는 띄어쓰기만 체크
                    Pattern pattern = Pattern.compile(" ");
                    if(Pattern.matches(pattern.pattern(),tag)){
                        toast.showToast(context.getString(R.string.chk_spacing));
                        rtn = false;
                    } else {
                        rtn = true;
                    }
                } catch (PatternSyntaxException e) {
                    rtn = false;

                    toast.showToast(context.getString(R.string.chk_err));
                    //System.err.println("An Exception Occured");
                    e.printStackTrace();
                }
            }else{
                toast.showToast(context.getString(R.string.chk_null));
            }
        }

        return rtn;
    }
}