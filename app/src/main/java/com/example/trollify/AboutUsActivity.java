package com.example.trollify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {
    private CircleImageView waqqasImage, alfrazImage, afaqImage;
    private ImageView waqqasGit,    alfrazGit, afaqGit;
    private TextView waqqasInfo, alfrazInfo, afaqInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        waqqasImage = (CircleImageView) findViewById(R.id.waqqas_profile_pic);
        waqqasGit = (ImageView) findViewById(R.id.waqqas_git_link);
        waqqasInfo = (TextView) findViewById(R.id.waqqas_info);

        alfrazImage = (CircleImageView) findViewById(R.id.alfraz_profile_pic);
        alfrazGit = (ImageView) findViewById(R.id.alfraz_git_link);
        alfrazInfo = (TextView) findViewById(R.id.alfraz_info);

        afaqImage = (CircleImageView) findViewById(R.id.afaq_profile_pic);
        afaqGit = (ImageView) findViewById(R.id.afaq_git_link);
        afaqInfo = (TextView) findViewById(R.id.afaq_info);

        waqqasInfo.setText("Hi! My name is Waqqas Akhter. I developed this social networking app Trollify with my best friends. I love to code and play games. Hope you enjoy the experience!\nPress the icon below to follow me on GitHub");
        alfrazInfo.setText("Hello! I'm Alfraz Ahmed. I developed this social networking app Trollify with my colleagues. Developing and flirting are two things i am good at. Happy Trolling...\nPress the icon below to follow me on GitHub");
        afaqInfo.setText("I'm Afaque Alam. Developing this app has truly been an amazing experience for me. I also build video games.\nPress the icon below to follow me on GitHub");


        String waqqasProfImageUrl = "https://firebasestorage.googleapis.com/v0/b/trollify-7e3cc.appspot.com/o/waqqas.jpg?alt=media&token=4763ab57-1c39-4792-a5ea-959bf6aacb58";
        Picasso.get().load(waqqasProfImageUrl).into(waqqasImage);

        String alfrazProfImageUrl = "https://firebasestorage.googleapis.com/v0/b/trollify-7e3cc.appspot.com/o/Alfraz.png?alt=media&token=f07193e6-9cab-49fd-99be-6ce18de479a2";
        Picasso.get().load(alfrazProfImageUrl).into(alfrazImage);

        String afaqProfImageUrl = "https://firebasestorage.googleapis.com/v0/b/trollify-7e3cc.appspot.com/o/Afaque.png?alt=media&token=8606a48f-65d7-4bd3-ba76-aacfc74fca1e";
        Picasso.get().load(afaqProfImageUrl).into(afaqImage);

        waqqasGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/waqqas7"));
                startActivity(intent);
            }
        });

        alfrazGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/Alfrazgit"));
                startActivity(intent);
            }
        });

        afaqGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/ALPHA-Dev-lab"));
                startActivity(intent);
            }
        });
    }
}