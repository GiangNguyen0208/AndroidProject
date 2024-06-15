package com.example.myandroidproject.customer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotifyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        LinearLayout dynamicContainer = view.findViewById(R.id.dynamic_container);
        sendNotificationRequest(dynamicContainer);
        return view;
    }

    private void sendNotificationRequest(LinearLayout dynamicContainer) {
        String url = Constraint.URL_READ_ADMIN_NOT;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject notification = response.getJSONObject(i);
                                String notificationTitle = notification.getString("title");
                                String notificationMessage = notification.getString("content");
                                addNotificationToLayout(dynamicContainer, notificationTitle, notificationMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        showErrorInLayout(dynamicContainer);
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void addNotificationToLayout(LinearLayout container, String title, String message) {
        TextView textView = new TextView(getContext());
        textView.setText(title.toUpperCase() + ": " + message);
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        textView.setBackgroundColor(Color.GREEN);
        textView.setBackground(Drawable.createFromPath("@drawable/border_background"));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setBackgroundColor(Color.WHITE);
                showNotificationDialog(title, message);
            }
        });
        container.addView(textView);
    }

    private void showErrorInLayout(LinearLayout container) {
        TextView errorTextView = new TextView(getContext());
        errorTextView.setText("Không thể tải thông báo. Vui lòng thử lại sau.");
        errorTextView.setTextSize(16);
        errorTextView.setPadding(16, 16, 16, 16);
        container.addView(errorTextView);
    }

    private void showNotificationDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
