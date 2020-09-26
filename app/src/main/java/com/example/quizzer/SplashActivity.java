package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseAuth mAuth;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        pref = getApplicationContext().getSharedPreferences("email",0);
        editor = pref.edit();

        final String email = pref.getString("email","123");
        final String pass =pref.getString("pass","123");

        if (email.equals("123") && pass.equals("123")){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        else {

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            editor.putString("email",email);
                            editor.putString("pass",pass);
                            editor.apply();
                            if (task.isSuccessful()) {



//                                Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                progressBar.setVisibility(View.GONE);

                            }
                            else {
//                                Toast.makeText(Login.this, "Invalid Password or E-mail", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
//                                forgot.setVisibility(View.VISIBLE);
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                finish();
                                Toast.makeText(SplashActivity.this,"Else Part",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}