package com.firebasedemo.nanochat;

import java.util.ArrayList;
import java.util.List;

public class d_status {
    public String message;

    public d_status(boolean left, String message) {
        super();

        this.message = message;
    }
}

class status {

    private String name;
    private String text;
    private String date;
    private int like_count;
    private String ref;
    List likers;

    public status(String name, String text,String dt,String ref) {
        this.name = name;
        this.text = text;
        this.date=dt;
        this.like_count= 0;
        this.ref=ref;
        this.likers = new ArrayList();


    }
    public status() {
        this.like_count= 0;
        this.likers = new ArrayList();
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
    public int getLike_count(){
        return like_count;
    }
    public String getRef(){
        return ref;
    }

    public void add_like(String name){
        likers.add(name);
        like_count++;
    }

    public List getLikers(){
        return likers;
    }
    public void setLike_count(){
        if(likers!=null)
        like_count=likers.size();
        else
            like_count=0;
    }
    public void setLikers(List l){
        likers=l;
    }
    public void inc_like(){

        like_count++;
    }



}