package com.firebasedemo.nanochat;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;


public class d_ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private d_ChatArrayAdapter d_ChatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend,buttonFile;
    Typeface myTypeface;
    public String my_name,friend_name;
    public static Firebase mFirebaseRef;
    public static Firebase ChatKeyRef;
    public static Firebase ChatHistoryRef;
    public  static  Firebase thisUserRef;
    public  static  Firebase thisFriendRef;
    public static  Firebase thisChatRef;
    FirebaseListAdapter<Message> mListAdapter;
    TextView fnameview;
    private static final int ACTIVITY_CHOOSE_FILE = 3;
    public static  HashMap <Integer,String> item_url=new HashMap<Integer,String>();




    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        my_name=intent.getStringExtra(OnlineActivity2.my_tag);
        friend_name=intent.getStringExtra(OnlineActivity2.friend_tag);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.d_chat);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://sweltering-torch-7725.firebaseio.com/");
        ChatKeyRef=mFirebaseRef.child("ChatKey");
        ChatHistoryRef=mFirebaseRef.child("ChatHistory");
        thisUserRef=ChatKeyRef.child(my_name);
        thisFriendRef=thisUserRef.child(friend_name);

        buttonSend = (Button) findViewById(R.id.send);
        buttonFile=(Button) findViewById(R.id.file_picker);
        fnameview=(TextView)findViewById(R.id.friendNameView);
        fnameview.setText(LoginActivity.map.get(friend_name));
        listView = (ListView) findViewById(R.id.msgview);

        d_ChatArrayAdapter = new d_ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(d_ChatArrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                Firebase fref=new Firebase(item_url.get(position));
                fref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // do some stuff once
                        FileMsg fileMsg=snapshot.getValue(FileMsg.class);
                        Log.d("foundLink", fileMsg.getMsg());
                        try {
                            /*FileOutputStream fileout=openFileOutput(fileMsg.getMsg(), MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                            outputWriter.write(fileMsg.getText());
                            outputWriter.close();*/
                            File root = new File(Environment.getExternalStorageDirectory(), "UConnect");

                            // if external memory exists and folder with name Notes

                            if (!root.exists()) {

                                root.mkdirs(); // this will create folder.

                            }

                            File filepath = new File(root,fileMsg.getMsg());  // file path to save

                            FileWriter writer = new FileWriter(filepath);

                            writer.append(fileMsg.getText());

                            writer.flush();

                            writer.close();

                            //display file saved message
                            Toast.makeText(getBaseContext(), "Saved as Root/Uconnect/"+fileMsg.getMsg(),
                                    Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

                return true;
            }
        });



        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        thisFriendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Watching Ref", dataSnapshot.getValue().toString());
                    thisChatRef = new Firebase(dataSnapshot.child("key").getValue().toString());
                    //thisChatRef.push().setValue(message);
                } else {
                    thisChatRef = ChatHistoryRef.push();
                    thisFriendRef.child("key").setValue(thisChatRef.toString());
                    ChatKeyRef.child(friend_name).child(my_name).child("key").setValue(thisChatRef.toString());
                    //thisChatRef.push().setValue(message);
                }

                thisChatRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("from").getValue().toString().equals(my_name)) {
                            side = false;
                        } else {
                            side = true;
                        }
                        String txt = dataSnapshot.child("msg").getValue().toString();
                        d_ChatArrayAdapter.add(new d_ChatMessage(side, txt));
                        if(dataSnapshot.child("text").getValue()!=null){
                            Log.d("Saved Link", String.valueOf(d_ChatArrayAdapter.getCount()));
                            item_url.put(d_ChatArrayAdapter.getCount() - 1, dataSnapshot.child("url").getValue().toString());
                            d_ChatArrayAdapter.getItem(d_ChatArrayAdapter.getCount()-1).setIsFile(true);
                        }
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
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                sendChatMessage();
            }
        });

        buttonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("file/*");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
            }
        });



        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(d_ChatArrayAdapter);

        //to scroll the list view to bottom on data change

        d_ChatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(d_ChatArrayAdapter.getCount() - 1);
            }
        });

        //d_ChatArrayAdapter.add(new d_ChatMessage(side, my_name));
        //d_ChatArrayAdapter.add(new d_ChatMessage(side,friend_name));
    }

    private boolean sendChatMessage() {
        //d_ChatArrayAdapter.add(new d_ChatMessage(side, chatText.getText().toString()));
        DateFormat dateFormat = new SimpleDateFormat("   MMMM dd, h:mm a");
        Calendar cal = Calendar.getInstance();
        final Message message = new Message(friend_name, my_name, chatText.getText().toString(), dateFormat.format(cal.getTime()));
        thisChatRef.push().setValue(message);
        chatText.setText("");
        side = !side;
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FileInputStream inputStream=null;
        if (resultCode != RESULT_OK) return;
        String path     = "";
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {
            Uri uri = data.getData();
            File file=null;
            try {
                file=new File(uri.getPath());
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String text=s.hasNext() ? s.next() : "";
            String fname=file.getName();
            DateFormat dateFormat = new SimpleDateFormat("   MMMM dd, h:mm a");
            Calendar cal = Calendar.getInstance();
            Firebase fref=thisChatRef.push();
            FileMsg fmsg=new FileMsg(fname,my_name,friend_name,dateFormat.format(cal.getTime()),text,fref.toString());

            fref.setValue(fmsg);
            //chatText.setText(s.hasNext() ? s.next() : "");
            //chatText.setText(file.getName());

            //String FilePath = getRealPathFromURI(uri);
            //Log.d("GotFile",FilePath);

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



}