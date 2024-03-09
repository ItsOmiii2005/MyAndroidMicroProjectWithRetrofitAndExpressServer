package com.example.mymicroprojectmad.apiservices;

import com.example.mymicroprojectmad.apiservices.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseApiService {

    protected static final String BASE_URL = " https://vertical-pharmacies-wellington-excluded.trycloudflare.com/";
    protected final ApiService apiService;

    public BaseApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
}

