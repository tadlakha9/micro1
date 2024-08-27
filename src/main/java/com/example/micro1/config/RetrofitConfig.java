package com.example.micro1.config;

import com.example.micro1.interceptor.TracingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private TracingInterceptor tracingInterceptor;

    @Bean
    public Retrofit retrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(tracingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl("http://localhost:8082")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
}