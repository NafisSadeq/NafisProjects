package com.firebasedemo.nanochat;

public class Message{
    String from;
    String to;
    String msg;
    String time;

    public Message(String to, String from, String msg, String time) {
        this.to = to;
        this.from = from;
        this.msg = msg;
        this.time = time;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
