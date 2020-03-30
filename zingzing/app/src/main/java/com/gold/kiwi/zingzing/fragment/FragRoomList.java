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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.zingzing.R;
import com.gold.kiwi.common.view.BaseFragment;
import com.gold.kiwi.zingzing.bean.HisBean;
import com.gold.kiwi.zingzing.bean.ResultBean;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.dialog.DialLogin;
import com.gold.kiwi.zingzing.dialog.DialMakeRoom;
import com.gold.kiwi.zingzing.dialog.DialSearchRoom;
import com.gold.kiwi.zingzing.retrof.RetrofitConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragRoomList extends BaseFragment implements View.OnClickListener{
    private Button btn_test;
    RetrofitConnection retrofitConnection;
    HisBean tmp = new HisBean();
    private FloatingActionButton fab_room_make;
    Spinner sp_type;
    ImageView iv_search;
    EditText edt_search;
    String is_room;
    public static List<RoomBean> searchRoomList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitConnection = new RetrofitConnection();

        tmp.setTime("50,50,50,150,50,50,50,50,50,200,200,50,50,50,50,50");
        tmp.setKey("fH5CPsksOfw:APA91bF7sh6Vsa_nibGeave7EM0dzA2kJXaj44mGOCxEne6g8N1cWsYnL9NeABP2uEAe116_B_HWD3cxVmvCzpHVDbptr-po-ud8D8IBcMGRrxjrRYH-YP7dCUirAbmijWOQvUqyNmVq");
        tmp.setMsg("TEST");
        tmp.setIspush("N");
        searchRoomList = new ArrayList<>();
        is_room = "Y";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.frag_room_list, container, false);

        edt_search = view.findViewById(R.id.edt_search);
        iv_search = view.findViewById(R.id.iv_search);
        sp_type = view.findViewById(R.id.sp_type);
        fab_room_make = view.findViewById(R.id.fab_room_make);
        fab_room_make.setOnClickListener(this);

        ArrayAdapter searchAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ar_search, android.R.layout.simple_spinner_item);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(searchAdapter);

        btn_test = view.findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LOG.d("spinner",parent.getSelectedItem().toString() + "//" + position);
                if(position == 0)
                    is_room = "Y";
                else
                    is_room ="N";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_test)
        {
            Call<HisBean> getrest =  retrofitConnection.server.getrestTest(tmp.getTime(),tmp.getKey(),tmp.getMsg(),tmp.getIspush());
            getrest.enqueue(new Callback<HisBean>() {
                @Override
                public void onResponse(Call<HisBean> call, Response<HisBean> response) {
                    if (response.isSuccessful()) {
                        LOG.d("getrest","successful retrofit");
                        // 성공적으로 서버에서 데이터 불러옴.
                    } else {
                        LOG.d("getrest","successful but err");
                    // 서버와 연결은 되었으나, 오류 발생
                    }
                }

                @Override
                public void onFailure(Call<HisBean> call, Throwable t) {
                    LOG.d("getrest","fail retrofit");
                }
             });

        }else if(id == R.id.fab_room_make){
            DialMakeRoom builder = new DialMakeRoom(getContext());
            builder.show();
        }else if(id == R.id.iv_search){
            //방 검색, is_room, user_name, room_name
            LOG.d("search",is_room + "//" + edt_search.getText());
            Call<List<RoomBean>> roomSearch = retrofitConnection.server.roomSearch(is_room, edt_search.getText().toString(), edt_search.getText().toString());
            roomSearch.enqueue(new Callback<List<RoomBean>>() {
                @Override
                public void onResponse(Call<List<RoomBean>> call, Response<List<RoomBean>> response) {

                    //LOG.d("cnt" , "test : " + response.body().size() + "/" + response.body().get(0).getRoom_name());
                    if (response.isSuccessful()) {

                        if(response.body().size() > 0 && response.body() != null){
                            searchRoomList.clear();
                            searchRoomList = response.body();
                            LOG.d("size " , " 있다");

                            app.fragAddStack(new FragSearchRoomList());
                        } else {
                            toast.showToast(getString(R.string.is_null));
                            LOG.d("size " , " 없다");
                        }

                        LOG.d("search room ", "successful retrofit : " + response.body().toString());

//                        if (response.body().getResult()) {
//                            toast.showToast(getContext().getString(R.string.success));
//                        } else {
//                            if ("DUP".equals(response.body().getMsg())) {
//                            } else {
//                                toast.showToast("다시 시도 해주세요.");
//                            }
//                        }
                        /**/
                        // 성공적으로 서버에서 데이터 불러옴.
                    } else {
                        LOG.d("search room", "successful but err : " + response.body());
                        // 서버와 연결은 되었으나, 오류 발생
                    }
                }

                @Override
                public void onFailure(Call<List<RoomBean>> call, Throwable t) {
                    LOG.d("make room fail", "fail retrofit");
                }
            });
        }
    }


}