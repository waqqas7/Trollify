package com.example.trollify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView myFriendList;
    private DatabaseReference FriendsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String online_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myFriendList = (RecyclerView)findViewById(R.id.friend_list);
        myFriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendList.setLayoutManager(linearLayoutManager);

        DisplayAllFriends();
    }

    private void DisplayAllFriends() {

        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(FriendsRef,Friends.class)
                .build();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Friends model) {

                        holder.setDate(model.getDate());

                        final String usersIDs = getRef(position).getKey();

                        UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){
                                    final String userName = snapshot.child("fullname").getValue().toString();
                                    final String profileImage = snapshot.child("profileimage").getValue().toString();
                                    final String type;

                                    if(snapshot.hasChild("userState"))
                                    {
                                        type = snapshot.child("userState").child("type").getValue().toString();

                                        if(type.equals("online"))
                                        {
                                            holder.onlineStatusView.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            holder.onlineStatusView.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    holder.setFullname(userName);
                                    holder.setProfileimage(profileImage);

                                    holder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            CharSequence options[] = new CharSequence[]{
                                                    userName + "'s Profile",
                                                    "Send Message"
                                            };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                                            builder.setTitle("Select Option");

                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (i == 0){
                                                        Intent profileintent = new Intent(FriendsActivity.this,PersonProfileActivity.class);
                                                        profileintent.putExtra("visit_user_id",usersIDs);
                                                        startActivity(profileintent);
                                                    }
                                                    if (i == 1){
                                                        Intent Chatintent = new Intent(FriendsActivity.this,ChatActivity.class);
                                                        Chatintent.putExtra("visit_user_id",usersIDs);
                                                        Chatintent.putExtra("userName",userName);
                                                        startActivity(Chatintent);
                                                    }
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_users_display_layout,parent,false);

                        return new FriendsViewHolder(view);
                    }
                };

                myFriendList.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageView onlineStatusView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            onlineStatusView = (ImageView) itemView.findViewById(R.id.all_user_online_icon);
        }

        public void setProfileimage(String profileimage)
        {
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname)
        {
            TextView myName = (TextView)mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);
        }

        public void setDate(String date)
        {
            TextView friendsDate = (TextView)mView.findViewById(R.id.all_users_status);
            friendsDate.setText("Friends since " + date);
        }
    }
}