package com.example.myandroidproject.customer.adapters;

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
import com.example.myandroidproject.customer.activities.PaymentActivity;
import com.example.myandroidproject.models.CartItem;
import com.example.myandroidproject.utilss.Constraint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class YourJourneyAdapter extends RecyclerView.Adapter<YourJourneyAdapter.ViewYourJourneyHolder> {
    private List<CartItem> cartItems;
    private Context context;
    public YourJourneyAdapter(List<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewYourJourneyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_your_journey_vehicle, parent, false);
        return new ViewYourJourneyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewYourJourneyHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        if (cartItem == null) return;
        YourJourneyAdapter.ViewYourJourneyHolder viewYourJourneyHolder = holder;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        viewYourJourneyHolder.name.setText(cartItem.getNameItem());
        viewYourJourneyHolder.price.setText(String.valueOf(cartItem.getPrice()));
        Glide.with(context).load(cartItem.getImageLink()).into(viewYourJourneyHolder.imageView);
        viewYourJourneyHolder.rentalDate.setText(formatter.format(cartItem.getRentalDate()));
        viewYourJourneyHolder.returnDate.setText(formatter.format(cartItem.getReturnDate()));

//        holder.itemView.setOnClickListener(v -> {
//            int id = cartItem.getId();
//            int idvehicle = cartItem.getIdVehicle();
//            Intent intent = new Intent(context, PaymentActivity.class);
//            intent.putExtra(Constraint.ID_CART_ITEM, id);
//            intent.putExtra(Constraint.ID_VEHICLE, idvehicle);
//            context.startActivity(intent);
//        });
        holder.itemView.findViewById(R.id.confirmPayment).setOnClickListener(v -> {
            int id = cartItem.getId();
            int idvehicle = cartItem.getIdVehicle();
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra(Constraint.ID_CART_ITEM, id);
            intent.putExtra(Constraint.ID_VEHICLE, idvehicle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewYourJourneyHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView rentalDate;
        TextView returnDate;
        TextView price;
        ImageView imageView;

        public ViewYourJourneyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tenXe);
            rentalDate = itemView.findViewById(R.id.ngayThue);
            returnDate = itemView.findViewById(R.id.ngayTra);
            price = itemView.findViewById(R.id.chiPhi);
            imageView = itemView.findViewById(R.id.imgVehicle);
        }
    }
}