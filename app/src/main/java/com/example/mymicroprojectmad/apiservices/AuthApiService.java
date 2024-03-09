package com.example.mymicroprojectmad.apiservices;

import com.example.mymicroprojectmad.auth.AuthToken;
import com.example.mymicroprojectmad.auth.ResponseMessage;
import com.example.mymicroprojectmad.auth.User;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthApiService extends BaseApiService {

    public void registerUser(String username, String password, Callback<ResponseMessage> callback) {
        Call<ResponseMessage> call = apiService.register(new User(username, password));
        call.enqueue(callback);
    }

    public void loginUser(String username, String password, Callback<AuthToken> callback) {
        Call<AuthToken> call = apiService.login(new User(username, password));
        call.enqueue(callback);
    }

    public void validateToken(String token, Callback<ResponseMessage> callback) {
        Call<ResponseMessage> call = apiService.validateToken(token);
        call.enqueue(callback);
    }
}
