package com.example.myandroidproject.customer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.LoginActivity;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.activities.AdminActivity;
import com.example.myandroidproject.shipper.activites.ShipperActivity;
import com.example.myandroidproject.utils.Constraint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MyLicense extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private TextView verificationStatus, read;
    private ImageView licenseImg;
    private Button captureImg, verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_license);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verificationStatus = findViewById(R.id.tvVerificationStatus);
        read = findViewById(R.id.readMe);
        licenseImg = findViewById(R.id.ivLicenseImage);
        captureImg = findViewById(R.id.btnCapture);
        verify = findViewById(R.id.btnVerifyNow);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);

        loadStatus(userId);
        read.setOnClickListener(v -> showReadMeDialog());

        captureImg.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkCameraHardware(this)) {
            Camera camera = getCameraInstance();
            if (camera != null) {
                camera.release();
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    licenseImg.setImageBitmap(photo);
                    try{
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int userId = sharedPreferences.getInt("id", -1);
                        uploadImg(userId, photo);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Somthing Wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Unable to access the camera", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "This device doesn't have a camera", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImg(int userId, Bitmap photo){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_BE + "/api/v1/users/" + userId + "/upload";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", userId);
            jsonBody.put("file", photo);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request body.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                    String message = response.getString("message");
                        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        queue.add(jsonObjectRequest);
    }



    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return c;
    }


    private void loadStatus(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_BE + "/api/v1/users/" + userId + "/uploadStatus";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        boolean status = user.optBoolean("status", true);
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