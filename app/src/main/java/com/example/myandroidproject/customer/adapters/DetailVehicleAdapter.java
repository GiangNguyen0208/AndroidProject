package com.example.myandroidproject.customer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DetailVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer id, quantityRent;
    private String name;
    private double price;
    private String imageLink;
    private Context context;
    private Date rentalDate, returnDate;
    private Vehicle vehicle;

    public DetailVehicleAdapter(Context context, Vehicle vehicle) {
        this.vehicle = vehicle;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_item, parent, false);
        return new DetailVehicleAdapter.ViewDetailVehicleHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (vehicle == null) return;
        DetailVehicleAdapter.ViewDetailVehicleHolder viewDetailVehicleHolder = (ViewDetailVehicleHolder) holder;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        viewDetailVehicleHolder.name.setText(vehicle.getNameVehicle());
        viewDetailVehicleHolder.brand.setText(vehicle.getBrandVehicle());
        viewDetailVehicleHolder.price.setText((int) vehicle.getPrice());
        viewDetailVehicleHolder.rentalDay.setText(formatter.format(vehicle.getRentalDate()));
        viewDetailVehicleHolder.returnDay.setText(formatter.format(vehicle.getReturnDate()));
        viewDetailVehicleHolder.price.setText((int) vehicle.getPrice());
        viewDetailVehicleHolder.desc.setText(vehicle.getDescription());
        Glide.with(this.context).load(vehicle.getImageLink()).into(viewDetailVehicleHolder.imageView);
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewDetailVehicleHolder extends  RecyclerView.ViewHolder {
        View viewDetail;
        TextView name, returnDay, desc;
        TextView brand, rentalDay;
        TextView price;
        ImageView imageView;

        @SuppressLint("WrongViewCast")
        public ViewDetailVehicleHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            brand = itemView.findViewById(R.id.brandValue);
            price = itemView.findViewById(R.id.priceAmount);
            imageView = itemView.findViewById(R.id.imageVehicle);
            rentalDay = itemView.findViewById(R.id.rentalDay);
            returnDay = itemView.findViewById(R.id.returnDate);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
