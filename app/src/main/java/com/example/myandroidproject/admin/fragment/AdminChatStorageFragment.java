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
    int ID_USER;
    Timer fetchMessages = new Timer();
    Map<Integer, String> customers = new HashMap<>();

    public AdminChatStorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
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
                System.out.println("fetch at adminstorage");
            }
        }, 100, 10000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID_USER = SharedPreferencesUtils.getInt(SharedPreferencesUtils.STATE_USER_ID, requireContext());
        queue = Volley.newRequestQueue(requireContext());
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
                Navigation.findNavController(this.getView()).navigate(R.id.action_global_admin_chat, bundle);
        });

        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null){
            queue.cancelAll(this);
        }
        fetchMessages.cancel();;
        System.out.println("huy diet");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queue != null){
            queue.cancelAll(this);
        }
        fetchMessages.cancel();
    }

}