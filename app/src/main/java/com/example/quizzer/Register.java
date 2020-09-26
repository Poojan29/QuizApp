package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText nuser,nemail,npass;
    MaterialButton nsign;
    FirebaseAuth mAuth;
    ProgressBar nprogressBar;
    ConstraintLayout constraintLayout;
    FirebaseFirestore nstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nuser = findViewById(R.id.username2);
        nemail = findViewById(R.id.emailid2);
        npass = findViewById(R.id.password2);
        nsign = findViewById(R.id.signupbtn2);
        mAuth = FirebaseAuth.getInstance();
        nstore = FirebaseFirestore.getInstance();
        nprogressBar = findViewById(R.id.progressBar2);
        constraintLayout = findViewById(R.id.registerCon);


        nsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nuser.getText().toString().trim();
                final String email = nemail.getText().toString().trim();
                final String pass = npass.getText().toString().trim();
                final String users = nuser.getText().toString();

                if (name.isEmpty()){
                    Snackbar.make(constraintLayout, "Please enter your Username.", Snackbar.LENGTH_SHORT)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Close Snackbar
                                }
                            }).show();
                    return;
                }
                if (email.isEmpty()){
                    Snackbar.make(constraintLayout, "Please enter your Email.", Snackbar.LENGTH_SHORT)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Close Snackbar
                                }
                            }).show();
                    return;
                }
                if (pass.length()<=6){
                    Snackbar.make(constraintLayout, "Your password must be contain 6 characters.", Snackbar.LENGTH_SHORT)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Close Snackbar
                                }
                            }).show();

                }else {
                    nprogressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "User created Successful.", Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid(); //for store userdata in firebase cloud database
                                DocumentReference documentReference = nstore.collection("Users").document(userID);  //here collection name is user and in that collection one document is there which name is userId in userID document our data is stored.
                                Map<String, Object> user = new HashMap<>();  //HashMap used for storing data in Firebase database.
                                user.put("UserName", users);
                                user.put("Email", email);
                                user.put("Password", pass);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Code", "User profile is created for "+userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Code", "onFailure: "+e.toString());
                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                                nprogressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }


}
