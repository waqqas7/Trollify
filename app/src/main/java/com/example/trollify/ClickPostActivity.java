package com.example.trollify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickPostActivity extends AppCompatActivity {

    private ImageView PostImage;
    private TextView PostDescription;
    private Button DeletePostButton, EditPostButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        PostImage = (ImageView)findViewById(R.id.post_image);
        PostDescription = (TextView)findViewById(R.id.post_description);
        DeletePostButton = (Button)findViewById(R.id.delete_post_button);
        EditPostButton = (Button)findViewById(R.id.edit_post_button);
    }
}