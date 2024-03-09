package com.example.mymicroprojectmad;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class idViewActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();

        // Retrieve data from intent extras
        String fullName = getIntent().getStringExtra("fullName");
        String className = getIntent().getStringExtra("className");
        String enrollmentNo = getIntent().getStringExtra("enrollmentNo");
        String dob = getIntent().getStringExtra("dob");
        String address = getIntent().getStringExtra("address");
        String mobileNo = getIntent().getStringExtra("mobileNo");
        byte[] imageByteArray = intent.getByteArrayExtra("image");

        // Assuming you have TextViews with IDs "textViewFullName", "textViewClassName", etc.
        TextView textViewFullName = findViewById(R.id.textViewFullName);
        TextView textViewClassName = findViewById(R.id.textViewClassName);
        TextView textViewEnrollmentNo = findViewById(R.id.textViewEnrollmentNo);
        TextView textViewDob = findViewById(R.id.textViewDob);
        TextView textViewAddress = findViewById(R.id.textViewAddress);
        TextView textViewMobileNo = findViewById(R.id.textViewMobileNo);

        // Display the retrieved data in TextViews
        textViewFullName.setText(fullName.toUpperCase());
        textViewClassName.setText( "Class: " +className);
        textViewEnrollmentNo.setText( "Enroll No. " +enrollmentNo);
        textViewDob.setText( "Date of Birth: " +dob);
        textViewAddress.setText( "Add.: " +address);
        textViewMobileNo.setText( "Mob.: " +mobileNo);

// Convert the byte array back to a Bitmap
        assert imageByteArray != null;
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

//         Set the Bitmap in an ImageView
        ImageView imageView = findViewById(R.id.imageView1);
        imageView.setImageBitmap(imageBitmap);

        // Generate QR code for enrollment number
        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
        Bitmap qrCodeBitmap = generateQRCode(enrollmentNo);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
        LinearLayout qrCodeLinearLayout = findViewById(R.id.idplusqr);

        Button btnSaveImage = findViewById(R.id.btnSaveImage);

        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to save the view as an image
                saveViewAsImage(qrCodeLinearLayout);
            }
        });
    }


    // Function to generate QR code
    private Bitmap generateQRCode(String data) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to save the composed view as an image
    public void saveViewAsImage(View view) {
        Bitmap bitmap = convertViewToBitmap(view);

        // Save the bitmap to a file
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "id_card_image.png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Toast.makeText(this, "Image saved successfully \n (Check Your Downloads)", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to convert a view to a bitmap
    private Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}
