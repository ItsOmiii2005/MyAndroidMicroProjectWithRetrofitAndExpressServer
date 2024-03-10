package com.example.mymicroprojectmad;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.apiservices.StudentApiService;
import com.example.mymicroprojectmad.auth.AuthToken;
import com.example.mymicroprojectmad.student.Student;
import com.example.mymicroprojectmad.student.StudentResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayStudentActivity extends AppCompatActivity{

    ImageView imageViewimg;
    ScrollView scrollView;
    LoadingDialogUtil    loadingDialogUtil;
    String base64Image;
    byte[] imageByteArray1;
    TextView
            textViewFullName,
            textViewClassName,
            textViewDob,
            textViewAddress,
            textViewMobileNo,
            textViewEnrollmentNo;

    DatePickerDialog datePickerDialog;

    EditText editTextEnrollmentNo;
    ImageButton updateImg;
    ImageView deleteBtn, updateBtn,imageView10;
    byte[] imageByteArray;
    EditText etFullName, etClassName, etDob, etAddress, etMobileNo, etEnrollmentNo;
    Button saveBtn, cancelBtn,gotoidview;
    String messageText;
    Intent intent;
    StudentResponse student;

    StudentApiService studentApiService = new StudentApiService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_student_layout);


        gotoidview = findViewById(R.id.gotoidview);
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewClassName = findViewById(R.id.textViewClassName);
        textViewDob = findViewById(R.id.textViewDob);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewMobileNo = findViewById(R.id.textViewMobileNo);
        textViewEnrollmentNo = findViewById(R.id.textViewEnrollmentNo);
        imageViewimg = findViewById(R.id.imageView);
        scrollView = findViewById(R.id.scrollView);
        deleteBtn = findViewById(R.id.deleteBtn);

        updateBtn = findViewById(R.id.updateBtn);

        etFullName = findViewById(R.id.etFullName);
        etClassName = findViewById(R.id.etClassName);
        etDob = findViewById(R.id.etDob);
        etAddress = findViewById(R.id.etAddress);
        etMobileNo = findViewById(R.id.etMobileNo);
        etEnrollmentNo = findViewById(R.id.etEnrollmentNo);

        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);


        imageView10 = findViewById(R.id.imageView10);


        loadingDialogUtil = new LoadingDialogUtil(DisplayStudentActivity.this);
        updateImg = findViewById(R.id.updateImg);

        intent = getIntent();
        messageText = intent.getStringExtra("enrollmentNo");
        assert messageText != null;
        if (messageText.equals( AuthToken.getInstance(this).getEnroll()) || AuthToken.getInstance(this).getToken().startsWith("fac") ){
            updateBtn.setOnClickListener(View -> {
                if (messageText.equals("")) {
                    Toast.makeText(DisplayStudentActivity.this, "Fetch data first", Toast.LENGTH_SHORT).show();
                    return;
                }
                showUpdateLayout();

            });
            deleteBtn.setOnClickListener(View -> {
                if (messageText.equals("")) {
                    Toast.makeText(DisplayStudentActivity.this, "Fetch data first", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteStudent(messageText);
            });

            updateImg.setOnClickListener(View -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(  MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, 1);
            });

        }else {
            updateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            updateImg.setVisibility(View.GONE);

        }

        getStudent(messageText);





        gotoidview.setOnClickListener(View -> {
            Intent intent = new Intent(DisplayStudentActivity.this, idViewActivity.class);

            // Pass data to SecondActivity using intent extras
            intent.putExtra("fullName", student.getFullName());
            intent.putExtra("className", student.getClassName());
            intent.putExtra("enrollmentNo", messageText);
            intent.putExtra("dob", student.getDob());
            intent.putExtra("address", student.getAddress());
            intent.putExtra("mobileNo", student.getMobileNo());
            intent.putExtra("image", student.getImage());  // Pass the image byte array
            startActivity(intent);
        });
        cancelBtn.setOnClickListener(View -> {
            showDetailsLayout();
        });




        // perform click event on edit text
        etDob.setOnClickListener(View -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // Format day and month with leading zeros
                            String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                            String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);

                            // Set the formatted date in the EditText
                            etDob.setText(formattedDay + "/" + formattedMonth + "/" + year);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        saveBtn.setOnClickListener(View -> {
            saveStudent();
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                ImageView imageView10 = findViewById(R.id.imageView10);
                imageView10.setImageBitmap(circularBitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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

    private void showUpdateLayout() {
        // Hide the views displaying details
        scrollView.setVisibility(View.GONE);
        // Show the update layout with EditText fields
        findViewById(R.id.updateLayout).setVisibility(View.VISIBLE);

        etFullName.setText(textViewFullName.getText().toString().replace("Full Name: ", ""));
        etClassName.setText(textViewClassName.getText().toString().replace("Class Name: ", ""));
        etDob.setText(textViewDob.getText().toString().replace("Date of Birth: ", ""));
        etAddress.setText(textViewAddress.getText().toString().replace("Address: ", ""));
        etMobileNo.setText(textViewMobileNo.getText().toString().replace("Mobile No: ", ""));
        etEnrollmentNo.setText(textViewEnrollmentNo.getText().toString().replace("Enrollment No: ", ""));
        imageView10.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));
    }
    private void showDetailsLayout() {
        // Show the views displaying details
        scrollView.setVisibility(View.VISIBLE);
        // Hide the update layout with EditText fields
        findViewById(R.id.updateLayout).setVisibility(View.GONE);
    }



    public void getStudent(String enrollmentNo) {
        loadingDialogUtil.showLoadingDialog("Fetching Student Data...");

        String token = AuthToken.getInstance(this).getToken();
        System.out.println("Token "+token);

        studentApiService.getStudent(enrollmentNo,token ,new Callback<StudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<StudentResponse> call, @NonNull Response<StudentResponse> response) {
                if (response.isSuccessful()) {
                    gotoidview.setVisibility(View.VISIBLE);
                     student = response.body();
                    if (student != null) {


                        Toast.makeText(DisplayStudentActivity.this, "Fetched Successfully", Toast.LENGTH_SHORT).show();
                        textViewFullName.setText("Full Name: " + student.getFullName());
                        textViewClassName.setText("Class Name: " + student.getClassName());
                        textViewDob.setText("Date of Birth: " + student.getDob());
                        textViewAddress.setText("Address: " + student.getAddress());
                        textViewMobileNo.setText("Mobile No: " + student.getMobileNo());
                        textViewEnrollmentNo.setText("Enrollment No: " + enrollmentNo);

                        base64Image = student.getImage();
                        imageByteArray = Base64.decode(base64Image, Base64.DEFAULT);

                        imageViewimg.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));

                        scrollView.setVisibility(View.VISIBLE);

                    }
                    else {
                        Toast.makeText(DisplayStudentActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }else{
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(DisplayStudentActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (statusCode == 401) {
                        Toast.makeText(DisplayStudentActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (statusCode == 403) {
                        Toast.makeText(DisplayStudentActivity.this, "Forbidden : Invalid Token", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(DisplayStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                loadingDialogUtil.dismissLoadingDialog();

            }

            @Override
            public void onFailure(@NonNull Call<StudentResponse> call, @NonNull Throwable t) {
                Log.e("Retrofit", "onFailure executed");
                loadingDialogUtil.dismissLoadingDialog();

                Toast.makeText(DisplayStudentActivity.this, "Student not found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteStudent(String enrollmentNo) {
        // Show an alert and ask for confirmation
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Enqueue the DELETE request
                        studentApiService.deleteStudent(enrollmentNo,new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {
                                        // Deletion successful
                                        AuthToken.getInstance(DisplayStudentActivity.this).logout();
                                        finish();
                                        Intent intent = new Intent(DisplayStudentActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(DisplayStudentActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                                       finish();
                                    }
                                } else {
                                    // Handle deletion error
                                    Toast.makeText(DisplayStudentActivity.this, "Failed to delete student", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Handle network error or other failures
                                Toast.makeText(DisplayStudentActivity.this, "Failed to make a delete request", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void saveStudent() {
        loadingDialogUtil.showLoadingDialog("Updating Student...");

        // Convert the selected image to a byte array
        BitmapDrawable drawable = (BitmapDrawable) imageView10.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        byte[] newImageByteArray = byteArrayOutputStream.toByteArray();

        // Convert the new image byte array to a Base64-encoded string
        String newBase64Image = Base64.encodeToString(newImageByteArray, Base64.DEFAULT);

        // Get the values from the EditText fields
        String fullName = etFullName.getText().toString();
        String className = etClassName.getText().toString();
        String dob = etDob.getText().toString();
        String address = etAddress.getText().toString();
        String mobileNo = etMobileNo.getText().toString();
        String enrollmentNo = etEnrollmentNo.getText().toString();

        // Check if any changes were made
        if (!fullName.equals(textViewFullName.getText().toString().replace("Full Name: ", ""))
                || !className.equals(textViewClassName.getText().toString().replace("Class Name: ", ""))
                || !dob.equals(textViewDob.getText().toString().replace("Date of Birth: ", ""))
                || !address.equals(textViewAddress.getText().toString().replace("Address: ", ""))
                || !mobileNo.equals(textViewMobileNo.getText().toString().replace("Mobile No: ", ""))
                || !newBase64Image.equals(base64Image)
        || !enrollmentNo.equals(textViewEnrollmentNo.getText().toString().replace("Enrollment No: ", ""))) {

            // Changes were made, proceed with the update

            // Create a new Student object with only the changed fields
            Student studentToUpdate = new Student();
//            studentToUpdate.setEnrollmentNo(enrollmentNo);

            if (!fullName.equals(textViewFullName.getText().toString().replace("Full Name: ", ""))) {
                studentToUpdate.setFullName(fullName);
            }

            if (!className.equals(textViewClassName.getText().toString().replace("Class Name: ", ""))) {
                studentToUpdate.setClassName(className);
            }

            if (!dob.equals(textViewDob.getText().toString().replace("Date of Birth: ", ""))) {
                studentToUpdate.setDob(dob);
            }

            if (!address.equals(textViewAddress.getText().toString().replace("Address: ", ""))) {
                studentToUpdate.setAddress(address);
            }

            if (!mobileNo.equals(textViewMobileNo.getText().toString().replace("Mobile No: ", ""))) {
                studentToUpdate.setMobileNo(mobileNo);
            }

            if (!newBase64Image.equals(base64Image)) {
                studentToUpdate.setImage(newBase64Image);
            }
            if (!enrollmentNo.equals(textViewEnrollmentNo.getText().toString().replace("Enrollment No: ", ""))) {
                studentToUpdate.setEnrollmentNo(enrollmentNo);
            }

            // Perform the update operation, e.g., through Retrofit
            studentApiService.updateStudent(messageText.toString(), studentToUpdate, new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DisplayStudentActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();

                        // Update the TextViews with the new values if the fields have changed
                        if (studentToUpdate.getFullName() != null) {
                            textViewFullName.setText("Full Name: " + studentToUpdate.getFullName());
                            student.setFullName(studentToUpdate.getFullName());
                        }

                        if (studentToUpdate.getClassName() != null) {
                            textViewClassName.setText("Class Name: " + studentToUpdate.getClassName());
                            student.setClassName(studentToUpdate.getClassName());
                        }

                        if (studentToUpdate.getDob() != null) {
                            textViewDob.setText("Date of Birth: " + studentToUpdate.getDob());
                            student.setDob(studentToUpdate.getDob());
                        }

                        if (studentToUpdate.getAddress() != null) {
                            textViewAddress.setText("Address: " + studentToUpdate.getAddress());
                            student.setAddress(studentToUpdate.getAddress());
                        }

                        if (studentToUpdate.getMobileNo() != null) {
                            textViewMobileNo.setText("Mobile No: " + studentToUpdate.getMobileNo());
                            student.setMobileNo(studentToUpdate.getMobileNo());
                        }

                        if (studentToUpdate.getImage() != null) {
                            // Update the image only if it has changed

                            student.setImage(studentToUpdate.getImage());
                            imageViewimg.setImageBitmap(BitmapFactory.decodeByteArray(newImageByteArray, 0, newImageByteArray.length));
                        }
                        if (studentToUpdate.getEnrollmentNo() != null) {
                            textViewEnrollmentNo.setText("Enrollment No: " + studentToUpdate.getEnrollmentNo());
                            messageText =studentToUpdate.getEnrollmentNo();
                        }

                        // Switch back to the details layout
                        showDetailsLayout();
                    } else {
                        showDetailsLayout();
                        Toast.makeText(DisplayStudentActivity.this, "Failed to update student", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialogUtil.dismissLoadingDialog();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    // Handle failure
                    Toast.makeText(DisplayStudentActivity.this, "Failed to make an update request", Toast.LENGTH_SHORT).show();
                    loadingDialogUtil.dismissLoadingDialog();
                }
            });
        } else {
            // No changes were made, simply switch back to the details layout
            showDetailsLayout();
            loadingDialogUtil.dismissLoadingDialog();
        }
    }


}
