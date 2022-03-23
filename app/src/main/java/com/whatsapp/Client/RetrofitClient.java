package com.whatsapp.Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whatsapp.API.BilicraAPI;
import com.whatsapp.BeforeLoginActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static RetrofitClient instance = null;
    private BilicraAPI myApi;


    private RetrofitClient(){
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-type","application/json")
                                .addHeader("Abp.TenantId","1")
                                .addHeader("Authorization"," Bearer "+ BeforeLoginActivity.accessToken)
                                .build();

                        Response response = chain.proceed(request);
                        if(response.code() == 500){
                            //Server Unavailable
                            return response;
                        }
                        return response;
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BilicraAPI.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJava2 kullanacağımızı Retrofit'e bildirmemiz gerekiyor.
                .addConverterFactory(GsonConverterFactory.create(gson)) // gelen gson'unu Model içerisindeki SerializedName'e göre alır.
                .client(okHttpClient)
                .build();
        myApi = retrofit.create(BilicraAPI.class);

    }

    public static synchronized RetrofitClient getInstance(){
        if (instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }

    public BilicraAPI getMyApi(){return myApi;}


}
