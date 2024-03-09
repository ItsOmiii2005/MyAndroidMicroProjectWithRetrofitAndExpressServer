package com.example.mymicroprojectmad.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mymicroprojectmad.apiservices.AuthApiService;

public class AuthToken {
    private static AuthToken instance;
    private String token;
    private final SharedPreferences sharedPreferences;

    private AuthToken(Context context) {
        // Private constructor to prevent instantiation
        sharedPreferences = context.getSharedPreferences("AuthTokenPrefs", Context.MODE_PRIVATE);
        // Retrieve the token from SharedPreferences on initialization
        token = sharedPreferences.getString("token", null);
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

    public boolean isTokenAvailable() {
        return token != null && !token.isEmpty();
    }

    public boolean isTokenValid() {
        
        return true;
    }

    public void logout() {
        // Clear the token and any other necessary cleanup
        token = null;
        sharedPreferences.edit().remove("token").apply();
    }
}
