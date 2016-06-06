package com.firebasedemo.nanochat;


        import android.content.Context;
        import android.graphics.Typeface;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

class d_ChatArrayAdapter extends ArrayAdapter<d_ChatMessage> {

    private TextView chatText;
    private List<d_ChatMessage> d_ChatMessageList = new ArrayList<d_ChatMessage>();
    private Context context;


    @Override
    public void add(d_ChatMessage object) {
        d_ChatMessageList.add(object);
        super.add(object);
    }

    public d_ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.d_ChatMessageList.size();
    }

    public d_ChatMessage getItem(int index) {
        return this.d_ChatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        d_ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(chatMessageObj.isFile){
            if (chatMessageObj.left) {
                row = inflater.inflate(R.layout.file_right, parent, false);
            }else{
                row = inflater.inflate(R.layout.file_left, parent, false);
            }
        }
        else{
            if (chatMessageObj.left) {
                row = inflater.inflate(R.layout.right, parent, false);
            }else{
                row = inflater.inflate(R.layout.left, parent, false);
            }
        }


        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        return row;
    }
}