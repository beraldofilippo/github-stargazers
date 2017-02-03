package com.beraldo.stargazers.data.source.net;

import com.beraldo.stargazers.data.source.model.Star;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubAPI {
    @GET("/repos/{username}/{repo_name}/stargazers")
    Call<List<Star>> getStars(@Path("username") String username, @Path("repo_name") String repoName);
}
