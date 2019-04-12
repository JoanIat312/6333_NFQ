package com.example.nfq;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity  extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;

    private int questionCounter;
    private int questionCountTotal;
    private Key currentQuestion;

    private int score;
    private boolean answered;

    private NFQDatabase myDB;
    private List<Key> questions;
    private int answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        initializeVies();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void initializeVies(){
        int note_id = getIntent().getExtras().getInt("note_id");
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();

        questions = new ArrayList<>();
        createList(note_id);

    }
    private void createList(int note_id){
        myDB = NFQDatabase.getInstance(QuizActivity.this);
        questions = myDB.getKeyDao().getByKeyId(note_id);
        questionCountTotal = questions.size();
        Log.d("STATE", String.valueOf(questionCountTotal) + " " + String.valueOf(note_id));
        Collections.shuffle(questions);
        showNextQuestion();
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questions.get(questionCounter);

            textViewQuestion.setText(Html.fromHtml("Which term does the following describes: <i>" + currentQuestion.getDefinition() + "</i>"));
            answer = (int)(Math.random() * ((3 - 1) + 1)) + 1;
            for(int i = 0; i < 3; i++){
                if("rb1".contains(String.valueOf(answer))){
                    rb1.setText(currentQuestion.getKeyword());
                    rb2.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                    rb3.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                }else if("rb2".contains(String.valueOf(answer))){
                    rb2.setText(currentQuestion.getKeyword());
                    rb1.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                    rb3.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                }else{
                    rb3.setText(currentQuestion.getKeyword());
                    rb2.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                    rb1.setText(questions.get((int)(Math.random() * ((questionCountTotal)))).getKeyword());
                }
            }

            questionCounter++;
            textViewQuestionCount.setText("Question " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            finishQuiz();
        }
    }
    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == answer) {
            score+= 10;
            textViewScore.setText("Score: " + score);
            textViewQuestion.setText("You are correct!");
        }else{
            textViewQuestion.setText(Html.fromHtml("Incorrect, the correct answer is <b>" + currentQuestion.getKeyword() + "<b>"));
        }

        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (answer) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        finish();
    }

}
