package com.example.micro1.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Micro2 {
    @GET("/your-endpoint/{id}")
    Call<NewDto> getYourData(@Path("id") String id);
}


