package com.example.quizzer.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzer.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuestionBank extends AppCompatActivity {

    int totalQuestions;
    int index = 1;
    private LinearLayout linearLayout, linearLayout1;
    private ProgressBar progressBar;
    private TextView questions, scoreCount, timeCount, totalQueCount, questionNumber;
    private MaterialButton nextButton;
    private DatabaseReference reference;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, selectedAns;
    private String correctAnswer;
    private int Score = 0;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionbank);

        questions = findViewById(R.id.questions);
        questionNumber = findViewById(R.id.questionNumber);
        nextButton = findViewById(R.id.nextbtn);
        radioGroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radiobtn1);
        radioButton2 = findViewById(R.id.radiobtn2);
        radioButton3 = findViewById(R.id.radiobtn3);
        radioButton4 = findViewById(R.id.radiobtn4);
        scoreCount = findViewById(R.id.scorecount);
        timeCount = findViewById(R.id.timercount);
        totalQueCount = findViewById(R.id.quecount);
        progressBar = findViewById(R.id.progressBar4);
        linearLayout = findViewById(R.id.linear1);
        linearLayout1 = findViewById(R.id.linear2);

        String LanguageName = getIntent().getStringExtra("LanguageName");

        reference = FirebaseDatabase.getInstance().getReference().child(LanguageName);

        getQuestion(index);

        startTimer();
        getTotalQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                selectedAns = findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(QuestionBank.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedAns.getText().toString().equals(correctAnswer)) {
//                        Toast.makeText(Gk.this, "Correct", Toast.LENGTH_SHORT).show();
                        Score++;
                        scoreCount.setText(String.valueOf(Score));
                    } else {
//                        Toast.makeText(Gk.this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                    countDownTimer.cancel();
                    getQuestion(index++);
                    Log.d("Next", "Next Button Clicked. And Now Question Number Is = " + index);
                }
                radioGroup.clearCheck();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCount.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                radioGroup.clearCheck();
                getQuestion(index++);
                Log.d("Timer", "Time Out and new question number is = " + index);
            }
        };
    }

    public void getQuestion(final int index) {
        reference.child(String.valueOf(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String que = snapshot.child("question").getValue().toString();
                    String option1 = snapshot.child("option1").getValue().toString();
                    String option2 = snapshot.child("option2").getValue().toString();
                    String option3 = snapshot.child("option3").getValue().toString();
                    String option4 = snapshot.child("option4").getValue().toString();
                    correctAnswer = snapshot.child("correct_answer").getValue().toString();

                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    questions.setText(que);
                    questionNumber.setText(String.valueOf(index));
                    radioButton1.setText(option1);
                    radioButton2.setText(option2);
                    radioButton3.setText(option3);
                    radioButton4.setText(option4);
                    countDownTimer.start();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    countDownTimer.cancel();
                    Intent intent = new Intent(QuestionBank.this, com.example.quizzer.Activities.Score.class);
                    intent.putExtra("Score", String.valueOf(Score));
                    startActivity(intent);
                    finish();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionBank.this, "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getTotalQuestion(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalQuetionNumber = (int) snapshot.getChildrenCount();
                    totalQueCount.setText(Integer.toString(totalQuetionNumber));
                    Log.i("Total", Integer.toString(totalQuetionNumber));
                    totalQuestions = totalQuetionNumber;
                } else {
                    totalQueCount.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setMessage("Are you sure to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countDownTimer.cancel();
                        finish();
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