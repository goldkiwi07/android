package com.gold.kiwi.zingzing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.common.view.BaseFragment;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.zingzing.bean.HisBean;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.dialog.DialMakeRoom;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragRoom extends BaseFragment implements View.OnClickListener{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.frag_room, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
