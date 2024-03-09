package com.example.mymicroprojectmad;



import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymicroprojectmad.auth.AuthToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class FindStudentActivity extends AppCompatActivity implements View.OnClickListener {

    Button scanBtn, fetchBtn,logout;


    TextView messageText, messageFormat;


    EditText editTextEnrollmentNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);
        AuthToken authToken = AuthToken.getInstance(this);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageText.setVisibility(View.GONE);

        messageFormat = findViewById(R.id.textFormat);

        fetchBtn = findViewById(R.id.fetchdatabtn);
        editTextEnrollmentNo = findViewById(R.id.editTextEnrollmentNo);
        logout = findViewById(R.id.logoutBtn);



        // adding listener to the button
        scanBtn.setOnClickListener(this);
        fetchBtn.setOnClickListener(View -> {

            if (editTextEnrollmentNo.getText().toString().equals("")) {
                Toast.makeText(FindStudentActivity.this, "Enter Enrollment No", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(FindStudentActivity.this, DisplayStudentActivity.class);
            intent.putExtra("enrollmentNo", editTextEnrollmentNo.getText().toString());
            startActivity(intent);
        });
        logout.setOnClickListener(v -> {
            // Create an AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(FindStudentActivity.this);

            // Set the title and message for the dialog
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to log out?");

            // Set positive button (Yes action)
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Perform logout
                authToken.logout();

                // Redirect to the login page or perform other actions as needed
               finish();
               Intent intent = new Intent(FindStudentActivity.this, MainActivity.class);
               startActivity(intent);
            });

            // Set negative button (No action)
            builder.setNegativeButton("No", (dialog, which) -> {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String enrollmentNo = intentResult.getContents();
                Intent intent = new Intent(FindStudentActivity.this, DisplayStudentActivity.class);
                intent.putExtra("enrollmentNo", enrollmentNo);
                startActivity(intent);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }}

}


