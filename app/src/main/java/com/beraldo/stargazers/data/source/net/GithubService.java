package com.beraldo.stargazers.data.source.net;

import android.support.annotation.NonNull;

import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.GenericError;
import com.beraldo.stargazers.data.source.StarsDataSource;
import com.beraldo.stargazers.util.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class GithubService implements StarsDataSource {
    private static GithubService instance;

    private GithubAPI api;
    private GithubService() {
        api = NetworkModule.getAPI();
    }

    public static GithubService getInstance() {
        if (instance == null) instance = new GithubService();
        return instance;
    }

    @Override
    public void getStars(@NonNull String username, @NonNull String repoName,
                        final @NonNull GetStarsCallback callback) {
        Call<List<Star>> getStarsCall = api.getStars(username, repoName);
        getStarsCall.enqueue(new Callback<List<Star>>() {
            @Override
            public void onResponse(Call<List<Star>> call, Response<List<Star>> response) {
                if(response.isSuccessful()) { // Call was successful
                    callback.onCompleted(new ArrayList<>(response.body()));
                } else { // An error coming from the API
                    callback.onError(ErrorUtils.parseError(NetworkModule.getRetrofit(), response));
                }
            }

            @Override
            public void onFailure(Call<List<Star>> call, Throwable t) {
                // Not an API error, probably some other kind of error
                callback.onError(new GenericError(t));
            }
        });
    }
}
