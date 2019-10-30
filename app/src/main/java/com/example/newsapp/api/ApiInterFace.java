package com.example.newsapp.api;

import com.example.newsapp.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterFace {
    @GET("top-headlines")
    Call<News> getNews(
      @Query("country") String Country,
      @Query("apiKey") String apiKey
    );
}
