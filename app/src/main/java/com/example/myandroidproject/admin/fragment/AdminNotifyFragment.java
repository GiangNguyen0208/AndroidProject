package com.example.myandroidproject.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminNotifyFragment extends Fragment {

    private EditText notifyTitleEditText;
    private EditText notifyMessageEditText;
    private Button sendNotifyButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_notify, container, false);

        notifyTitleEditText = view.findViewById(R.id.admin_notify_title_input);
        notifyMessageEditText = view.findViewById(R.id.admin_notify_message_input);
        sendNotifyButton = view.findViewById(R.id.admin_send_notify_button);

        sendNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        return view;
    }

    private void sendNotification() {
        String title = notifyTitleEditText.getText().toString().trim();
        String message = notifyMessageEditText.getText().toString().trim();

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tiêu đề và nội dung thông báo", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constraint.URL_READ_ADMIN_NOT;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Thông báo đã được gửi thành công", Toast.LENGTH_SHORT).show();
                        notifyTitleEditText.setText("");
                        notifyMessageEditText.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Có lỗi xảy ra khi gửi thông báo", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
