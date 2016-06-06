package com.firebasedemo.nanochat;


import com.sinch.android.rtc.SinchError;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VideoLoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    //private Button mLoginButton;
    //private EditText mLoginName;
    private ProgressDialog mSpinner;
    public static String EXTRA_MESSAGE="Username";
    public String user_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        user_name = intent.getStringExtra(OptionActivity.EXTRA_MESSAGE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

       /*mLoginName = (EditText) findViewById(R.id.loginName);

       mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);



        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();

            }
        });*/
    }

    @Override
    protected void onServiceConnected() {
        //mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);

        loginClicked();
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked() {
        //String userName = mLoginName.getText().toString();
        String userName=user_name;


        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }


    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, OnlineActivity.class);
        String message = user_name;
        mainActivity.putExtra(EXTRA_MESSAGE, message);
        finish();
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Connecting");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}
