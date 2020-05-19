package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.forgotEmailText)
    EditText forgotEmail;
    @BindView(R.id.SendEmailBtn)
    Button sendEmail;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.SendEmailBtn)
    public void onViewClicked(View view){
        progressDialog.setMessage("Sending Reset Email...");
        progressDialog.show();
        String emailId = forgotEmail.getText().toString();
        if (!emailId.equalsIgnoreCase("")){
            firebaseAuth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPassword.this,"Reset Email Send",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ForgotPassword.this,""+error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(ForgotPassword.this,"Enter Email Address",Toast.LENGTH_SHORT).show();
        }
    }
}
