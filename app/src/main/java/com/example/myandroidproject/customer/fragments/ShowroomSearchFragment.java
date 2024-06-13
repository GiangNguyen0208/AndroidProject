package com.example.myandroidproject.customer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowroomSearchFragment extends Fragment {

    RecyclerView listViewVehicle;
    List<Vehicle> vehicleList = new ArrayList<>();
    ListVehicleAdapter listVehicleAdapter;
    String vehicleType = "";
    SearchView searchView;

    TextView findByCar, findByMotor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_showroom, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findByCar = view.findViewById(R.id.findByCar);
        findByMotor = view.findViewById(R.id.findByMotor);
        searchView = view.findViewById(R.id.searchBar);
        listViewVehicle = view.findViewById(R.id.listViewVehicle);
        listVehicleAdapter = new ListVehicleAdapter(vehicleList, view.getContext());
        listViewVehicle.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listViewVehicle.setAdapter(listVehicleAdapter);
        findByCar.setOnClickListener(v -> {
            vehicleType = "car";
            getListVehicleByType(vehicleType);
        });
        findByMotor.setOnClickListener(v -> {
            vehicleType = "motor";
            getListVehicleByType(vehicleType);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findBySearchParam(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findBySearchParam(newText);
                return false;
            }
        });
        getListVehicle();
    }

    private void findBySearchParam(String query) {
        vehicleList.clear(); // Clear the list before adding new items
        listVehicleAdapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_SEARCH_VEHICLE + query; // Assume URL_SEARCH_VEHICLE is your search endpoint

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String brand = jsonObject.getString("brand");
                                double price = jsonObject.getDouble("price");
                                String imageLink = jsonObject.getString("imageUrl");
                                Vehicle vehicle = Vehicle.builder()
                                        .id(id)
                                        .nameVehicle(name)
                                        .brandVehicle(brand)
                                        .imageLink(imageLink)
                                        .price(price)
                                        .build();
                                vehicleList.add(vehicle);
                            }
                            listVehicleAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void getListVehicleByType(String vehicleType) {
        vehicleList.clear(); // Clear the list before adding new items
        listVehicleAdapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_FIND_BY_TYPE + vehicleType;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String brand = jsonObject.getString("brandName");
                                double price = jsonObject.getDouble("price");
                                String imageLink = jsonObject.getString("imageUrl");
                                Vehicle vehicle = Vehicle.builder()
                                        .id(id)
                                        .nameVehicle(name)
                                        .brandVehicle(brand)
                                        .imageLink(imageLink)
                                        .price(price)
                                        .build();
                                vehicleList.add(vehicle);
                            }
                            listVehicleAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void getListVehicle() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_VEHICLE_LIST;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String brand = jsonObject.getString("brandName");
                                double price = jsonObject.getDouble("price");
                                String imageLink = jsonObject.getString("image");
                                Vehicle vehicle = Vehicle.builder()
                                        .id(id)
                                        .nameVehicle(name)
                                        .brandVehicle(brand)
                                        .imageLink(imageLink)
                                        .price(price)
                                        .build();
                                vehicleList.add(vehicle);
                            }
                            // Use itemList to update UI (e.g., RecyclerView Adapter)
                            listVehicleAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
}