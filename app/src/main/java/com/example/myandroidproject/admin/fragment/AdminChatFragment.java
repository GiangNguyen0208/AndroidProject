package com.example.myandroidproject.admin.fragment;

import static com.example.myandroidproject.utilss.Constraint.URL_GET_ADMINS_MESSAGE;
import static com.example.myandroidproject.utilss.Constraint.URL_READ_MESSAGE;
import static com.example.myandroidproject.utilss.Constraint.URL_SEND_MESSAGE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.adapter.MessageAdapter;
import com.example.myandroidproject.models.Message;
import com.example.myandroidproject.models.MessageStorage;
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
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class AdminChatFragment extends Fragment {
    int ID_USER;
    int customerId;

    String tempOwnerName = null;

    List<Message> messages = new ArrayList<>();
    MessageAdapter adapter = new MessageAdapter(messages);

    ImageView chatExitBtnAdmin;
    TextView sendBtnAdmin;
    RecyclerView recyclerViewAdmin;
    TextInputEditText messageInputAdmin;
    RequestQueue queue;
    Timer fetchMessages;

    public AdminChatFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_admin, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            customerId = getArguments().getInt("customerId");
        ID_USER = SharedPreferencesUtils.getInt(SharedPreferencesUtils.STATE_USER_ID, requireContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        queue = Volley.newRequestQueue(requireContext());
        fetchMessages = new Timer();
        fetchMessages.schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("id", ID_USER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JsonObjectRequest rq = new JsonObjectRequest(Request.Method.POST, URL_READ_MESSAGE, jsonBody, resp -> {
                    try {
                        JSONArray res = resp.getJSONArray("data");
                        List<Message> temp = new ArrayList<>();
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject jsonObject = res.getJSONObject(i);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
                            temp.add(new Message(jsonObject.getInt("from"), jsonObject.getInt("to"), jsonObject.getString("fromFirstName"), jsonObject.getString("toFirstName"), jsonObject.getString("content"), formatter.parse(jsonObject.getString("createAt")), jsonObject.getInt("from") == ID_USER ? true : false));
                        }
                            //Filter message relate with customerId current
                            temp.removeIf(message ->{
                                //Get first name (for virtual message on first request)
                                boolean isCustomer = message.getFrom() == customerId || message.getTo() == customerId;
                                if (isCustomer && tempOwnerName == null)
                                    if (message.getFrom() == ID_USER){
                                        tempOwnerName = message.getFromFirstName();
                                    }else{
                                        tempOwnerName = message.getToFirstName();
                                    }

                                return !isCustomer;
                            });

                            if (temp.size() > messages.size()) {
                                messages.clear();
                                messages.addAll(temp);
                                messages.sort(Message::sortOldToNew);
                                adapter.notifyDataSetChanged(); //Only change SOMETHING SHOULD BE CHANGEEEEEEEEEEEEEE!
                                recyclerViewAdmin.scrollToPosition(messages.size() - 1);
                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
                queue.add(rq);
                System.out.println("fetch at admin");
            }
        }, 100, 10000);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chatExitBtnAdmin = view.findViewById(R.id.chatExitBtnAdmin);
        recyclerViewAdmin = view.findViewById(R.id.admin_chat_recycleview);
        sendBtnAdmin = view.findViewById(R.id.sendBtnAdmin);
        messageInputAdmin = view.findViewById(R.id.messageInputAdmin);

        recyclerViewAdmin.setAdapter(adapter);
        recyclerViewAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));

        messageInputAdmin.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Editable e = messageInputAdmin.getText();
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

        sendBtnAdmin.setOnClickListener(v -> {
            Editable e = messageInputAdmin.getText();
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

        chatExitBtnAdmin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.fragment_admin_chat_storage);
        });
    }

    private void sendMessage(String msg) {
        //Create a virtual send message
        Message virtualMessage = new Message(ID_USER, -1, tempOwnerName, null, msg, Date.from(Instant.now()), true);
        virtualMessage.isVirtualMessage = true;
        messages.add(virtualMessage);
        adapter.notifyItemInserted(messages.size());
        recyclerViewAdmin.scrollToPosition(messages.size() - 1);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("content", msg);
            jsonBody.put("from", ID_USER);
            jsonBody.put("to", customerId);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Gửi tin thất bại", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest rq1 = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, jsonBody, resp1 -> {
            messages.remove(virtualMessage);
        }, Throwable::printStackTrace);
        queue.add(rq1);
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