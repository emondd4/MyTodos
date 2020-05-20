package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email_text)
    EditText loginText;
    @BindView(R.id.login_pass_text)
    EditText loginPass;
    @BindView(R.id.loginBtn)
    Button login;
    @BindView(R.id.forgot_text)
    TextView forgotText;
    @BindView(R.id.text_signup)
    TextView signup;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    @OnClick({R.id.loginBtn, R.id.text_signup, R.id.forgot_text})
    public void onViewClicked(View view){
        String email = loginText.getText().toString();
        String pass = loginPass.getText().toString();

        switch (view.getId()){
            case R.id.loginBtn:

                if (!email.equalsIgnoreCase("")){
                    if (!pass.equalsIgnoreCase("")){

                        signIn(email,pass);

                    }else {
                        Toast.makeText(LoginActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.text_signup:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.forgot_text:
                Intent intent1 = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent1);
                break;

        }
    }

    public void signIn(String email,String pass){
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this,""+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
