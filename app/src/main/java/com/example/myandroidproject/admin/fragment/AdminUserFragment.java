package com.example.myandroidproject.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.adapter.ListUserAdapter;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.models.User;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminUserFragment extends Fragment {

    private ArrayList<User> users;

    public AdminUserFragment() {
        users = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getUserLists();
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }


    private void getUserLists() {
        users.clear();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_USER_LIST;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                onReturn(), onError());

        queue.add(jsonObjectRequest);
    }

    private Response.Listener<JSONArray> onReturn() {
        return response -> {
            JSONObject obj;
            for (int i = 0; i < response.length(); i++) {
                try {
                    obj = response.getJSONObject(i);
                    User user = User.builder()
                            .firstname(obj.getString("firstname"))
                            .lastname(obj.getString("lastname")).build();
                    users.add(user);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }
            }

            ListUserAdapter adapter = new ListUserAdapter(getContext(), users);
            RecyclerView view = getView().findViewById(R.id.admin_user_container);
            RecyclerView.LayoutManager mgr = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            view.setAdapter(adapter);
            view.setLayoutManager(mgr);
        };
    }

    private Response.ErrorListener onError() {
        return error -> {
            System.err.println(error.getMessage());
            Toast.makeText(getContext(), "Error trying to show products", Toast.LENGTH_SHORT).show();
        };
    }
}