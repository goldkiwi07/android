package com.gold.kiwi.zingzing.retrof;

import com.gold.kiwi.zingzing.bean.HisBean;
import com.gold.kiwi.zingzing.bean.ResultBean;
import com.gold.kiwi.zingzing.bean.RoomBean;
import com.gold.kiwi.zingzing.bean.UserBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
http://goldkiwi07.cafe24.com/getrest/
public interface RESTfulApi {
    @POST("getrest/")
    Call<HisBean> getrest(@Body HisBean post);

    @POST("getrest/")
    Call<HisBean> getrestTest(@Query("time") String time, @Query("key") String key, @Query("msg") String msg, @Query("ispush") String ispush);

    @POST("join/")
    //fcmkey, 닉네임, 비밀번호
    Call<ResultBean> join(@Query("user_name") String user_name, @Query("user_id") String user_id, @Query("user_pw") String user_pw);

    @POST("login/")
    Call<ResultBean> login(@Query("user_name") String user_name, @Query("user_id") String user_id, @Query("user_pw") String user_pw);

    @POST("fcm-update/")
    Call<UserBean> fcmUpdate(@Query("user_name") String user_name, @Query("user_id") String user_id);

    @POST("room-make/")
    Call<ResultBean> roomMake(@Query("room_name") String room_name, @Query("room_pw") String room_pw, @Query("user_name") String user_name);

    @POST("room-search/")
    Call<List<RoomBean>> roomSearch(@Query("is_room") String is_room, @Query("user_name") String user_name, @Query("room_name") String room_name);

    @POST("room-join/")
    Call<ResultBean> roomJoin(@Query("room_seq") int room_seq, @Query("room_pw") String room_pw, @Query("user_name") String user_name);

    //time=111&key=fH5CPsksOfw:APA91bF7sh6Vsa_nibGeave7EM0dzA2kJXaj44mGOCxEne6g8N1cWsYnL9NeABP2uEAe116_B_HWD3cxVmvCzpHVDbptr-po-ud8D8IBcMGRrxjrRYH-YP7dCUirAbmijWOQvUqyNmVq&msg=333&ispush=N

    //회원가입
    //로그인
    //FCM 키 업데이트

    //방만들기 던(제목, 비밀번호, 닉네임)
    //방이름검색 리스트 받(제목, 비밀번호, 닉네임) 던(제목)
    //방접속

    //참여방리스트 던(닉네임) 받(제목, 닉네임)
    //참여방 사용자리스트
    //참여방 사용자 강퇴
    //참여방 제거
    //참여방 히스토리 던(방시퀀스, 닉네임) 받(시간,닉네임,TIME, MSG)
    //나의 히스토리 던(닉네임) 받(시간,닉네임,TIME, MSG)

    //징징던지기,히스토리 넣기
    //푸시던지기,히스토리 넣기

    //== 징징보내는 시간 셋팅
    //== 푸시메세지 표시 잠금 셋팅

//    @POST("/posts/")
//    Call<PostItem> post_posts(@Body PostItem post);
//
//    @PATCH("/posts/{pk}/")
//    Call<PostItem> patch_posts(@Path("pk") int pk, @Body PostItem post);
//
//    @DELETE("/posts/{pk}/")
//    Call<PostItem> delete_posts(@Path("pk") int pk);
//
//    @GET("/posts/")
//    Call<List<PostItem>> get_posts();
//
//    @GET("/posts/{pk}/")
//    Call<PostItem> get_post_pk(@Path("pk") int pk);
}
