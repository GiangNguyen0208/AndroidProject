package com.example.myandroidproject.admin.fragment;

import android.os.Bundle;

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
import com.example.myandroidproject.admin.adapter.ListVehicleAdapter;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminVehicleFragment extends Fragment {

    private ArrayList<Vehicle> vehicles;

    public AdminVehicleFragment() {
        vehicles = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getVehicleList();
        return inflater.inflate(R.layout.fragment_admin_vehicle, container, false);
    }


    private void getVehicleList() {
        vehicles.clear();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_VEHICLE_LIST;

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
                    Vehicle vehicle = Vehicle.builder()
                            .id(obj.getInt("id"))
                            .nameVehicle(obj.getString("name"))
                            .price(obj.getDouble("price"))
                            .imageLink(obj.getString("image")).build();
                    vehicles.add(vehicle);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }
            }

            ListVehicleAdapter adapter = new ListVehicleAdapter(vehicles, getContext());
            RecyclerView view = requireView().findViewById(R.id.admin_user_container);
            RecyclerView.LayoutManager mgr = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            view.setAdapter(adapter);
            view.setLayoutManager(mgr);
            requireView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        };
    }

    private Response.ErrorListener onError() {
        return error -> {
            System.err.println(error.getMessage());
            Toast.makeText(getContext(), "Error trying to show users", Toast.LENGTH_SHORT).show();
        };
    }
}