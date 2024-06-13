package com.example.myandroidproject.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.models.MessageStorage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageStorageAdapter extends RecyclerView.Adapter<MessageStorageAdapter.ViewHolder> {
    private List<MessageStorage> data;
    private OnClickListener bind;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorChatStorage, contentChatStorage,timeChatStorage;

        public ViewHolder(View view, OnClickListener clickListener) {
            super(view);
            authorChatStorage = (TextView) view.findViewById(R.id.author_chat_storage);
            contentChatStorage = (TextView) view.findViewById(R.id.content_chat_storage);
            timeChatStorage = (TextView) view.findViewById(R.id.time_chat_storage);
            view.setOnClickListener(v->{
                clickListener.onClick(getAdapterPosition());
            });
        }

        public TextView getAuthorChatStorage() {
            return authorChatStorage;
        }

        public TextView getContentChatStorage() {
            return contentChatStorage;
        }

        public TextView getTimeChatStorage() {
            return timeChatStorage;
        }

    }

    public MessageStorageAdapter(List<MessageStorage> data) {
        this.data = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chat_storage, viewGroup, false);


        return new ViewHolder(view, this.bind);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAuthorChatStorage().setText(data.get(position).getFromFirstName());
        viewHolder.getContentChatStorage().setText(data.get(position).getLastMessage().getContent(30));
        viewHolder.getTimeChatStorage().setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH).format(data.get(position).getLastMessage().getCreateAt()));

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void setBind(OnClickListener bind){
        this.bind = bind;
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position);
    }
}
