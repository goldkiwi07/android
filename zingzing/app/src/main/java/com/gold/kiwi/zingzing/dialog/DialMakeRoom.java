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
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;
import com.google.android.material.tabs.TabLayout;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialMakeRoom extends AlertDialog.Builder implements View.OnClickListener
{
    private Context context;
    private DataUtil dataUtil;
    private AlertDialog dialog;
    private Button btn_save;
    private ClearableEditText edt_room_nm, edt_room_pw;
    private ToastUtil toast;
    private TextView tv_title,tv_msg;
    RetrofitConnection retrofitConnection;

    public DialMakeRoom(Context context)
    {
        super(context);
        this.context = context;
        setCancelable(true);

        retrofitConnection = new RetrofitConnection();

        View view = View.inflate(context, R.layout.dial_room_make, null);

        toast = new ToastUtil(getContext());
        dataUtil = new DataUtil(getContext());

        edt_room_nm = view.findViewById(R.id.edt_room_nm);
        edt_room_pw = view.findViewById(R.id.edt_room_pw);
        tv_title = view.findViewById(R.id.tv_title);
        tv_msg = view.findViewById(R.id.tv_msg);
        btn_save = view.findViewById(R.id.btn_save);

        tv_title.setText(getContext().getString(R.string.title_room_make));

        btn_save.setOnClickListener(this);

        //edt_tag.setText(dataUtil.getStringData("search_first_data", context.getString(R.string.default_first_search_tag)));
        edt_room_nm.setHint(getContext().getString(R.string.hint_room_name));
        edt_room_pw.setHint(getContext().getString(R.string.hint_room_pw));

        setView(view);
    }

    public DialMakeRoom(Context context, boolean cancelable)
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

        if (id == R.id.btn_save) {
            if (tagValidation()) {
                //roomMake(@Query("room_name") String room_name, @Query("room_pw") String room_pw, @Query("user_name") String user_name);
                //방만들기
                Call<ResultBean> roomMake = retrofitConnection.server.roomMake(edt_room_nm.getText().toString(), edt_room_pw.getText().toString(), dataUtil.getStringData("set_user_name"));
                roomMake.enqueue(new Callback<ResultBean>() {
                    @Override
                    public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                        if (response.isSuccessful()) {

                            LOG.d("make room ", "successful retrofit : " + response.body().toString());

                            if (response.body().getResult()) {
                                toast.showToast(getContext().getString(R.string.success));
                                dismiss();
                            } else {
                                if ("DUP".equals(response.body().getMsg())) {
                                    tv_msg.setText("뭘까");
                                } else {
                                    tv_msg.setText("다시 시도 해주세요.");
                                }
                            }
                            /**/
                            // 성공적으로 서버에서 데이터 불러옴.
                        } else {
                            LOG.d("make room", "successful but err : " + response.body());
                            // 서버와 연결은 되었으나, 오류 발생
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultBean> call, Throwable t) {
                        LOG.d("make room fail", "fail retrofit");
                    }
                });
            }
        }
    }

    public boolean tagValidation(){
        boolean rtn = false;
            if(!"".equals(edt_room_nm.getText().toString())){
                if(!"".equals(edt_room_pw.getText().toString())){
                    rtn = true;
                }else{
                    toast.showToast(context.getString(R.string.chk_null));
                }
            }else{
                toast.showToast(context.getString(R.string.chk_null));
            }

        return rtn;
    }
}

