package com.example.mymicroprojectmad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.apiservices.AuthApiService;
import com.example.mymicroprojectmad.auth.AuthToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyLoginActivity extends AppCompatActivity {

    LoadingDialogUtil loadingDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_login_layout);




        // Find views
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        loadingDialogUtil = new LoadingDialogUtil(this);


        btnLogin.setOnClickListener(v -> {
            // Get username and password from EditText

            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();


            if (!username.isEmpty() && !password.isEmpty()) {
                // Call the login method
                loginUser(username, password);
            } else {
                // Show a toast indicating that username and password are required
                Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        loadingDialogUtil.showLoadingDialog("Logging in...");
        super.onResume();
        // Check if the token is already available and valid
        if (AuthToken.getInstance(this).isTokenAvailable()) {
            // Token is available and valid, redirect to FindStudentActivity
            redirectToFindStudentActivity();

            return;  // Skip the rest of the onCreate method
        }
        loadingDialogUtil.dismissLoadingDialog();
    }
    private void redirectToFindStudentActivity() {
        Intent intent = new Intent(FacultyLoginActivity.this, FindStudentActivity.class);
        startActivity(intent);
    }

    private void loginUser(String username, String password) {
        loadingDialogUtil.showLoadingDialog("Logging in...");

        // Call the login endpoint using Retrofit
            AuthApiService authApiService = new AuthApiService();

            authApiService.loginUser(username, password, new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful()) {
                    // Login successful
                    AuthToken authTokenObject = response.body();
                    assert authTokenObject != null;
                    AuthToken.getInstance(FacultyLoginActivity.this).setToken(authTokenObject.getToken());

                    System.out.println("Token: " + authTokenObject.getToken());


                    Intent intent = new Intent(FacultyLoginActivity.this, FindStudentActivity.class);
                    startActivity(intent);
                    Toast.makeText(FacultyLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed
                    Toast.makeText(FacultyLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
                loadingDialogUtil.dismissLoadingDialog();
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                // Handle failure
                loadingDialogUtil.dismissLoadingDialog();

                Toast.makeText(FacultyLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
