package com.example.myandroidproject.admin.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminUserDetailFragment extends Fragment {

    private int userID;
    private Button backBtn;
    private ArrayAdapter<String> adapter;
    private CheckBox isAdminMessage;

    public AdminUserDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt("userID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.user_detail_to_user_list);
        });
        //aa
        firstname = view.findViewById(R.id.etFirstname);
        lastname = view.findViewById(R.id.etLastname);
        phone = view.findViewById(R.id.etPhone);
        email = view.findViewById(R.id.etEmail);
//        password = findViewById(R.id.etPassword);
        birthday = view.findViewById(R.id.etBirthday);
        gender = view.findViewById(R.id.radioGenderGroup);
        roleSpinner = view.findViewById(R.id.role_input);
        isAdminMessage = view.findViewById(R.id.can_send_message);
        Button btnSave = view.findViewById(R.id.btnSave);
        loadUser(userID);
        birthday.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveUserData(userID));
    }


    private EditText firstname, lastname, phone, email, birthday;//, password;
    private RadioGroup gender;
    private Spinner roleSpinner;

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String selectedDate = dateFormat.format(calendar.getTime());
                    birthday.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadUserData(int userId) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_USER_BY_ID + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        //bye xd
                        getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        //hey
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
                        roleSpinner.setSelection(adapter.getPosition(user.optString("roleName", "user")), true);
                        isAdminMessage.setChecked(user.optBoolean("isAdminMessage"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "NOOOO", Toast.LENGTH_SHORT).show();
                });
        queue.add(stringRequest);
    }

    private void saveUserData(int userId) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_USER_BY_ID + userId;

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
            user.put("roleName", roleSpinner.getSelectedItem().toString());
            user.put("isAdminMessage", isAdminMessage.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                response -> Toast.makeText(requireContext(), "User updated successfully, restart this app if you updated yourself", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(requireContext(), "Error updating user", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }

    private void loadUser(int userID) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_GET_ROLES;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ArrayList<String> roleList = new ArrayList<>();
                        for (int i = 0; i< response.length(); i++) {
                            roleList.add(response.getJSONObject(i).getString("name"));
                        }
                        adapter = new ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, roleList);
                        roleSpinner.setAdapter(adapter);
                        loadUserData(userID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "NOOOO", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}