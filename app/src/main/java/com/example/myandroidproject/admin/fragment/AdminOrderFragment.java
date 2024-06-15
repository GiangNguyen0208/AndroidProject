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
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.shipper.adapter.ListOrderAdapter;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminOrderFragment extends Fragment {

    private ArrayList<OrderItem> orderItems;
    public AdminOrderFragment() {
        orderItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOrderList();
    }

    private void getOrderList() {
        orderItems.clear();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_LIST_ORDER;

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
                    SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                    Date rental = fromUser.parse(obj.getString("rental_date"));
                    OrderItem order = OrderItem.builder()
                            .id(obj.getInt("id"))
                            .status(obj.getString("status"))
                            .rentalDate(rental).build();
                    orderItems.add(order);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            ListOrderAdapter adapter = new ListOrderAdapter(getContext(), orderItems);
            RecyclerView view = requireView().findViewById(R.id.admin_order_container);
            RecyclerView.LayoutManager mgr = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            view.setAdapter(adapter);
            view.setLayoutManager(mgr);
            requireView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        };
    }

    private Response.ErrorListener onError() {
        return error -> {
            System.err.println(error.getMessage());
            Toast.makeText(getContext(), "Error trying to show order items", Toast.LENGTH_SHORT).show();
        };
    }
}