package com.firebasedemo.nanochat;

/**
 * Created by Nafis on 6/5/2016.
 */
public class FileMsg {
    String msg;
    String from;
    String to;
    String time;
    String text;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;

    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public FileMsg() {
    }

    public FileMsg(String msg, String from, String to, String time, String text, String url) {
        this.msg = msg;
        this.from = from;
        this.to = to;
        this.time = time;
        this.text = text;
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}
