package com.example.trollify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {

    private Toolbar ChattoolBar;
    private ImageButton SendMessageButton, SendImagefileButton;
    private EditText userMessageInput;
    private RecyclerView userMessagesList;

    private String messageReceiverID, messageReceiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("userName").toString();

        InitializeFields();
    }

    private void InitializeFields() {
        ChattoolBar = (Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(ChattoolBar);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        SendImagefileButton = (ImageButton) findViewById(R.id.send_image_file_button);
        userMessageInput = (EditText) findViewById(R.id.input_message);

    }
}