package com.firebasedemo.nanochat;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class d_statusArrayAdapter extends ArrayAdapter<d_status> {

    private TextView chatText;
    private List<d_status> d_statusList = new ArrayList<d_status>();
    private Context context;

    @Override
    public void add(d_status object) {
        d_statusList.add(object);
        super.add(object);
    }

    public d_statusArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.d_statusList.size();
    }

    public d_status getItem(int index) {
        return this.d_statusList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        d_status statusObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.d_status, parent, false);
        chatText = (TextView) row.findViewById(R.id.textView5);
        chatText.setText(statusObj.message);
        return row;
    }
}