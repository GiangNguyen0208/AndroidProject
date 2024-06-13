package com.example.myandroidproject.admin.fragment;

import static com.example.myandroidproject.utilss.Constraint.URL_READ_MESSAGE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myandroidproject.R;
import com.example.myandroidproject.admin.adapter.MessageStorageAdapter;
import com.example.myandroidproject.models.Message;
import com.example.myandroidproject.models.MessageStorage;
import com.example.myandroidproject.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class AdminChatStorageFragment extends Fragment {
    List<MessageStorage> messageStorages = new ArrayList<>();
    List<Message> allMessages = new ArrayList<>();
    MessageStorageAdapter adapter = new MessageStorageAdapter(messageStorages);
    RequestQueue queue;
    int ID_USER = 1;
    Timer timer = new Timer(),
    timer2 = new Timer();
    Map<Integer, String> customers = new HashMap<>();

    public AdminChatStorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        ID_USER = sharedPreferences.getInt("id", 1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_chat_storage, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.admin_chat_storage_recycleview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        adapter.setBind((position)->{
                Bundle bundle = savedInstanceState;
                int customer = messageStorages.get(position).getLastMessage().getFrom()==ID_USER?messageStorages.get(position).getLastMessage().getTo():messageStorages.get(position).getLastMessage().getFrom();
                if (bundle == null){
                    bundle = new Bundle();
                }
                bundle.putInt("customerId", customer);
                Navigation.findNavController(getView()).navigate(R.id.action_global_admin_chat, bundle);
        });

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
                        boolean newChat = false;

                        for (int i=0; i<res.length(); i++){
                            JSONObject jsonObject = res.getJSONObject(i);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
                            int from = jsonObject.getInt("from"),
                                    to = jsonObject.getInt("to");
                            String fromFirstName = jsonObject.getString("fromFirstName"),
                                    toFirstName = jsonObject.getString("toFirstName"),
                                    content = jsonObject.getString("content");
                            Date createAt = formatter.parse(jsonObject.getString("createAt"));
                            boolean isOwner = jsonObject.getInt("from")==ID_USER;
                            if (from != ID_USER){
                                customers.put(from, fromFirstName);
                            }else{
                                customers.put(to, toFirstName);
                            }

                            temp.add(new Message(from, to, fromFirstName, toFirstName, content, createAt, isOwner));
                            if (temp.size() > allMessages.size()){
                                allMessages.clear();
                                allMessages.addAll(temp);
                                newChat = true;
                            }
                        }

                        if (!newChat)
                            return;
                        List<MessageStorage> tempStorages = new ArrayList<>();
                        if (!customers.isEmpty() && !allMessages.isEmpty()){
                            for (Map.Entry<Integer, String> cI: customers.entrySet()){
                                List<Message> ms = new ArrayList<>();
                                for (Message m: allMessages){
                                    if (m.getFrom() == cI.getKey() || m.getTo() == cI.getKey()){
                                        ms.add(m);
                                    }
                                }
                                ms.sort(Message::sortOldToNew);
                                tempStorages.add(new MessageStorage(ms, cI.getValue()));
                            }
                                messageStorages.clear();
                                messageStorages.addAll(tempStorages);
                                messageStorages.sort(MessageStorage::sortOldToNew);
                                adapter.notifyDataSetChanged();

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }, err->{
                    Toast.makeText(getActivity(), "Load messages fail", Toast.LENGTH_LONG).show();
                });

                queue.add(rq);
            }
        }, 100, 2000);



        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null){
            queue.cancelAll(this);
        }
        timer.cancel();
        timer2.cancel();
    }
}