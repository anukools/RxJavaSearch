package com.examples.anukool.rxSearch;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RequestInterface {

    //https://api.github.com/search/users?q=tom&sort=followers&order=desc
    @GET("/search/users")
    Call<GitJsonResponse> getJSON(@QueryMap Map<String, String> options);
}
