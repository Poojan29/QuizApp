package com.example.quizzer.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView forgot;
    TextInputEditText nemail,npass;
    MaterialButton nlogin,nsignup;
    ConstraintLayout constraintLayout;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nemail = findViewById(R.id.emailid1);
        npass  = findViewById(R.id.password1);
        nlogin = findViewById(R.id.loginbtn1);
        nsignup = findViewById(R.id.signupbtn1);
        forgot = findViewById(R.id.forgottext);
        constraintLayout = findViewById(R.id.loginCon);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar1);
        firebaseAuth = FirebaseAuth.getInstance();

        pref = getApplicationContext().getSharedPreferences("email", 0);
        editor = pref.edit();


        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(this, "You are already logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {

        }


        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = nemail.getText().toString().trim();
                String pass = npass.getText().toString().trim();
                if (email.isEmpty()) {
                    Snackbar.make(constraintLayout, "Please enter your Email.", Snackbar.LENGTH_SHORT)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                    return;
                }
                if (pass.length() < 6) {
                    Snackbar.make(constraintLayout, "Please enter your Password.", Snackbar.LENGTH_SHORT)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        progressBar.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(Login.this, "Invalid Password or E-mail", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        forgot.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        nsignup.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}


