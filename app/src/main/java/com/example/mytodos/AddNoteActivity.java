package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytodos.ModelClass.UserNotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText title,descrip;
    FloatingActionButton save;
    DatabaseReference databaseNotes;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.noteEditText);
        descrip = findViewById(R.id.descriptionEditText);
        save = findViewById(R.id.saveFloatingButton);

        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();
        databaseNotes = FirebaseDatabase.getInstance().getReference("UserNotes").child(uid);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String noteTitle = title.getText().toString();
                String noteDescrip = descrip.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Calendar calendar = Calendar.getInstance();
                String todaysDate =  simpleDateFormat.format(calendar.getTime());

                if (!noteTitle.equalsIgnoreCase("")){
                    if (!noteDescrip.equalsIgnoreCase("")){
                        progressDialog.show();
                        String key = databaseNotes.push().getKey();
                        UserNotes userNotes = new UserNotes(noteTitle,noteDescrip,todaysDate,key);
                        databaseNotes.child(key).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Upload Is Unsuccessful",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else {
                        Toast.makeText(getApplicationContext(),"Description Is Empty",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Title Is Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
