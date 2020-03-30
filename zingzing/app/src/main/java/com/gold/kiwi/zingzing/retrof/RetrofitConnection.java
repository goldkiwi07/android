package com.gold.kiwi.zingzing.retrof;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {
    final String baseUrl = "http://goldkiwi07.cafe24.com/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RESTfulApi server = retrofit.create(RESTfulApi.class);
}
