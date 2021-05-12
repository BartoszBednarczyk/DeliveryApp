package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Login activity.
 * Login activity for existing users.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * The Login email.
     */
    EditText LoginEmail;
    /**
     * The Login password.
     */
    EditText LoginPassword;
    /**
     * The Login button.
     */
    Button LoginButton;
    /**
     * The Progress bar.
     */
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginEmail = (EditText)findViewById(R.id.loginEmail);
        LoginPassword = (EditText)findViewById(R.id.loginPassword);
        LoginButton = (Button)findViewById(R.id.loginButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            openMainActivity();
            //handle the already login user
        }
    }

    private void loginUser(){
        final String email = LoginEmail.getText().toString().trim();
        final String password = LoginPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, getString(R.string.login_succes),Toast.LENGTH_LONG).show();
                    openMainActivity();
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.login_unsucces),Toast.LENGTH_LONG).show(); //handle unsuccesful login
                }
            }
        });
    }

    /**
     * Open main activity.
     */
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
