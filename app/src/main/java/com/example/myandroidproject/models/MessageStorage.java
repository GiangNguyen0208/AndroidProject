package com.example.myandroidproject.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageStorage{
    private List<Message> messages;
    private Message lastMessage;
    private String fromFirstName;
    public MessageStorage(List<Message> messages, String fromFirstName){
        this.messages = messages;
        this.fromFirstName = fromFirstName;
        this.setLastMessage();
    }

    public MessageStorage(){
        this.messages =new ArrayList<>();
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage() {
        this.messages.sort(Message::sortOldToNew);
        this.lastMessage = this.messages.get(this.messages.size()-1);
    }

    public void add(Message message){
        messages.add(message);
        setLastMessage();
    }

    public void addAll(List<Message> messages){
        messages.addAll(messages);
        setLastMessage();
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public void setFromFirstName(String fromFirstName) {
        this.fromFirstName = fromFirstName;
    }

    public static int sortOldToNew(MessageStorage m1, MessageStorage m2){
        return (int)m2.getLastMessage().getCreateAt().getTime() - (int)m1.getLastMessage().getCreateAt().getTime();
    }
}
