package com.example.mymicroprojectmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;

import android.app.DatePickerDialog;
import android.provider.MediaStore;


import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymicroprojectmad.apiservices.StudentApiService;
import com.example.mymicroprojectmad.student.Student;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText date;
    LoadingDialogUtil loadingDialogUtil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isImageSelected = false;
    private Dialog loadingDialog;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new StudentApiService();

        // Assuming you have a button with the ID "btnNavigate" in your layout
        Button stdLogin = findViewById(R.id.stdLogin);
        Button stdRegisterBtn = findViewById(R.id.stdRegisterBtn);
        Button btngotoLogin = findViewById(R.id.btngotoLogin);

        stdRegisterBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, StudentRegistrationActivity.class);
            startActivity(intent);
        });

        stdLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });
        btngotoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,FacultyLoginActivity.class);
            startActivity(intent);
        });







    }

}