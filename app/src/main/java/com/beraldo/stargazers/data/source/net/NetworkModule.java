package com.beraldo.stargazers.data.source.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {
    private static String baseUrl = "https://api.github.com/";

    /** For the sake of simplicity I don't use dependency injection, it would add a lot of complexity
     * which is not justified by the small purpose of this app.
     * I remain with something that resembles it, using this module to provide static building of
     * the required components.
     * */

    public static Retrofit getRetrofit() {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return builder.client(client).build();
    }

    public static GithubAPI getAPI() {
        return getRetrofit().create(GithubAPI.class);
    }
}
