package com.example.myandroidproject.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.customer.activities.DetailOrderActivity;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.utilss.Constraint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TripDoneAdapter extends RecyclerView.Adapter<TripDoneAdapter.ViewTripDoneHolder> {
    private List<OrderItem> orderItemList;
    private Context context;
    public TripDoneAdapter(List<OrderItem> orderItemList, Context context) {
        this.orderItemList = orderItemList;
        this.context = context;
    }
    @NonNull
    @Override
    public TripDoneAdapter.ViewTripDoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_completed, parent, false);
        return new TripDoneAdapter.ViewTripDoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDoneAdapter.ViewTripDoneHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        if (orderItem == null) return;
        TripDoneAdapter.ViewTripDoneHolder viewTripDoneHolder = holder;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat formatterCurrency = new DecimalFormat("###,###,###");

        viewTripDoneHolder.nameCar.setText(orderItem.getNameVehicle());
        viewTripDoneHolder.totalPrice.setText(String.valueOf(orderItem.getPrice()));
        viewTripDoneHolder.rentalDay.setText(formatter.format(orderItem.getRentalDate()));
        viewTripDoneHolder.returnDay.setText(formatter.format(orderItem.getReturnDate()));

        holder.itemView.setOnClickListener(v -> {
            int id = orderItem.getId();

            Intent intent = new Intent(context.getApplicationContext(), DetailOrderActivity.class);
            intent.putExtra(Constraint.ID_ORDER_ITEM, id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ViewTripDoneHolder extends RecyclerView.ViewHolder {
        TextView nameCar, totalPrice, rentalDay, returnDay;
        public ViewTripDoneHolder(View itemView) {
            super(itemView);
            nameCar = itemView.findViewById(R.id.tenXe);
            totalPrice = itemView.findViewById(R.id.chiPhi);
            rentalDay = itemView.findViewById(R.id.ngayThue);
            returnDay = itemView.findViewById(R.id.ngayTra);
        }
    }
}
