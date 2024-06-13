package com.example.myandroidproject.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidproject.R;
import com.example.myandroidproject.models.Message;
import com.example.myandroidproject.models.MessageStorage;
import com.example.myandroidproject.utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> data;
    final static int VIEW_OWNER = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorChatOwner, contentChatOwner, timeChatOwner,
                authorChatOther, contentChatOther, timeChatOther;

        public ViewHolder(View view) {
            super(view);
            authorChatOwner = (TextView) view.findViewById(R.id.author_chat_owner);
            contentChatOwner = (TextView) view.findViewById(R.id.content_chat_owner);
            timeChatOwner = (TextView) view.findViewById(R.id.time_chat_owner);

            authorChatOther = (TextView) view.findViewById(R.id.author_chat_other);
            contentChatOther = (TextView) view.findViewById(R.id.content_chat_other);
            timeChatOther = (TextView) view.findViewById(R.id.time_chat_other);
        }

        public TextView getAuthorChatOwner() {
            return authorChatOwner;
        }

        public TextView getContentChatOwner() {
            return contentChatOwner;
        }

        public TextView getTimeChatOwner() {
            return timeChatOwner;
        }

        public TextView getAuthorChatOther() {
            return authorChatOther;
        }

        public TextView getContentChatOther() {
            return contentChatOther;
        }

        public TextView getTimeChatOther() {
            return timeChatOther;
        }
    }

    public MessageAdapter(List<Message> data) {
        this.data = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        View view;
        if (viewType == VIEW_OWNER){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_chat_owner, viewGroup, false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_chat_other, viewGroup, false);
        }

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        String fromFirstName = data.get(position).getFromFirstName();
        String content = data.get(position).getContent();
        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH).format(data.get(position).getCreateAt());

        System.out.println(fromFirstName);
        if (viewHolder.getItemViewType() == VIEW_OWNER){
            viewHolder.getAuthorChatOwner().setText(fromFirstName);
            viewHolder.getContentChatOwner().setText(content);
            viewHolder.getTimeChatOwner().setText(date);
        }else{
            viewHolder.getAuthorChatOther().setText(fromFirstName);
            viewHolder.getContentChatOther().setText(content);
            viewHolder.getTimeChatOther().setText(date);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isOwner()){
            return VIEW_OWNER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
