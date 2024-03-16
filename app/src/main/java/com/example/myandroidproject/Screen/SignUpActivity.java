package com.example.myandroidproject.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myandroidproject.Models.User;
import com.example.myandroidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private TextView goBackLogin;
    private EditText firstName, lastName, emailSignUp, password, passwordConfirm;
    private MaterialButton signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        goBackLogin = findViewById(R.id.go_back_login);
        goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        firstName = findViewById(R.id.first_Name);
        lastName = findViewById(R.id.last_name);
        emailSignUp = findViewById(R.id.email_Sign_Up);
        password = findViewById(R.id.password_Sign_Up);
        passwordConfirm = findViewById(R.id.password_Sign_Up_Confirm);
        signUpButton = findViewById(R.id.sign_Up);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (lastName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (emailSignUp.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(passwordConfirm.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    if (emailChecker(emailSignUp.getText().toString().trim())) {
                        createUser(firstName.getText().toString().trim(), lastName.getText().toString().trim(), emailSignUp.getText().toString().trim(), password.getText().toString().trim());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String firstName, String lastName, String email, String password) {
        // Khởi tạo Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Thực hiện tạo tài khoản người dùng với email và password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Tạo đối tượng User với thông tin của người dùng
                            User user = new User(firstName, lastName, email, password);

                            // Khởi tạo Firebase Realtime Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            // Lưu thông tin người dùng vào Firebase Realtime Database
                            reference.push().setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Nếu lưu thành công, chuyển đến HomeActivity
                                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                finish();
                                                Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Nếu lưu không thành công, hiển thị thông báo lỗi
                                                Toast.makeText(SignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Nếu tạo tài khoản không thành công, hiển thị thông báo lỗi
                            Toast.makeText(SignUpActivity.this, "Failed to create user account", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý ngoại lệ và hiển thị thông báo lỗi
                        Log.e("FirebaseError", "createUserWithEmailAndPassword failed: " + e.getMessage());
                        Toast.makeText(SignUpActivity.this, "Failed to create user account", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}