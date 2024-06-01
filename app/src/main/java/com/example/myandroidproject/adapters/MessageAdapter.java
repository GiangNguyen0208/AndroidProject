package com.example.myandroidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myandroidproject.R;
import com.example.myandroidproject.models.ChatMessage;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private List<ChatMessage> chatMessages;

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<ChatMessage> chatMessages) {
        super(context, resource, chatMessages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = this.getItem(position);

        //Chat paragram for sender
            if (message.isSender())
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.chat_paragram_sender_layout, parent, false);
            else
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.chat_paragram_other_layout, parent, false);

        TextView author = convertView.findViewById(R.id.chat_paragram_author);
        author.setText(message.getAuthor());

        TextView content = convertView.findViewById(R.id.chat_paragram_content);
        content.setText(message.getContent());

        return convertView;
    }
}
