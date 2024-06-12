package com.example.myandroidproject.customer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapter.VehicleAdapter;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.models.Vehicles;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowroomFragment extends Fragment {

    private List<Vehicle> vehiclesList;

    public ShowroomFragment() {
        vehiclesList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getNewProduct();
        return inflater.inflate(R.layout.fragment_showroom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.showroomActivity);
        });
    }

    private void getNewProduct() {
        vehiclesList.clear();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                            .imageLink(obj.getString("image"))
                            .build();
                    vehiclesList.add(vehicle);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }
            }

            ListVehicleAdapter adapter = new ListVehicleAdapter(vehiclesList, getContext());
            RecyclerView view = getView().findViewById(R.id.recommended_container);
            RecyclerView.LayoutManager mgr = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
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