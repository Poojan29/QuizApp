package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class gk extends AppCompatActivity {

    TextView questions, scorecount, timecount;
    MaterialButton next;
    DatabaseReference reference;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4,selectedAns;
    String Correctans;
    int Score = 0;
    CountDownTimer countDownTimer;
    final int[] total = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gk);

        questions = findViewById(R.id.questions);
        next = findViewById(R.id.nextbtn);
        radioGroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radiobtn1);
        radioButton2 = findViewById(R.id.radiobtn2);
        radioButton3 = findViewById(R.id.radiobtn3);
        radioButton4 = findViewById(R.id.radiobtn4);
        scorecount = findViewById(R.id.scorecount);
        timecount = findViewById(R.id.timercount);

        reference  = FirebaseDatabase.getInstance().getReference().child("questions");

        getQuestion(total[0]);

        startTimer();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedid = radioGroup.getCheckedRadioButtonId();
                selectedAns = (RadioButton)findViewById(selectedid);
                if (selectedid==-1){
                    Toast.makeText(gk.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }else{

                    if (selectedAns.getText().toString().equals(Correctans)){
                        Toast.makeText(gk.this, "Correct", Toast.LENGTH_SHORT).show();
                        Score++;
                        scorecount.setText(String.valueOf(Score));
                    }else{
                        Toast.makeText(gk.this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                    total[0]++;
                    countDownTimer.start();

                }
                //Toast.makeText(gk.this,"Button Clicked",Toast.LENGTH_SHORT).show();
                //total[0]++;
                getQuestion(total[0]);
                radioGroup.clearCheck();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timecount.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                total[0]++;
                getQuestion(total[0]++);
                countDownTimer.start();
            }
        };
        countDownTimer.start();
    }

    public void getQuestion(final int total) {

        reference.child(Integer.toString(total)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String que = snapshot.child("question").getValue().toString();
                    String option1 = snapshot.child("option1").getValue().toString();
                    String option2 = snapshot.child("option2").getValue().toString();
                    String option3 = snapshot.child("option3").getValue().toString();
                    String option4 = snapshot.child("option4").getValue().toString();
                    Correctans = snapshot.child("correct_answer").getValue().toString();

                    // list.add(new QuestionData(que,option1, option2, option3, option4, Correctans));
                    Log.i("MSG", que);

                    questions.setText(que);
                    radioButton1.setText(option1);
                    radioButton2.setText(option2);
                    radioButton3.setText(option3);
                    radioButton4.setText(option4);
                }
                else {
                    countDownTimer.cancel();
//                    Intent intent = new Intent(gk.this, Score.class);
//                    //intent.putExtra("Score", scorecount);
//                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(gk.this, "Database error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setMessage("Are you sure to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gk.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

}