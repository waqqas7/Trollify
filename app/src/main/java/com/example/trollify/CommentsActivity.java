package com.example.trollify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity
{
    private RecyclerView CommentsList;
    private ImageButton PostCommentButton;

    private EditText CommentInputText;

    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String Post_Key, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Post_Key = getIntent().getExtras().get("PostKey").toString();

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("Comments");

        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        CommentInputText = (EditText) findViewById(R.id.comment_input);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_btn);

        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            String userName = snapshot.child("username").getValue().toString();

                            ValidateComment(userName);

                            CommentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(PostsRef, Comments.class)
                .build();
        FirebaseRecyclerAdapter firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model) {
                holder.setUsername(model.getUsername());
                holder.setComment(model.getComment());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
            }
            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout,parent,false);
                return new CommentsViewHolder(view);
            }
        };

        CommentsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setUsername(String username) {
            TextView myUserName = (TextView) mView.findViewById(R.id.comment_username);
            myUserName.setText("@"+username+" ");
        }
        public void setComment(String comment) {
            TextView myComment = (TextView) mView.findViewById(R.id.comment_text);
            myComment.setText(comment);
        }
        public void setDate(String date) {
            TextView myDate = (TextView) mView.findViewById(R.id.comment_date);
            myDate.setText("Date: "+date);
        }
        public void setTime(String time) {
            TextView myTime = (TextView) mView.findViewById(R.id.comment_time);
            myTime.setText("Time:"+time);
        }
    }


    private void ValidateComment(String userName)
    {
        String commentText = CommentInputText.getText().toString();

        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "please write text to comment...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
            final String saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            final String saveCurrentTime = currentTime.format(calForTime.getTime());

            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;

            HashMap commentsMap = new HashMap();
                commentsMap.put("uid", current_user_id);
                commentsMap.put("comment", commentText);
                commentsMap.put("date", saveCurrentDate);
                commentsMap.put("time", saveCurrentTime);
                commentsMap.put("username", userName);
            PostsRef.child(RandomKey).updateChildren(commentsMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(CommentsActivity.this, "You've commented successfully....", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(CommentsActivity.this, "Error occurred, try again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}