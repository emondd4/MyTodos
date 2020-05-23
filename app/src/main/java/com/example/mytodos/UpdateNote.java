package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytodos.ModelClass.UserNotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateNote extends AppCompatActivity{

    private EditText updatetitle,updatedesc;
    FloatingActionButton update;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseNotes;
    ProgressDialog progressDialog;

    private String noteid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        updatetitle = findViewById(R.id.UpdatenoteEditText);
        updatedesc = findViewById(R.id.UpdatedescriptionEditText);
        update = findViewById(R.id.UpdateFloatingButton);

        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();
        databaseNotes = FirebaseDatabase.getInstance().getReference("UserNotes").child(uid);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            String oldtitle = (String) bundle.get("oldtitle");
            String olddesc = (String) bundle.get("olddesc");
            noteid = (String) bundle.get("noteid");

            updatetitle.setText(oldtitle);
            updatedesc.setText(olddesc);
        }

        KeyboardVisibilityEvent.setEventListener(UpdateNote.this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if (b){
                    update.setVisibility(View.INVISIBLE);
                }else {
                    update.setVisibility(View.VISIBLE);
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = updatetitle.getText().toString();
                String noteDescrip = updatedesc.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Calendar calendar = Calendar.getInstance();
                String todaysDate =  simpleDateFormat.format(calendar.getTime());

                if (!noteTitle.equalsIgnoreCase("")){
                    if (!noteDescrip.equalsIgnoreCase("")){
                        progressDialog.show();

                        UserNotes userNotes = new UserNotes(noteTitle,noteDescrip,todaysDate,noteid);
                        databaseNotes.child(noteid).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Update Is Unsuccessful",Toast.LENGTH_SHORT).show();
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
