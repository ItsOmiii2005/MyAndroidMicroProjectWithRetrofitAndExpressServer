package com.example.mymicroprojectmad.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mymicroprojectmad.apiservices.AuthApiService;

public class AuthToken {
    private static AuthToken instance;
    private String token;
    private String enroll;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // Save the token to SharedPreferences when it's set
        sharedPreferences.edit().putString("name", name).apply();
    }

    private final SharedPreferences sharedPreferences;

    public String getEnroll() {
        return enroll;
    }

    private AuthToken(Context context) {
        // Private constructor to prevent instantiation
        sharedPreferences = context.getSharedPreferences("AuthTokenPrefs", Context.MODE_PRIVATE);
        // Retrieve the token from SharedPreferences on initialization
        token = sharedPreferences.getString("token", null);
        enroll = sharedPreferences.getString("enroll", null);
        name = sharedPreferences.getString("name", null);


    }

    public static AuthToken getInstance(Context context) {
        if (instance == null) {
            instance = new AuthToken(context);
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        // Save the token to SharedPreferences when it's set
        sharedPreferences.edit().putString("token", token).apply();
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
        // Save the token to SharedPreferences when it's set
        sharedPreferences.edit().putString("enroll", enroll).apply();
    }

    public boolean isTokenAvailable() {
        return token != null && !token.isEmpty();
    }


    public void logout() {
        // Clear the token and any other necessary cleanup
        token = null;
        sharedPreferences.edit().remove("token").apply();
    }
}
