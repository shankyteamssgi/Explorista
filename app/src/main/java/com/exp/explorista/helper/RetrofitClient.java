package com.exp.explorista.helper;

import com.exp.explorista.api.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{

    private static final String ROOT_URL = "http://3.16.188.152/explorista_customer/";

    private static Retrofit getRetrofitInstance()
    {


        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(100, TimeUnit.SECONDS);
        client.readTimeout(100, TimeUnit.SECONDS);
        client.writeTimeout(100, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static ApiService getApiService()
    {
        return getRetrofitInstance().create(ApiService.class);
    }

}