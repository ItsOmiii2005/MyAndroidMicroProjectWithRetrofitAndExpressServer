package com.example.mymicroprojectmad.apiservices;

import com.example.mymicroprojectmad.auth.AuthToken;
import com.example.mymicroprojectmad.auth.RegisterUser;
import com.example.mymicroprojectmad.auth.ResponseMessage;
import com.example.mymicroprojectmad.auth.User;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthApiService extends BaseApiService {

    public void registerUser(String username, String password, String name, Callback<ResponseMessage> callback) {
        Call<ResponseMessage> call = apiService.register(new RegisterUser(username, password,name));
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

    public void loginUserStd(String enroll, String dob, Callback<AuthToken> callback) {
        Call<AuthToken> call = apiService.loginStd(new User(enroll, dob));
        call.enqueue(callback);
    }
}
