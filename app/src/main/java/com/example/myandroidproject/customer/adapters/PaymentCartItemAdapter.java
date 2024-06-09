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
import com.example.myandroidproject.models.CartItem;
import com.example.myandroidproject.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentCartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer id, quantityRent;
    private String name;
    private double price;
    private String imageLink;
    private Context context;
    private Date rentalDate, returnDate;
    private CartItem cartItem;
    public PaymentCartItemAdapter(Context context, CartItem cartItem) {
        this.cartItem = cartItem;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_payment, parent, false);
        return new PaymentCartItemAdapter.ViewPaymentCartItemHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (cartItem == null) return;
        PaymentCartItemAdapter.ViewPaymentCartItemHolder viewPaymentCartItemHolder = (ViewPaymentCartItemHolder) holder;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        viewPaymentCartItemHolder.totalPrice.setText((int) cartItem.getPrice());
        viewPaymentCartItemHolder.rentalDate.setText(formatter.format(cartItem.getRentalDate()));
        viewPaymentCartItemHolder.returnDate.setText(formatter.format(cartItem.getReturnDate()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewPaymentCartItemHolder extends RecyclerView.ViewHolder {
        TextView totalPrice, rentalDate, returnDate;
        public ViewPaymentCartItemHolder(View view) {
            super(view);
            totalPrice = view.findViewById(R.id.totalPrice);
            rentalDate = view.findViewById(R.id.rentalDate);
            returnDate = view.findViewById(R.id.returnDay);
        }
    }
}
