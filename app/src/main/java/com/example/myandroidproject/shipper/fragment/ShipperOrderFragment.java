package com.example.myandroidproject.shipper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.adapter.ListUserAdapter;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.User;
import com.example.myandroidproject.shipper.adapter.ListOrderAdapter;
import com.example.myandroidproject.utilss.Constraint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShipperOrderFragment extends Fragment {

    private ArrayList<OrderItem> orderItem;

    public ShipperOrderFragment() {
        orderItem = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                getOrderLists();
        return inflater.inflate(R.layout.activity_shipper_order_fragment, container, false);
    }

    private void getOrderLists() {
        orderItem.clear();
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
                    orderItem.add(order);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            ListOrderAdapter adapter = new ListOrderAdapter(getContext(), orderItem);
            RecyclerView view = requireView().findViewById(R.id.shipper_order_list);
            RecyclerView.LayoutManager mgr = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            view.setAdapter(adapter);
            view.setLayoutManager(mgr);
            requireView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        };
    }

    private Response.ErrorListener onError() {
        return error -> {
            System.err.println(error.getMessage());
            Toast.makeText(getContext(), "Error trying to show orders", Toast.LENGTH_SHORT).show();
        };
    }
}