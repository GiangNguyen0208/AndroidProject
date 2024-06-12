package com.example.myandroidproject.admin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myandroidproject.R;
import com.example.myandroidproject.models.User;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.UserViewer> {

    private Context context;
    private ArrayList<User> userList;

    public ListUserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    public UserViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.fragment_user_item, parent, false);
        return new UserViewer(heroView);
    }

    public void onBindViewHolder(@NonNull UserViewer holder, int position) {
        User user = userList.get(position);
        holder.userID = user.getId();
        holder.userName.setText(user.getLastname().concat(" ").concat(user.getFirstname()));
        holder.userInfo.setText("Quyền hạn: ".concat(user.getRoleName()));
    }

    public int getItemCount() {
        return userList.size();
    }


    public class UserViewer extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int userID;
        private ImageView userImage;
        private TextView userName;
        private TextView userInfo;


        public UserViewer(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.admin_user_image);
            userName = itemView.findViewById(R.id.admin_user_name);
            userInfo = itemView.findViewById(R.id.admin_user_smallInfo);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Bundle b = new Bundle();
            b.putInt("userID", userID);
            Navigation.findNavController(v).navigate(R.id.user_list_to_user_detail, b);
        }
    }

}
