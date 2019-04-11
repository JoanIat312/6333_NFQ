package com.example.nfq;

import java.io.Serializable;
import java.util.List;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Entity;

@Entity(tableName = "questions")
public class Question implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo (name = "note_id")
    private int note_id;//note id

    @ColumnInfo (name = "question")
    private String question;

    @ColumnInfo (name = "option1")
    private String option1;

    @ColumnInfo (name = "option2")
    private String option2;

    @ColumnInfo (name = "option3")
    private String option3;

    @ColumnInfo (name = "answerNr")
    private int answerNr;

    public Question(int note_id, String question, String option1, String option2, String option3, int answerNr) {
        this.note_id = note_id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }
}
