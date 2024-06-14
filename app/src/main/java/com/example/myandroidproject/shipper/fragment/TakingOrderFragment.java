package com.example.myandroidproject.shipper.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.activities.ChangesPass;
import com.example.myandroidproject.customer.activities.MyLicense;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.shipper.adapter.ListOrderAdapter;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakingOrderFragment extends Fragment {
    private Integer idOrder;
    private OrderItem orderItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idOrder = getArguments().getInt("Order");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getOrder();
        return inflater.inflate(R.layout.shipper_take_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button take_order = view.findViewById(R.id.btn_order);
        take_order.setOnClickListener(v -> take_order());
        Button goBack = view.findViewById(R.id.go_Back);
        goBack.setOnClickListener(v -> { Navigation.findNavController(v).navigate(R.id.action_takingOrderFragment_to_shipper_order_item);});

    }

    private void take_order() {
            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String url = Constraint.URL + "/api/v1/" + idOrder + "/setStatus";

            JSONObject user = new JSONObject();
            try {
                user.put("status", Constraint.SHIPPING);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                    response -> {Toast.makeText(requireContext(), "Nhận Đơn Hàng Thành Công !!!", Toast.LENGTH_SHORT).show();},
                    error -> Toast.makeText(requireContext(), "Ai Cho Mà Nhận ???", Toast.LENGTH_SHORT).show());

            queue.add(jsonObjectRequest);
    }

    private void getOrder() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constraint.URL_ORDER + idOrder + "/getOrder";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                onReturn(), onError());

        queue.add(jsonObjectRequest);
    }

    private Response.Listener<JSONObject> onReturn() {
        return response -> {
            JSONObject obj;
                try {
                    obj = response;
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                    Date rental = fromUser.parse(obj.getString("rentalDate"));
                    orderItem = OrderItem.builder()
                            .status(obj.getString("status"))
                            .phone(obj.getString("phone"))
                            .rentalDate(rental)
                            .address(obj.getString("address"))
                            .vehicleid(obj.getInt("vehicleid"))
                            .imageLink(obj.getString("imageLink")).build();

                    TextInputEditText phone = getView().findViewById(R.id.phoneNumber);
                    TextInputEditText address = getView().findViewById(R.id.address);
                    TextInputEditText rentalDate = getView().findViewById(R.id.rentalDate);
                    TextInputEditText vehicleid = getView().findViewById(R.id.idVehicle);
                    ImageView imageView = getView().findViewById(R.id.imgVehicle);
                    phone.setText(orderItem.getPhone());
                    address.setText(orderItem.getAddress());
                    rentalDate.setText(String.valueOf(orderItem.getRentalDate()));
                    vehicleid.setText(String.valueOf(orderItem.getVehicleid()));
                    Glide.with(requireContext()).load(orderItem.getImageLink()).into(imageView);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
        };
    }

    private Response.ErrorListener onError() {
        return error -> {
            System.err.println(error.getMessage());
            Toast.makeText(getContext(), "Error trying to show order", Toast.LENGTH_SHORT).show();
        };
    }
}
