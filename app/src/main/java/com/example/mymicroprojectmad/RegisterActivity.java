package com.example.mymicroprojectmad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.apiservices.AuthApiService;
import com.example.mymicroprojectmad.auth.ResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    LoadingDialogUtil    loadingDialogUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        loadingDialogUtil = new LoadingDialogUtil(this);


        EditText editTextUsernameRegister = findViewById(R.id.editTextUsernameRegister);
        EditText editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        Button btnRegister = findViewById(R.id.btnRegister);
        EditText editTextName = findViewById(R.id.editTextName);

        btnRegister.setOnClickListener(v -> {
            // Get username and password from EditText
            String username = editTextUsernameRegister.getText().toString();
            String password = editTextPasswordRegister.getText().toString();
            String name = editTextName.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                // Call the login method
                registerUser(username, password,name);
            } else {
                // Show a toast indicating that username and password are required
                Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void registerUser(String username, String password,String name) {
        // Call the login endpoint using Retrofit
        loadingDialogUtil.showLoadingDialog("Registering new Faculty...");
AuthApiService apiService = new AuthApiService();

        apiService.registerUser(username, password, name, new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMessage> call, @NonNull Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    // Login successful
                    Toast.makeText(RegisterActivity.this, "Registration successful! \nNew Faculty Added", Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed
                    Toast.makeText(RegisterActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                }
                loadingDialogUtil.dismissLoadingDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMessage> call, @NonNull Throwable t) {
                loadingDialogUtil.dismissLoadingDialog();

                // Handle failure
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }

        });
    }

}
