package com.example.mymicroprojectmad;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.apiservices.AuthApiService;
import com.example.mymicroprojectmad.auth.AuthToken;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLoginActivity extends AppCompatActivity {
EditText editTextEnroll,editTextDob;
DatePickerDialog datePickerDialog;
LoadingDialogUtil loadingDialogUtil;
String enroll, dob;
    Button btnstdLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login_activity);

        editTextDob = findViewById(R.id.editTextDob);
        editTextEnroll = findViewById(R.id.editTextEnroll);
        btnstdLogin = findViewById(R.id.btnstdLogin);
        loadingDialogUtil = new LoadingDialogUtil(this);


btnstdLogin.setOnClickListener(v->{
    enroll = editTextEnroll.getText().toString();
    dob = editTextDob.getText().toString();
   if (editTextEnroll.getText().toString().isEmpty() || editTextDob.getText().toString().isEmpty()) {
       Toast.makeText(this, "Enrollment Number and Date of Birth are required", Toast.LENGTH_SHORT).show();
       return;
   }

   getLogIn(enroll, dob);
});




        // perform click event on edit text
        editTextDob.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(StudentLoginActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // Format day and month with leading zeros
                            String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                            String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);

                            // Set the formatted date in the EditText
                            editTextDob.setText(formattedDay + "/" + formattedMonth + "/" + year);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
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
        Intent intent = new Intent(StudentLoginActivity.this, FindStudentActivity.class);
        startActivity(intent);
    }
    private void getLogIn(String enroll, String dob) {
        loadingDialogUtil.showLoadingDialog("Logging in...");
        AuthApiService authApiService = new AuthApiService();

        authApiService.loginUserStd(enroll, dob, new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful()) {
                    // Login successful
                    AuthToken authTokenObject = response.body();
                    assert authTokenObject != null;
                    AuthToken.getInstance(StudentLoginActivity.this).setToken(authTokenObject.getToken());
                    AuthToken.getInstance(StudentLoginActivity.this).setEnroll(authTokenObject.getEnroll());
                    AuthToken.getInstance(StudentLoginActivity.this).setName(authTokenObject.getName());

                    System.out.println("Token: " + authTokenObject.getToken());

Intent intent = new Intent(StudentLoginActivity.this, FindStudentActivity.class);
                    startActivity(intent);
                    Toast.makeText(StudentLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed
                    Toast.makeText(StudentLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
                loadingDialogUtil.dismissLoadingDialog();
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                // Handle failure
                loadingDialogUtil.dismissLoadingDialog();

                Toast.makeText(StudentLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
