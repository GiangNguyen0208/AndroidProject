package com.example.myandroidproject.customer.activities;

import static com.example.myandroidproject.utilss.Constraint.ID_VEHICLE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.DetailVehicleAdapter;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailItemActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameCar, nameType, nameColor, description, discountValue, priceDiscount, brand;
    TextView price;
    Integer id;
    Vehicle vehicleView;
    ImageView back_evt;
    DetailVehicleAdapter detailVehicleAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        back_evt = findViewById(R.id.back_evtt);
        back_evt.setOnClickListener(v -> {
            startActivity(new Intent(this, ShowroomActivity.class));
            finish();
        });
        imageView = findViewById(R.id.imageVehicle);
        nameCar = findViewById(R.id.title);
        price = findViewById(R.id.priceAmount);
        brand = findViewById(R.id.brandValue);
        nameType = findViewById(R.id.type);
        nameColor = findViewById(R.id.color);
//        description = findViewById(R.id.desc);
        discountValue = findViewById(R.id.discount);
        priceDiscount = findViewById(R.id.priceDiscount);

        id = getIntent().getIntExtra(ID_VEHICLE, 0);
        detailVehicleAdapter = new DetailVehicleAdapter(this, vehicleView);
        getDetailCallAPI(id);

    }

    private void getDetailVehicle() {
        if (vehicleView != null) {
            Glide.with(this).load(vehicleView.getImageLink()).into(imageView);
            nameCar.setText(vehicleView.getNameVehicle());
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            price.setText(formatter.format(vehicleView.getPrice()) + " VNĐ");
            brand.setText(vehicleView.getBrandVehicle());
            nameType.setText(vehicleView.getType());
            nameColor.setText(vehicleView.getColor());
//            description.setText(vehicleView.getDescription());
            String formattedValue = formatToPercentage(vehicleView.getDiscount());
            discountValue.setText(formattedValue);
            priceDiscount.setText(formatter.format(vehicleView.getPriceDiscount()) + " VNĐ");
        }
    }
    private String formatToPercentage(double discount) {
        return String.format("%.0f%%", discount * 100);
    }

    private void getDetailCallAPI(Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_VEHICLE_DETAIL_BY_ID  + String.valueOf(id);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                int carID = response.getInt("id");
                                String name = response.getString("name");
                                String brand = response.getString("brand");
                                double price = Double.parseDouble(response.getString("price"));
                                String imageLink = response.getString("image");
                                String type = response.getString("type");
                                String color = response.getString("color");
//                                String desc = response.getString("desc");
                                double discount = Double.parseDouble(response.getString("discount"));
                                double priceDiscount = price*(1-discount);
                                vehicleView = Vehicle.builder()
                                        .id(carID)
                                        .color(color)
                                        .type(type)
                                        .discount(discount)
                                        .priceDiscount(priceDiscount)
                                        .nameVehicle(name)
                                        .brandVehicle(brand)
                                        .imageLink(imageLink)
                                        .price(price)
//                                        .description(desc)
                                        .build();
                                Toast.makeText(DetailItemActivity.this, "Describe Detail", Toast.LENGTH_SHORT).show();
                        // Use itemList to update UI (e.g., RecyclerView Adapter)
                            getDetailVehicle();
                            detailVehicleAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailItemActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailItemActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id+"");
                return params;
            }
        };
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }


}