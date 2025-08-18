package com.tech.sonet.di.module;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Arvind Poonia on 12/12/2018.
 */
public class RequestInterceptor implements Interceptor {

    String credentials;

    public RequestInterceptor() {
        this.credentials = Credentials.basic("admin", "1234");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder().
                header("Authorization", credentials);
        //builder.addHeader("Content-Type", "application/json");
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

}