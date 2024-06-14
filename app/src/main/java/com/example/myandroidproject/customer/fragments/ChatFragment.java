package com.example.myandroidproject.customer.fragments;

import static com.example.myandroidproject.utilss.Constraint.URL_GET_ADMINS_MESSAGE;
import static com.example.myandroidproject.utilss.Constraint.URL_READ_MESSAGE;
import static com.example.myandroidproject.utilss.Constraint.URL_SEND_MESSAGE;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.account.LoginActivity;
import com.example.myandroidproject.admin.adapter.MessageAdapter;
import com.example.myandroidproject.models.Message;
import com.example.myandroidproject.utils.SharedPreferencesUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ChatFragment extends Fragment {
    int ID_USER;
    List<Integer> admins = new ArrayList<>();
    String tempOwnerName;
    ImageView chatExitBtn;
    TextView sendBtn;
    RecyclerView recyclerView;
    TextInputEditText messageInput;
    RequestQueue queue;
    List<Message> messages = new ArrayList<>();
    MessageAdapter adapter = new MessageAdapter(messages);
    Timer fetchMessages = new Timer();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ID_USER = SharedPreferencesUtils.getInt("id_user", requireContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        queue = Volley.newRequestQueue(requireContext());
        getAdmins();
        fetchMessages = new Timer();
        fetchMessages.schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("id", ID_USER);
                }catch (Exception e){
                    e.printStackTrace();
                }
                JsonObjectRequest rq = new JsonObjectRequest(Request.Method.POST, URL_READ_MESSAGE, jsonBody, resp->{

                    try {
                        JSONArray res = resp.getJSONArray("data");
                        List<Message> temp = new ArrayList<>();
                        for (int i=0; i<res.length(); i++) {
                            JSONObject jsonObject = res.getJSONObject(i);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
                            if (tempOwnerName == null){
                                if(jsonObject.getInt("from")== ID_USER)
                                    tempOwnerName = jsonObject.getString("fromFirstName");
                                else
                                    tempOwnerName = jsonObject.getString("toFirstName");
                            }
                            temp.add(new Message(jsonObject.getInt("from"), jsonObject.getInt("to"), jsonObject.getString("fromFirstName"), jsonObject.getString("toFirstName"), jsonObject.getString("content"), formatter.parse(jsonObject.getString("createAt")), jsonObject.getInt("from") == ID_USER));
                        }
                            if (temp.size() > messages.size()){
                                int start = messages.size()-1;
                                int offset = temp.size() - messages.size(); //Check how many new messages
                                messages.clear();
                                messages.addAll(temp);
                                messages.sort(Message::sortOldToNew);
                                adapter.notifyItemRangeInserted(start, offset); //Only change SOMETHING SHOULD BE CHANGEEEEEEEEEEEEEE!
                                recyclerView.scrollToPosition(messages.size() - 1);
                            }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }, err->{

                });

                queue.add(rq);
            }
        }, 100, 10000);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chatExitBtn = view.findViewById(R.id.chatExitBtn);
        recyclerView = view.findViewById(R.id.customer_chat_recycleview);
        sendBtn = view.findViewById(R.id.sendBtn);
        messageInput = view.findViewById(R.id.messageInput);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        messageInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Editable e = messageInput.getText();
                    if (e == null) {
                        Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    String msg = e.toString().trim();

                    if (msg.isEmpty() || msg.isBlank()) {
                        Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    sendMessage(msg);
                    e.clear();
                }
            }
            return false;
        });

        chatExitBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_global_customer_support);
        });


        sendBtn.setOnClickListener(v -> {
            Editable e = messageInput.getText();
            if (e == null) {
                Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                return;
            }
            String msg = e.toString().trim();

            if (msg.isEmpty() || msg.isBlank()) {
                Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                return;
            }
            sendMessage(msg);
            e.clear();
        });


    }

    private void getAdmins(){
        if (admins.size() <= 0){
            JsonObjectRequest rq = new JsonObjectRequest(Request.Method.GET, URL_GET_ADMINS_MESSAGE, null, resp->{
                try {
                    JSONArray res = resp.getJSONArray("data");
                    List<Integer>  temp = new ArrayList<>();
                    for (int i=0; i<res.length(); i++){
                        temp.add(res.getInt(i));
                    }
                    admins.addAll(temp);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }, Throwable::printStackTrace);
            queue.add(rq);
        }
    }

    private void sendMessage(String msg){
        if (admins.size() <= 0){
            getAdmins();
            Toast.makeText(requireContext(), "Not found an admin for support message!", Toast.LENGTH_LONG).show();
            return;
        }
        Message virtualMessage = new Message(ID_USER, -1, tempOwnerName, null, msg, Date.from(Instant.now()), true);
        virtualMessage.isVirtualMessage = true;
        messages.add(virtualMessage);
        adapter.notifyItemInserted(messages.size());
        recyclerView.scrollToPosition(messages.size() - 1);

        for (Integer adminId : admins){
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("content", msg);
                jsonBody.put("from", ID_USER);
                jsonBody.put("to", adminId);
            }catch (Exception e){
                Toast.makeText(getActivity(), "Gửi tin thất bại", Toast.LENGTH_LONG).show();
            }

            JsonObjectRequest rq1 = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, jsonBody, resp1->{
                messages.remove(virtualMessage);
            }, err->{err.printStackTrace();});
            queue.add(rq1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_customer, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null)
            queue.cancelAll(this);

        if (fetchMessages != null)
            fetchMessages.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queue != null)
            queue.cancelAll(this);

        if (fetchMessages != null)
            fetchMessages.cancel();
    }
}