package com.firebasedemo.nanochat;

public class d_ChatMessage {
    public boolean left;
    public  boolean isFile;
    public String message;


    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public d_ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
        this.isFile=false;


    }


}

