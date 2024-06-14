package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangesPass extends AppCompatActivity {
    private TextInputEditText old_Pass, new_Pass, confirm_newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changes_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int userId = SharedPreferencesUtils.getInt(SharedPreferencesUtils.STATE_USER_ID, this);

        old_Pass = findViewById(R.id.oldPass);
        new_Pass = findViewById(R.id.newPass);
        confirm_newPass = findViewById(R.id.confirmNewPass);
        Button save = findViewById(R.id.btnSave);

        save.setOnClickListener(v -> {
            if (validateFields()) {
                changePassword(userId);
            }
        });
    }

    private boolean validateFields() {
        boolean valid = true;
        String pass = old_Pass.getText().toString();
        String newPass = new_Pass.getText().toString();
        String newPassConf = confirm_newPass.getText().toString();

        if (pass.isEmpty()) {
            old_Pass.setError("Mật khẩu không được để trống.");
            valid = false;
        } else {
            old_Pass.setError(null);
        }

        if (newPass.isEmpty()) {
            new_Pass.setError("Mật khẩu mới không được để trống.");
            valid = false;
        } else {
            new_Pass.setError(null);
        }

        if (newPassConf.isEmpty()) {
            confirm_newPass.setError("Xác nhận mật khẩu không được để trống.");
            valid = false;
        } else {
            confirm_newPass.setError(null);
        }

        if (!newPass.equals(newPassConf)) {
            confirm_newPass.setError("Mật khẩu không khớp !!!");
            valid = false;
        } else {
            confirm_newPass.setError(null);
        }

        return valid;
    }

    private void changePassword(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL + "/api/v1/users/changesPass/" + userId;

        JSONObject user = new JSONObject();
        try {
            user.put("password", old_Pass.getText().toString());
            user.put("newPassword", new_Pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                response -> {Toast.makeText(ChangesPass.this, "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangesPass.this, MyLicense.class));},
                error -> Toast.makeText(ChangesPass.this, "Đổi Mật Khẩu Không Thành Công", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }
}
