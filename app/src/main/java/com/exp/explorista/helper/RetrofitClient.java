package com.exp.explorista.helper;

import com.exp.explorista.api.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient

{

    private static final String ROOT_URL = "http://192.168.43.226/explorista_customer/";
    // 3.16.188.152
   
    private static synchronized Retrofit getRetrofitInstance()
    {

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);
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