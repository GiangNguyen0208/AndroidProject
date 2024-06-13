package com.example.myandroidproject.shipper.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.adapter.ListUserAdapter;
import com.example.myandroidproject.models.OrderItem;
import com.example.myandroidproject.models.User;

import java.util.ArrayList;

public class ListOrderAdapter extends RecyclerView.Adapter<ListOrderAdapter.ShipperViewer> {
    private Context context;
    private ArrayList<OrderItem> orderItems;

    public ListOrderAdapter(Context context, ArrayList<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    public ListOrderAdapter.ShipperViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.list_order_item, parent, false);
        return new ListOrderAdapter.ShipperViewer(heroView);
    }

    public void onBindViewHolder(@NonNull ListOrderAdapter.ShipperViewer holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.orderID = item.getId();
        holder.status.setText(item.getStatus());
        holder.date.setText(item.getRental_day());
    }

    public int getItemCount() {
        return orderItems.size();
    }


    public class ShipperViewer extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int orderID;
        private ImageView orderImage;
        private TextView date;
        private TextView status;


        public ShipperViewer(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.admin_user_image);
            date = itemView.findViewById(R.id.dateShipping);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Bundle b = new Bundle();
            b.putInt("Order", orderID);
            Navigation.findNavController(v).navigate(R.id.user_list_to_user_detail, b);
        }
    }
}
