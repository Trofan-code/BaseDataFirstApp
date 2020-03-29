package com.example.basedatafirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button bStart, bSignIn, bSignUp, bSignOut;
    private TextView tvUserName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null)
        {
            showSigned();
            String userName = "Вы вошли как: " + cUser.getEmail();
            tvUserName.setText(userName);

            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            notSigned();
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void init()
    {
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);
        bStart = findViewById(R.id.bStart);
        bSignOut = findViewById(R.id.bSignOut);
        tvUserName = findViewById(R.id.tvUserEmail);
        bSignIn = findViewById(R.id.bSignIn);
        bSignUp = findViewById(R.id.bSignUp);
        mAuth = FirebaseAuth.getInstance();
    }
    public void onClickSignUp(View view)
    {
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        sendEmailVer();
                        Toast.makeText(getApplicationContext(), "User SignUp Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "User SignUp failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignIn(View view)
    {
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())){
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "User SignIn Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "User SignIn failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignOut(View view){
        FirebaseAuth.getInstance().signOut();
        notSigned();
    }
    public void onClickStart(View view){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }

    private void showSigned(){
        FirebaseUser user = mAuth.getCurrentUser();

        String userName = "Вы вошли как: " + user.getEmail();
        tvUserName.setText(userName);

        assert user != null;
        if(user.isEmailVerified()) {
            bStart.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            bSignOut.setVisibility(View.VISIBLE);
            edLogin.setVisibility(View.GONE);
            edPassword.setVisibility(View.GONE);
            bSignIn.setVisibility(View.GONE);
            bSignUp.setVisibility(View.GONE);
        }else {
            Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтвержления e-mail адреса.", Toast.LENGTH_SHORT).show();
        }
    }
    private void notSigned(){

        bStart.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
    }

    private void sendEmailVer(){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтвержления e-mail адреса.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Send e-mail failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}