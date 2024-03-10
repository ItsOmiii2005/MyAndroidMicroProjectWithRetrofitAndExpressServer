package com.example.mymicroprojectmad;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.apiservices.StudentApiService;
import com.example.mymicroprojectmad.student.Student;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRegistrationActivity  extends  AppCompatActivity {
    EditText date;
    LoadingDialogUtil loadingDialogUtil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isImageSelected = false;
    private Dialog loadingDialog;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_activity);

        Button btnNavigate = findViewById(R.id.buttonNavigate);
        date = findViewById(R.id.date);
        ImageButton btnPickImage = findViewById(R.id.btnPickImage);




        btnNavigate.setOnClickListener(view -> {

            // Retrieve text from EditText fields
            EditText et1 = findViewById(R.id.et1);
            EditText et2 = findViewById(R.id.et2);
            EditText et3 = findViewById(R.id.et3);
            EditText et5 = findViewById(R.id.et5);
            EditText et6 = findViewById(R.id.et6);
            ImageView iv = findViewById(R.id.imageView);


            String fullName = et1.getText().toString();
            String className = et2.getText().toString();
            String enrollmentNo = et3.getText().toString();
            String dob = date.getText().toString();
            String address = et5.getText().toString();
            String mobileNo = et6.getText().toString();

            if (enrollmentNo.trim().isEmpty() || !isImageSelected ) {
                Toast.makeText(StudentRegistrationActivity.this, "Please Enter All Details and upload image", Toast.LENGTH_SHORT).show();
                return;  // Stop further processing if image is not selected
            }


            // Retrieve the bitmap from ImageView
            BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
            Bitmap imageBitmap = drawable.getBitmap();

            // Convert the bitmap to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
            byte[] imageByteArray = byteArrayOutputStream.toByteArray();
            try {

                loadingDialogUtil = new LoadingDialogUtil(StudentRegistrationActivity.this);


                insertDataMongo(enrollmentNo, fullName, className, dob, address, mobileNo, imageByteArray);

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        // perform click event on edit text
        date.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(StudentRegistrationActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // Format day and month with leading zeros
                            String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                            String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);

                            // Set the formatted date in the EditText
                            date.setText(formattedDay + "/" + formattedMonth + "/" + year);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });



        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside your button click or any event handler
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(  MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                // Convert the selected image to a circular bitmap
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Resize the image to a reasonable dimension (e.g., 500x500)
                int targetWidth = 500;
                int targetHeight = (int) ((float) targetWidth / originalBitmap.getWidth() * originalBitmap.getHeight());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);

                // Convert the resized bitmap to a circular bitmap
                Bitmap circularBitmap = getRoundedBitmap(resizedBitmap);

                // Display the circular bitmap in your ImageView
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(circularBitmap);
                imageView.setVisibility(View.VISIBLE);
                isImageSelected = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Function to create a circular bitmap from the original bitmap
    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Create a square bitmap with the length equal to the minimum of width and height
        int diameter = Math.min(width, height);
        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);

        // Create a canvas to draw on the square bitmap
        Canvas canvas = new Canvas(output);

        // Create a paint object with anti-aliasing enabled
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Create a rect object representing the bounds of the source bitmap
        Rect rect = new Rect(0, 0, width, height);

        // Create a rectF object representing the bounds of the destination bitmap
        RectF rectF = new RectF(0, 0, diameter, diameter);

        // Draw the source bitmap on the destination bitmap using a circular path
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(diameter / 2f, diameter / 2f, diameter / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return output;
    }
    private void insertDataMongo(String enrollmentNo, String fullName, String className, String dob, String address, String mobileNo, byte[] imageByteArray) {
//
        loadingDialogUtil.showLoadingDialog("Adding Student...");

        Student student = new Student();
        student.setEnrollmentNo(enrollmentNo);
        student.setFullName(fullName);
        student.setClassName(className);

        student.setDob(dob);
        student.setAddress(address);
        student.setMobileNo(mobileNo);

        String base64Image = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
//        System.out.println(base64Image);
        student.setImage(base64Image);

        StudentApiService studentApiService = new StudentApiService();

        studentApiService.addStudent(student, new Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                Log.d("Retrofit", "onResponse executed");
                if (response.isSuccessful()) {
                    // Student added successfully
                    System.out.println(response.body());
                    // Create an Intent to start SecondActivity
                    Intent intent = new Intent(StudentRegistrationActivity.this, idViewActivity.class);

                    // Pass data to SecondActivity using intent extras
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("className", className);
                    intent.putExtra("enrollmentNo", enrollmentNo);
                    intent.putExtra("dob", dob);
                    intent.putExtra("address", address);
                    intent.putExtra("mobileNo", mobileNo);
                    intent.putExtra("image", imageByteArray);  // Pass the image byte array
                    startActivity(intent);
                    Toast.makeText(StudentRegistrationActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();

                } else {
                    // Error adding student
                    int statusCode = response.code();
                    if (statusCode == 400) {
                        // Bad request
                        Toast.makeText(StudentRegistrationActivity.this, "Student already exists", Toast.LENGTH_SHORT).show();
                    } else if (statusCode == 500) {
                        // Internal server error
                        Intent intent = new Intent(StudentRegistrationActivity.this, idViewActivity.class);

                        // Pass data to SecondActivity using intent extras
                        intent.putExtra("fullName", fullName);
                        intent.putExtra("className", className);
                        intent.putExtra("enrollmentNo", enrollmentNo);
                        intent.putExtra("dob", dob);
                        intent.putExtra("address", address);
                        intent.putExtra("mobileNo", mobileNo);
                        intent.putExtra("image", imageByteArray);  // Pass the image byte array
                        startActivity(intent);
                        Toast.makeText(StudentRegistrationActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                    }
                }
                loadingDialogUtil.dismissLoadingDialog();
            }
            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Intent intent = new Intent(StudentRegistrationActivity.this, idViewActivity.class);

                // Pass data to SecondActivity using intent extras
                intent.putExtra("fullName", fullName);
                intent.putExtra("className", className);
                intent.putExtra("enrollmentNo", enrollmentNo);
                intent.putExtra("dob", dob);
                intent.putExtra("address", address);
                intent.putExtra("mobileNo", mobileNo);
                intent.putExtra("image", imageByteArray);  // Pass the image byte array
                startActivity(intent);
                Toast.makeText(StudentRegistrationActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                // Handle failure
                Log.d("Retrofit", "onFailure executed");
                if (t instanceof IOException) {
                    // Network or conversion error
                    Log.e("Network Error", t.toString());
                    Toast.makeText(StudentRegistrationActivity.this, "Network error !\n(Please check internet connection)", Toast.LENGTH_SHORT).show();
                } else {
                    // Unexpected error
                    Log.e("Unexpected Error", t.toString());
                    Toast.makeText(StudentRegistrationActivity.this, "Unexpected error !", Toast.LENGTH_SHORT).show();
                }
                loadingDialogUtil.dismissLoadingDialog();
            }

        });
    }
}
