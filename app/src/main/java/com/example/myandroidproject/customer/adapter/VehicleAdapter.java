package com.example.myandroidproject.customer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.models.Vehicles;

import java.io.InputStream;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicles> vehicleList;

    public VehicleAdapter(List<Vehicles> contacts) {
        vehicleList = contacts;
    }

    @NonNull
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.fragment_car_card, parent, false);
        VehicleViewHolder viewHolder = new VehicleViewHolder(contactView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicles vehicle = vehicleList.get(position);
        TextView textView = holder.nameTextView;
        textView.setText(vehicle.getName());
        ImageView imageView = holder.imgView;
        new DownloadImageTask(imageView).execute(vehicle.getImgURL());
    }

    public int getItemCount() {
        return vehicleList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public ImageView imgView;
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.carName);
            imgView = itemView.findViewById(R.id.carImage);
        }
    }
}
