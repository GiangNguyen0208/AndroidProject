package com.example.myandroidproject.customer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.YourJourneyAdapter;
import com.example.myandroidproject.models.CartItem;
import com.example.myandroidproject.utilss.Constraint;
import com.google.android.material.color.utilities.Contrast;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YourJourneyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private YourJourneyAdapter yourJourneyAdapter;
    private List<CartItem> cartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_journey);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.listViewVehicle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra(Constraint.CART_ITEM);

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");

        Date rentalDate = null;
        Date returnDate = null;
        try {
            rentalDate = formatterDate.parse(jsonObject.get("rentalDate").getAsString());
            returnDate = formatterDate.parse(jsonObject.get("returnDate").getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CartItem cartItem = CartItem.builder()
                .nameItem(jsonObject.get("name").getAsString())
                .price(jsonObject.get("price").getAsDouble())
                .imageLink(jsonObject.get("image").getAsString())
                .rentalDate(rentalDate)
                .returnDate(returnDate)
                .build();
        cartItems.add(cartItem);

        yourJourneyAdapter = new YourJourneyAdapter(cartItems, this);
        recyclerView.setAdapter(yourJourneyAdapter);
    }
}