package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.activities.AdminActivity;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.databinding.ActivityShowroomBinding;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.shipper.activites.ShipperActivity;
import com.example.myandroidproject.utils.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowroomActivity extends AppCompatActivity {

    ActivityShowroomBinding activityShowroomBinding;
    RecyclerView listViewVehicle;
    List<Vehicle> vehicleList = new ArrayList<>();
    ListVehicleAdapter listVehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_showroom);
        listViewVehicle = findViewById(R.id.listViewVehicle);
        listVehicleAdapter = new ListVehicleAdapter(vehicleList, this);
        listViewVehicle.setAdapter(listVehicleAdapter);
        callApi();
    }

    private void callApi() {
        RequestQueue queue = Volley.newRequestQueue(ShowroomActivity.this);
        String url = Constraint.URL_VEHICLE_LIST;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Vehicle> vehicleList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String brand = jsonObject.getString("brand");
                                double price = Double.parseDouble(jsonObject.getString("price"));
                                String imageLink = jsonObject.getString("image");
                                Vehicle vehicle = new Vehicle(name, brand, price, imageLink);
                                vehicleList.add(vehicle);
                            }
                            // Use itemList to update UI (e.g., RecyclerView Adapter)
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShowroomActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowroomActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);

    }
}