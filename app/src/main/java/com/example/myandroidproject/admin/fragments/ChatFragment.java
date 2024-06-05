package com.example.myandroidproject.admin.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myandroidproject.R;
import com.example.myandroidproject.adapters.MessageAdapter;
import com.example.myandroidproject.models.ChatMessage;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    ImageView chatExitBtn;
    TextView sendBtn;
    ListView listView;
    TextInputEditText messageInput;
    List<ChatMessage> chatMessages = new ArrayList<>();
    MessageAdapter messageAdapter;
    String SENDER = "Huy pham";

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chatExitBtn = view.findViewById(R.id.chatExitBtn);
        listView = view.findViewById(R.id.listMessage);
        sendBtn = view.findViewById(R.id.sendBtn);
        messageInput = view.findViewById(R.id.messageInput);
        messageAdapter = new MessageAdapter(view.getContext(), R.id.listMessage, chatMessages);

        //load message here
        chatMessages.add(new ChatMessage("Five Bros", "Hi there, if you need a help, please leave a message and wait a minutes :>", false));


        chatExitBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_global_support_fragment);
        });

        listView.setAdapter(messageAdapter);


        sendBtn.setOnClickListener(v->{
            Editable e = messageInput.getText();
            if (e == null)
                return;
            String msg = e.toString();
            if (msg.isEmpty() || msg.isBlank())
                return;
            sendMessage(msg);
            e.clear();
        });


    }

    private void sendMessage(String msg){
        ChatMessage newMessage = new ChatMessage(SENDER, msg);
        chatMessages.add(newMessage);
        messageAdapter.notifyDataSetChanged();
    }
}