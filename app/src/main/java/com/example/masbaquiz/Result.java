package com.example.masbaquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity {
    private List<QuestionsList> questionsLists = new ArrayList<>();
    TextView congrats, congratsTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        final TextView score = findViewById(R.id.scoreTV);
        final TextView totalScoreTV = findViewById(R.id.totalScoreTV);
        final TextView correctTV = findViewById(R.id.correctTV);
        final TextView incorrectTV = findViewById(R.id.inCorrectTV);
        final AppCompatButton reTakeBtn = findViewById(R.id.reTakeQuizBtn);
        final AppCompatButton endQuizBtn = findViewById(R.id.endQuizBtn);
        congrats = findViewById(R.id.congrats);
        congratsTV = findViewById(R.id.congratsTV);

        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("questions");
        totalScoreTV.setText("/"+questionsLists.size()*5);
        score.setText(getCorrectAnswers()*5+"");
        correctTV.setText(getCorrectAnswers()+"");
        incorrectTV.setText(String.valueOf(questionsLists.size() - getCorrectAnswers()));
        if (getCorrectAnswers()<3){
            congrats.setText("OPPS !!");
            congratsTV.setText("You have failed to get pass mark. You can RETAKE");
        }

        reTakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Result.this,Question1.class));
                finish();
            }
        });

        endQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result.this.finish();
                System.exit(0);
            }
        });

    }
    private int getCorrectAnswers(){
        int correctAnswer = 0;
        for (int i=0;i<questionsLists.size();i++){
            int getUserSelectedOption = questionsLists.get(i).getUserSelectedAnswer();
            int getQuestionAnswer = questionsLists.get(i).getAnswer();

            if (getUserSelectedOption==getQuestionAnswer){
                correctAnswer++;
            }
        }
        return correctAnswer;
    }
}