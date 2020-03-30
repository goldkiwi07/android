package com.gold.kiwi.zingzing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.view.BaseFragment;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.zingzing.adapter.RecyclerRoomAdapter;
import com.gold.kiwi.zingzing.bean.HisBean;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.dialog.DialMakeRoom;
import com.gold.kiwi.zingzing.dialog.DialSearchRoom;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragSearchRoomList extends BaseFragment {
    RetrofitConnection retrofitConnection;
    private RecyclerView rv_room_list;
    private RecyclerRoomAdapter roomAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitConnection = new RetrofitConnection();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.frag_room_search_list, container, false);

        rv_room_list = view.findViewById(R.id.rv_room_list);
        LinearLayout ll_no_data = view.findViewById(R.id.ll_no_data);
        if(FragRoomList.searchRoomList.size() > 0){
            ll_no_data.setVisibility(View.GONE);
        }else{
            ll_no_data.setVisibility(View.VISIBLE);
        }

        roomAdapter = new RecyclerRoomAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv_room_list = view.findViewById(R.id.rv_room_list);
        rv_room_list.setHasFixedSize(true);
        rv_room_list.setLayoutManager(linearLayoutManager);
        rv_room_list.setAdapter(roomAdapter);

        roomAdapter.setRoomList(FragRoomList.searchRoomList);
        roomAdapter.notifyDataSetChanged();

        return view;
    }
}