package com.example.trollify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;

    private DatabaseReference allUsersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

        SearchResultList = (RecyclerView)findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = (ImageButton)findViewById(R.id.search_people_friends_button);

        SearchInputText = (EditText)findViewById(R.id.search_box_input);

        SearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String  searchBoxInput = SearchInputText.getText().toString();
                SearchPeopleAndFriends(searchBoxInput);
            }
        });

    }

    private void SearchPeopleAndFriends(String searchBoxInput)
    {
        Toast.makeText(this, "Searching....", Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerOptions<FindFriends> options =
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(searchPeopleandFriendsQuery, FindFriends.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(options)
        {
            @Override
            public FindFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout, parent, false);

                return new FindFriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(FindFriendsViewHolder holder, int position, FindFriends model)
            {
                 holder.setFullname(model.getFullname());
                 holder.setStatus(model.getStatus());
                 holder.setProfileimage(model.getProfileimage());
            }

        };
        adapter.startListening();
        SearchResultList.setAdapter(adapter);
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
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

        public void setStatus(String status)
        {
            TextView myStatus = (TextView)mView.findViewById(R.id.all_users_status);
            myStatus.setText(status);
        }
    }
}