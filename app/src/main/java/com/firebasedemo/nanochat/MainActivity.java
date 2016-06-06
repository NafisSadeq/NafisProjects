package com.firebasedemo.nanochat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    public static Firebase mFirebaseRef;
    public static Firebase ChatRef;
    public static Firebase UserRef;
    FirebaseListAdapter<ChatMessage> mListAdapter;
    EditText textEdit;
    Button sendButton;
    String user_name;
    //login fr_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        user_name = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Button loginButton;
        //fr_login=new login();
        mFirebaseRef = new Firebase("https://sweltering-torch-7725.firebaseio.com/");
        ChatRef=mFirebaseRef.child("ChatList");
        UserRef=mFirebaseRef.child("UserList");


        textEdit = (EditText) this.findViewById(R.id.text_edit);

        sendButton = (Button) this.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                ChatMessage message = new ChatMessage(user_name, text,dateFormat.format(cal.getTime()));
                System.out.println();
                ChatRef.push().setValue(message);
                textEdit.setText("");

                /*UserRef.push().setValue(new UserProfile("nafissadeq@gmail.com", "1205008", "Nafis Sadeq"));
                UserRef.push().setValue(new UserProfile("abdullahalzishan@yahoo.com","1205040","Abdullah Al Zishan"));
                UserRef.push().setValue(new UserProfile("fr_rahat@yahoo.com", "1205018", "Fazle Rahat"));
                UserRef.push().setValue(new UserProfile("monowaranjum@gmail.com", "1205022", "Monowar Anjum Rashik"));*/
            }
        });

        /*Firebase ref = new Firebase("https://sweltering-torch-7725.firebaseio.com/");
        ref.authWithPassword("nafissadeq@gmail.com", "12345", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });*/


        /*loginButton = (Button) this.findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr_login.showFirebaseLoginPrompt();
            }
        });*/


        final ListView listView = (ListView) this.findViewById(android.R.id.list);

        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                android.R.layout.two_line_list_item, ChatRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getdate()+"\n"+model.getText());

            }
        };
        listView.setAdapter(mListAdapter);




    }


    /*@Override
    protected Firebase getFirebaseRef() {
        return mFirebaseRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter.cleanup();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
    }*/




}



