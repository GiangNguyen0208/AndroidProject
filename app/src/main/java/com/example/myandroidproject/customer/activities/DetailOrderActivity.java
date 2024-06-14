package com.example.myandroidproject.customer.activities;

import static com.example.myandroidproject.utilss.Constraint.ID_ORDER_ITEM;
import static com.example.myandroidproject.utilss.Constraint.ID_VEHICLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.DetailVehicleAdapter;
import com.example.myandroidproject.customer.adapters.OrderDetailAdapter;
import com.example.myandroidproject.customer.fragments.TripFragment;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailOrderActivity extends AppCompatActivity {
    TextView nameCar, dayRent, pricePerOn, address, brandName, email, phone, rentalDay, returnDay, totalPrice;
    ImageView back;
    int idOrder;
    OrderDetailAdapter adapter;
    OrderItem orderItem;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameCar = findViewById(R.id.title);
        dayRent = findViewById(R.id.dayRental);
        pricePerOn = findViewById(R.id.priceAmount);
        brandName = findViewById(R.id.brandValue);
        rentalDay = findViewById(R.id.rentalDate);
        returnDay = findViewById(R.id.returnDate);
        totalPrice = findViewById(R.id.totalPrice);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        back = findViewById(R.id.back_evtt);
        back.setOnClickListener(v -> {
            startActivity(new Intent(this, TripFragment.class));
            finish();
        });
        idOrder = getIntent().getIntExtra(ID_ORDER_ITEM, -1);
        adapter = new OrderDetailAdapter(this, orderItem);
        generateDetailOrder(idOrder);
    }

    private void generateDetailOrder(int idOrder) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_ORDER_ITEM_DETAIL_BY_ID + "?idOrderItem="  + idOrder;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            int id = response.getInt("id");
                            int carID = response.getInt("vehicleid");
                            String name = response.getString("nameVehicle");
                            String brand = response.getString("brandVehicle");
                            String address = response.getString("address");
                            String phone = response.getString("phone");
                            String email = response.getString("email");
                            int day = response.getInt("rental_day");
                            double price = Double.parseDouble(response.getString("price"));
                            Date rental = formatter.parse(response.getString("rentalDate"));
                            Date returnDate = formatter.parse(response.getString("returnDate"));
                            orderItem = OrderItem.builder()
                                    .id(id)
                                    .vehicleid(carID)
                                    .nameVehicle(name)
                                    .brandVehicle(brand)
                                    .address(address)
                                    .phone(phone)
                                    .email(email)
                                    .rental_day(day)
                                    .price(price)
                                    .rentalDate(rental)
                                    .returnDate(returnDate)
                                    .build();
                            Toast.makeText(DetailOrderActivity.this, "Describe Order Detail", Toast.LENGTH_SHORT).show();
                            getDetailOrder();
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailOrderActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailOrderActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",idOrder+"");
                return params;
            }
        };
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
    private void getDetailOrder() {
        if (orderItem != null) {
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            nameCar.setText(orderItem.getNameVehicle());
            totalPrice.setText(orderItem.getPrice() + " VNĐ");
            brandName.setText(orderItem.getBrandVehicle());
            rentalDay.setText(formatterDate.format(orderItem.getRentalDate()));
            returnDay.setText(formatterDate.format(orderItem.getReturnDate()));
            dayRent.setText(String.valueOf(orderItem.getRental_day()));
            phone.setText(orderItem.getPhone());
            address.setText(orderItem.getAddress());
            email.setText(orderItem.getEmail());
            pricePerOn.setText(orderItem.getPrice()/orderItem.getRental_day()+" VNĐ");
        }
    }
}