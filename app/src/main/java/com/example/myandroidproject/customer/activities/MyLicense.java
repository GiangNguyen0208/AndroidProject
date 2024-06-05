package com.example.myandroidproject.customer.activities;

import static com.example.myandroidproject.R.layout.activity_my_license;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.helpers.VolleyMultipartRequest;
import com.example.myandroidproject.utils.Constraint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyLicense extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    private TextView verificationStatus;
    private TextView textView;
    private Bitmap bitmap;
    private ImageView licenseImg;

    private ActivityResultLauncher<Intent> captureImageResultLauncher;
    private ActivityResultLauncher<Intent> pickImageResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_my_license);

        verificationStatus = findViewById(R.id.tvVerificationStatus);
        TextView read = findViewById(R.id.readMe);
        licenseImg = findViewById(R.id.ivLicenseImage);
        Button captureImg = findViewById(R.id.btnCapture);
        Button verify = findViewById(R.id.btnVerifyNow);
        textView = findViewById(R.id.textView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);

        loadStatus(userId);
        read.setOnClickListener(v -> showReadMeDialog());

        captureImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        licenseImg.setImageBitmap(photo);
                        uploadBitmap(photo, userId);
                    }
                });

        pickImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri picUri = result.getData().getData();
                        if (picUri != null) {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                                licenseImg.setImageBitmap(bitmap);
                                textView.setText("File Selected");
                                uploadBitmap(bitmap, userId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        captureImg.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageResultLauncher.launch(cameraIntent);
        });

        textView.setOnClickListener(v -> {
            if (checkStoragePermissions()) {
                showFileChooser();
            } else {
                requestStoragePermissions();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
    }

    private void uploadBitmap(final Bitmap bitmap, int userId) {
        String url = Constraint.URL_BE + "/api/v1/users/" + userId + "/upload";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imageName = "userLicense" + userId;
                params.put("file", new DataPart(imageName + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void loadStatus(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_BE + "/api/v1/users/" + userId + "/uploadStatus";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        boolean status = user.optBoolean("status", false);
                        if (status) {
                            verificationStatus.setText("Verification Status: Verified");
                            verificationStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        } else {
                            verificationStatus.setText("Verification Status: Not Verified");
                            verificationStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                });

        queue.add(stringRequest);
    }

    private void showReadMeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lưu Ý");
        builder.setMessage("Hành vi sử dụng bằng lái xe giả là hành vi vi phạm pháp luật. Người thực hiện hành vi này có thể bị xử phạt vi phạm hành chính theo quy định tại điểm a khoản 5, điểm b khoản 7 Điều 21 Nghị định 100/2019 (sửa đổi bởi Nghị định 123/2021).");

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
