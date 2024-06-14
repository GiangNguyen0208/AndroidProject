package com.example.myandroidproject.customer.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.ListVehicleAdapter;
import com.example.myandroidproject.customer.adapters.TripAdapter;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class TripFragment extends Fragment {
    Integer userId;
    private List<OrderItem> orderItemList, orderItemCompletedList;
    TripAdapter adapter;
    public TripFragment() {
        orderItemList = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", -1);
        orderItemList = new ArrayList<>();
        orderItemCompletedList = new ArrayList<>();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateListTripContinue(userId);
        generateListTripDone(userId);
    }

    private void generateListTripDone(Integer userId) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Constraint.URL_GET_HISTORY_ORDERITEM_COMPLETED + "?idUser=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            OrderItem orderItem = new OrderItem();
                            orderItem.setId(obj.getInt("id"));
                            orderItem.setNameVehicle(obj.getString("nameVehicle"));
                            orderItem.setPrice(obj.getDouble("price"));
                            orderItem.setRentalDate(formatterDate.parse(obj.getString("rentalDate")));
                            orderItem.setReturnDate(formatterDate.parse(obj.getString("returnDate")));
                            orderItemCompletedList.add(orderItem);
                        }
                        RecyclerView recyclerView = getView().findViewById(R.id.recycler_completed_trips);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new TripAdapter(orderItemCompletedList, getContext());
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error trying to show history", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonArrayRequest);
    }

    private void generateListTripContinue(Integer userId) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Constraint.URL_GET_HISTORY_ORDERITEM_CONTINUE + "?idUser=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            OrderItem orderItem = new OrderItem();
                            orderItem.setId(obj.getInt("id"));
                            orderItem.setNameVehicle(obj.getString("nameVehicle"));
                            orderItem.setPrice(obj.getDouble("price"));
                            orderItem.setRentalDate(formatterDate.parse(obj.getString("rentalDate")));
                            orderItem.setReturnDate(formatterDate.parse(obj.getString("returnDate")));
                            orderItemList.add(orderItem);
                        }
                        RecyclerView recyclerView = getView().findViewById(R.id.recycler_current_trips);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new TripAdapter(orderItemList, getContext());
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error trying to show history", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonArrayRequest);
    }
}
