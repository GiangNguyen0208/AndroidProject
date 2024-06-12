package com.example.myandroidproject.customer.activities;

import static com.example.myandroidproject.utilss.Constraint.ID_VEHICLE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.myandroidproject.customer.fragments.ShowroomSearchFragment;
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

public class DetailItemActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameCar, nameType, nameColor, description, discountValue, priceDiscount, brand, day;
    TextView price, rentalDate, returnDate;
    ImageView add, remove;
    Integer id;
    Integer quantityRent = 1;
    Vehicle vehicleView;
    ImageView back_evt;
    DetailVehicleAdapter detailVehicleAdapter;
    Button rentalButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        back_evt = findViewById(R.id.back_evtt);
        rentalButton = findViewById(R.id.rentButton);

        day = findViewById(R.id.quantityDay);
        add = findViewById(R.id.addDay);
        remove = findViewById(R.id.removeDay);
        add.setOnClickListener(v -> {
            if (quantityRent < 10) {
                quantityRent++;
//                    day.setText(String.valueOf(quantityRent));
            }
        });
        remove.setOnClickListener(v -> {
            if (quantityRent > 0) {
                quantityRent--;
//                    day.setText(String.valueOf(quantityRent));
            }
        });
        back_evt.setOnClickListener(v -> {
//            startActivity(new Intent(this, ShowroomSearchFragment.class));
            finish();
        });
        imageView = findViewById(R.id.imageVehicle);
        nameCar = findViewById(R.id.title);
        price = findViewById(R.id.priceAmount);
        brand = findViewById(R.id.brandValue);
        nameType = findViewById(R.id.type);
        nameColor = findViewById(R.id.color);
        description = findViewById(R.id.desc);
        discountValue = findViewById(R.id.discount);
        priceDiscount = findViewById(R.id.priceDiscount);
        rentalDate = findViewById(R.id.rentalDay);
        returnDate = findViewById(R.id.returnDate);

        id = getIntent().getIntExtra(ID_VEHICLE, 0);
        detailVehicleAdapter = new DetailVehicleAdapter(this, vehicleView);
        getDetailCallAPI(id);

        rentalButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, ShowroomSearchFragment.class));
            getAddToJourney(id);
            finish();
        });
    }

    private void getDetailVehicle() {
        if (vehicleView != null) {
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            Glide.with(this).load(vehicleView.getImageLink()).into(imageView);
            nameCar.setText(vehicleView.getNameVehicle());
            price.setText(formatter.format(vehicleView.getPrice()) + " VNĐ");
            brand.setText(vehicleView.getBrandVehicle());
            nameType.setText(vehicleView.getType());
            nameColor.setText(vehicleView.getColor());
            description.setText(vehicleView.getDescription());
            String formattedValue = formatToPercentage(vehicleView.getDiscount());
            discountValue.setText(formattedValue);
            priceDiscount.setText(formatter.format(vehicleView.getPriceDiscount()) + " VNĐ");
            rentalDate.setText(formatterDate.format(vehicleView.getRentalDate()));
            returnDate.setText(formatterDate.format(vehicleView.getReturnDate()));
            day.setText(String.valueOf(quantityRent));
        }
    }

    private String formatToPercentage(double discount) {
        return String.format("%.0f%%", discount * 100);
    }

    // Add Journey...
    private void getAddToJourney(Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_ADD_TO_JOURNEY + String.valueOf(id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Intent intent = new Intent(DetailItemActivity.this, HomeActivity.class);
//                        startActivity(intent);
                        Toast.makeText(DetailItemActivity.this, "Add success !!!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailItemActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getDetailCallAPI(Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_VEHICLE_DETAIL_BY_ID + String.valueOf(id);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            int carID = response.getInt("id");
                            String name = response.getString("name");
                            String brand = response.getString("brand");
                            double price = Double.parseDouble(response.getString("price"));
                            String imageLink = response.getString("image");
                            String type = response.getString("type");
                            String color = response.getString("color");
                            Date rental = formatter.parse(response.getString("rentalDate"));
                            Date returnDate = formatter.parse(response.getString("returnDate"));
                            String desc = response.getString("desc");
                            double discount = Double.parseDouble(response.getString("discount"));
                            double priceDiscount = price * (1 - discount);
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
                                    .description(desc)
                                    .rentalDate(rental)
                                    .returnDate(returnDate)
                                    .build();
                            Toast.makeText(DetailItemActivity.this, "Describe Detail", Toast.LENGTH_SHORT).show();
                            // Use itemList to update UI (e.g., RecyclerView Adapter)
                            getDetailVehicle();
                            detailVehicleAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailItemActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailItemActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id + "");
                return params;
            }
        };
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }


}