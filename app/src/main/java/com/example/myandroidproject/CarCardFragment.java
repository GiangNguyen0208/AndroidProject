package com.example.myandroidproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.activities.SignUpActivity;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView goto_showroom;

    RecyclerView recyclerViewHome;
    List<Vehicle> vehicleList = new ArrayList<>();
    CardView cardView;

    ListVehicleAdapter listVehicleAdapter;

    public CarCardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarCardFragment newInstance(String param1, String param2) {
        CarCardFragment fragment = new CarCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_card, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        goto_showroom = this.getActivity().findViewById(R.id.goto_showroom);
        goto_showroom.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), CarCardFragment.class));
        });
//        cardView = view.findViewById(R.id.searchView);
//        recyclerViewHome = view.findViewById(R.id.listViewVehicleInHome);
//        listVehicleAdapter = new ListVehicleAdapter(vehicleList, getContext());
//        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewHome.setAdapter(listVehicleAdapter);
//        getListVehicle();
    }
    private void getListVehicle() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Constraint.URL_VEHICLE_LIST;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Vehicle> temps = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String brand = jsonObject.getString("brand");
                                double price = Double.parseDouble(jsonObject.getString("price"));
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
                            Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
}