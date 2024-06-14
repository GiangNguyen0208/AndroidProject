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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ChatFragment extends Fragment {
    int ID_USER = 3;
    final Set<Integer> admins = new HashSet<>();
    ImageView chatExitBtn;
    TextView sendBtn;
    RecyclerView recyclerView;
    TextInputEditText messageInput;
    RequestQueue queue;
    List<Message> messages = new ArrayList<>();
    MessageAdapter adapter = new MessageAdapter(messages);
    Timer timer = new Timer();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        ID_USER = sharedPreferences.getInt("id_user", -1);

        System.out.println(ID_USER);
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
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    Editable e = messageInput.getText();
                    if (e == null){
                        Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    String msg = e.toString().trim();

                    if (msg.isEmpty() || msg.isBlank()){
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


        sendBtn.setOnClickListener(v->{
            Editable e = messageInput.getText();
            if (e == null){
                Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                return;
            }
            String msg = e.toString().trim();

            if (msg.isEmpty() || msg.isBlank()){
                Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                return;
            }

            sendMessage(msg);
            e.clear();
        });


    }

    private void sendMessage(String msg){
        if (admins.size() <= 0){
            JsonObjectRequest rq = new JsonObjectRequest(Request.Method.GET, URL_GET_ADMINS_MESSAGE, null, resp->{
                try {
                    JSONArray res = resp.getJSONArray("data");
                    for (int i=0; i<res.length(); i++){
                        admins.add(res.getInt(i));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }, err->{err.printStackTrace();});
            queue.add(rq);
        }

        for (Integer adminId : admins){
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("content", msg);
                jsonBody.put("from", ID_USER);
                jsonBody.put("to", adminId);
            }catch (Exception e){
                Toast.makeText(getActivity(), "Gửi tin thất bại", Toast.LENGTH_LONG).show();
            }

            JsonObjectRequest rq1 = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, jsonBody, resp1->{}, err->{err.printStackTrace();});
            queue.add(rq1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", ID_USER);
        }catch (Exception e){
            e.printStackTrace();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JsonObjectRequest rq = new JsonObjectRequest(Request.Method.POST, URL_READ_MESSAGE, jsonBody, resp->{

                    try {
                        JSONArray res = resp.getJSONArray("data");
                        List<Message> temp = new ArrayList<>();
                        for (int i=0; i<res.length(); i++){
                            JSONObject jsonObject = res.getJSONObject(i);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
                            temp.add(new Message(jsonObject.getInt("from"), jsonObject.getInt("to"), jsonObject.getString("fromFirstName"), jsonObject.getString("toFirstName"), jsonObject.getString("content"), formatter.parse(jsonObject.getString("createAt")), jsonObject.getInt("from")==ID_USER?true:false));
                            temp.sort(Message::sortOldToNew);

                            if (temp.size() > messages.size()){
                                messages.clear();
                                messages.addAll(temp);
                            }
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messages.size()-1);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }, err->{

                });

                queue.add(rq);
            }
        }, 100, 2000);


        return inflater.inflate(R.layout.fragment_chat_customer, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null){
            queue.cancelAll(this);
        }
        timer.cancel();
    }
}