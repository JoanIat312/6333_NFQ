package com.example.nfq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizStartingActivity  extends AppCompatActivity {
    private TextView mEtNoteTitle;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start);

        mEtNoteTitle = findViewById(R.id.quiz_title);
        if ((note = (Note) getIntent().getSerializableExtra("note")) != null) {
            mEtNoteTitle.setText("Quiz for " + note.getTitle());
        }else{
            Toast.makeText(this, "Opps, something is wrong. We could not find this note.", Toast.LENGTH_SHORT).show();
            //finish();
        }

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStartingActivity.this, QuizActivity.class).putExtra("note_id", note.getId());
                startActivity(intent);
            }
        });

    }


    private void startQuiz() {
        Intent intent = new Intent(QuizStartingActivity.this, QuizActivity.class).putExtra("note_id", note.getId());
        startActivity(intent);
    }
}
