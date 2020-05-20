package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mytodos.Adapter.NoteAdapter;
import com.example.mytodos.ModelClass.UserNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addnote,exit;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseNotes;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<UserNotes> allNotesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addnote = findViewById(R.id.addFloatingButton);
        exit = findViewById(R.id.ExitfloatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();
        databaseNotes = FirebaseDatabase.getInstance().getReference("UserNotes").child(uid);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddNoteActivity.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null){
                    firebaseAuth.signOut();
                }
                Toast.makeText(MainActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        readAllNotes();
    }

    public void readAllNotes(){
        allNotesList.clear();
        progressDialog.show();

        databaseNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    allNotesList.add(userNotes);
                }
                progressDialog.dismiss();
                NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(),allNotesList);
                recyclerView.setAdapter(noteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
