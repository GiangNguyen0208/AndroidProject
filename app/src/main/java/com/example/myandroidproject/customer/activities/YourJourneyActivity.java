package com.example.myandroidproject.customer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.YourJourneyAdapter;
import com.example.myandroidproject.models.CartItem;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YourJourneyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private YourJourneyAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    Button btn_ConfirmPay;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_journey);
        recyclerView = findViewById(R.id.listCartItem);
        adapter = new YourJourneyAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        userId = SharedPreferencesUtils.getInt(SharedPreferencesUtils.STATE_USER_ID, this);

        getListCart(userId);
    }
    private void getListCart(Integer userId) {
        RequestQueue queue = Volley.newRequestQueue(YourJourneyActivity.this);
        String url = Constraint.URL_CART_ITEM + "?idUser=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            System.out.println(response.length());
                            for (int i = 0; i < response.length(); i++) {
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                int idVehicle = jsonObject.getInt("vehicleid");
                                String name = jsonObject.getString("nameVehicle");
                                double price = Double.parseDouble(jsonObject.getString("price"));
                                String imageLink = jsonObject.getString("imageLink");
                                Date rentalDate = formatterDate.parse(jsonObject.getString("rentalDate"));
                                Date returnDate = formatterDate.parse(jsonObject.getString("returnDate"));
                                CartItem cartItem = CartItem.builder()
                                        .id(id)
                                        .idVehicle(id)
                                        .nameItem(name)
                                        .imageLink(imageLink)
                                        .price(price)
                                        .returnDate(returnDate)
                                        .rentalDate(rentalDate)
                                        .build();
                                cartItems.add(cartItem);
                            }
                            adapter.notifyDataSetChanged();
                            // Use itemList to update UI (e.g., RecyclerView Adapter)
                            Toast.makeText(YourJourneyActivity.this, "Show !!!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(YourJourneyActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YourJourneyActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

}