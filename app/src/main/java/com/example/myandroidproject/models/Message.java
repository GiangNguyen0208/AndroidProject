package com.example.myandroidproject.models;

import java.util.Date;

public class Message {
    private int from, to;
    private String fromFirstName, toFirstName;
    private String content;
    private Date createAt;
    private boolean isOwner;

    public Message(int from, int to, String fromFirstName, String toFirstName, String content, Date createAt, boolean isOwner) {
        this.from = from;
        this.to = to;
        this.fromFirstName = fromFirstName;
        this.toFirstName = toFirstName;
        this.content = content;
        this.createAt = createAt;
        this.isOwner = isOwner;
    }

    public String getContent(int maxLength) {
        String res = null;
        if (content.length()< maxLength-3 || maxLength==0){
            res = content.substring(0);
        }else
            res = content.substring(0, maxLength-3).concat("...");
        return res;
    }

    public String getContent(){
        return getContent(0);
    }


    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public void setFromFirstName(String fromFirstName) {
        this.fromFirstName = fromFirstName;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public void setToFirstName(String toFirstName) {
        this.toFirstName = toFirstName;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public static int sortOldToNew(Message m1, Message m2){
        return (int)m1.getCreateAt().getTime() - (int)m2.getCreateAt().getTime();
    }
}
