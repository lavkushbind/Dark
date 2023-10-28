package com.example.profile;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blank_learn.dark.R;
import com.blank_learn.dark.databinding.ActivityProBinding;
import com.example.chat.ChatAA;
import com.example.home.homeadapter;
import com.example.loginandsignup.Users;
import com.example.payment.postmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityProBinding binding;
    String name;
    String fb;
    Intent intent;
    TextView bio;
    ArrayList<postmodel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        auth= FirebaseAuth.getInstance();
        name = intent.getStringExtra("name");
        bio = findViewById(R.id.bio);
        list= new ArrayList<>();
        //Bundle bundle= getIntent().getExtras();
        //ReceiversName = bundle.getString("name");
        // binding.Username.setText(getIntent().getStringExtra("name"));
        binding = ActivityProBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database  = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        homeadapter homeadapter = new homeadapter(list, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        binding.  ProRV.setLayoutManager(layoutManager);
        binding. ProRV.setAdapter(homeadapter);
        binding.ProRV.scrollToPosition(homeadapter.getItemCount() - 1);
        layoutManager.setStackFromEnd(true);
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    postmodel postmodel =dataSnapshot.getValue(postmodel.class);
                    postmodel.setPostid(dataSnapshot.getKey());

                    list.add(postmodel);
                }
                homeadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        binding.fbtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.instatext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.twittertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.linkedintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(users.getInstagram()));
//                startActivity(urlIntent);

            }
        });
        binding.bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareFact = bio.getText().toString();
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareFact));
                startActivity(urlIntent);
            }
        });
        database.getReference().child("Users").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users user= snapshot.getValue(Users.class);
                            Picasso.get().load(user.getCoverpic()).placeholder(R.drawable.lavkushbind).into(binding.coverpic);
                            Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.profileuser).into(binding.profilepic);
                            binding.emailtext.setText(user.getEmail());
                            binding.fbtext.setText(user.getFb());
                            binding.linkedintext.setText(user.getLinkedin());
                            binding.instatext.setText(user.getInstagram());
                            binding.twittertext.setText(user.getTwitter());
                            binding.bio.setText(user.getBio());
                            binding.Username.setText(user.getName());
                            binding.profesiontext.setText(user.getProfesion());
                            binding.followid.setText(user.getFollowercount()+"");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

}