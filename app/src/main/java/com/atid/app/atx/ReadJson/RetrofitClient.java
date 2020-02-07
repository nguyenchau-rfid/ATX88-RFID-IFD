package com.atid.app.atx.ReadJson;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;


import com.atid.app.atx.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit retrofit = null;

    private RetrofitClient() {
        // constructor nay la private vi the ban khong the khoi tao truc tiep
    }
    private static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("localhost", session);
            }
        };
    }

    public static Retrofit getClient(String baseUrl) {
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Temp_ATX";
        File file = new File(sdCard);

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        //create directory if not exist
        //  File folder=new File(Environment.getExternalStorageDirectory().toString()+"/Temp_ATX");

        if (!file.isDirectory()) {
            file.mkdirs();
        }

            Cache cache = new Cache(file, cacheSize);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(2);
        // ghi log body request
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // OkHttp de tao request client-server va add authenticator, interceptors, dispatchers
        OkHttpClient okClient =
                null;
        try {
            okClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                    .sslSocketFactory(new TLSSocketFactory())
                    .hostnameVerifier(getHostnameVerifier())
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .dispatcher(dispatcher)
                    .cache(cache)
                    .addInterceptor(interceptor)
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okClient)

                    .build();

        return retrofit;


    }




}


//    final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        if (retrofit==null) {
//                httpClient.addInterceptor(new Interceptor() {
//@Override
//public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request()
//        .newBuilder()
//        .build();
//
//        return chain.proceed(request);
//        }
//        });
//        retrofit = new Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient.build())
//        .build();
//        }
//        else
//        { retrofit = new Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient.build())
//        .build();}
//
//        return retrofit;
