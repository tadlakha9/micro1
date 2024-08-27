package com.example.micro1.service;

import com.example.micro1.client.Micro2;
import com.example.micro1.client.NewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

@Service
public class YourService {

    private Micro2 micro2;

    @Autowired
    private Retrofit retrofit;

    public NewDto getYourData(String id) throws IOException {
        micro2 = retrofit.create(Micro2.class);
        Call<NewDto> call = micro2.getYourData(id);
        Response<NewDto> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch data");
        }
    }
}