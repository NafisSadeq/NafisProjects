package com.firebasedemo.nanochat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Nafis on 3/20/2016.
 */
public class OptionActivity extends Activity {

    public static String EXTRA_MESSAGE="Username";
    String user_name;

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        user_name = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

    }


    public void gotoHome(View view){
        Intent intent = new Intent(OptionActivity.this,com.firebasedemo.nanochat.d_homeActivity.class);

        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void gotoCall(View view){
        Intent intent = new Intent(OptionActivity.this,com.firebasedemo.nanochat.VideoLoginActivity.class);
        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void gotoChat(View view){
        Intent intent = new Intent(OptionActivity.this,OnlineActivity2.class);
        intent.putExtra(EXTRA_MESSAGE,user_name);
        startActivity(intent);

    }
}
