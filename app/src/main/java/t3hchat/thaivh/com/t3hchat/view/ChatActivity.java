package t3hchat.thaivh.com.t3hchat.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import t3hchat.thaivh.com.t3hchat.R;
import t3hchat.thaivh.com.t3hchat.adapter.ListMessageAdapter;
import t3hchat.thaivh.com.t3hchat.base.BaseActivity;
import t3hchat.thaivh.com.t3hchat.config.StaticConfig;
import t3hchat.thaivh.com.t3hchat.helper.FireHelper;
import t3hchat.thaivh.com.t3hchat.helper.SharedPreferenceHelper;
import t3hchat.thaivh.com.t3hchat.model.Consersation;
import t3hchat.thaivh.com.t3hchat.model.Message;

/**
 * Created by thais on 3/9/2018.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    private String roomId;
    private ArrayList<CharSequence> idFriend;
    private Consersation consersation;
    private ImageButton btnSend;
    private ImageButton btnAttach;
    private EditText editWriteMessage;
    private LinearLayoutManager linearLayoutManager;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        Intent intentData = getIntent();
        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        String nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
        consersation = new Consersation();
        if (idFriend != null && nameFriend != null) {
            getSupportActionBar().setTitle(nameFriend);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = findViewById(R.id.recyclerChat);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageAdapter(this, consersation, bitmapAvataFriend, bitmapAvataUser);
            FirebaseDatabase.getInstance().getReference().child("message/" + roomId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        Message newMessage = new Message();
                        newMessage.idSender = (String) mapMessage.get("idSender");
                        newMessage.idReceiver = (String) mapMessage.get("idReceiver");
                        newMessage.text = (String) mapMessage.get("text");
                        newMessage.timestamp = (long) mapMessage.get("timestamp");
                        consersation.getListMessageData().add(newMessage);
                        adapter.notifyDataSetChanged();
                        linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
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
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recyclerChat.setAdapter(adapter);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initLizeComponent() {
        btnSend = findViewById(R.id.btnSend);
        btnAttach = findViewById(R.id.btn_attach);
        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            bitmapAvataUser = null;
        }

        editWriteMessage = findViewById(R.id.editWriteMessage);
    }

    @Override
    public void registerListener() {
        btnSend.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btn_attach:
                showOptionMenu(view);
                break;
            default:
                break;
        }
    }

    private void showOptionMenu(View v) {
        PopupMenu popup = new PopupMenu(ChatActivity.this, v);

        /** Adding menu items to the popumenu */
        popup.getMenuInflater().inflate(R.menu.attach_file, popup.getMenu());

        /** Defining menu item click listener for the popup menu */
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /** Showing the popup menu */
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.attach_file, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("idFriend", idFriend.get(0));
        setResult(RESULT_OK, result);
        this.finish();
    }

    private void sendMessage() {
        String content = editWriteMessage.getText().toString().trim();
        if (content.length() > 0) {
            editWriteMessage.setText("");
            Message newMessage = new Message();
            newMessage.text = content;
            newMessage.idSender = FireHelper.getCurUserId();
            newMessage.idReceiver = roomId;
            newMessage.timestamp = System.currentTimeMillis();
            FirebaseDatabase.getInstance().getReference().child("message/" + roomId).push().setValue(newMessage);
        }
    }
}
