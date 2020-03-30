package com.gold.kiwi.zingzing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.kiwi.common.DataUtil;
import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.ToastUtil;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.zingzing.adapter.ClearableEditText;
import com.gold.kiwi.zingzing.adapter.RecyclerRoomAdapter;
import com.gold.kiwi.zingzing.bean.ResultBean;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.fragment.FragRoomList;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialSearchRoom extends AlertDialog.Builder
{
    private Context context;
    private DataUtil dataUtil;
    private AlertDialog dialog;
    private ToastUtil toast;
    RetrofitConnection retrofitConnection;
    private RecyclerView rv_room_list;
    private RecyclerRoomAdapter roomAdapter;

    public DialSearchRoom(Context context)
    {
        super(context);
        this.context = context;
        setCancelable(true);

        retrofitConnection = new RetrofitConnection();

        View view = View.inflate(context, R.layout.dial_room_search, null);

        toast = new ToastUtil(getContext());
        dataUtil = new DataUtil(getContext());

        LinearLayout ll_no_data = view.findViewById(R.id.ll_no_data);
        if(FragRoomList.searchRoomList.size() > 0){
            ll_no_data.setVisibility(View.GONE);
        }else{
            ll_no_data.setVisibility(View.VISIBLE);
        }

        roomAdapter = new RecyclerRoomAdapter();
        roomAdapter.setDialSearchRoom(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv_room_list = view.findViewById(R.id.rv_room_list);
        rv_room_list.setHasFixedSize(true);
        rv_room_list.setLayoutManager(linearLayoutManager);
        rv_room_list.setAdapter(roomAdapter);

        roomAdapter.setRoomList(FragRoomList.searchRoomList);
        roomAdapter.notifyDataSetChanged();

        setView(view);
    }

    public DialSearchRoom(Context context, boolean cancelable)
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
}

