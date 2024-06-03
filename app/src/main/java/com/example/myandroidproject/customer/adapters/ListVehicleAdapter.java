package com.example.myandroidproject.customer.adapters;

import android.content.Context;
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

import java.util.List;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_vehicle, parent, false);
        return new ListVehicleAdapter.ViewVehicleHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        if (vehicle == null) return;
        ViewVehicleHolder viewVehicleHolder = (ViewVehicleHolder) holder;
        viewVehicleHolder.name.setText(vehicle.getNameVehicle());
        viewVehicleHolder.brand.setText(vehicle.getBrandVehicle());
        Glide.with(this.context).load(vehicle.getImageLink()).into(viewVehicleHolder.imageView);
    }
    @Override
    public int getItemCount() {
        return vehicleList.size();
    }
    class ViewVehicleHolder extends RecyclerView.ViewHolder {
        View viewDetail;
        TextView name;
        TextView brand;
        TextView price;
        ImageView imageView;

        public ViewVehicleHolder(@NonNull View itemView) {
            super(itemView);
            name = name.findViewById(R.id.nameVehicle);
            brand = brand.findViewById(R.id.brandVehicle);
            price = price.findViewById(R.id.rentalPrice);
            imageView = itemView.findViewById(R.id.imageVehicle);
        }
    }
}
