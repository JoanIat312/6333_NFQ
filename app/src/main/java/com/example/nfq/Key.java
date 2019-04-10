package com.example.nfq;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "keys")
public class Key {

        @PrimaryKey(autoGenerate = true)
        private int id;


        @ColumnInfo(name = "keyword")
        private String keyword;//title of the note

        @ColumnInfo(name = "definition")
        private String definition; //content of the note

        @ColumnInfo(name = "note_id")
        private int note_id; //creation/update time of the note

        public Key(String keyword, String definition, int note_id) {
            this.keyword = keyword;
            this.definition = definition;
            this.note_id = note_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }
}
