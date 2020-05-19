package com.example.mytodos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytodos.ModelClass.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signup_name)
    TextView signUpName;
    @BindView(R.id.signupEmail)
    TextView signUpEmail;
    @BindView(R.id.signupPass)
    TextView signUpPass;
    @BindView(R.id.signupBtn)
    Button signUpBtn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        progressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.signupBtn)
    public void onViewClicked(View view){
        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPass.getText().toString();

        if (!name.equalsIgnoreCase("")){
            if (!email.equalsIgnoreCase("")){
                if (!password.equalsIgnoreCase("")){

                    registerUser(name,email,password);

            }else {
                    Toast.makeText(this,"Please enter more than 5 digit and not null",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show();
        }
    }

    public void registerUser(String name, String email, String password){
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    //save user info in db
                    FirebaseUser currentuser = firebaseAuth.getCurrentUser();
                    String uid = currentuser.getUid();

                    UserInfo userInfo = new UserInfo(name,email,"");

                    databaseUsers.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"User is Registered Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SignUpActivity.this,"Error in Registration",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
