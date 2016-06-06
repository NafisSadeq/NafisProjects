package com.firebasedemo.nanochat;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class d_homeActivity extends Activity {
    private static final String TAG = "homeActivity";
    public static String EXTRA_MESSAGE="Username";
    private ProgressDialog mSpinner;

    private d_statusArrayAdapter d_statusArrayAdapter;
    FirebaseListAdapter<status> mListAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    public static Firebase mFirebaseRef;
    public static Firebase StatusRef;
    public static Firebase UserRef;
    public  static  Firebase update_status;
    String user_name;
    EditText textEdit;
    Button sendButton,likeButton;
    Typeface myTypeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        user_name = intent.getStringExtra(OptionActivity.EXTRA_MESSAGE);
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.d_home);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://sweltering-torch-7725.firebaseio.com/");
        StatusRef=mFirebaseRef.child("StatusList");
        update_status=mFirebaseRef.child("Updated_Status");
        UserRef=StatusRef.child(user_name);
        myTypeface = Typeface.createFromAsset(getAssets(), "Vera.ttf");



        textEdit = (EditText) this.findViewById(R.id.text_edit);

        sendButton = (Button) this.findViewById(R.id.send_button);
        likeButton=(Button) this.findViewById(R.id.button);

        Button button = (Button) findViewById(R.id.button2);
        button.setBackgroundResource(R.drawable.round_button2);
        //button.setTextColor(Color.parseColor("#ff7e51c2"));

        Log.v(TAG, "testing log");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = textEdit.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("   MMMM dd, h:mm a");
                Calendar cal = Calendar.getInstance();
                Firebase status_ref=UserRef.push();
                status status_ob=new status(user_name,text,dateFormat.format(cal.getTime()),status_ref.toString());
                status_ref.setValue(status_ob);

                textEdit.setText("");
            }
        });

        final ListView listView = (ListView) this.findViewById(android.R.id.list);

        for(int i=0;i<LoginActivity.users.size();i++){
            final  int index=i;
            Query queryRef = StatusRef.child(LoginActivity.users.get(i).getUserName()).orderByValue().limitToLast(1);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    //System.out.println("The " + snapshot.getKey() + " dinosaur's score is " + snapshot.getValue());
                    status status_ob= new status(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("text").getValue().toString(),dataSnapshot.child("date").getValue().toString(),dataSnapshot.child("ref").getValue().toString());
                    status_ob.setLikers((List) dataSnapshot.child("likers").getValue());
                    status_ob.setLike_count();
                    Firebase this_user=update_status.child(LoginActivity.users.get(index).getUserName());

                    this_user.setValue(status_ob);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    status status_ob= new status(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("text").getValue().toString(),dataSnapshot.child("date").getValue().toString(),dataSnapshot.child("ref").getValue().toString());

                    status_ob.setLikers((List) dataSnapshot.child("likers").getValue());
                    status_ob.setLike_count();
                    Firebase this_user=update_status.child(LoginActivity.users.get(index).getUserName());
                    this_user.setValue(status_ob);

                }


                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    status status_ob= new status(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("text").getValue().toString(),dataSnapshot.child("date").getValue().toString(),dataSnapshot.child("ref").getValue().toString());

                    status_ob.setLikers((List) dataSnapshot.child("likers").getValue());
                    status_ob.setLike_count();
                    Firebase this_user=update_status.child(LoginActivity.users.get(index).getUserName());
                    this_user.setValue(status_ob);

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
                // ....
            });
        }


        mListAdapter = new FirebaseListAdapter<status>(this, status.class,
                R.layout.d_status, update_status) {
            @Override
            protected void populateView(View v, final status model, int position) {

                ((TextView)v.findViewById(R.id.title)).setText(LoginActivity.map.get(model.getName()));
                ((TextView)v.findViewById(R.id.title)).setTypeface(myTypeface);
                ((TextView)v.findViewById(R.id.textView5)).setText(model.getText());
                ((TextView)v.findViewById(R.id.textView5)).setTypeface(myTypeface);
                ((TextView)v.findViewById(R.id.duration)).setText(model.getdate());
                ((TextView)v.findViewById(R.id.duration)).setTypeface(myTypeface);
                ((TextView)v.findViewById(R.id.textView6)).setText(model.getLike_count()+" people like this ");
                ((TextView)v.findViewById(R.id.textView6)).setTypeface(myTypeface);
                List likers=model.getLikers();
                ((Button)v.findViewById(R.id.button)).setText("Like");
                for(int i=0;i<likers.size();i++){
                    if(likers.get(i).equals(user_name)){
                        ((Button)v.findViewById(R.id.button)).setText("Unlike");
                        break;
                    }
                }

                View.OnClickListener onClickListener=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase status_ref=new Firebase(model.getRef());
                        Firebase like_ref=status_ref.child("likers");
                        //status status_ob=new status(model.getName(),model.getText(),model.getdate(),model.getRef());
                        //status_ob.add_like(user_name);
                        if(((Button)v.findViewById(R.id.button)).getText().toString().equalsIgnoreCase("Like")){
                            List likers=model.getLikers();
                            likers.add(user_name);

                            like_ref.setValue(likers);
                            status_ref.child("like_count").setValue(likers.size());
                            ((Button)v.findViewById(R.id.button)).setText("UNLIKE");

                        }
                        else if(((Button)v.findViewById(R.id.button)).getText().toString().equalsIgnoreCase("Unlike")){
                            List likers=model.getLikers();
                            likers.remove(user_name);

                            like_ref.setValue(likers);
                            status_ref.child("like_count").setValue(likers.size());
                            ((Button)v.findViewById(R.id.button)).setText("LIKE");

                        }



                    }

                };
                ((Button)v.findViewById(R.id.button)).setOnClickListener(onClickListener);

            }
        };
        listView.setAdapter(mListAdapter);


    }

    private boolean sendChatMessage() {
        d_statusArrayAdapter.add(new d_status(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }

    public void gotoHome(View view){
        /*Intent intent = new Intent(getApplicationContext(),com.firebasedemo.nanochat.d_homeActivity.class);

        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);*/
    }

    public void gotoCall(View view){
        Intent intent = new Intent(getApplicationContext(),com.firebasedemo.nanochat.VideoLoginActivity.class);
        String message = user_name;
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void gotoChat(View view){
        Intent intent = new Intent(getApplicationContext(),OnlineActivity2.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);

    }

    public void logout(View view){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        mFirebaseRef.child("OnlineList").child(user_name).removeValue();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Connecting");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}