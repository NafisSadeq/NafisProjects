package com.firebasedemo.nanochat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
import com.sinch.android.rtc.calling.Call;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OnlineActivity2 extends BaseActivity {

    public static Firebase mFirebaseRef;
    public static ArrayList<UserProfile> users=new ArrayList<UserProfile>();
    public static Firebase OnlineRef;
    FirebaseListAdapter<UserProfile> mListAdapter;
    public static String EXTRA_MESSAGE="Username";
    public HashMap<Integer,String> rev_map=new HashMap<>();
    Typeface myTypeface;
    ListView listView;

    String user_name="";
    public  static  String my_tag="my_tag";
    public static String friend_tag="friend_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        user_name = intent.getStringExtra(OptionActivity.EXTRA_MESSAGE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlineusers2);
        Firebase.setAndroidContext(this);
        System.out.println("Testing terminal");
        mFirebaseRef = new Firebase("https://sweltering-torch-7725.firebaseio.com/");

        OnlineRef=mFirebaseRef.child("UserList");

        Button button = (Button) findViewById(R.id.button3);
        button.setBackgroundResource(R.drawable.round_button2);
        //button.setTextColor(Color.parseColor("#ff7e51c2"));
        myTypeface = Typeface.createFromAsset(getAssets(), "myFont.ttf");

        listView = (ListView) this.findViewById(android.R.id.list);

        mListAdapter=new FirebaseListAdapter<UserProfile>(this,UserProfile.class,R.layout.list_item2,OnlineRef) {
            @Override
            protected void populateView(View view, UserProfile s, int i) {
                users.add(s);
                rev_map.put(i,s.getUserName());
                ((TextView)view.findViewById(R.id.list_text)).setText(s.getName());
                ((TextView)view.findViewById(R.id.list_text)).setTypeface(myTypeface);
                if(s.equals(user_name))((TextView)view.findViewById(R.id.list_text)).setTextColor(Color.BLACK);
                else ((TextView)view.findViewById(R.id.list_text)).setTextColor(Color.BLACK);

            }
        };
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users );

        listView.setAdapter(mListAdapter);
        //listView.setAdapter(arrayAdapter);
        //listView.setOnItemClickListener(new ListClickHandler());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView listText = (TextView) view.findViewById(R.id.list_text);
                String userName =  rev_map.get(position);
                if(!userName.equals(user_name)){


                    Intent intent = new Intent(OnlineActivity2.this, d_ChatActivity.class);
                    intent.putExtra(my_tag, user_name);
                    intent.putExtra(friend_tag,users.get(position).getUserName());
                    startActivity(intent);
                }


            }


        });




    }

    @Override
    protected void onServiceConnected() {
        //TextView userName = (TextView) findViewById(R.id.loggedInName);
        //userName.setText(getSinchServiceInterface().getUserName());
        //listView.setOnItemClickListener(new ListClickHandler());
        //mCallButton.setEnabled(true);
    }




    @Override
    protected void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
        //super.onDestroy();
        mListAdapter.cleanup();
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.list_text);
            String userName = listText.getText().toString();
            Call call = getSinchServiceInterface().callUserVideo(userName);
            String callId = call.getCallId();
            System.out.println(userName);
            Intent intent = new Intent(OnlineActivity2.this, d_ChatActivity.class);
            intent.putExtra(my_tag, user_name);
            intent.putExtra(friend_tag,userName);
            startActivity(intent);

        }

    }

    public void gotoHome(View view){
        Intent intent = new Intent(getApplicationContext(),com.firebasedemo.nanochat.d_homeActivity.class);

        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void gotoCall(View view){
        Intent intent = new Intent(getApplicationContext(),com.firebasedemo.nanochat.VideoLoginActivity.class);
        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void gotoChat(View view){
        /*Intent intent = new Intent(getApplicationContext(),OnlineActivity2.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);*/

    }

    public void logout(View view){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        mFirebaseRef.child("OnlineList").child(user_name).removeValue();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


}





