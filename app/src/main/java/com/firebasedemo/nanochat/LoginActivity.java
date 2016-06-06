package com.firebasedemo.nanochat;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.sinch.android.rtc.SinchError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Android login screen Activity
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>{

    private static final String DUMMY_CREDENTIALS = "user@test.com:hello";

    public static ArrayList<UserProfile> users=new ArrayList<UserProfile>();

    public static Firebase mFirebaseRef;
    public static Firebase UserRef;
    public static Firebase OnlineRef;
    public static Firebase myRef=null;
    public  static HashMap<String,String> map=new HashMap<>();


    private UserLoginTask userLoginTask = null;
    private View loginFormView;
    private View progressView;
    private AutoCompleteTextView emailTextView;
    private EditText passwordTextView;
    private TextView signUpTextView;
    public String userName="";
    private String onlinekey="";
    public static String EXTRA_MESSAGE="Username";
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        Firebase.setAndroidContext(this);

        mFirebaseRef = new Firebase("https://sweltering-torch-7725.firebaseio.com/");

        UserRef=mFirebaseRef.child("UserList");
        OnlineRef=mFirebaseRef.child("OnlineList");




        UserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                UserProfile facts = snapshot.getValue(UserProfile.class);
                users.add(new UserProfile(facts.getUserName(), facts.getPassword(), facts.getName()));
                //mFirebaseRef.child("Mapping").child(facts.getUserName()).setValue(facts.getName());
                map.put(facts.getUserName(),facts.getName());
                //System.out.println(snapshot.getKey() + " was " + facts.getHeight() + " meters tall");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            // ....
        });

        emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
        loadAutoComplete();

        passwordTextView = (EditText) findViewById(R.id.password);
        passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    initLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin();
            }
        });



        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        //adding underline and link to signup textview
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(signUpTextView, Linkify.ALL);

        signUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginActivity", "Sign Up Activity activated.");
                // this is where you should start the signup Activity
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignUp.class));
            }
        });
    }



    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Validate Login form and authenticate.
     */
    public void initLogin() {
        if (userLoginTask != null) {
            return;
        }

        emailTextView.setError(null);
        passwordTextView.setError(null);

        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) ) {
            passwordTextView.setError(getString(R.string.field_required));
            focusView = passwordTextView;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailTextView.setError(getString(R.string.field_required));
            focusView = emailTextView;
            cancelLogin = true;
        } else if (!isEmailValid(email)) {
            emailTextView.setError(getString(R.string.invalid_email));
            focusView = emailTextView;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            userLoginTask = new UserLoginTask(email, password);
            userLoginTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //add your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //add your own logic
        return true;
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if(myRef!=null){
            myRef.removeValue();
        }

        super.onDestroy();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailTextView.setAdapter(adapter);
    }




    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Async Login Task to authenticate
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String emailStr;
        private final String passwordStr;

        UserLoginTask(String email, String password) {
            emailStr = email;
            passwordStr = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //using a local dummy credentials store to authenticate
            for(int i=0;i<users.size();i++){
                if(emailStr.equals(users.get(i).getUserName())&&passwordStr.equals(users.get(i).getPassword())){
                    //userName=users.get(i).getName();

                    myRef=OnlineRef.child(users.get(i).getUserName());
                    myRef.setValue(users.get(i).getUserName());


                    return true;
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;
            //stop the progress spinner
            showProgress(false);

            if (success) {
                //  login success and move to main Activity here.
                EditText editText = (EditText) findViewById(R.id.email);
                userName = editText.getText().toString();
                Intent intent = new Intent(LoginActivity.this,d_homeActivity.class);

                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                //finish();
                startActivity(intent);
                //setContentView(R.layout.options);


            } else {
                // login failure
                passwordTextView.setError(getString(R.string.incorrect_password));
                passwordTextView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            showProgress(false);
        }






    }
}