package com.firebasedemo.nanochat;

public class ChatMessage {
    private String name;
    private String text;
    private String date;

    public ChatMessage() {
        // necessary for Firebase's deserializer
    }
    public ChatMessage(String name, String text,String dt) {
        this.name = name;
        this.text = text;
        this.date=dt;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getdate() {
        return date;
    }
}