package com.example.myandroidproject.models;

public class ChatMessage {
    private String author, content;
    private boolean isSender;

    public ChatMessage(String author, String content) {
        this(author, content, true);
    }

    public ChatMessage(String author, String content, boolean isSender) {
        this.author = author;
        this.content = content;
        this.isSender = isSender;
    }

    public String getAuthor() {
        if (this.author == null)
            this.author = "No name";
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        if (this.content == null)
            this.content = "No content";
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }
}
