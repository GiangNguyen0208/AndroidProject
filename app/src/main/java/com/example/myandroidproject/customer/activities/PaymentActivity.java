package com.example.myandroidproject.customer.activities;

import static com.example.myandroidproject.utilss.Constraint.ID_VEHICLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.adapters.PaymentCartItemAdapter;
import com.example.myandroidproject.models.CartItem;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends AppCompatActivity {
    TextView totalPrice, rentalDate, returnDate, nameVehicle;
    EditText dayRent, emaiTxt, phoneTxt, addressTxt;
    Button buttonConfirm, buttonSave;
    Integer id, day, userId, idVehicle;
    CartItem cartItem;
    OrderItem orderItem;
    PaymentCartItemAdapter paymentCartItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
    }

    private void initView() {
        totalPrice = findViewById(R.id.totalPrice);
        rentalDate = findViewById(R.id.rentalDate);
        returnDate = findViewById(R.id.returnDay);
        nameVehicle = findViewById(R.id.nameVehicle);

        dayRent = findViewById(R.id.day);
        emaiTxt = findViewById(R.id.email);
        phoneTxt = findViewById(R.id.phoneNumber);
        addressTxt = findViewById(R.id.address);

        buttonConfirm = findViewById(R.id.button_book_now);
        buttonSave = findViewById(R.id.save);



        id = getIntent().getIntExtra(Constraint.ID_CART_ITEM, -1);   // Get id CartItem.
        getDetailCartItem(id);

        buttonSave.setOnClickListener(v -> {
            day = Integer.valueOf(dayRent.getText().toString()); // Lấy giá trị khi người dùng nhấn nút
            String email = emaiTxt.getText().toString();
            String phone = phoneTxt.getText().toString();
            String address = addressTxt.getText().toString();

            setStateCartItem(id, day, email, phone, address);
        });
        buttonConfirm.setOnClickListener(v -> {
            day = Integer.valueOf(dayRent.getText().toString()); // Lấy giá trị khi người dùng nhấn nút
            String email = emaiTxt.getText().toString();
            String phone = phoneTxt.getText().toString();
            String address = addressTxt.getText().toString();
            String rentalDay = rentalDate.getText().toString();
            String returnDay = returnDate.getText().toString();
            String priceToPay = totalPrice.getText().toString();

            idVehicle = cartItem.getIdVehicle();

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            userId = sharedPreferences.getInt("id_user", -1); // -1 là giá trị mặc định nếu không tìm thấy id_user

            paymentCartItem(id, day, email, phone, address, rentalDay, returnDay, priceToPay, idVehicle, userId);
        });
    }
    private void detailCartItem() {
        if (cartItem != null) {
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            totalPrice.setText(formatter.format(cartItem.getPrice()) + " VNĐ");
            rentalDate.setText(formatterDate.format(cartItem.getRentalDate()));
            returnDate.setText(formatterDate.format(cartItem.getReturnDate()));
            nameVehicle.setText(cartItem.getNameItem());
            emaiTxt.setText(cartItem.getEmail());
            phoneTxt.setText(cartItem.getPhone());
            addressTxt.setText(cartItem.getAddress());
        }
    }
    private void getDetailCartItem(Integer id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_GET_CART_ITEM_TO_PAY + String.valueOf(id);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            int vehicleid = response.getInt("vehicleid");
                            String name = response.getString("nameVehicle");
                            double price = response.getDouble("price");
                            String imageLink = response.getString("imageLink");
                            Date rental = formatter.parse(response.getString("rentalDate"));
                            Date returnDate = formatter.parse(response.getString("returnDate"));
                            cartItem = CartItem.builder()
                                    .id(id)
                                    .nameItem(name)
                                    .idVehicle(vehicleid)
                                    .price(price)
                                    .rentalDate(rental)
                                    .returnDate(returnDate)
                                    .build();
                            Toast.makeText(PaymentActivity.this, "Describe Detail", Toast.LENGTH_SHORT).show();
                            detailCartItem();
                            if (paymentCartItemAdapter != null) {
                                paymentCartItemAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void setStateCartItem(Integer cartItemId, Integer day, String email, String phone, String address) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_SET_STATE_CART_ITEM;

        JSONObject statusUpdateDTO = new JSONObject();
        try {
            statusUpdateDTO.put("rentalDate", rentalDate.getText().toString());
            statusUpdateDTO.put("id", cartItemId);
            statusUpdateDTO.put("day", day);
            statusUpdateDTO.put("email", email);
            statusUpdateDTO.put("phone", phone);
            statusUpdateDTO.put("address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                statusUpdateDTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            int vehicleid = response.getInt("vehicleid");
                            String name = response.getString("nameVehicle");
                            int dayRent = response.getInt("rental_day");
                            double price = response.getDouble("price");
                            Date rental = formatter.parse(response.getString("rentalDate"));
                            Date returnDate = formatter.parse(response.getString("returnDate"));
                            String email = response.getString("email");
                            String phone = response.getString("phone");
                            String address = response.getString("address");
                            cartItem = CartItem.builder()
                                    .id(cartItemId)
                                    .nameItem(name)
                                    .idVehicle(vehicleid)
                                    .price(price)
                                    .rental_day(dayRent)
                                    .rentalDate(rental)
                                    .returnDate(returnDate)
                                    .email(email)
                                    .phone(phone)
                                    .address(address)
                                    .build();
                            Toast.makeText(PaymentActivity.this, "Describe Detail", Toast.LENGTH_SHORT).show();
                            detailCartItem();
                            if (paymentCartItemAdapter != null) {
                                paymentCartItemAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void paymentCartItem(Integer id, Integer day, String email, String phone, String address, String rentalDay, String returnDay, String price, Integer idVehicle, Integer userId) {
        if (!validationEmail()|| !validationPhone()|| !validationAddress()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constraint.URL_ADD_ORDER_ITEM;
        JSONObject paymentInfo = new JSONObject();

        String cleanedStr = price.replace(" VNĐ", "");
        // Loại bỏ dấu phân cách hàng nghìn
        cleanedStr = cleanedStr.replace(".", "");
        // Chuyển đổi chuỗi sang double
        double amount = Double.parseDouble(cleanedStr);

        try {
            paymentInfo.put("id", id);
            paymentInfo.put("day", day);
            paymentInfo.put("email", email);
            paymentInfo.put("phone", phone);
            paymentInfo.put("price", amount);
            paymentInfo.put("address", address);
            paymentInfo.put("userid", userId);
            paymentInfo.put("vehicleid", idVehicle);
            paymentInfo.put("rentalDate", rentalDay);
            paymentInfo.put("returnDate", returnDay);

            // Gửi thông tin thanh toán đến server
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    paymentInfo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            removeItemRegisterSuccess(id);
                            // Chuyển người dùng về màn hình HomeActivity
                            gotoHomeShow();
                            Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PaymentActivity.this, "Payment failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void gotoHomeShow() {
        startActivity(new Intent(this, YourJourneyActivity.class));
    }
    public boolean validationEmail() {
        String email = emaiTxt.getText().toString();
        if (email.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public boolean validationPhone() {
        String phone = phoneTxt.getText().toString();
        if (phone.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public boolean validationAddress() {
        String address = addressTxt.getText().toString();
        if (address.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public int validateDayRent(int day) {
        String dayString = dayRent.getText().toString();
        if (dayString != null) {
            return 1;
        } else {
            return day;
        }
    }

    private void removeItemRegisterSuccess(Integer id) {
        String url = Constraint.URL_REMOVE_CART_ITEM + "/" + String.valueOf(id);

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PaymentActivity.this, "Deleted CartItem success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý phản hồi lỗi
                        Toast.makeText(PaymentActivity.this, "Error deleting rental item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}