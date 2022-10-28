package com.example.masbaquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Question1 extends AppCompatActivity {
    private final List<QuestionsList> questionsLists = new ArrayList<>();
    private TextView quizTimer;
    private RelativeLayout option1layout,option2layout,option3layout,option4layout;
    private TextView option1TV,option2TV,option3TV,option4TV,totalQuestionsTV,currentQuestion,questionTV;
    private ImageView option1icon,option2icon,option3icon,option4icon;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://masbaquiz-default-rtdb.firebaseio.com/");
    private CountDownTimer countDownTimer;
    private int currentQuestionPosition = 0;
    private int selectedOption = 0;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBar);
        quizTimer = findViewById(R.id.quiz_timer);
        option1layout = findViewById(R.id.option1layout);
        option2layout = findViewById(R.id.option2layout);
        option3layout = findViewById(R.id.option3layout);
        option4layout = findViewById(R.id.option4layout);

        option1TV = findViewById(R.id.option1TV);
        option2TV = findViewById(R.id.option2TV);
        option3TV = findViewById(R.id.option3TV);
        option4TV = findViewById(R.id.option4TV);
        questionTV = findViewById(R.id.questionTV);

        option1icon = findViewById(R.id.option1icon);
        option2icon = findViewById(R.id.option2icon);
        option3icon = findViewById(R.id.option3icon);
        option4icon = findViewById(R.id.option4icon);

        totalQuestionsTV = findViewById(R.id.totalQuestionsTV);
        currentQuestion = findViewById(R.id.currentQuestionTV);

        final AppCompatButton nextBtn = findViewById(R.id.nextQuestionBtn);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
                final int getQuizTime = snapshot.child("time").getValue(Integer.class);
                for (DataSnapshot questions: snapshot.child("questions").getChildren()){
                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                    int getAnswer = questions.child("answer").getValue(Integer.class);

                    QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2,getOption3,getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }
                totalQuestionsTV.setText("/"+questionsLists.size());
                startQuizTimer(getQuizTime);
                selectQuestion(currentQuestionPosition);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Question1.this,"Failed to retrive data from firebase",Toast.LENGTH_SHORT).show();

            }
        });
        option1layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 1;
                selectOption(option1layout, option1icon);
            }
        });

        option2layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 2;
                selectOption(option2layout, option2icon);
            }
        });

        option3layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 3;
                selectOption(option3layout, option3icon);
            }
        });

        option4layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = 4;
                selectOption(option4layout, option4icon);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedOption != 0){
                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);

                    selectedOption = 0;
                    currentQuestionPosition++;

                    if (currentQuestionPosition==4){
                        nextBtn.setText("Submit");
                    }

                    if (currentQuestionPosition < questionsLists.size()){
                        selectQuestion(currentQuestionPosition);
                    }else{
                        countDownTimer.cancel();
                        finishQuiz();
                    }
                }else{
                    Toast.makeText(Question1.this,"Please Select an Option",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void finishQuiz(){
        Intent intent = new Intent(Question1.this,Result.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions",(Serializable)questionsLists);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    private void startQuizTimer(int maxTimeInSeconds){
        countDownTimer = new CountDownTimer(maxTimeInSeconds*1000,1000) {
            @Override
            public void onTick(long l) {
                long getHour = TimeUnit.MILLISECONDS.toHours(l);
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(l);
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(l);

                String generateTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",getHour,
                        getMinute-TimeUnit.HOURS.toMinutes(getHour),
                        getSecond-TimeUnit.MINUTES.toSeconds(getMinute));
                quizTimer.setText(generateTime);
            }

            @Override
            public void onFinish() {
                Toast.makeText(Question1.this,"Time is up",Toast.LENGTH_SHORT).show();
                finishQuiz();
            }
        };
        countDownTimer.start();
    }
    private void selectQuestion(int questionListPosition){
        resetOptions();
        questionTV.setText(questionsLists.get(questionListPosition).getQuestion());
        option1TV.setText(questionsLists.get(questionListPosition).getOption1());
        option2TV.setText(questionsLists.get(questionListPosition).getOption2());
        option3TV.setText(questionsLists.get(questionListPosition).getOption3());
        option4TV.setText(questionsLists.get(questionListPosition).getOption4());

        currentQuestion.setText("Question"+(questionListPosition+1));
    }
    private void resetOptions(){
        option1layout.setBackgroundResource(R.drawable.round_background);
        option2layout.setBackgroundResource(R.drawable.round_background);
        option3layout.setBackgroundResource(R.drawable.round_background);
        option4layout.setBackgroundResource(R.drawable.round_background);

        option1icon.setImageResource(R.drawable.round_option);
        option2icon.setImageResource(R.drawable.round_option);
        option3icon.setImageResource(R.drawable.round_option);
        option4icon.setImageResource(R.drawable.round_option);
    }
    private void selectOption(RelativeLayout selectedOptionLayout , ImageView selectedOptionIcon){
        resetOptions();

        selectedOptionIcon.setImageResource(R.drawable.check);

    }
}