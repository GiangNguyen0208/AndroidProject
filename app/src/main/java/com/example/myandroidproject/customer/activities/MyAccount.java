package com.example.myandroidproject.customer.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utilss.Constraint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyAccount extends AppCompatActivity {

    private EditText firstname, lastname, phone, email, birthday;//, password;
    private RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        firstname = findViewById(R.id.etFirstname);
        lastname = findViewById(R.id.etLastname);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
//        password = findViewById(R.id.etPassword);
        birthday = findViewById(R.id.etBirthday);
        gender = findViewById(R.id.radioGenderGroup);
        Button btnSave = findViewById(R.id.btnSave);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1);

        if (userId != -1) {
            loadUserData(userId);
        }

        birthday.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveUserData(userId));
    }

    private String convertDateFormat(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = fromUser.parse(dateStr);
            assert date != null;
            return myFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    private String convertDateFormatToServer(String dateStr) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = fromUser.parse(dateStr);
            assert date != null;
            return myFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String selectedDate = dateFormat.format(calendar.getTime());
                    birthday.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadUserData(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL + "/api/v1/users/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        firstname.setText(user.optString("firstname", ""));
                        lastname.setText(user.optString("lastname", ""));
                        email.setText(user.optString("email", ""));
//                        password.setText(user.optString("password", ""));
                        phone.setText(user.optString("phone", ""));
                        boolean isMale = user.optBoolean("gender", true);
                        if (isMale) {
                            gender.check(R.id.radioMale);
                        } else {
                            gender.check(R.id.radioFemale);
                        }
                        birthday.setText(convertDateFormat(user.optString("birthDay", "")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                });

        queue.add(stringRequest);
    }

    private void saveUserData(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL + "/api/v1/users/" + userId;

        JSONObject user = new JSONObject();
        try {
            if (!firstname.getText().toString().isEmpty()) {
                user.put("firstname", firstname.getText().toString());
            }
            if (!lastname.getText().toString().isEmpty()) {
                user.put("lastname", lastname.getText().toString());
            }
            if (!email.getText().toString().isEmpty()) {
                user.put("email", email.getText().toString());
            }
//            if (!password.getText().toString().isEmpty()) {
//                user.put("password", password.getText().toString());
//            }
            if (!phone.getText().toString().isEmpty()) {
                user.put("phone", phone.getText().toString());
            }
            boolean isMale = gender.getCheckedRadioButtonId() == R.id.radioMale;
            user.put("gender", isMale);
            if (!birthday.getText().toString().isEmpty()) {
                user.put("birthDay", convertDateFormatToServer(birthday.getText().toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                response -> Toast.makeText(MyAccount.this, "User updated successfully", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MyAccount.this, "Error updating user", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }
}
