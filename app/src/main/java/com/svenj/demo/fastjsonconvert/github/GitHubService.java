package com.svenj.demo.fastjsonconvert.github;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("users/{user}/repos")
    Observable<List<Repo>> getRepos(@Path("user") String user, @Query("type") String type);
}
