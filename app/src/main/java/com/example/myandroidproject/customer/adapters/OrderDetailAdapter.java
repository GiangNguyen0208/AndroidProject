package com.example.myandroidproject.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private OrderItem orderItem;

    public OrderDetailAdapter(Context context, OrderItem orderItem) {
        this.orderItem = orderItem;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_order, parent, false);
        return new OrderDetailAdapter.ViewDetailOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (orderItem == null) return;
        OrderDetailAdapter.ViewDetailOrderHolder viewDetailOrderHolder = (ViewDetailOrderHolder) holder;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        viewDetailOrderHolder.nameCar.setText(orderItem.getNameVehicle());
        viewDetailOrderHolder.brand.setText(orderItem.getBrandVehicle());
        viewDetailOrderHolder.day.setText(orderItem.getRental_day());
        viewDetailOrderHolder.pricePerOn.setText((int) orderItem.getPrice()/orderItem.getRental_day());
        viewDetailOrderHolder.rentalDate.setText(formatter.format(orderItem.getRentalDate()));
        viewDetailOrderHolder.returnDate.setText(formatter.format(orderItem.getReturnDate()));
        viewDetailOrderHolder.totalPrice.setText((int) orderItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewDetailOrderHolder extends RecyclerView.ViewHolder {
        TextView nameCar, day, pricePerOn, type, brand, color, about, rentalDate, returnDate, totalPrice;
        public ViewDetailOrderHolder(View view) {
            super(view);
            nameCar = view.findViewById(R.id.title);
            day = view.findViewById(R.id.dayRental);
            pricePerOn = view.findViewById(R.id.priceAmount);
            type = view.findViewById(R.id.type);
            brand = view.findViewById(R.id.brandValue);
            color = view.findViewById(R.id.color);
            about = view.findViewById(R.id.desc);
            rentalDate = view.findViewById(R.id.rentalDay);
            returnDate = view.findViewById(R.id.returnDay);
            totalPrice = view.findViewById(R.id.totalPrice);
        }
    }
}
