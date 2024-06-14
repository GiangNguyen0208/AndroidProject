package com.example.myandroidproject.admin.adapter;

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
import com.example.myandroidproject.customer.activities.DetailItemActivity;
import com.example.myandroidproject.models.Vehicle;
import com.example.myandroidproject.utilss.Constraint;

import java.util.List;
import java.util.Locale;

public class ListVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Vehicle> vehicleList;
    private Context context;

    public ListVehicleAdapter(List<Vehicle> vehicleList, Context context) {
        this.vehicleList = vehicleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_admin_vehicle_item, parent, false);
        return new VehicleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        if (vehicle == null) return;
        VehicleHolder vehicleHolder = (VehicleHolder) holder;
        vehicleHolder.name.setText(vehicle.getNameVehicle());
        vehicleHolder.price.setText(String.format(Locale.getDefault(), "Giá gốc: %f - Giảm giá: %f%%", vehicle.getPrice(), vehicle.getDiscount() * 100));
        vehicleHolder.type.setText(vehicle.getType());
        Glide.with(this.context).load(vehicle.getImageLink()).into(vehicleHolder.imageView);
        vehicleHolder.itemView.setOnClickListener(v -> {
            int id = vehicle.getId();
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra(Constraint.ID_VEHICLE, id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }



    public class VehicleHolder extends RecyclerView.ViewHolder {
        View viewDetail;
        TextView name;
        TextView type;
        TextView price;
        ImageView imageView;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.admin_vehicle_name);
            type = itemView.findViewById(R.id.admin_vehicle_type);
            price = itemView.findViewById(R.id.admin_vehicle_price);
            imageView = itemView.findViewById(R.id.admin_vehicle_image);
        }
    }
}
